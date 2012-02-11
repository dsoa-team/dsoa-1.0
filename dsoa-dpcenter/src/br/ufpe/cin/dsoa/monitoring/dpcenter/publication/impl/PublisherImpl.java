package br.ufpe.cin.dsoa.monitoring.dpcenter.publication.impl;

import java.util.Map;

import br.ufpe.cin.dsoa.monitoring.dpcenter.publication.api.Publisher;

import com.espertech.esper.client.EPServiceProvider;

public class PublisherImpl implements Publisher{

	private EPServiceProvider epServiceProvider;
	
	public PublisherImpl(EPServiceProvider epServiceProvider) {
		this.epServiceProvider = epServiceProvider;
	}

	public EPServiceProvider getEpServiceProvider() {
		return epServiceProvider;
	}

	@SuppressWarnings("unchecked")
	public void publish(Map map, String eventTypeName) {
		getEpServiceProvider().getEPRuntime().sendEvent(map, eventTypeName);
	}

}
