package br.ufpe.cin.dsoa.qos.monitoring.service;

import java.util.HashMap;
import java.util.Map;

import br.ufpe.cin.dsoa.qos.epcenter.service.EventConsumer;


public class MonitoringConfiguration implements EventConsumer {
	
	private static long idGenerator = 1;
	private long id;
	
	private MonitoringListener listener;
	private Map<String,MonitoringConfigurationItem> itens;
	
	private String serviceId;
	private String clientId;
	private boolean old;
	
	private synchronized static long getNextId() {
		return idGenerator++;
	}

	public MonitoringConfiguration(String consumerId, String serviceId, MonitoringListener listener) {
		this.listener = listener;
		this.clientId = consumerId;
		this.serviceId = serviceId;
		this.itens = new HashMap<String,MonitoringConfigurationItem>();
		this.old = false;
		this.id = getNextId();
	}

	public MonitoringListener getListener() {
		return listener;
	}

	public String getClientId() {
		return clientId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public void addConfigurationItem(MonitoringConfigurationItem item) {
		this.itens.put(String.valueOf(item.getId()), item);
	}

	public Map<String,MonitoringConfigurationItem> getItens() {
		return itens;
	}

	@Override
	public void receive(Map result, Object userObject, String statementName) {
		System.out.println("***************   Quebrou   ***************");
		MonitoringConfigurationItem brokenItem = this.itens.get(statementName);
		listener.listen(result, brokenItem, statementName);
	}

	public long getId() {
		return id;
	}

	public boolean isOld() {
		// TODO Auto-generated method stub
		return old;
	}

	public void setOld() {
		// TODO Auto-generated method stub
		this.old = true;
	}
	
}
