package br.ufpe.cin.dsoa.monitoring.dgcenter.impl;

import org.osgi.framework.BundleContext;

import br.ufpe.cin.dsoa.monitoring.dgcenter.DataGatheringCenter;
import br.ufpe.cin.dsoa.monitoring.dgcenter.DynamicMonitoringChainerInterceptor;
import br.ufpe.cin.dsoa.monitoring.dgcenter.DynamicMonitoringPublisherInterceptor;


public class DataGatheringCenterImpl implements DataGatheringCenter {

	private boolean enabled;
	private BundleContext ctx;
	
	public DataGatheringCenterImpl(BundleContext ctx, boolean b) {
		this.enabled = b;
		this.ctx = ctx;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public DynamicMonitoringChainerInterceptor getChainer(Long serviceId, boolean isIn, boolean isFault) {
		// TODO Auto-generated method stub
		return new DynamicMonitoringChainerInterceptor(ctx, serviceId, isIn, isFault);
	}

	@Override
	public DynamicMonitoringPublisherInterceptor getPublisher(Long serviceId, boolean isIn, boolean isFault) {
		// TODO Auto-generated method stub
		return new DynamicMonitoringPublisherInterceptor(ctx, serviceId, isIn, isFault);
	}

}
