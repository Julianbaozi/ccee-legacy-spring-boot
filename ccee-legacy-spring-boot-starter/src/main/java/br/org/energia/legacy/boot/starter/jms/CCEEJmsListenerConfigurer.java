package br.org.energia.legacy.boot.starter.jms;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;

/**
 *
 * Programmatic endpoints registration
 * 
 * @author jelizondo
 *
 */
@Configuration
@ConditionalOnProperty(prefix = "ccee.legacy.jms", name = "enabled-listener", havingValue = "true")
public class CCEEJmsListenerConfigurer implements JmsListenerConfigurer {

	@Autowired
	private ApplicationContext context;

	@SuppressWarnings("unchecked")
	@Override
	public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
		// habilita quais pacotes podem ser usados para deserializar uma mensagem.
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");
		//Assert.isTrue(context.containsBean("endpoints"), "ESTA HABILITADO JMS LISTENERS POREM NAO FOI ENCONTRADO NENHUM.");
		if (!context.containsBean("endpoints")) {
			return;
		}
		List<MessageListenerInfo> messageListenerInfos = (List<MessageListenerInfo>) context.getBean("endpoints");

		if (messageListenerInfos == null || messageListenerInfos.isEmpty()) {
			return;
		}
		messageListenerInfos.forEach(messageListenerInfo -> {
			SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
			endpoint.setId((new Date()).getTime() + messageListenerInfo.getId());
			endpoint.setDestination(messageListenerInfo.getDestination());
			endpoint.setMessageListener(messageListenerInfo.getMessageListener());// existing mdb
			
			//endpoint.set
			registrar.registerEndpoint(endpoint);
		});

	}
}