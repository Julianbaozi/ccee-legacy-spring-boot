package br.org.energia.legacy.boot.starter.jms;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import br.org.energia.legacy.boot.starter.test.CCEETestDataManagerAutoConfiguration;

@Configuration
@ConditionalOnWebApplication
@ConditionalOnMissingBean(CCEETestDataManagerAutoConfiguration.class)
@ConditionalOnProperty(prefix = "ccee.legacy.jms", name = "enabled-listener", havingValue = "true") 
public class CCEEJmsAutoConfiguration {

	@Bean
	public final static CCEEJmsBeanPostProcessor jmsBeanPostProcessor() {
		return new CCEEJmsBeanPostProcessor();
	}

	

}