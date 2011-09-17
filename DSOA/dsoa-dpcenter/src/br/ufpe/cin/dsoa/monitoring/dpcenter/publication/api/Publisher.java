package br.ufpe.cin.dsoa.qos.dpcenter.publication.api;

import java.util.Map;


public interface Publisher {
	
	@SuppressWarnings("unchecked")
	public void publish(Map map, String eventTypeName);
}
