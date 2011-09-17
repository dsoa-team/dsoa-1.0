package br.ufpe.cin.dsoa.qos.dpcenter;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;


import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

public class Activator implements BundleActivator{
	
	private BundleContext       bundleContext;
	private EPServiceProvider   epServiceProvider;
	private ServiceRegistration registrationServiceDataProcessingCenter;

	public void start(BundleContext context) throws Exception {
		this.epServiceProvider = EPServiceProviderManager.getProvider("EngineInstance", new Configuration());
		this.bundleContext     = context;
		registerDataProcessingCenterService();
	}

	public void stop(BundleContext context) throws Exception {
		
	}

	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}
	
	public void registerDataProcessingCenterService(){
		setRegistrationServiceDataProcessingCenter(getBundleContext().registerService(DataProcessingCenter.class.getName(), new DataProcessingCenterImpl(getEpServiceProvider(),getBundleContext()), null));
	}
	
	public void unregisterQosDataProcessingCenterService(){
		getRegistrationServiceDataProcessingCenter().unregister();
		setRegistrationServiceDataProcessingCenter(null);
	}
	
	public void setRegistrationServiceDataProcessingCenter(ServiceRegistration registrationServiceDataProcessingCenter) {
		this.registrationServiceDataProcessingCenter = registrationServiceDataProcessingCenter;
	}

	public ServiceRegistration getRegistrationServiceDataProcessingCenter() {
		return registrationServiceDataProcessingCenter;
	}

	public EPServiceProvider getEpServiceProvider() {
		return epServiceProvider;
	}

}
