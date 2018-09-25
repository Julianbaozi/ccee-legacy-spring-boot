package br.org.energia.legacy.boot.starter.ejb;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.org.energia.legacy.boot.starter.test.CCEETestDataManagerAutoConfiguration;

/**
 * Registrador de serviços remotos para serem consumidos por módulos externos.
 *
 * @author vscarin
 */
@Configuration
@ConditionalOnMissingBean(CCEETestDataManagerAutoConfiguration.class)
public class CCEEHttpInvokerAutoConfiguration {

	@Bean
	public static final CCEEEjbPostProcessor cceeHttpInvokerPostProcessor() {
		return new CCEEEjbPostProcessor();
	}
	
}