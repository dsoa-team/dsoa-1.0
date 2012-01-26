package br.ufpe.cin.dsoa.qos.monitoring.impl;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.EventAdmin;

import br.ufpe.cin.dsoa.qos.epcenter.service.EventConsumer;
import br.ufpe.cin.dsoa.qos.epcenter.service.EventProcessingCenter;
import br.ufpe.cin.dsoa.qos.monitoring.service.MonitoringConfiguration;
import br.ufpe.cin.dsoa.qos.monitoring.service.MonitoringConfigurationItem;
import br.ufpe.cin.dsoa.qos.monitoring.service.MonitoringService;
import br.ufpe.cin.dsoa.qos.monitoring.service.events.ArrivalEvent;
import br.ufpe.cin.dsoa.qos.monitoring.service.events.Availability;
import br.ufpe.cin.dsoa.qos.monitoring.service.events.CPUEvent;
import br.ufpe.cin.dsoa.qos.monitoring.service.events.DepartureEvent;
import br.ufpe.cin.dsoa.qos.monitoring.service.events.ErrorEvent;
import br.ufpe.cin.dsoa.qos.monitoring.service.events.MemoryEvent;
import br.ufpe.cin.dsoa.qos.monitoring.service.events.RequestEvent;
import br.ufpe.cin.dsoa.qos.monitoring.service.events.ResponseEvent;
import br.ufpe.cin.dsoa.qos.monitoring.service.events.ResponseTime;

public class MonitoringServiceImpl implements MonitoringService {

	private BundleContext         bundleContext;
	private EventProcessingCenter eventProcessingCenter;
	private EventAdmin            eventAdmin;

	public MonitoringServiceImpl(BundleContext bundleContext) {
		this.bundleContext                = bundleContext;
		ServiceReference serviceReference = this.bundleContext.getServiceReference(EventProcessingCenter.class.getName());
		
		if(serviceReference != null){
			this.eventProcessingCenter   = (EventProcessingCenter) this.bundleContext.getService(serviceReference);
		}
		
		ServiceReference ref = bundleContext.getServiceReference(EventAdmin.class.getName());
		if (ref != null) {
			this.eventAdmin = (EventAdmin) bundleContext.getService(ref);
		}
		
		registerEventsQoS();
	}

	private void registerEventsQoS() {
		
		this.eventProcessingCenter.defineEvent(RequestEvent.class);
		this.eventProcessingCenter.defineEvent(ResponseEvent.class);
		this.eventProcessingCenter.defineEvent(ErrorEvent.class);
		this.eventProcessingCenter.defineEvent(ArrivalEvent.class);
		this.eventProcessingCenter.defineEvent(DepartureEvent.class);
		this.eventProcessingCenter.defineEvent(MemoryEvent.class);
		this.eventProcessingCenter.defineEvent(CPUEvent.class);
		this.eventProcessingCenter.defineEvent(ResponseTime.class);
		this.eventProcessingCenter.defineEvent(Availability.class);
		
		//ResponseTime
		String requestEventName  = RequestEvent.class.getSimpleName();
		String responseEventName = ResponseEvent.class.getSimpleName();
		String responseTimeStmt = "select (b.timestamp - a.timestamp) as value, a.consumerId as consumerId," +
		" a.operationName as operationName, a.serviceId as serviceId " + 
		"from pattern [every a=" + requestEventName + "->" + "b=" + responseEventName + 
		"(b.correlationId = a.correlationId)]";
		this.eventProcessingCenter.defineStatement(ResponseTime.class.getSimpleName(), responseTimeStmt);
		this.eventProcessingCenter.subscribe(ResponseTime.class.getSimpleName(), new Listener());
		
		//Avaliability
		String departureEventName = DepartureEvent.class.getSimpleName();
		String arrivalEventName   = ArrivalEvent.class.getSimpleName();
		String availabilityStmt       = "select (b.timestamp - a.timestamp) as value, a.serviceId as serviceId " + 
		"from pattern [every a=" + arrivalEventName + "->" + "b=" + departureEventName  + 
		"(b.serviceId = a.serviceId)]";
		
		this.eventProcessingCenter.defineStatement(Availability.class.getSimpleName(), availabilityStmt);
		this.eventProcessingCenter.subscribe(Availability.class.getSimpleName(), new Listener());

		
		
	}
	
	class Listener implements EventConsumer{
		
		@SuppressWarnings("rawtypes")
		@Override
		public void receive(Map result, Object userObject,String statementName) {
			System.out.println("");
			System.out.println(" *******  Receive: " + statementName + "   ********");
			Map<String,Object> propriedades = result;
			for (Map.Entry<String, Object>  elemento : propriedades.entrySet()) {
				System.out.println("chave: " + elemento.getKey() + " - " + "valor: " + elemento.getValue() + "  tipo do valor: " + elemento.getValue().getClass());
			}
			
			if(statementName.equalsIgnoreCase(ResponseTime.class.getSimpleName())){
				long value = Long.valueOf(String.valueOf(result.get("value")));
				ResponseTime responseTime = new ResponseTime(value,String.valueOf(result.get("consumerId")),
											String.valueOf(result.get("operationName")),String.valueOf(result.get("serviceId"))) ;
				eventProcessingCenter.publishEvent(responseTime);
			}
			
			if(statementName.equalsIgnoreCase(Availability.class.getSimpleName())){
				double v = Double.valueOf(String.valueOf(result.get("value")));
				Availability avaliability = new Availability(v, String.valueOf(result.get("serviceId")));
				eventProcessingCenter.publishEvent(avaliability);
			}
			
		}
		
	}
	
	
	@Override
	public void startMonitoring(MonitoringConfiguration configuration) {
		if (!configuration.isOld()) {
			for (MonitoringConfigurationItem item : configuration.getItens().values()) {
				String name = String.valueOf(item.getId());
				this.eventProcessingCenter.definePreparedStatement(name, item.getStatement());
				this.eventProcessingCenter.modifyStatement(name, 1, configuration.getServiceId());
				this.eventProcessingCenter.createPreparedStatement(name);
				this.eventProcessingCenter.subscribe(name, configuration);
			}
			configuration.setOld();
		} 
		else{
			for (MonitoringConfigurationItem item : configuration.getItens().values()) {
				String name = String.valueOf(item.getId());
				//Verificar o início da indexação
				this.eventProcessingCenter.modifyStatement(name, 1, configuration.getServiceId());
				this.eventProcessingCenter.destroyStatement(name);
				this.eventProcessingCenter.createPreparedStatement(name);
				this.eventProcessingCenter.subscribe(name, configuration);
				
			}
		}
	}
	

	@Override
	public void publishMonitoringEvent(Object event) {
		org.osgi.service.event.Event  eventSent = null;
		Map<String, Object>           eventMap  = new HashMap<String, Object>();
		
		eventMap.put(event.getClass().getSimpleName(), event);
		eventSent = new org.osgi.service.event.Event(event.getClass().getSimpleName(), eventMap);
		this.eventAdmin.postEvent(eventSent);
		
	}
	
	
	
}
