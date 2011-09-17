package br.ufpe.cin.dsoa.handlers.provider;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.apache.felix.ipojo.metadata.Element;

import br.ufpe.cin.dsoa.slamanager.agreement.Slo;

public class ProviderMetadata {

	private String    proPid;
	private String    proName;
	private List<Slo> slos;
	private String    durationUnit;
	private double    durationValue;
	
	ProviderMetadata(){
		this.slos = new ArrayList<Slo>();
	}

	@SuppressWarnings("rawtypes")
	public void createMetadata(Element metadata, Dictionary configuration){

		/**
		 *Get offer information
		 */
		
		Element[] elementsPro = metadata.getElements(ConstantsProvider.NAME, ConstantsProvider.NAMESPACE);
		
			setProName(elementsPro[0].getAttribute(ConstantsProvider.PROVIDER_NAME_ATTRIBUTE));
			setProPid(elementsPro[0].getAttribute(ConstantsProvider.PROVIDER_PID_ATTRIBUTE));
			setDurationUnit(elementsPro[0].getAttribute(ConstantsProvider.PROVIDER_DURATION_UNIT_ATTRIBUTE));
			setDurationValue(Double.parseDouble(elementsPro[0].getAttribute(ConstantsProvider.PROVIDER_DURATION_VALUE_ATTRIBUTE)));
		/**
		 *Get elements slos
		 */
			
			Element[] elementsSlo = elementsPro[0].getElements(ConstantsProvider.SLO_ELEMENT);
			for(Element sloEle : elementsSlo){
				
				Slo slo = new Slo();
				
					//name
					slo.setName(sloEle.getAttribute(ConstantsProvider.SLO_NAME_ATTRIBUTE));
					
					//target
					slo.setTarget(sloEle.getAttribute(ConstantsProvider.SLO_TARGET_ATTRIBUTE));
					
					//value
					slo.setValue(Double.parseDouble(sloEle.getAttribute(ConstantsProvider.SLO_VALUE_ATTRIBUTE)));
					
					getSlos().add(slo);
					
			}
		
	}

	public String getProPid() {
		return proPid;
	}

	public void setProPid(String proPid) {
		this.proPid = proPid;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getDurationUnit() {
		return durationUnit;
	}

	public void setDurationUnit(String durationUnit) {
		this.durationUnit = durationUnit;
	}

	public double getDurationValue() {
		return durationValue;
	}

	public void setDurationValue(double durationValue) {
		this.durationValue = durationValue;
	}

	public List<Slo> getSlos() {
		return slos;
	}

	
	
	
}
