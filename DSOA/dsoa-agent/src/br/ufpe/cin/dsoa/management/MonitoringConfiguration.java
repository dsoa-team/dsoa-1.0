package br.ufpe.cin.dsoa.qos.management;

import java.util.Date;
import java.util.List;

public interface MonitoringConfiguration extends MonitoringService {
	int getId();
	String getServiceName();
	String getOperationName();
	String getQosCategory();
	String getQosCharacteristic();
	String getQosStatistic();
	String getQosUnit();
	String getWindowType();
	int getWindowSize();
	String getWindowUnit();
	Date getStartTime();
	Date getStopTime();
	List<String> getHandlers();
	String getEventTopic();
}