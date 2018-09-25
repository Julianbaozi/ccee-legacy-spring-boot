package br.org.energia.legacy.boot.starter.ejb;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CCEEHttpClient implements MethodInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CCEEHttpClient.class);

	private CCEEHttpInvokerProxyFactoryBean factoryBean;
	private Object object;

	public CCEEHttpClient(CCEEHttpInvokerProxyFactoryBean factoryBean, Object object) {
		this.factoryBean = factoryBean;
		this.object = object;
	}

	public CCEEHttpInvokerProxyFactoryBean getHttInvokerProxyFactoryBean() {
		return factoryBean;
	}

	public void setHttInvokerProxyFactoryBean(CCEEHttpInvokerProxyFactoryBean httInvokerProxyFactoryBean) {
		this.factoryBean = httInvokerProxyFactoryBean;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		String serviceName = invocation.getMethod().getDeclaringClass() + "." + invocation.getMethod().getName();
		LOGGER.debug(serviceName + " - inicio");
		long start = System.currentTimeMillis();
		Object value = null;
		try {
			value = invocation.getMethod().invoke(getObject(), invocation.getArguments());
		} catch (Exception e) {
			LOGGER.error(factoryBean.getServiceUrl(), e);
			throw e;
		}
		long duration = System.currentTimeMillis() - start;
		LOGGER.debug(serviceName + " [" + duration + "] ms - fim");
		return value;
	}

}
