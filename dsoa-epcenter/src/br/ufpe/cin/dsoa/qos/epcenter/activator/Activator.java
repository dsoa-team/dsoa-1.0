package br.ufpe.cin.dsoa.qos.epcenter.activator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import br.ufpe.cin.dsoa.qos.epcenter.impl.EventProcessingCenterImpl;
import br.ufpe.cin.dsoa.qos.epcenter.service.EventProcessingCenter;


public class Activator implements BundleActivator{

	private BundleContext bundleContext;
	
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("xxxxxxx Istalou Brow =========");
		this.bundleContext = context;
		this.bundleContext.registerService(EventProcessingCenter.class.getName(), new EventProcessingCenterImpl(this.bundleContext), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
