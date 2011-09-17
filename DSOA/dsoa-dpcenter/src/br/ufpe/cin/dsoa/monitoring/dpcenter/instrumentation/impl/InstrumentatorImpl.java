package br.ufpe.cin.dsoa.monitoring.dpcenter.instrumentation.impl;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import br.ufpe.cin.dsoa.monitoring.dpcenter.instrumentation.api.Instrumentator;
import br.ufpe.cin.dsoa.monitoring.dpcenter.publication.impl.EventHandlerImpl;

import com.espertech.esper.client.EPServiceProvider;

public class InstrumentatorImpl implements Instrumentator{

	private EPServiceProvider  epServiceProvider;
	private BundleContext      bundleContext;
	
	@SuppressWarnings("unchecked")
	@Override
	public void createInstrumentationCell(String eventTypeName, Map typeMap,String topic) {
		getEpServiceProvider().getEPAdministrator().getConfiguration().addEventType(eventTypeName, typeMap);
		Dictionary<Object,Object> propriedades = new Hashtable<Object,Object>();
		String[] topics = new String[] {topic};
		propriedades.put(EventConstants.EVENT_TOPIC, topics);
		getBundleContext().registerService(EventHandler.class.getName(),new EventHandlerImpl(getEpServiceProvider()), propriedades);
	}

	public InstrumentatorImpl(EPServiceProvider  epServiceProvider, BundleContext bundleContext) {
		this.epServiceProvider = epServiceProvider;
		this.setBundleContext(bundleContext);
	}

	@Override
	public boolean destroyInstrumentationCell(String eventTypeName) {
		Set<String> statementsUsed = getEpServiceProvider().getEPAdministrator().getConfiguration().getEventTypeNameUsedBy(eventTypeName);
		for (Iterator<String> iterator = statementsUsed.iterator(); iterator.hasNext();) {
			String statement = (String) iterator.next();
			getEpServiceProvider().getEPAdministrator().getStatement(statement).destroy();
			
		}
		return getEpServiceProvider().getEPAdministrator().getConfiguration().removeEventType(eventTypeName, false);
	}

	public void setEpServiceProvider(EPServiceProvider epServiceProvider) {
		this.epServiceProvider = epServiceProvider;
	}

	public EPServiceProvider getEpServiceProvider() {
		return epServiceProvider;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public BundleContext getBundleContext() {
		return bundleContext;
	}

}
