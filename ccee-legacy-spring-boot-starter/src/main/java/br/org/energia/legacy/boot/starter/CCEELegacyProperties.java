package br.org.energia.legacy.boot.starter;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import br.org.energia.legacy.boot.starter.ejb.CCEELegacyHttpInvokerProperties;
import br.org.energia.legacy.boot.starter.jms.CCEEJmsProperties;
import br.org.energia.legacy.boot.starter.persistence.CCEEPersistenceProperties;
import br.org.energia.legacy.boot.starter.web.CCEELegacyWebProperties;
import br.org.energia.legacy.boot.starter.ws.SoapWebServiceProperties;

/**
 * Registrador de serviços remotos para serem consumidos por módulos externos.
 *
 * @author vscarin
 */
@ConfigurationProperties(prefix = "ccee.legacy")
public class CCEELegacyProperties implements InitializingBean, ApplicationContextAware {

	@Autowired
	private static ApplicationContext applicationContext;
	
	@NestedConfigurationProperty
	private SoapWebServiceProperties soap = new SoapWebServiceProperties();

	@NestedConfigurationProperty
	private CCEELegacyWebProperties web = new CCEELegacyWebProperties();

	@NestedConfigurationProperty
	private CCEELegacyHttpInvokerProperties httpInvoker  = new CCEELegacyHttpInvokerProperties();

	@NestedConfigurationProperty
	private CCEEJmsProperties jms = new CCEEJmsProperties();

	@NestedConfigurationProperty
	private Collection<CCEEPersistenceProperties> persistences = new ArrayList<>();

	private String basePackage;
	
	private String importResourcePath;

	public CCEELegacyHttpInvokerProperties getHttpInvoker() {
		return httpInvoker;
	}

	public void setHttpInvoker(CCEELegacyHttpInvokerProperties httpInvokerProperties) {
		this.httpInvoker = httpInvokerProperties;
	}

	public CCEEJmsProperties getJms() {
		return jms;
	}

	public void setJms(CCEEJmsProperties jmsProperties) {
		this.jms = jmsProperties;
	}
	
	public SoapWebServiceProperties getSoap() {
		return soap;
	}
	
	public void setSoap(SoapWebServiceProperties soap) {
		this.soap = soap;
	}

	public Collection<CCEEPersistenceProperties> getPersistences() {
		return persistences;
	}

	public void setPersistence(Collection<CCEEPersistenceProperties> persistences) {
		this.persistences = persistences;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}
	
	public String getImportResourcePath() {
		return importResourcePath;
	}
	
	public void setImportResourcePath(String importResourcePath) {
		this.importResourcePath = importResourcePath;
	}

	public CCEELegacyWebProperties getWeb() {
		return web;
	}

	public void setWeb(CCEELegacyWebProperties web) {
		this.web = web;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		httpInvoker.setApplicationContext(getApplicationContext());
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		applicationContext = ac;
	}

}
