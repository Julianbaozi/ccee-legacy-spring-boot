package br.org.energia.legacy.boot.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

import br.org.energia.legacy.boot.starter.ejb.CCEEHttpInvokerAutoConfiguration;
import br.org.energia.legacy.boot.starter.jms.CCEEJmsAutoConfiguration;
import br.org.energia.legacy.boot.starter.persistence.CCEEPersistenceAutoConfiguration;
import br.org.energia.legacy.boot.starter.scan.CCEECommonAnnotationAutoConfiguration;
import br.org.energia.legacy.boot.starter.test.CCEETestDataManagerAutoConfiguration;
import br.org.energia.legacy.boot.starter.web.RedisHttpSessionConfig;
import br.org.energia.legacy.boot.starter.web.WebApplicationAutoConfiguration;
import br.org.energia.legacy.boot.starter.ws.SoapWebServiceAutoConfiguration;
import br.org.energia.spring.security.CCEESecuritySpringBoot;
import br.org.energia.spring.security.WebSecurityConfig;

/**
 * Classe padrão para inicializar Spring Boot de aplicações JEE da CCEE
 *  
 * @author vscarin
 */
@SpringBootApplication
@EnableConfigurationProperties({ CCEELegacyProperties.class })
@Import({ CCEEJmsAutoConfiguration.class, RedisHttpSessionConfig.class, WebApplicationAutoConfiguration.class, SoapWebServiceAutoConfiguration.class
	, CCEEPersistenceAutoConfiguration.class, CCEECommonAnnotationAutoConfiguration.class, WebSecurityConfig.class
	, CCEEHttpInvokerAutoConfiguration.class, CCEETestDataManagerAutoConfiguration.class })
public class CCEELegacyApplicationStarter extends SpringBootServletInitializer {

	public static void main(String[] args) {
		System.setProperty("ccee.security.impl", CCEESecuritySpringBoot.class.getCanonicalName());
		String[] newArgs = new String[args.length+1];
		System.arraycopy(args, 0, newArgs, 0, args.length);
		newArgs[newArgs.length-1] = "banner.location=classpath:/banner.txt";
		SpringApplication.run(CCEELegacyApplicationStarter.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CCEELegacyApplicationStarter.class);
	}

}