/**
 * 
 */
package com.denali.product.routes;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.XStreamDataFormat;
import org.springframework.stereotype.Component;

import com.denali.product.model.UserPojo;

/**
 * @author zentere
 *
 */
@Component
public class MultipleOutputRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:csvinput")
			.process(new Processor() {
				
				@Override
				public void process(Exchange exchange) throws Exception {
					String newBody = exchange.getIn().getBody(String.class);
			        StringTokenizer tokenizer = new StringTokenizer(newBody, ",");
			        UserPojo employee = new UserPojo();
			        while (tokenizer.hasMoreElements()){
			            employee.setId((int) tokenizer.nextElement());
			            employee.setName((String) tokenizer.nextElement());
			        }
			        exchange.getIn().setBody(employee);
				}
			}).marshal(populateStreamDef())
            .to("log:?level=INFO&showBody=true")
            .log("Message after conversion ${body}")
            .to("mock:output");
	}

	private XStreamDataFormat populateStreamDef() {
        XStreamDataFormat xstreamDefinition = new XStreamDataFormat();
        Map<String, String> aliases = new HashMap<String, String>();
        aliases.put("User", UserPojo.class.getName());
        xstreamDefinition.setAliases(aliases);
        return xstreamDefinition;
    }
}
