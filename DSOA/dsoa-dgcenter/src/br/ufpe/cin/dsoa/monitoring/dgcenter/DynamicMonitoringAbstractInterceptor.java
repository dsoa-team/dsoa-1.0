package br.ufpe.cin.dsoa.qos.dgcenter;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;

import br.ufpe.cin.dsoa.management.MetaData;
import br.ufpe.cin.dsoa.management.MonitoringConfiguration;

/**
 * This class is responsible for maintaining a list MonitoringServices which are
 * currently registered in the OSGi repository.
 * 
 * @author fabions
 * 
 */
public abstract class DynamicMonitoringAbstractInterceptor extends
		AbstractPhaseInterceptor<Message> {

	private BundleContext ctx;
	private ServiceTracker dgCenterTracker;
	private ServiceTracker monitoringConfigurationTracker;

	public DynamicMonitoringAbstractInterceptor(Long serviceId, String phase, BundleContext ctx) {
		super(phase);
		this.ctx = ctx;
		
		this.dgCenterTracker = new ServiceTracker(ctx, DataGatheringCenter.class.getName(), null);
		this.dgCenterTracker.open();
		
		Filter filter = null;
		try {
			filter = ctx.createFilter("(&(" + Constants.OBJECTCLASS + "="
					+ MonitoringConfiguration.class.getName() + ")" + "("
					+ MetaData.SERVICE_REF_NAME + "=" + serviceId + "))");
			System.out.println(filter);
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.monitoringConfigurationTracker = new ServiceTracker(ctx, filter, null);
		this.monitoringConfigurationTracker.open();
	}

	public ServiceTracker getMonitoringConfigurationTracker() {
		return monitoringConfigurationTracker;
	}
	
	public ServiceTracker getDataGatheringCenterTracker() {
		return dgCenterTracker;
	}
}
