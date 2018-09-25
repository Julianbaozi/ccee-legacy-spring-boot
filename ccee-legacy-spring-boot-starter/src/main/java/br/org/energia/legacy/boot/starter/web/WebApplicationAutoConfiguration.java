package br.org.energia.legacy.boot.starter.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

import com.opensymphony.sitemesh.webapp.SiteMeshFilter;

@Configuration
@ConditionalOnWebApplication
public class WebApplicationAutoConfiguration {
	
	@Bean
	@ConditionalOnProperty(prefix = "ccee.legacy.web", name = "enabled-id-header-filter", havingValue = "true")
	public CCEEIdHeaderFilter CCEEIdHeaderFilter() {
		return new CCEEIdHeaderFilter();
	}

	@Bean
	@ConditionalOnProperty(prefix = "ccee.legacy.web", name = "enabled-site-mesh", havingValue = "true")
	public FilterRegistrationBean siteMeshFilter(){
		FilterRegistrationBean filter = new FilterRegistrationBean();
		SiteMeshFilter siteMeshFilter = new SiteMeshFilter();
		filter.setFilter(siteMeshFilter);
		return filter;
	}
	
	@Bean
	public FilterRegistrationBean registerOpenEntityManagerInViewFilterBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		OpenEntityManagerInViewFilter filter = new OpenEntityManagerInViewFilter();
		registrationBean.setFilter(filter);
		registrationBean.setOrder(5);
		return registrationBean;
	}

	@ConditionalOnProperty(prefix = "ccee.legacy.web", name = "enabled-portal", havingValue = "true")
	@ComponentScan("br.org.energia.portal")
	public class ComponentScanPortal {
	}

}