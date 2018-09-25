package br.org.energia.legacy.boot.starter.scan;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
/**
 * Altera o modo com a interpretação da tag jndi:lookup
 *
 * @author vscarin
 */
public class CCEELegacyHandler extends NamespaceHandlerSupport {
	
	public CCEELegacyHandler() {
		init();
	}
	
    public void init() {
        registerBeanDefinitionParser("jndi-lookup", new CCEELegacyAliasDefinitionParser("jndi-name"));
        registerBeanDefinitionParser("remote-slsb", new CCEELegacyAliasDefinitionParser("business-interface"));
    }
}