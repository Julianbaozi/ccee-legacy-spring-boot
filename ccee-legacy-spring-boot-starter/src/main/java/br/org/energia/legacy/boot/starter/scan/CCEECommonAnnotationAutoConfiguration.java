package br.org.energia.legacy.boot.starter.scan;

import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe auxilia para gerar nome de beans no spring.
 * @author valdir.scarin
 */
@Configuration
public class CCEECommonAnnotationAutoConfiguration {

	@Bean(AnnotationConfigUtils.COMMON_ANNOTATION_PROCESSOR_BEAN_NAME)
	public final static CCEECommonAnnotationBeanPostProcessor cceeCommonAnnotationBeanPostProcessor() {
		return new CCEECommonAnnotationBeanPostProcessor();
	}

	@Bean
	public final static CCEELegacyScanRegistryPostProcessor cceeLegacyScanRegistryPostProcessor() {
		return new CCEELegacyScanRegistryPostProcessor();
	}
	
}