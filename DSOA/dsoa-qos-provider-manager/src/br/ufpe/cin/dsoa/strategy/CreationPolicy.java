package br.ufpe.cin.dsoa.strategy;

import org.apache.felix.ipojo.InstanceManager;

public interface CreationPolicy {
	
	public Object createInstance(InstanceManager m_manager);//FIXME:PROPERTIES

}
