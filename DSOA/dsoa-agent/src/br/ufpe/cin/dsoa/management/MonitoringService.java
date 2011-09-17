package br.ufpe.cin.dsoa.qos.management;


public interface MonitoringService {
	Object getInInterceptor();
	Object getInFaultInterceptor();
	Object getOutInterceptor();
	Object getOutFaultInterceptor();
}
