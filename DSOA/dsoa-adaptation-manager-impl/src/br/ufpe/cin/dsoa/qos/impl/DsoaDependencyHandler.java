package br.ufpe.cin.dsoa.qos.impl;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.apache.felix.ipojo.ConfigurationException;
import org.apache.felix.ipojo.PrimitiveHandler;
import org.apache.felix.ipojo.architecture.ComponentTypeDescription;
import org.apache.felix.ipojo.metadata.Element;
import org.apache.felix.ipojo.parser.FieldMetadata;
import org.apache.felix.ipojo.parser.PojoMetadata;
import org.apache.felix.ipojo.util.DependencyModel;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

import br.ufpe.cin.dsoa.qos.Constants;

public class DsoaDependencyHandler extends PrimitiveHandler {
	private String consumerPID;
	private String consumerName;
	private String qosMode;

	private List<DsoaDependency> dependencies = new ArrayList<DsoaDependency>();

	
	public void initializeComponentFactory(ComponentTypeDescription ctd, Element metadata) throws ConfigurationException {
		Element[] requiresElems = metadata.getElements(Constants.NAME, Constants.NAMESPACE);
		if (requiresElems.length != 1) {
			throw new ConfigurationException("One and only one "+Constants.NAME+" element is allowed in component "+ctd.getName()+" configuration. "
					+"use 'require' sub-elements to declare multiple dependencies.");
		}

		super.initializeComponentFactory(ctd, metadata);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void configure(Element metadata, Dictionary configuration)
			throws ConfigurationException {
		
		Element      requires     = metadata.getElements(Constants.NAME, Constants.NAMESPACE)[0];
		PojoMetadata manipulation = getFactory().getPojoMetadata();
		
		// Get consumer information
		setConsumerPID((String)  requires.getAttribute(Constants.CONSUMER_PID_ATTRIBUTE));
		setConsumerName((String) requires.getAttribute(Constants.CONSUMER_NAME_ATTRIBUTE));
		setQoSMode(requires.getAttribute(Constants.QOS_MODE));
		
		Element[] serviceElems = requires.getElements(Constants.SERVICE_ELEMENT);
		DsoaDependencyMetadata dm = new DsoaDependencyMetadata();
		
		for (Element service : serviceElems) {
			dm.reset();
			dm.createMetadata(service, configuration);
			
			String field            = dm.getField();
			FieldMetadata fieldmeta = manipulation.getField(field);

			String svcInterface = fieldmeta.getFieldType();
			Class specification = DependencyModel.loadSpecification(svcInterface, getInstanceManager().getContext());
			
			DsoaDependency dependency = new DsoaDependency(this, specification, dm.getSlos());
			dependencies.add(dependency);
			// register the service field
			getInstanceManager().register(fieldmeta, dependency);
		}
	}
	
/*	public void imprime(){
		
		System.out.println("Consumer Pid: " + getConsumerPID());
		System.out.println("Consumer Name: " + getConsumerName());
		System.out.println("size dep: " + getDependencies().size());
		for(DsoaDependency dep: getDependencies()){
			List<Slo> slos =	dep.getSlos();
			for(Slo  entrada : slos){
				System.out.println("slo Name: " +  entrada.getName() + " value: " + entrada.getValue());
			}
			System.out.println("");
			System.out.println("");
			
		}
		
	}*/

	private void setQoSMode(String qosMode) {
		this.qosMode = qosMode;
	}

	public String getQosMode() {
		return qosMode;
	}
	
	@Override
	public void start() {
		this.setValidity(false);
		for (DsoaDependency dep : dependencies) {
			dep.start();
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	public void checkValidate(){
		
		boolean valid = true;
		for(DsoaDependency dep : dependencies){
			if(dep.isValid() == false){
				valid = false;
				break;
			}
		}
		setValidity(valid);
	}

	public String getConsumerPID() {
		return consumerPID;
	}

	public void setConsumerPID(String consumerPID) {
		this.consumerPID = consumerPID;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public BundleContext getContext() {
		// TODO Auto-generated method stub
		return null;
	}
}
