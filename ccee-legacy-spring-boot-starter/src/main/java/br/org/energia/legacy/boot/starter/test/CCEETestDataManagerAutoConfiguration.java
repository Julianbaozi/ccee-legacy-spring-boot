package br.org.energia.legacy.boot.starter.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

//import br.org.energia.test.integration.CCEEIntegrationTestConfiguration;

/**
 * Auto configuration of CCEE Test.  
 *
 * @author vscarin
 */
@Profile("integration-test")
@Configuration
//@Import(CCEEIntegrationTestConfiguration.class)
public class CCEETestDataManagerAutoConfiguration {
	
}