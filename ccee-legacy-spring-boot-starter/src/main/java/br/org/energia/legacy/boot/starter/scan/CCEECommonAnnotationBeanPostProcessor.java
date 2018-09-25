package br.org.energia.legacy.boot.starter.scan;

import java.util.LinkedHashSet;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;

/**
 * Classe auxilia para desconsiderar os atributos das anotações javax.ejb.EJB quando avaliadas pelo spring.
 * 
 * @author valdir.scarin
 */
public class CCEECommonAnnotationBeanPostProcessor extends CommonAnnotationBeanPostProcessor {

	private static final long serialVersionUID = 6280331542963992626L;
	/** Atributo bean factory. */
    private BeanFactory beanFactory;

    /** {@inheritDoc} */
    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        super.setBeanFactory(beanFactory);
        super.setJndiFactory(beanFactory);
    }

    /** {@inheritDoc} */
    protected Object getResource(final LookupElement element, final String requestingBeanName)
        throws BeansException {
        Object bean = null;

        //System.out.println("Ref: " + element + " " + requestingBeanName);
        
        try {
	        try {
	            bean = autowireResource(beanFactory, element, requestingBeanName);
	        } catch (final BeanNotOfRequiredTypeException e ) {
	        	bean = getResourceByType(element, requestingBeanName);
	        } catch (final BeansException e) {
	            if (element.getName() != null) {
	                logger.error(String.format(
	                        "Error to find bean by name [%s]. Try resolve by type of dependency",
	                        element.getName()));
	                bean = getResourceByType(element, requestingBeanName);
	            } else {
	                throw e;
	            }
	        }
        } catch (Exception e) {
        	logger.error(String.format(
        			"Error to find bean [%s]. Dependency will be set with null"
        			, element.getName()));
        }

        return bean;
    }

    /**
     * Nome: getResourceByType
     * Recupera o valor do atributo 'resourceByType'.
     *
     * @param element the element
     * @param requestingBeanName the requesting bean name
     * @return valor do atributo 'resourceByType'
     * @see
     */
    protected Object getResourceByType(final LookupElement element,
            final String requestingBeanName) {
        Object bean;
        final LinkedHashSet<String> autowiredBeanNames = new LinkedHashSet<String>();
        bean = ((AutowireCapableBeanFactory) beanFactory)
                .resolveDependency(element.getDependencyDescriptor(), requestingBeanName,
                        autowiredBeanNames, null);
        if (beanFactory instanceof ConfigurableBeanFactory) {
            final ConfigurableBeanFactory configurableBeanFactory =
                    (ConfigurableBeanFactory) beanFactory;
            for (final String autowiredBeanName : autowiredBeanNames) {
                configurableBeanFactory.registerDependentBean(autowiredBeanName,
                        requestingBeanName);
            }
        }
        return bean;
    }

}