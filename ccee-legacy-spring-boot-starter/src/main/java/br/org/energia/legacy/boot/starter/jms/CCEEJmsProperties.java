package br.org.energia.legacy.boot.starter.jms;

/**
 * Propriedades para Jms
 * 
 * @author jelizondo
 */
@Deprecated
public class CCEEJmsProperties {

	private Boolean enabledListeners = Boolean.FALSE;

	public Boolean isEnabledListeners() {
		return enabledListeners;
	}

	public void setEnabledListeners(Boolean enableListeners) {
		this.enabledListeners = enableListeners;
	}

}