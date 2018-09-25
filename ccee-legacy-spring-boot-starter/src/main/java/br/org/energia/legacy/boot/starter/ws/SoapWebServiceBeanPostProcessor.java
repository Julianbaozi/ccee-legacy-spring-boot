package br.org.energia.legacy.boot.starter.ws;

import static br.org.energia.spring.utils.BeanPostProcessorUtils.getAnnotatedClass;
import static br.org.energia.spring.utils.BeanPostProcessorUtils.getBeanType;

import javax.jws.WebService;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;

import br.org.energia.legacy.boot.starter.CCEELegacyProperties;

/**
 * Registrador de web services para serem consumidos por módulos externos.
 * 
 * @author vscarin
 */
public class SoapWebServiceBeanPostProcessor implements MergedBeanDefinitionPostProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(SoapWebServiceBeanPostProcessor.class);

	@Autowired
	private CCEELegacyProperties props;

	@Autowired
	private Bus bus;

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	public ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ConfigurableApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public Bus getBus() {
		return bus;
	}

	public void setBus(Bus bus) {
		this.bus = bus;
	}

	public CCEELegacyProperties getProps() {
		return props;
	}

	public void setProps(CCEELegacyProperties props) {
		this.props = props;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (getProps().getSoap().isEnabledServices()) {
			Class<?> webServiceClass = getAnnotatedClass(getBeanType(bean), WebService.class);
			if (webServiceClass != null) {
				registerService(bean, webServiceClass);
			}
		}
		return bean;
	}

	private void registerService(Object bean, Class<?> type) {
		EndpointImpl endpoint = new EndpointImpl(getBus(), bean);
		endpoint.publish("/" + type.getSimpleName());
		String beanName = type.getName() + "SoapWebServiceEndpoint";
		getApplicationContext().getBeanFactory().registerSingleton(beanName, endpoint);

		LOG.info("CCEELegacy: " + beanName + " was registered as SoapService to url [/" + type.getSimpleName() + "]");
	}

	@Override
	public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
	}

}