package br.org.energia;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import br.org.energia.legacy.boot.starter.CCEELegacyApplicationStarter;
import ccee.legacy.test.ejb.FakeStateless;
import ccee.legacy.test.ejb.TestFacade;

@Ignore
@Configuration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CCEELegacyApplicationStarter.class })
//@Import({Config.class})
public class CCEEStarterTest {

	@Autowired
	private ApplicationContext applicationContext;
	
	@Test
	public void testStart() {
		// from scan
		assertNotNull(applicationContext.getBean("ejb/FakeStatelessWithName"));
		assertNotNull(applicationContext.getBean("fakeStatelessWithAttribute"));
		assertNotNull(applicationContext.getBean("fakeStatelessWithOutAttribute"));
		assertNotNull(applicationContext.getBean("fakeStateless"));
		// from xml
		assertNotNull(applicationContext.getBean("TestFacade"));
		//
		TestFacade testFacade = (TestFacade) applicationContext.getBean("TestFacade");
		assertNotNull(testFacade.getFakeStateless());
		//
		assertNotNull(applicationContext.getBean("fakeMDB"));
		Object bean = applicationContext.getBean("ejb/AnotherFakeInterface");
		assertNotNull(bean);
		System.out.println(bean.getClass());
		//
		assertNotNull(applicationContext.getBean("ejb/BStatelessWithOutAttribute")); //BStatelessWithOutAttribute
		//
		
		//client definition (ignored by getBeanDefitionNames)
		//assertNotNull(applicationContext.getBean("http://localhost:3322/anothermodule/remote/ccee/legacy/anothermodule/ejb/AnotherFakeInterface"));
		//http invoker registed
		assertNotNull(applicationContext.getBean("/remote/ccee/legacy/test/ejb/FakeInterface"));
		//
		/*String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
		for (String string : beanDefinitionNames) {
			System.out.println(string + " - " + applicationContext.getBean(string).getClass().getName());
		}*/
		
		
	}

	@Test
	public void testSendJMS() {

		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");
		
		FakeStateless fakeStateless = (FakeStateless) applicationContext.getBean("fakeStateless");
		fakeStateless.testSendJMS();
		
	}
	/*
	@Autowired
	private StatusTestsApplicationListener listener;
	
	public static class SampleContextApplicationListener implements ApplicationListener<ApplicationContextEvent> {

	    private Map<String,ApplicationContext> contextMap = new Hashtable<String,ApplicationContext>();

	    @Override
	    public void onApplicationEvent(ApplicationContextEvent event) {
	        if( event instanceof ContextStartedEvent || event instanceof ContextRefreshedEvent){
	            this.getContextMap().put(event.getApplicationContext().getDisplayName(), event.getApplicationContext());
	        }

	    }

	    public Map<String,ApplicationContext> getContextMap() {
	        return contextMap;
	    }   
	}*/
	

}
