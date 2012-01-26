package br.ufpe.cin.dsoa.qos.monitoring.service.events;

public class Availability {
	
	private double value;
	private String serviceId;
	
	
	public Availability(double value, String serviceId) {
		super();
		this.value = value;
		this.serviceId = serviceId;
	}


	public double getValue() {
		return value;
	}


	public String getServiceId() {
		return serviceId;
	}
	
}
