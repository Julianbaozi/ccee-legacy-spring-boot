package br.org.energia.legacy.boot.starter.scan;

import static br.org.energia.spring.utils.BeanPostProcessorUtils.firstCharLower;
import static br.org.energia.spring.utils.BeanPostProcessorUtils.firstCharUpper;
import static br.org.energia.spring.utils.BeanPostProcessorUtils.tryDiscoveryJndiName;

import org.apache.cxf.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.w3c.dom.Element;

/**
 * Classe responsável por analisar a tag jee e criar uma referencia de um bean
 * existentes no contexto do spring do tipo indicado para o id indicado.
 * 
 * @author vscarin
 */
public class CCEELegacyAliasDefinitionParser implements BeanDefinitionParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(CCEELegacyAliasDefinitionParser.class);

	private String[] referenceAttribute;
	
	public CCEELegacyAliasDefinitionParser(String... referenceAttribute) {
		this.referenceAttribute = referenceAttribute;
	}
	
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String alias = element.getAttribute("id");

		if (!parserContext.getRegistry().isBeanNameInUse(alias)) {
			
			for (String attr : referenceAttribute) {
				String reference = cleanJndName(element.getAttribute(attr));
				if (!"".equals(reference)) {
					Assert.notNull(alias, "Alias attribute must be filled.");
					Assert.notNull(reference, "Reference attribute must be filled.");
					
					String realReference = discoveryRealReference(reference, parserContext);
					
					LOGGER.info("CCEELegacy: registering alias " + alias + " to beanName " + realReference);
					parserContext.getRegistry().registerAlias(realReference, alias);
					break;
				}
			}
			
		} else {
			LOGGER.info("alias " + alias + " already in use");
		}

		return null;
	}

	private String cleanJndName(String jndi) {
		return jndi.replace("ejblocal:", "").replace("ejbremote:", "");
	}

	private String discoveryRealReference(String reference, ParserContext parserContext) {
		String realReference = reference;

		//System.out.println("CC: " + reference);

		if (ClassUtils.isPresent(reference, getClass().getClassLoader())) {
			realReference = null;
			try {
				Class<?> referenceClass = ClassUtils.forName(reference, getClass().getClassLoader());
				realReference = tryDiscoveryJndiName(referenceClass);
				if (StringUtils.isEmpty(realReference)) {
					realReference = firstCharLower(referenceClass.getSimpleName());
				}
			} catch (ClassNotFoundException | LinkageError e) {
				throw new IllegalStateException("Error when processing 'jndi-lookup' " + reference, e);
			}
		} else if (parserContext.getRegistry().isBeanNameInUse(reference)) {
			realReference = reference;
		} else if (!parserContext.getRegistry().isBeanNameInUse(reference)) {
			if (reference.contains("/")) {
				String[] split = reference.split("\\/");
				String ref = firstCharUpper(split[split.length - 1]);
				if (parserContext.getRegistry().isBeanNameInUse(ref)) {
					realReference = ref;
				} else if (parserContext.getRegistry().isBeanNameInUse(ref + "Local")) {
					realReference = ref + "Local";
				}
			}
		}

		return realReference;
	}
}