package br.ufpe.cin.dsoa.jmx.monitor;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	private PlatformMonitor platformMonitor;

	public void registerPlatformMBean() {
		platformMonitor = new PlatformMonitor();
		platformMonitor.start();
		platformMonitor.startMonitoring();
	}

	public void start(BundleContext context) {
		this.registerPlatformMBean();
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		platformMonitor.stop();
	}

	public static void main(String[] args) {
		new Activator().registerPlatformMBean();
	}
}
