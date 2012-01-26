package br.ufpe.cin.dsoa.jmx.monitor;

public interface PlatformMonitorMBean {

	public void startMonitoring();

	public void stopMonitoring();
	
	public void setInterval(long interval);
	
	public long getInterval();

}
