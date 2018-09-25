package br.org.energia.legacy.boot.starter.scan;

import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerResolver;

/**
 * Classe responsável por alterar o Parser da tag jee do xml
 * 
 * @author vscarin
 */
public class CCEENamespaceHandlerResolver implements NamespaceHandlerResolver {

	private static final CCEELegacyHandler CCEE_LEGACY_HANDLER = new CCEELegacyHandler();

	private static final String JEE_NAMESPACE = "http://www.springframework.org/schema/jee";
	private NamespaceHandlerResolver resolver;

	public CCEENamespaceHandlerResolver(NamespaceHandlerResolver resolver) {
		this.resolver = resolver;
	}

	public NamespaceHandlerResolver getResolver() {
		return resolver;
	}

	@Override
	public NamespaceHandler resolve(String namespaceUri) {
		NamespaceHandler handler = null;
		if (JEE_NAMESPACE.equals(namespaceUri)) {
			handler = CCEE_LEGACY_HANDLER;
		} else {
			handler = resolver.resolve(namespaceUri);
		}
		return handler;
	}

}
