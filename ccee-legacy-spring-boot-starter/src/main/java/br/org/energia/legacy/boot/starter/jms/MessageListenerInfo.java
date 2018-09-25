package br.org.energia.legacy.boot.starter.jms;

import javax.jms.MessageListener;

/**
 * Estrutura para recupar informações dos messagelistener a registrar
 * @author jelizondo
 *
 */
public class MessageListenerInfo {

	private String id;
	private String destination;
	private MessageListener messageListener;

	public MessageListenerInfo() {
	}

	public MessageListenerInfo(String mappedName, String destination, Object bean) {
		super();
		this.id = mappedName;
		this.destination = destination;
		this.messageListener = (MessageListener) bean;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destionation) {
		this.destination = destionation;
	}

	public MessageListener getMessageListener() {
		return messageListener;
	}

	public void setMessageListener(MessageListener messageListener) {
		this.messageListener = messageListener;
	}

}
