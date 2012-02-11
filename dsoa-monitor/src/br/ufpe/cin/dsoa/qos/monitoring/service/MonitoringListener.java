package br.ufpe.cin.dsoa.qos.monitoring.service;

import java.util.Map;

public interface MonitoringListener {
	
	public void listen(Map result, Object userObject,String statementName);

}
