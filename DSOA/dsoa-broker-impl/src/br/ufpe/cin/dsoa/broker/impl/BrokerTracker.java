package br.ufpe.cin.dsoa.broker.impl;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import br.ufpe.cin.dsoa.qos.QosDependencyListener;

public class BrokerTracker extends ServiceTracker {

	private QosDependencyListener qdl;
	
	public BrokerTracker(QosDependencyListener qdl, BundleContext context, Filter filter) {
		super(context, filter, null);
		this.qdl = qdl;
	}	
	
	@Override
	public Object addingService(ServiceReference reference) {
		qdl.setSelected(reference);
		this.close();
		return reference;
	}
	
}
