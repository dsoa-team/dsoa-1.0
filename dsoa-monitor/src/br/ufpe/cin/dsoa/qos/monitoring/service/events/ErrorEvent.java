package br.ufpe.cin.dsoa.qos.monitoring.service.events;

public class ErrorEvent extends InvocationEvent {

	private String exceptionClass;
	private String exceptionMessage;
	private String rootCauseClass;
	private String rootCauseMessage;

	@SuppressWarnings("rawtypes")
	public ErrorEvent(long timestamp, String consumerId, String serviceId,
			String correlationId, String operationName, String exceptionClass, String exceptionMessage,
			String rootCauseClass, String rootCauseMessage) {
		super(timestamp, consumerId, serviceId, correlationId,
				operationName);
		this.exceptionClass 	= exceptionClass;
		this.exceptionMessage 	= exceptionMessage;
		this.rootCauseClass 	= rootCauseClass;
		this.rootCauseMessage 	= rootCauseMessage;
	}
	
	public ErrorEvent(long timestamp, String consumerId, String consumerName,
			String correlationId, String operationName, String exceptionClass, String exceptionMessage) {
		this(timestamp, consumerId, consumerName, correlationId,
				operationName, exceptionClass, exceptionMessage, null, null);
	}

	public String getExceptionClass() {
		return exceptionClass;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public String getRootCauseClass() {
		return rootCauseClass;
	}

	public String getRootCauseMessage() {
		return rootCauseMessage;
	}

}
