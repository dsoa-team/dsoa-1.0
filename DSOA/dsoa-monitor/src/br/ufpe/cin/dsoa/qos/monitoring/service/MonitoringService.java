package br.ufpe.cin.dsoa.qos.monitoring.service;






public interface MonitoringService {
	
	public void startMonitoring(MonitoringConfiguration configuration);
	public void publishMonitoringEvent(Object event);
}
