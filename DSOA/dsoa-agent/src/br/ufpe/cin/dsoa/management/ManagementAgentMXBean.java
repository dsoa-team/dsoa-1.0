package br.ufpe.cin.dsoa.management;

import java.util.Date;

public interface ManagementAgentMXBean {
	
	String[] getServices();
	
	String[] getOperations(String serviceName);
	
	String[] getHandlers();
	
	int createMonitoringConfiguration(
			String serviceName, String operationName,
			String qosCategory, String qosCharacteristic, 
			String qosStatistic, String qosUnit,
			String windowType, int windowSize, String windowUnit,
			Date startTime, Date stopTime,
			String[] handlers);
}
