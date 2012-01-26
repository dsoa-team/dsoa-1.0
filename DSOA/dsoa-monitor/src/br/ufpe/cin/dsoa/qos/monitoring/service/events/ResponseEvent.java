package br.ufpe.cin.dsoa.qos.monitoring.service.events;

public class ResponseEvent extends InvocationEvent {

	private String returnType;
	private Object returnValue;

	@SuppressWarnings("rawtypes")
	public ResponseEvent(long timestamp, String consumerId,
			String serviceId, String correlationId, String operationName,
			String returnType,
			Object returnValue) {
		super(timestamp, consumerId, serviceId, correlationId,
				operationName);
		this.returnType  = returnType;
		this.returnValue = returnValue;
	}


	public String getReturnType() {
		return returnType;
	}

	public Object getReturnValue() {
		return returnValue;
	}

}
