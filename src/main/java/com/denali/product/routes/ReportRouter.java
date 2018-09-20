/**
 * 
 */
package com.denali.product.routes;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.CsvDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.denali.product.model.UserPojo;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author zentere
 *
 */
@Component
public class ReportRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		restConfiguration().component("restlet").host("localhost").port(4002).bindingMode(RestBindingMode.auto);
		
		CsvDataFormat csv = new CsvDataFormat();
		csv.setDelimiter("|");

		// use the rest DSL to define the rest services
		rest("/users/")
		    .post().type(UserPojo.class)
		        .to("direct:newUser");
		
		from("direct:newUser").log("Result from endpoint -> ${body}")
		.process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				String dataString = (String) exchange.getIn().getBody();
				System.out.println(dataString);
				JSONObject jsonObject = new JSONObject(dataString);
				System.out.println(jsonObject);
				ObjectMapper oMapper = new ObjectMapper();
				//UserPojo data = (UserPojo) exchange.getIn().getBody();
				UserPojo staff1 = oMapper.readValue(jsonObject.toString(), UserPojo.class);
				
				@SuppressWarnings("unchecked")
				Map<String, Object> convertedData = oMapper.convertValue(staff1, Map.class);

				exchange.getIn().setBody(convertedData);
			}
		})
		.marshal().csv()
		.log("After converting ${body}")
		.setHeader(Exchange.FILE_NAME, constant("stream.csv"))
		.to("file:outbox?fileExist=Append");
	}

}
