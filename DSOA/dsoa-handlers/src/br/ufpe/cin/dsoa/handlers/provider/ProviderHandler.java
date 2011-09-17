package br.ufpe.cin.dsoa.qos.prohandler;

import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.felix.ipojo.ConfigurationException;
import org.apache.felix.ipojo.PrimitiveHandler;
import org.apache.felix.ipojo.handlers.providedservice.ProvidedServiceHandler;
import org.apache.felix.ipojo.metadata.Element;

import br.ufpe.cin.dsoa.qos.slamanager.agreement.Slo;

public class ProviderHandler extends PrimitiveHandler {

	
	private ProviderMetadata providerMetadata;
	
	@SuppressWarnings("rawtypes")
	public void configure(Element metadata, Dictionary configuration) throws ConfigurationException {
		
		providerMetadata = new ProviderMetadata();
		providerMetadata.createMetadata(metadata,configuration);
		
	}

	@Override
	public void stop() {
		// TODO do something ?? Need remove entries from Maps ?
	}       

	@Override
	public void start() {
		ProvidedServiceHandler providedServiceHandler = (ProvidedServiceHandler) getInstanceManager().getHandler(
														ConstantsProvider.IPOJO_PROVIDED_SERVICE_HANDLER);
		
		Hashtable<String, Object> propertiesToAdd = new Hashtable<String, Object>();
		
		if (providedServiceHandler != null){

			propertiesToAdd.put(ConstantsProvider.PROVIDER_PID, getProviderMetadata().getProPid());
			propertiesToAdd.put(ConstantsProvider.PROVIDER_NAME, getProviderMetadata().getProName());
			propertiesToAdd.put(ConstantsProvider.DURATION_UNIT, getProviderMetadata().getDurationUnit());
			propertiesToAdd.put(ConstantsProvider.DURATION_VALUE, getProviderMetadata().getDurationValue());
			

			for (Slo slo : getProviderMetadata().getSlos()) {
				if(slo.getTarget() != null){
					propertiesToAdd.put(slo.getName()+ "." + slo.getTarget(), slo.getValue());
				}
				else{
					propertiesToAdd.put(slo.getName(), slo.getValue());
				}
				
				
				
			}

			// Then add the DSLA properties
			providedServiceHandler.addProperties(propertiesToAdd);
			
			
		}
		
		
		
	}

	public ProviderMetadata getProviderMetadata() {
		return providerMetadata;
	}
	

}