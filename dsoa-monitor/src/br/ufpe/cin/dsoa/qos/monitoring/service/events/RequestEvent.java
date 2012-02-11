package br.ufpe.cin.dsoa.qos.monitoring.service.events;

public class RequestEvent extends InvocationEvent {
	private String[] parameterNames;
	@SuppressWarnings("rawtypes")
	private Class[] parameterTypes;
	private Object[] parameterValues;
	
	@SuppressWarnings("rawtypes")
	public RequestEvent(long timestamp, String consumerId, String serviceId,
			String correlationId, String operationName,
			String[] parameterNames, Class[] parameterTypes,
			Object[] parameterValues) {
		super(timestamp, consumerId, serviceId, correlationId, operationName);
		
		this.parameterNames = parameterNames;
		this.parameterTypes = parameterTypes;
		this.parameterValues = parameterValues;
	}
	
	public String[] getParameterNames() {
		return parameterNames;
	}

	@SuppressWarnings("rawtypes")
	public Class[] getParameterTypes() {
		return parameterTypes;
	}

	public Object[] getParameterValues() {
		return parameterValues;
	}
}
