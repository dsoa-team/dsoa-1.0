package br.ufpe.cin.dsoa.qos.epcenter.service;

import java.util.Map;


public interface EventProcessingCenter {
	
	@SuppressWarnings("rawtypes")
	public void  defineEvent(Class event);
	public void  publishEvent(Object event);
	@SuppressWarnings("rawtypes")
	public void  publishEvent(Map event, String eventName);
	public void  definePreparedStatement(String name, String statement);
	public void  defineStatement(String name, String statement);
	public void  createPreparedStatement(String name);
	public void  subscribe(String nameStatement, EventConsumer eventConsumer);
	public void destroyStatement(String statementName);
	public void modifyStatement(String statementName,int index, Object value);

}
