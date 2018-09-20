package com.denali.product.routes;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

/**
 * 
 * @author zentere
 *
 */
@Component
public class Common {
	public void sendConfirmEmail(Exchange exchange) {
		String data = exchange.getIn().getBody(String.class);
		System.out.println(data);
	}

	public void registerOrder(Exchange exchange) {
		String data = exchange.getIn().getBody(String.class);
		System.out.println(data);
	}

	public void validateOrder(Exchange exchange) {
		String data = exchange.getIn().getBody(String.class);
		System.out.println(data);
	}
}