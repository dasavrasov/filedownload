package com.example;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("timer:foo?fixedRate=true&period=5000")
		.to("http://fssprus.ru/files/udostov/8c93865786bd780c76fd89856c76f3679a68831d.crl")
		.log("my response")
		.to("file:D:\\!out?fileName=8c93865786bd780c76fd89856c76f3679a68831d.crl");		
	}

}
