package br.ufpe.cin.dsoa.monitoring.dgcenter;


public interface DataGatheringCenter extends DataGatheringCenterMXBean {
	DynamicMonitoringChainerInterceptor getChainer(Long serviceId, boolean isIn, boolean isFault);
	
	DynamicMonitoringPublisherInterceptor getPublisher(Long serviceId, boolean isIn, boolean isFault);
}
