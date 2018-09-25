package org.springframework.boot.context.embedded;

import javax.servlet.Servlet;

/**
 * Classe cria para manter compatibilidade com a versão do cxf-jaxws-spring-boot-starter
 * @author vscarin
 *
 */
public class ServletRegistrationBean extends  org.springframework.boot.web.servlet.ServletRegistrationBean {

	public ServletRegistrationBean() {
		super();
	}

	public ServletRegistrationBean(Servlet servlet, boolean alwaysMapUrl, String... urlMappings) {
		super(servlet, alwaysMapUrl, urlMappings);
	}

	public ServletRegistrationBean(Servlet servlet, String... urlMappings) {
		super(servlet, urlMappings);
	}
}
