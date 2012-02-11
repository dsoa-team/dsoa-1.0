package br.ufpe.cin.dsoa.qos.monitoring.service.events;

public class MemoryEvent extends ResourceEvent {

	private String type;
	private double free;

	public MemoryEvent(long timestamp) {
		super(timestamp);
	}

	public String getType() {
		return type;
	}

	public double getFree() {
		return free;
	}
}
