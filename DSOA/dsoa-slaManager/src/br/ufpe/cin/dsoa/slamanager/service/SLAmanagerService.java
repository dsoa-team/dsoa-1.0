package br.ufpe.cin.dsoa.qos.slamanager.service;

import java.util.Map;
import java.util.Properties;

import br.ufpe.cin.dsoa.qos.slamanager.agreement.Slo;


public interface SLAmanagerService {

	public void createAgreement(Properties consumerInfo, Properties providerInfo, Map<String,Slo> SLOs);

}
