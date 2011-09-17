package br.ufpe.dsoa.broker.service;

import java.util.List;

import br.ufpe.cin.dsoa.qos.reqhandler.QosDependencyListener;
import br.ufpe.cin.dsoa.qos.slamanager.agreement.Slo;

public interface Broker {

	public void getService(String spe, List<Slo> slos, QosDependencyListener dep, String policy);

}
