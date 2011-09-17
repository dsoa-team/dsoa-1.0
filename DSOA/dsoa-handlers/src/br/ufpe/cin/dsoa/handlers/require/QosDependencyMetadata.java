package br.ufpe.cin.dsoa.handlers.require;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.apache.felix.ipojo.ConfigurationException;
import org.apache.felix.ipojo.metadata.Element;

import br.ufpe.cin.dsoa.slamanager.agreement.Slo;

public class QosDependencyMetadata {
	
	private String field;
	private List<Slo> slos;
	
	
	public QosDependencyMetadata() {
		this.slos = new ArrayList<Slo>();
	}
	
	@SuppressWarnings("rawtypes")
	public void createMetadata(Element service, Dictionary configuration) throws ConfigurationException {   

		// Initializes field
		buildAttributes(service);

		// SLOs
		buildSLOs(service, configuration);

	}
	
	private void buildAttributes(Element demand) throws ConfigurationException {
		// field attribute
		setField(demand.getAttribute(Constants.SERVICE_FIELD_ATTRIBUTE));

	}
	
	@SuppressWarnings("rawtypes")
	private void buildSLOs(Element demand, Dictionary configuration){
		
			Element[] sloSet = demand.getElements(Constants.SLO_ELEMENT);
			for (Element sloEle : sloSet){

					//name
					String name = sloEle.getAttribute(Constants.SLO_NAME_ATTRIBUTE);
					
					//value
					double value = Double.parseDouble(sloEle.getAttribute(Constants.SLO_VALUE_ATTRIBUTE));
					
					//expression
					String expression = sloEle.getAttribute(Constants.SLO_EXPRESSION_ATTRIBUTE);
					
					//target
					String target = sloEle.getAttribute(Constants.SLO_TARGET_ATTRIBUTE);
					
					//weight
					long weight = Long.parseLong(sloEle.getAttribute(Constants.SLO_WEIGHT_ATTRIBUTE));
					
					
					Slo  slo = new Slo();
				//	slo.setExpression(expression);
					slo.setName(name);
					slo.setTarget(target);
					slo.setValue(value);
					slo.setWeight(weight);
					
					this.slos.add(slo);
			}
		}

	public void setField(String field) {
		this.field = field;
	}

	public String getField() {
		return field;
	}

	public void reset() {
		setField(null);
		getSlos().clear();
	}

	
	
	public List<Slo> getSlos() {
		return slos;
	}

}
