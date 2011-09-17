package br.ufpe.cin.dsoa.qos.slamanager.agreement;

 import java.util.Date;
import java.util.Map;
import java.util.Properties;

import br.ufpe.cin.dsoa.qos.slamanager.service.ConstantsSLA;

public class Agreement {

	/**
	 * Agreement unique identifier
	 */
	private String m_ID;

	/**
	 * Service managed by this agreement
	 * TODO allow several services per agreement ?
	 */
	private String m_serviceInterface;

	/**
	 * Agreement context. It contains involved parties information and other information regarding the context (start and expiration date)
	 */
	private Context m_ctxt;

	/**
	 * Service Level Objectives defined in this agreement
	 */
	private Map<String, Slo> m_slos;

	/**
	 * Constructs a new Agreement with provided information about the service consumer and service provider and negotiated SLOs.
	 * @param consumerInfo Consumer information : pid, name
	 * @param providerInfo Provider information : pid, name, service, agreement duration
	 * @param SLOs Service level objectives negotiated by the consumer and the provider
	 * @param timestamp
	 */
	public Agreement(Properties consumerInfo, Properties providerInfo, Map<String,Slo> SLOs){

		String providerPID, providerName;
		String consumerPID, consumerName;

		// initialization
		providerPID            = providerInfo.getProperty(ConstantsSLA.PROVIDER_PID);
		providerName           = providerInfo.getProperty(ConstantsSLA.PROVIDER_NAME);
		long agreementDuration = Long.parseLong(providerInfo.getProperty(ConstantsSLA.AGREEMENT_DURATION));

		consumerPID  = consumerInfo.getProperty(ConstantsSLA.CONSUMER_PID);
		consumerName = consumerInfo.getProperty(ConstantsSLA.CONSUMER_NAME);
		
		Party providerParty = new Party(providerPID, providerName);
		Party consumerParty = new Party(consumerPID, consumerName);
		Party thirdParty    = new Party("",""); // TODO where to set it?

		long timeStamp      = System.currentTimeMillis();
		Date startDate      = new Date(timeStamp);
		Date expirationDate = new Date(timeStamp+agreementDuration);

		m_ctxt = new Context(providerParty, consumerParty, thirdParty, startDate, expirationDate);
		m_slos = SLOs;

		m_serviceInterface = providerInfo.getProperty(ConstantsSLA.SVC_INTERFACE);
		m_ID = "sla:"+consumerPID+"-"+providerPID;

	}

	/**
	 * @return the agreement context
	 */
	public Context getContext() {return this.m_ctxt;}

	/**
	 * @return the agreement ID
	 */
	public String getID() {
		return m_ID;
	}

	/**
	 * @return the service interface managed by this agreement
	 */
	public String getServiceInterface() {
		return m_serviceInterface;
	}

	/**
	 * @return the service level objectives defined by this agreement
	 */
	public Map<String, Slo> getSlos() {
		return m_slos;
	}

}
