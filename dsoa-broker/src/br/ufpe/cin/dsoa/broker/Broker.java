package br.ufpe.cin.dsoa.broker;

import java.util.List;

import br.ufpe.cin.dsoa.qos.QosDependencyListener;
import br.ufpe.cin.dsoa.qos.Slo;


/**
 * 
 * @author David
 **/

public interface Broker {
	
	public void getBestService(String spe, List<Slo> slos, QosDependencyListener dep);
	//Tinha um mode;
	
}