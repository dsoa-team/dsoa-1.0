package br.ufpe.cin.dsoa.monitoring.dgcenter.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import br.ufpe.cin.dsoa.monitoring.dgcenter.DataGatheringCenter;

public class Activator implements BundleActivator {
	private static final String MANAGEMENT_DGCENTER_MXBEAN_NAME = "management:type=DataGatheringCenter";
	private DataGatheringCenterImpl dgCenter;
	private ServiceRegistration dgCenterRegistration;
	
	public void start(BundleContext ctx) throws Exception {
		this.dgCenter = new DataGatheringCenterImpl(ctx, true);
		this.dgCenterRegistration = ctx.registerService(DataGatheringCenter.class.getName(), dgCenter, null);
		JMXUtil.registerMXBean(dgCenter, JMXUtil.buildObjectName(MANAGEMENT_DGCENTER_MXBEAN_NAME));
	}

	public void stop(BundleContext cxt) throws Exception {
		dgCenterRegistration.unregister();
		dgCenter = null;
		JMXUtil.removeMXBean(JMXUtil.buildObjectName(MANAGEMENT_DGCENTER_MXBEAN_NAME));
	}

}
