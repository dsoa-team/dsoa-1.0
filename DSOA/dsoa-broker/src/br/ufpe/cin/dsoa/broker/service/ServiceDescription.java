package br.ufpe.cin.dsoa.broker.service;

public class ServiceDescription {

	private Object service;
	private String providerId;
	private String providerName;
	private long duration;
	
	public Object getService() {
		return service;
	}
	public void setService(Object service) {
		this.service = service;
	}
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
}
