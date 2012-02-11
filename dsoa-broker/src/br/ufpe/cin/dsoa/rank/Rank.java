package br.ufpe.cin.dsoa.rank;

import java.util.List;

import org.osgi.framework.ServiceReference;

import br.ufpe.cin.dsoa.qos.Slo;



public interface Rank {
	
	public ServiceReference ranking(List<Slo> slos, ServiceReference... references);

}
