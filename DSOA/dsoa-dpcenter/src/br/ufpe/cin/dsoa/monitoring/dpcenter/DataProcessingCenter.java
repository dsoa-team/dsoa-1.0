package br.ufpe.cin.dsoa.qos.dpcenter;

import java.util.Map;

import br.ufpe.cin.dsoa.qos.management.MonitoringConfiguration;

public interface DataProcessingCenter {
	
	@SuppressWarnings("rawtypes")
	public void    createInstrumentationCell(String eventTypeName, Map typeMap,String topic);
	public boolean destroyInstrumentationCell(String eventTypeName);
	
	public void addMonitoringCell(MonitoringConfiguration monitoringConfiguration);
	
}
