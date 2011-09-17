package br.ufpe.cin.dsoa.slamanager.agreement;

import java.util.Date;

public class Context {

	private Party m_providerParty, m_consumerParty, m_thirdParty;
	private Date  m_startDate, m_expirationDate;

	public Context (Party providerParty, Party consumerParty, Party thirdParty, Date startDate, Date expirationDate){
		m_providerParty = providerParty;
		m_consumerParty = consumerParty;
		m_thirdParty = thirdParty;
		m_startDate = startDate;
		m_expirationDate = expirationDate;
	}

	public Party getConsumerParty() {
		return m_consumerParty;
	}

	public Party getProviderParty() {
		return m_providerParty;
	}

	public Party getThirdParty() {
		return m_thirdParty;
	}

	public Date getStartDate() {
		return m_startDate;
	}

	public Date getExpirationDate() {
		return m_expirationDate;
	}

	public void setConsumerParty(Party party) {
		m_consumerParty = party;
	}

	public void setThirdParty(Party thirdParty) {
		m_thirdParty = thirdParty;
	}

}
