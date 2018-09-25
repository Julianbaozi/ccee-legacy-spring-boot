package br.org.energia.legacy.boot.starter.ws;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(name="ccee.legacy.soap.enabled-services")
public class SoapWebServiceAutoConfiguration {

	@Bean
	public final static SoapWebServiceBeanPostProcessor soapWebServiceBeanPostProcessor() {
		return new SoapWebServiceBeanPostProcessor();
	}


}