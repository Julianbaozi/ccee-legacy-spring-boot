package br.org.energia.legacy.boot.starter.ejb;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

public class CCEEHttpInvokerProxyFactoryBean extends HttpInvokerProxyFactoryBean {

	@Override
	public Object getObject() {
		return ProxyFactory.getProxy(getServiceInterface(), new CCEEHttpClient(this, super.getObject()));
	}

}
