package br.ufpe.cin.dsoa.qos.reqhandler;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Properties;

import org.apache.felix.ipojo.ConfigurationException;
import org.apache.felix.ipojo.PrimitiveHandler;
import org.apache.felix.ipojo.architecture.ComponentTypeDescription;
import org.apache.felix.ipojo.metadata.Element;
import org.apache.felix.ipojo.parser.FieldMetadata;
import org.apache.felix.ipojo.parser.PojoMetadata;
import org.apache.felix.ipojo.util.DependencyModel;
import org.apache.felix.ipojo.util.DependencyStateListener;

import br.ufpe.cin.dsoa.qos.slamanager.agreement.Slo;
import br.ufpe.cin.dsoa.qos.slamanager.service.ConstantsSLA;
import br.ufpe.cin.dsoa.qos.slamanager.service.SLAmanagerService;

public class QosDependencyHandler extends PrimitiveHandler implements DependencyStateListener{
	
	private String consumerPID;
	private String consumerName;
	private SLAmanagerService slaManagerService;
	
	private List<QosDependency> dependencies = new ArrayList<QosDependency>();
	
	public List<QosDependency> getDependencies() {
		return dependencies;
	}

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
		
		Element[] serviceElems = requires.getElements(Constants.SERVICE_ELEMENT);
		QosDependencyMetadata dm = new QosDependencyMetadata();
		
		for (Element service : serviceElems) {
			dm.reset();
			dm.createMetadata(service, configuration);
			
			String field            = dm.getField();
			FieldMetadata fieldmeta = manipulation.getField(field);

			String svcInterface = fieldmeta.getFieldType();
			Class specification = DependencyModel.loadSpecification(svcInterface, getInstanceManager().getContext());
			
			// SLOs
			List<Slo> slos = dm.getSlos();
			
			QosDependency dependency = new QosDependency(specification, getInstanceManager().getContext(), this, slos);
			
			this.dependencies.add(dependency);

								
			// register the service field
			getInstanceManager().register(fieldmeta, dependency);
		}
		
	}
	
	public void imprime(){
		
		System.out.println("Consumer Pid: " + getConsumerPID());
		System.out.println("Consumer Name: " + getConsumerName());
		
		for(QosDependency dep: getDependencies()){
			List<Slo> slos =	dep.getSlos();
			for(Slo  entrada : slos){
				System.out.println("slo Name: " +  entrada.getName() + " value: " + entrada.getValue());
			}
			System.out.println("");
			System.out.println("");
			
		}
		
	}

	@Override
	public void start() {
		//imprime();
		this.setValidity(false);
		
		for(QosDependency qosDep: getDependencies()){
			qosDep.start();
		}
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * forma valida para o dependency model
	 
	@Override
	public void invalidate(DependencyModel arg0) {
		setValidity(false);
		
	}

	@Override
	public void validate(DependencyModel arg0) {
		boolean m_state = true;
		for(QosDependency dep : getDependencies()){
			if(dep.getState() == 0){
				m_state = false;
				break;
			}
		}
		setValidity(m_state);
	}
	*/
	
	public void checkValidate(){
		
		boolean m_state = true;
		for(QosDependency dep : getDependencies()){
			if(dep.isStateDep() == false){
				m_state = false;
				break;
			}
		}
		
		if(m_state == true){
			Properties consumerInfo = new Properties();
			consumerInfo.setProperty(ConstantsSLA.CONSUMER_PID, getConsumerPID());
			consumerInfo.setProperty(ConstantsSLA.CONSUMER_NAME,getConsumerName());
			
			for(QosDependency depe : getDependencies()){
				Properties providerInfo = new Properties();
				providerInfo.setProperty(ConstantsSLA.PROVIDER_PID, depe.getProviderPid());
				providerInfo.setProperty(ConstantsSLA.PROVIDER_NAME,depe.getProviderName());
				providerInfo.setProperty(ConstantsSLA.AGREEMENT_DURATION,String.valueOf(depe.getDuration()));
				
				
				slaManagerService.createAgreement(consumerInfo, providerInfo, depe.getSlos());
			}
			setValidity(true);
		}
		
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

	@Override
	public void validate(DependencyModel dependency) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void invalidate(DependencyModel dependency) {
		// TODO Auto-generated method stub
		
	}


	public SLAmanagerService getSlaManagerService() {
		return slaManagerService;
	}
	
}
