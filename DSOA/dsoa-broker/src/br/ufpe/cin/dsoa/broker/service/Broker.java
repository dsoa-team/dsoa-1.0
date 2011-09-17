package br.ufpe.cin.dsoa.broker.service;

import java.util.List;

import br.ufpe.cin.dsoa.handlers.require.QosDependencyListener;
import br.ufpe.cin.dsoa.slamanager.agreement.Slo;

public interface Broker {

	public void getService(String spe, List<Slo> slos, QosDependencyListener dep, String policy);

}
