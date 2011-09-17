package br.ufpe.cin.dsoa.broker.service.impl;

import java.util.List;

import br.ufpe.cin.dsoa.broker.service.Broker;
import br.ufpe.cin.dsoa.handlers.require.QosDependencyListener;
import br.ufpe.cin.dsoa.slamanager.agreement.Slo;

public class BrokerImpl implements Broker {

	@Override
	public void getService(String spe, List<Slo> slos,
			QosDependencyListener dep, String policy) {
		// TODO Auto-generated method stub
	}

}
