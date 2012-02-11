package br.ufpe.cin.dsoa.qos.monitoring.service.events;

public class CPUEvent extends ResourceEvent {

	private double load;

	public CPUEvent(long timestamp) {
		super(timestamp);
	}

	public double getLoad() {
		return load;
	}
}
