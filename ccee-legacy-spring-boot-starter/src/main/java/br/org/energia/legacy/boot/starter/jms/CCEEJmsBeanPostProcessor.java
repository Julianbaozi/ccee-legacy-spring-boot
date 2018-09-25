package br.org.energia.legacy.boot.starter.jms;

import static br.org.energia.spring.utils.BeanPostProcessorUtils.getAnnotatedClass;
import static br.org.energia.spring.utils.BeanPostProcessorUtils.getBeanType;
import static br.org.energia.spring.utils.BeanPostProcessorUtils.isClassInBasePackage;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;

import br.org.energia.legacy.boot.starter.CCEELegacyProperties;

public class CCEEJmsBeanPostProcessor implements MergedBeanDefinitionPostProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(CCEEJmsBeanPostProcessor.class);

	private List<MessageListenerInfo> lista = new ArrayList<>();

	@Autowired
	private CCEELegacyProperties props;

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

		String[] basePackages = props.getBasePackage().split(",");
		Class<?> remoteClass = getAnnotatedClass(getBeanType(bean), MessageDriven.class);
		if (isClassInBasePackage(basePackages, remoteClass)) {
			LOG.trace("MessageDrive>:" + remoteClass.getName());
			registerListener(bean, remoteClass);
		}
		return bean;

	}

	private void registerListener(Object bean, Class<?> remoteInterface) {

		MessageListenerInfo messageListenerInfo = getQueueDestionation(bean, remoteInterface);
		if (messageListenerInfo != null && !applicationContext.containsBean(messageListenerInfo.getDestination())) {
			lista.add(messageListenerInfo);
			applicationContext.getBeanFactory().registerSingleton("endpoints", lista);
			applicationContext.getBeanFactory().registerSingleton(messageListenerInfo.getDestination(),
					messageListenerInfo.getMessageListener());
			LOG.info("CCEELegacy: " + remoteInterface.getName() + " was registered as listner to be queue ["
					+ messageListenerInfo.getDestination() + "]");
		}

	}

	private MessageListenerInfo getQueueDestionation(Object bean, Class<?> remoteInterface) {

		Annotation[] annotations = remoteInterface.getAnnotations();

		for (Annotation annotation : annotations) {
			if (annotation instanceof MessageDriven) {
				MessageDriven myAnnotation = (MessageDriven) annotation;

				ActivationConfigProperty[] activationConfig = myAnnotation.activationConfig();
				List<ActivationConfigProperty> asList = Arrays.asList(activationConfig);
				Map<String, String> collect = asList.stream().collect(Collectors
						.toMap(ActivationConfigProperty::propertyName, ActivationConfigProperty::propertyValue));

				return new MessageListenerInfo(myAnnotation.mappedName(), collect.get("destination"), bean);

			}
		}
		return null;
	}

	@Override
	public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
		// TODO Auto-generated method stub

	}

}