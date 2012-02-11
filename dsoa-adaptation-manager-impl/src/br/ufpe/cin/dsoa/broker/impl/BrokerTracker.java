package br.ufpe.cin.dsoa.broker.impl;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import br.ufpe.cin.dsoa.qos.QosDependencyListener;

public class BrokerTracker extends ServiceTracker {

	private QosDependencyListener qdl;
	private List<ServiceReference> blackList;
	
	public BrokerTracker(QosDependencyListener qdl, BundleContext context, Filter filter, List<ServiceReference> blackList) {
		super(context, filter, null);
		this.blackList = blackList;
		this.qdl = qdl;
	}	
	
	@Override
	public Object addingService(ServiceReference reference) {
		if (!blackList.contains(reference)) {
			qdl.setSelected(reference);
			this.close();
		}
		return reference;
	}
	
}
