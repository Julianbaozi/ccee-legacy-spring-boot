package br.org.energia.legacy.boot.starter.ejb.interceptor;

import java.lang.reflect.Method;
import java.util.Map;

import javax.interceptor.InvocationContext;

public class CCEEInvocationContext implements InvocationContext {

	private static final Object[] NO_OBJECTS = new Object[0];

	private final Object target;
	private final Method method;
	private Object[] parameters;
	private final Map<String, Object> contextData;

	/**
	 * Construct a new instance.
	 *
	 * @param target
	 *            the target object instance
	 * @param method
	 *            the invocation method (may be {@code null})
	 * @param parameters
	 *            the invocation parameters (may be {@code null})
	 * @param contextData
	 *            the context data map to use
	 * @param timer
	 *            the associated timer (may be {@code null})
	 */
	public CCEEInvocationContext(final Object target, final Method method, final Object[] parameters,
			final Map<String, Object> contextData) {
		this.target = target;
		this.method = method;
		this.parameters = parameters;
		this.contextData = contextData;
	}

	/**
	 * Construct a new instance.
	 *
	 * @param target
	 *            the target object instance
	 * @param method
	 *            the invocation method (may be {@code null})
	 * @param parameters
	 *            the invocation parameters (may be {@code null})
	 */
	public CCEEInvocationContext(final Object target, final Method method, final Object[] parameters) {
		this(target, method, parameters, null);
	}

	@Override
	public Object getTarget() {
		return target;
	}

	@Override
	public Method getMethod() {
		return method;
	}

	@Override
	public Object[] getParameters() {
		return parameters;
	}

	@Override
	public void setParameters(Object[] params) {
		this.parameters = parameters == null ? NO_OBJECTS : parameters;

	}

	@Override
	public Map<String, Object> getContextData() {
		return contextData;
	}

	@Override
	public Object proceed() throws Exception {
		return null;
	}

}
