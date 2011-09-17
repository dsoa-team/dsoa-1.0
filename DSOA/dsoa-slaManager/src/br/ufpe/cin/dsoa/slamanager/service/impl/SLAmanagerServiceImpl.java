package br.ufpe.cin.dsoa.slamanager.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import br.ufpe.cin.dsoa.slamanager.agreement.Agreement;
import br.ufpe.cin.dsoa.slamanager.agreement.Party;
import br.ufpe.cin.dsoa.slamanager.agreement.Slo;
import br.ufpe.cin.dsoa.slamanager.service.SLAmanagerService;


public class SLAmanagerServiceImpl implements SLAmanagerService {
	
	List<Agreement> agreements;
	
	
	public SLAmanagerServiceImpl() {
		this.agreements = new ArrayList<Agreement>();
	}

	@Override
	public void createAgreement(Properties consumerInfo,
			Properties providerInfo, Map<String, Slo> SLOs) {
		
		Agreement sla    = new Agreement(consumerInfo, providerInfo, SLOs);
		Party thirdParty = startMonitoring();
		sla.getContext().setThirdParty(thirdParty);
		this.agreements.add(sla);
		
	}
	
	private Party startMonitoring() {
		return null;
	}
	

}
