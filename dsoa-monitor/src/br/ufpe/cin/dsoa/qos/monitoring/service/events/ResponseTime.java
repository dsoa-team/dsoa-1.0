package br.ufpe.cin.dsoa.qos.monitoring.service.events;

public class ResponseTime {
	
	
	private long value;
	private String consumerId;
	private String operationName;
	private String serviceId;
	
	public ResponseTime(long value, String consumerId, String operationName,String serviceId) {
		this.value = value;
		this.consumerId = consumerId;
		this.operationName = operationName;
		this.serviceId = serviceId;
	}

	public long getValue() {
		return value;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public String getOperationName() {
		return operationName;
	}

	public String getServiceId() {
		return serviceId;
	}
	
}
