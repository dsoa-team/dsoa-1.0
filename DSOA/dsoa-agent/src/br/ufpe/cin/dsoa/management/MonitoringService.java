package br.ufpe.cin.dsoa.management;


public interface MonitoringService {
	Object getInInterceptor();
	Object getInFaultInterceptor();
	Object getOutInterceptor();
	Object getOutFaultInterceptor();
}
