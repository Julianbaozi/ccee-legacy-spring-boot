package br.org.energia.legacy.boot.starter.ejb;

import static br.org.energia.spring.utils.BeanPostProcessorUtils.getAnnotatedClass;
import static br.org.energia.spring.utils.BeanPostProcessorUtils.getBeanType;
import static br.org.energia.spring.utils.BeanPostProcessorUtils.isClassInBasePackage;
import static br.org.energia.spring.utils.BeanPostProcessorUtils.tryDiscoveryJndiName;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

import br.org.energia.legacy.boot.starter.CCEELegacyProperties;

/**
 * Registrador de serviços remotos para serem consumidos por modulos externos.
 *
 * @author vscarin
 */
public class CCEEEjbPostProcessor implements MergedBeanDefinitionPostProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(CCEEEjbPostProcessor.class);
	
	private int order = Ordered.LOWEST_PRECEDENCE - 1;

	@Autowired
	private CCEELegacyProperties props;

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	public ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	/**
	 * Registra cliente para requisição de serviços de outros módulos
	 * 
	 * @param field
	 * @param type
	 */
	protected void registerHttpInvokerClient(Field field, Class<?> type) {
		String[] names = getApplicationContext().getBeanNamesForType(type);
		if (names == null || names.length == 0) {
			HttpInvokerProxyFactoryBean client = prepareClientFactory(type);
			String beanName = getClientBeanName(type);

			if (beanName != null) {
				getApplicationContext().getBeanFactory().registerSingleton(beanName, client);
				getApplicationContext().getBeanFactory().registerAlias(beanName, type.getCanonicalName());
			} else {
				getApplicationContext().getBeanFactory().registerSingleton(type.getCanonicalName(), client);
			}

			registerAlias(field, beanName);
			LOG.info("CCEELegacy: Created " + beanName + " was registered as invoker client accessing the url ["
					+ client.getServiceUrl() + "]");
		}
	}

	/**
	 * Prepare http invoker client to remote type
	 * 
	 * @param type
	 * @return
	 */
	protected HttpInvokerProxyFactoryBean prepareClientFactory(Class<?> type) {
		HttpInvokerProxyFactoryBean client = new CCEEHttpInvokerProxyFactoryBean();
		client.setServiceUrl(getRemoteUrl(type));
		client.setServiceInterface(type);
		client.afterPropertiesSet();
		return client;
	}

	/**
	 * Register alias to field if it's annotation Qualifier.class
	 * 
	 * @param field
	 * @param beanName
	 */
	protected void registerAlias(Field field, String beanName) {
		if (field.isAnnotationPresent(Qualifier.class)) {
			String alias = field.getAnnotation(Qualifier.class).value();
			if (!getApplicationContext().getBeanFactory().containsBean(alias)) {
				getApplicationContext().getBeanFactory().registerAlias(beanName, alias);
			}
		}
	}

	protected String getClientBeanName(Class<?> type) {
		String beanName = tryDiscoveryJndiName(type);
		if (beanName == null) {
			LOG.info("Not possible discovery " + type + " JNDI. Use beanName " + beanName);
		}
		return beanName;
	}

	protected String getRemoteUrl(Class<?> type) {
		return getUrlPrefixBySystem(type)
				+ getApplicationUrPrefix(type, props.getHttpInvoker().getApplicationPrefix())
				+ getUrlSufixByType(type);
	}

	protected String getUrlPrefixBySystem(Class<?> type) {
		String urlPrefixBySystem = getApplicationUrl(type, props.getHttpInvoker().getApplicationsHost());
		if (urlPrefixBySystem == null) {
			throw new IllegalStateException(
					"Property url to connect remote service not registered - application = " + type);
		}

		return urlPrefixBySystem;
	}

	protected String getApplicationUrPrefix(Class<?> type, Map<String, String> map) {
		Optional<Entry<String, String>> retorno = map.entrySet().stream()
				.filter(a -> type.getCanonicalName().startsWith(a.getKey())).findFirst();
		String value = "/";
		if (retorno.isPresent()) {
			value = retorno.get().getValue();
		} else {
			LOG.error("Revise ccee.legacy.httpinvoker.applications-host, nao ha registro para este pacote/sistema: " + type.getPackage());
		}
		return value;
	}	
	
	protected String getApplicationUrl(Class<?> type, List<ChaveValor> entrySet) {
		Optional<ChaveValor> retorno = entrySet.stream()
				.filter(a -> type.getCanonicalName().startsWith(a.getChave())).findFirst();
		String value = "/";
		if (retorno.isPresent()) {
			value = retorno.get().getValor();
		}
		return value;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

		if (props.getHttpInvoker().isEnabledServices()) {
			String[] basePackages = props.getBasePackage().split(",");

			Class<?> remoteClass = getAnnotatedClass(getBeanType(bean), Remote.class);
			if (isClassInBasePackage(basePackages, remoteClass)) {
				LOG.trace("Remote>:" + remoteClass.getName());
				registerService(bean, remoteClass);
			}
			
			Class<?> statelessClass = getAnnotatedClass(getBeanType(bean), Stateless.class);
			if (isClassInBasePackage(basePackages, statelessClass)) {
				LOG.trace("Stateless>:" + statelessClass.getName());
				registerInterceptors(statelessClass);
			}
		}
		return bean;
	}

	private void registerInterceptors(Class<?> remoteClass) {
		Interceptors annotation = remoteClass.getAnnotation(Interceptors.class);
		if (annotation == null)
			return;
		List<Class<?>> interceptorsList = Arrays.asList(annotation.value());
		interceptorsList.forEach(interceptorClass -> {
			try {
				if (!applicationContext.containsBean(interceptorClass.getCanonicalName())) {
					getApplicationContext().getBeanFactory().registerSingleton(interceptorClass.getCanonicalName(),
							interceptorClass.newInstance());
					LOG.info("CCEELegacy: " + interceptorClass.getCanonicalName()
							+ " was registered as interceptor bean.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error(remoteClass.getName() + " couldn't be instanciated.");
			}
		});

	}

	/**
	 * Cria Http Invoker Client para EJB de fora do modulo
	 */
	@Override
	public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class beanType, String beanName) {

		String[] basePackages = props.getBasePackage().split(",");
		
		if (props.getHttpInvoker().isEnabledClients()) {
			Field[] fields = beanType.getDeclaredFields();
			if (fields != null && fields.length > 0) {
				for (Field field : fields) {
					Class<?> remoteClass = getAnnotatedClass(field.getType(), Remote.class);
					if (!isClassInBasePackage(basePackages, remoteClass)) {
						registerHttpInvokerClient(field, remoteClass);
					}
				}
			}
		}
	}

	/**
	 * Cria e registra o serviço remoto do httpInvoker.
	 * 
	 * @param bean
	 * @param remoteInterface
	 */
	private void registerService(Object bean, Class<?> remoteInterface) {
		String url = getLocalUrl(remoteInterface);
		if (!getApplicationContext().containsBean(url)) {
			HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
			exporter.setService(bean);
			exporter.setServiceInterface(remoteInterface);
			exporter.afterPropertiesSet();
			getApplicationContext().getBeanFactory().registerSingleton(url, exporter);
			LOG.info("CCEELegacy:" + remoteInterface.getName()
					+ " was registered as spring http invoker server to be accessed by url [" + url + "]");

		}
	}

	/**
	 * Determina a url do serviço
	 * 
	 * @param type
	 * @return
	 */
	private String getLocalUrl(Class<?> type) {
		return props.getHttpInvoker().getLocalContextPrefix() + getUrlSufixByType(type);
	}

	/**
	 * Retorna url unica por serviço
	 * 
	 * @param type
	 * @return
	 */
	private String getUrlSufixByType(Class<?> type) {
		return "/" + type.getName().replace(".", "/");
	}
	
	public int getOrder() {
		return order;
	}

}