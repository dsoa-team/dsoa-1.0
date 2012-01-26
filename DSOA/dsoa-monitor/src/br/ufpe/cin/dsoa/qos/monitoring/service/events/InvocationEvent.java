package br.ufpe.cin.dsoa.qos.monitoring.service.events;

public class InvocationEvent extends Event {

	private String consumerId;
	private String serviceId;

	private String correlationId;
	private String operationName;

	@SuppressWarnings("rawtypes")
	public InvocationEvent(long timestamp, String consumerId,
			String serviceId, String correlationId, String operationName) {
		super(timestamp);
		this.consumerId = consumerId;
		this.serviceId = serviceId;
		this.correlationId = correlationId;
		this.operationName = operationName;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public String getOperationName() {
		return operationName;
	}

}
