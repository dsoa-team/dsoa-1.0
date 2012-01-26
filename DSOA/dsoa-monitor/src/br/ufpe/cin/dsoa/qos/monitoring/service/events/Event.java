package br.ufpe.cin.dsoa.qos.monitoring.service.events;

public class Event {

	private long timestamp;

	public Event(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}
}
