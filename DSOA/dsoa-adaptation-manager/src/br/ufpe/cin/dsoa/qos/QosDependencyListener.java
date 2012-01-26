package br.ufpe.cin.dsoa.qos;

import org.osgi.framework.ServiceReference;

public interface QosDependencyListener {
	
	public void setSelected(ServiceReference serviceDescription);

}
