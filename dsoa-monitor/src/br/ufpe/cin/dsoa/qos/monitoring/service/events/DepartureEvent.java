package br.ufpe.cin.dsoa.qos.monitoring.service.events;

public class DepartureEvent extends ServiceEvent {

	public DepartureEvent(long timestamp, String serviceId, String serviceName,
			String serviceInterface) {
		super(timestamp, serviceId, serviceName, serviceInterface);
	}

}
