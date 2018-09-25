package br.org.energia.legacy.boot.starter.scan;

import javax.ejb.Stateless;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;

import br.org.energia.legacy.boot.starter.CCEELegacyProperties;
import br.org.energia.spring.utils.CCEENameGenerator;

/**
 * Classe responsável por carregar pacotes base do módulo.
 * 
 * @author vscarin
 */
public class CCEELegacyScanRegistryPostProcessor
		implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

	private ApplicationContext applicationContext;
	private static final Logger log = LoggerFactory.getLogger(CCEELegacyScanRegistryPostProcessor.class);

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		String basePackageProp = applicationContext.getEnvironment().getProperty("ccee.legacy.base-package");
		if (!StringUtils.isEmpty(basePackageProp)) {
			String[] basePackages = basePackageProp.split(",");
			log.debug("scanning base package " + basePackageProp);
			ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
			scanner.addIncludeFilter(new AnnotationTypeFilter(Stateless.class));
			scanner.addIncludeFilter(new AnnotationTypeFilter(WebService.class));
			scanner.setBeanNameGenerator(new CCEENameGenerator());
			scanner.scan(basePackages);
		}

		String paths = applicationContext.getEnvironment().getProperty("ccee.legacy.import-resource-path");
		if (paths == null) {
			return;
		}
		String[] split = paths.split(",");
		for (String path : split) {
			if (!StringUtils.isEmpty(path)) {
				log.debug("load import resource " + path);
				XmlBeanDefinitionReader beanDefinitionReader = new CCEEXmlBeanDefinitionReader(registry);
				beanDefinitionReader.setEnvironment(applicationContext.getEnvironment());
				beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(applicationContext));
				beanDefinitionReader.setResourceLoader(applicationContext);
				beanDefinitionReader.loadBeanDefinitions(path);
			}
		}

	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// nothing to do
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}