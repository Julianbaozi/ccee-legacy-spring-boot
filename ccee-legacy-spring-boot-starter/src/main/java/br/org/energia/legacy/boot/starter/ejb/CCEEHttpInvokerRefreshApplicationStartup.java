package br.org.energia.legacy.boot.starter.ejb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;

/**
 * Atualiza urls dos serviços expostos após o contexto estar todo carregado.
 * @author vscarin
 *
 */
@Component
public class CCEEHttpInvokerRefreshApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(CCEEHttpInvokerRefreshApplicationStartup.class);
	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		LOG.info("ApplicationReadyEvent - inicio");
		LOG.info("refresh beannameUrlHandlerMapping");
		BeanNameUrlHandlerMapping bean = applicationContext.getBean(BeanNameUrlHandlerMapping.class);
		bean.initApplicationContext();
		LOG.info("ApplicationReadyEvent - fim");
	}
}