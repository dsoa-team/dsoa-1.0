package br.ufpe.cin.dsoa.monitoring.dpcenter.publication.api;

import java.util.Map;


public interface Publisher {
	
	@SuppressWarnings("unchecked")
	public void publish(Map map, String eventTypeName);
}
