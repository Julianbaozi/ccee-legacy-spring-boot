package br.org.energia;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import br.org.energia.legacy.boot.starter.CCEELegacyProperties;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CCEEPropertiesTest.FakeBoot.class })
@Component
@Ignore //cant run two tests in same vm
public class CCEEPropertiesTest {

	@Autowired
	private CCEELegacyProperties props;

	public CCEEPropertiesTest() {
	}

	@SpringBootApplication
	@EnableConfigurationProperties({ CCEELegacyProperties.class })
	@ComponentScan("br.org.energia.legacy")
	public static class FakeBoot {

	}

	@Test
	public void testStart() {
		// from application.yml
		assertNotNull(props);
		assertNotNull(props.getHttpInvoker());
		assertNotNull(props.getHttpInvoker().getApplicationsHost());
		assertTrue(props.getHttpInvoker().getApplicationsHost().size() > 0);
		// from /META-INF/context.properties
		assertTrue(props.getHttpInvoker().getApplicationPrefix().size() > 0);
	}

}
