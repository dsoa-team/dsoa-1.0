package br.ufpe.cin.dsoa.qos.epcenter.impl;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import br.ufpe.cin.dsoa.qos.epcenter.service.EventConsumer;
import br.ufpe.cin.dsoa.qos.epcenter.service.EventProcessingCenter;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPPreparedStatement;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.soda.EPStatementObjectModel;

public class EventProcessingCenterImpl implements EventProcessingCenter{
	
	private EPServiceProvider         epServiceProvider;
	private BundleContext             bundleContext;
	private Map<String,EventNotifier> notifierMap;
	private Map<String,EPPreparedStatement> preparedStmts;
	
	public EventProcessingCenterImpl(BundleContext context) {
		this.bundleContext = context;
		this.notifierMap = new HashMap<String,EventNotifier>();
		this.epServiceProvider = EPServiceProviderManager.getProvider("EngineInstance", new Configuration());
		this.preparedStmts = new HashMap<String,EPPreparedStatement>();
	}
	
	@Override
	public void publishEvent(Object event) {
		System.out.println("************* Event Published:  " + event.getClass().getSimpleName() + "  *************");
		this.epServiceProvider.getEPRuntime().sendEvent(event);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void publishEvent (Map event, String eventName){
		this.epServiceProvider.getEPRuntime().sendEvent(event, eventName);
	}
	

	@Override
	public void definePreparedStatement(String name, String statement) {
		EPPreparedStatement preparedStmt = this.epServiceProvider.getEPAdministrator().prepareEPL(statement);
		this.preparedStmts.put(name, preparedStmt);
	}
	
	public void createPreparedStatement(String name) {
		this.epServiceProvider.getEPAdministrator().create(this.preparedStmts.get(name), name);
		System.out.println("");
		System.out.println("");
		for(String s : this.epServiceProvider.getEPAdministrator().getStatementNames()){
			System.out.println("statment name: " + this.epServiceProvider.getEPAdministrator().getStatement(s).getName());
			System.out.println("statment text: " + this.epServiceProvider.getEPAdministrator().getStatement(s).getText());
		}
		System.out.println("");
		System.out.println("");
	}

	public void defineStatement(String name, String statement) {
		this.epServiceProvider.getEPAdministrator().createEPL(statement, name);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void defineEvent(Class event) {
		this.epServiceProvider.getEPAdministrator().getConfiguration().addEventType(event);
		Dictionary<Object,Object> propriedades = new Hashtable<Object,Object>();
		String[] topics = new String[] {event.getSimpleName()};
		propriedades.put(EventConstants.EVENT_TOPIC, topics);
		this.bundleContext.registerService(EventHandler.class.getName(),new EventHandlerImpl(), propriedades);
	}

	@Override
	public void subscribe(String nameStatement, EventConsumer eventConsumer) {
		EventNotifier notifier = notifierMap.get(nameStatement);
		if (null == notifier) {
			notifier = new EventNotifier();
			this.notifierMap.put(nameStatement, notifier);
		}
		notifier.addEventConsumer(eventConsumer);
		this.epServiceProvider.getEPAdministrator().getStatement(nameStatement).addListener(notifier);
		
	}
	
	public void unsubscribe(String nameStatement, EventConsumer eventConsumer) {
		EventNotifier notifier = this.notifierMap.get(nameStatement);
		notifier.removeEventConsumer(eventConsumer);
		if (!notifier.hasEventConsumers()) {
			this.epServiceProvider.getEPAdministrator().getStatement(nameStatement).destroy();
		}
	}
	
	@Override
	public void destroyStatement(String statementName) {
		this.epServiceProvider.getEPAdministrator().getStatement(statementName).destroy();
	}

	@Override
	public void modifyStatement(String statementName,int index, Object value) {
		this.preparedStmts.get(statementName).setObject(index, value);
	}
	
	
	class EventHandlerImpl implements EventHandler {
		
		@Override
		public void handleEvent(org.osgi.service.event.Event event) {
			Object eventReceived = event.getProperty(event.getTopic());
			publishEvent(eventReceived);
		}

	}
	
}
