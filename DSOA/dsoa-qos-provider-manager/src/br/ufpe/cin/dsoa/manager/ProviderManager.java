package br.ufpe.cin.dsoa.manager;

import java.lang.reflect.Method;
import java.util.Dictionary;

import org.apache.felix.ipojo.ConfigurationException;
import org.apache.felix.ipojo.PrimitiveHandler;
import org.apache.felix.ipojo.handlers.providedservice.ProvidedServiceHandler;
import org.apache.felix.ipojo.metadata.Element;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ManagedService;

import br.ufpe.cin.dsoa.manager.metadata.ProviderMetadata;

public class ProviderManager extends PrimitiveHandler implements ManagedService {

	private ServiceRegistration registration;
	private ProviderMetadata p_metadata;

	@Override
	public void configure(Element metadata,
			@SuppressWarnings("rawtypes") Dictionary configuration)
			throws ConfigurationException {

		p_metadata = new ProviderMetadata();
		p_metadata.loadMetadata(metadata, configuration);

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {

		registration = getInstanceManager().getContext().registerService(
				ManagedService.class.getName(), this, null);

		ProvidedServiceHandler handlerProvider = (ProvidedServiceHandler) super
				.getInstanceManager().getHandler(
						"org.apache.felix.ipojo:provides");
		
		if (null != handlerProvider) {
			handlerProvider.addProperties(p_metadata.getRegisterProperties());
			
			/* 'Serializa' a lista de SLOs
			Dictionary<String, Object> properties =  new Hashtable<String, Object>();
			StringBuffer s = new StringBuffer();
			int i = 0;
			for(Slo slo : p_metadata.getSlos()){
				s.append(slo.toString());
				if(i < p_metadata.getSlos().size()){
					s.append(";");
				}
				i++;
				
			}
			properties.put("provider.slos", s.toString());
			handlerProvider.addProperties(properties);*/
		}

	}

	@Override
	public void onEntry(Object arg0, Method arg1, Object[] arg2) {
		System.err.println("ON ENTRY");
	}

	@Override
	public void onError(Object arg0, Method arg1, Throwable arg2) {
		System.err.println("ON ERROR");
	}

	@Override
	public void onExit(Object arg0, Method arg1, Object arg2) {
		System.err.println("ON EXIT");
	}

	@Override
	public Object onGet(Object pojo, String fieldName, Object value) {
		System.err.println("ON GET");
		return super.onGet(pojo, fieldName, value);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary arg0)
			throws org.osgi.service.cm.ConfigurationException {

		registration.setProperties(arg0);

		ProvidedServiceHandler handlerProvider = (ProvidedServiceHandler) super
				.getInstanceManager().getHandler(
						"org.apache.felix.ipojo:provides");

		if (null != handlerProvider) {
			handlerProvider.addProperties(arg0);
		}

	}

	public ProviderMetadata getProviderMetadata() {
		return this.p_metadata;
	}
}
