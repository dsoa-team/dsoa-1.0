package br.ufpe.cin.dsoa.qos.monitoring.service.events;

public class ArrivalEvent extends ServiceEvent {

	public ArrivalEvent(long timestamp, String serviceId, String serviceName,
			String serviceInterface) {
		super(timestamp, serviceId, serviceName, serviceInterface);
	}

}
