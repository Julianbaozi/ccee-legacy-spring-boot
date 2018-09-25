package br.org.energia.legacy.boot.starter.jms;

import javax.jms.ConnectionFactory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
/*import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;*/
import org.springframework.util.ErrorHandler;

/**
 * Classe auxilia para configurar conversores e tratamento de exceção
 * @author jelizondo  
 */
//@EnableTransactionManagement
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "ccee.legacy.jms", name = "enabled-listener", havingValue = "true") 
public class CCEEJmsConfig {
	
	
	
	/*@Bean
	public JmsTemplate jmsTemplate() throws Throwable{
		JmsTemplate jmsTemplate = new JmsTemplate(xaFactory());
		jmsTemplate.setSessionTransacted(true);
		return jmsTemplate;
	}	*/	
	
	//@Autowired
	//private Environment environment;
	
	/*@Bean(initMethod = "init", destroyMethod = "close")
	public ConnectionFactory xaFactory() {
		
		ActiveMQXAConnectionFactory activeMQXAConnectionFactory = new ActiveMQXAConnectionFactory();
		activeMQXAConnectionFactory.setBrokerURL(environment.getProperty( "spring.activemq.broker-url")  );
		activeMQXAConnectionFactory.setUserName(environment.getProperty( "spring.activemq.user")  );
		activeMQXAConnectionFactory.setPassword(environment.getProperty( "spring.activemq.password")  );
		AtomikosConnectionFactoryBean atomikosConnectionFactoryBean = new AtomikosConnectionFactoryBean();
		atomikosConnectionFactoryBean.setUniqueResourceName("xamq");
		atomikosConnectionFactoryBean.setLocalTransactionMode(false);
		atomikosConnectionFactoryBean.setXaConnectionFactory(activeMQXAConnectionFactory);
		
		return atomikosConnectionFactoryBean;
	}*/
	
	
//    @Bean // Serialize message content to json using TextMessage
//    public MessageConverter jacksonJmsMessageConverter() {
//        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//        converter.setTargetType(MessageType.TEXT);
//        converter.setTypeIdPropertyName("_type");
//        return converter;
//    }
	
	/**
	 * Seta Error Handler para o connectionFactory
	 * @param connectionFactory
	 * @param configurer
	 * @return
	 */
	@Bean
	public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		
		//factory.setMessageConverter(jacksonJmsMessageConverter());
		
		//factory.setSessionTransacted(true);
		//factory.setConnectionFactory(connectionFactory);
		//factory.setTransactionManager(transactionManager);
		
		// anonymous class
		factory.setErrorHandler(new ErrorHandler() {
			@Override
			public void handleError(Throwable t) {
				System.err.println("An error has occurred in the transaction");
			}
		});
		// lambda function
		factory.setErrorHandler(t -> System.err.println("An error has	occurred in the transaction"));
		configurer.configure(factory, connectionFactory);
		
		
		return factory;
	}
	
}