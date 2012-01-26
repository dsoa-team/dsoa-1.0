package br.ufpe.cin.dsoa.qos.epcenter.service;

import java.util.Map;

public interface EventConsumer {
	
	@SuppressWarnings("rawtypes")
	public void receive(Map result, Object userObject,String statementName);

}
