package br.ufpe.cin.dsoa.qos.dpcenter.processing.impl;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import br.ufpe.cin.dsoa.management.MetaData;
import br.ufpe.cin.dsoa.management.MonitoringHandler;

public class MonitoringHandlerServiceTracker extends ServiceTracker{

	private BundleContext bundleContext;
	
	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public MonitoringHandlerServiceTracker(BundleContext context,String clazz, 
	    ServiceTrackerCustomizer customizer){
		super(context, clazz, customizer);
		
		this.bundleContext = context;
	}

	public MonitoringHandler getHandler(String name){
		MonitoringHandler monitoringHandler  = null;
		ServiceReference[] serviceReferences = super.getServiceReferences();
		for (ServiceReference serviceReference : serviceReferences) {
			String handlerName = (String)serviceReference.getProperty(MetaData.HANDLER_NAME);
			if(handlerName.equalsIgnoreCase(name)){
				monitoringHandler = (MonitoringHandler)super.getService(serviceReference);
			}
		}
		return monitoringHandler;
	}
}