package br.org.energia.legacy.boot.starter.web;

public class RequestCorrelationId {

	public static final String CORRELATION_ID = "x-cceerequestid";

	private static final ThreadLocal<String> id = new ThreadLocal<String>();

	public static String getId() {
		return id.get();
	}

	public static void setId(String correlationId) {
		id.set(correlationId);
	}

}
