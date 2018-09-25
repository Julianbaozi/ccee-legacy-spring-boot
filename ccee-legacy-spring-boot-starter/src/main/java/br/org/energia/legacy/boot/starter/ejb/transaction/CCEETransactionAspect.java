package br.org.energia.legacy.boot.starter.ejb.transaction;

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_MANDATORY;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NEVER;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NOT_SUPPORTED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_SUPPORTS;

import java.lang.reflect.Method;

import javax.transaction.Transactional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Classe auxilia para gerar nome de beans no spring.
 * @author valdir.scarin
 */
@Aspect
@Component
public class CCEETransactionAspect implements Ordered {

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Around("@within(javax.ejb.Stateless) || @within(javax.ejb.MessageDriven) || @within(javax.jws.WebService)")
	public Object post(ProceedingJoinPoint pjp) throws Throwable {
		if(pjp.getSignature() instanceof MethodSignature) {
			MethodSignature ms = (MethodSignature) pjp.getSignature();
			TransactionStatus status = null;
			try {
				TransactionDefinition definition =
						new DefaultTransactionDefinition(convertTransactionDefinition(ms.getMethod()));
				status = transactionManager.getTransaction(definition);
				Object result = pjp.proceed();
				transactionManager.commit(status);
				return result;
			} catch (Throwable e) {
				transactionManager.rollback(status);
				throw e;
			}
		}
		return pjp.proceed();
	}

	private int convertTransactionDefinition(Method method) {
		int propagationRequired = PROPAGATION_REQUIRED;
		if (method.isAnnotationPresent( Transactional.class)) {
			Transactional tx = method.getAnnotation(Transactional.class);
			switch (tx.value()) {
				case MANDATORY: {
					propagationRequired = PROPAGATION_MANDATORY;
					break;
				} case NEVER: {
					propagationRequired = PROPAGATION_NEVER;
					break;
				} case NOT_SUPPORTED: {
					propagationRequired = PROPAGATION_NOT_SUPPORTED;
					break;
				} case REQUIRED: {
					propagationRequired = PROPAGATION_REQUIRED;
					break;
				} case REQUIRES_NEW: {
					propagationRequired = PROPAGATION_REQUIRES_NEW;
					break;
				} case SUPPORTS: {
					propagationRequired = PROPAGATION_SUPPORTS;
					break;
				}
			}
		}
		return propagationRequired;
	}

	@Override
	public int getOrder() {
		return 0;
	}
	
}