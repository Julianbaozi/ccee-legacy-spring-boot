package br.org.energia.legacy.boot.starter.ejb.interceptor;


import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;


/**
 * Classe auxilia para gerar nome de beans no spring.
 * 
 * @author valdir.scarin
 */
@Aspect
@Component
@ConditionalOnProperty(prefix = "ccee.legacy.interceptor", name = "enabled", havingValue = "true", matchIfMissing = true)
public class CCEEInterceptorAspect implements Ordered {

	@Autowired
	private ApplicationContext context;

	/**
	 * Ao redor de qualquer spring bean anotado com @Interceptor 
	 * 1 - localizar os Interceptors passados como valor
	 * 2 - para cada um deles localizar o método anotado com @ArroundInvoce
	 * 3 - transformar o ProceedingJointPoint em InvokationContext
	 * 4 - executar o método e retornar
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */

	@Around("@within(javax.interceptor.Interceptors)")
	public Object arround(ProceedingJoinPoint p) throws Throwable {
		
		MethodInvocationProceedingJoinPoint pjp = (MethodInvocationProceedingJoinPoint) p;
		
		Interceptors interceptAnnot = pjp.getTarget().getClass().getAnnotation(Interceptors.class);
		if (interceptAnnot != null) {
			tryExecuteInterceptor(pjp, interceptAnnot);			
		} 
		return pjp.proceed();

	}

	private void tryExecuteInterceptor(MethodInvocationProceedingJoinPoint pjp, Interceptors interceptAnnot) {
		List<Class<?>> interceptorsList = Arrays.asList(interceptAnnot.value());
		interceptorsList.stream().forEach(i -> {
			//considerando um único método anotado com @ArroundInvoke por interceptor cujo parametro é InvocationContext 
			Optional<Method> findFirst = Arrays.asList(i.getMethods()).stream()
					.filter(m -> m.isAnnotationPresent(AroundInvoke.class)).findFirst();
			if (findFirst.isPresent()) {
				Method interceptorMethod = findFirst.get();
				Object interceptorBean = context.getBean(i.getCanonicalName());
				
				MethodSignature ms = (MethodSignature) pjp.getSignature();
				Method q = ms.getMethod();

				try {
					//sem target, pq invokator serao chamados individualmente
					InvocationContext ic = new CCEEInvocationContext(null, q, pjp.getArgs());
					interceptorMethod.invoke(interceptorBean, ic);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public int getOrder() {
		return 1;
	}

}