package br.org.energia.legacy.boot.starter.scan;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.NamespaceHandlerResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * Classe que é responsável por criar interpretador de xml especifico para os projetos da CCEE
 * @author vscarin
 */
public class CCEEXmlBeanDefinitionReader extends XmlBeanDefinitionReader {

	@Override
	protected NamespaceHandlerResolver createDefaultNamespaceHandlerResolver() {
		return new CCEENamespaceHandlerResolver(super.createDefaultNamespaceHandlerResolver());
	}
	
	public CCEEXmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
		super(registry);
	}
	
}