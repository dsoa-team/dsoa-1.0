package br.ufpe.cin.dsoa.qos.monitoring.activator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import br.ufpe.cin.dsoa.qos.monitoring.impl.MonitoringServiceImpl;
import br.ufpe.cin.dsoa.qos.monitoring.service.MonitoringService;


public class Activator implements BundleActivator{

	private BundleContext bundleContext;
	
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("====== Instalou Monitor ======= ");
		this.bundleContext = context;
		this.bundleContext.registerService(MonitoringService.class.getName(), new MonitoringServiceImpl(this.bundleContext), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
