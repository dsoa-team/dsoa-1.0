package br.ufpe.cin.dsoa.broker;

import java.util.List;

import org.osgi.framework.ServiceReference;

import br.ufpe.cin.dsoa.qos.QosDependencyListener;
import br.ufpe.cin.dsoa.qos.Slo;


/**
 * 
 * @author David
 **/

public interface Broker {
	
	public void getBestService(String spe, List<Slo> slos, QosDependencyListener dep, List<ServiceReference> trash);
	//Tinha um mode;
	
}