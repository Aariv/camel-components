/**
 * 
 */
package com.denali.product.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * @author zentere
 *
 */
@Component
public class FileRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("netty:tcp://0.0.0.0:4001?textline=true&clientMode=false")
			.pipeline()
				.bean(Common.class, "validateOrder")
				.log("output from validateOrder ${body}")
				.bean(Common.class, "registerOrder")
				.log("output from registerOrder ${body}")
				.bean(Common.class, "sendConfirmEmail")
				.log("output from sendConfirmEmail ${body}");
	}

}

