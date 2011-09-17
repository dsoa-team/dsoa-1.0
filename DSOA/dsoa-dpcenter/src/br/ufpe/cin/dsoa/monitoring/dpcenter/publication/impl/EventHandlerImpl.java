package br.ufpe.cin.dsoa.monitoring.dpcenter.publication.impl;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import br.ufpe.cin.dsoa.management.MetaData;
import br.ufpe.cin.dsoa.monitoring.dpcenter.publication.api.Publisher;

import com.espertech.esper.client.EPServiceProvider;

public class EventHandlerImpl implements EventHandler {

	Publisher publisher;
	
	public EventHandlerImpl(EPServiceProvider epsProvider) {
		this.publisher = new PublisherImpl(epsProvider);
	}
	
	public Publisher getPublisher() {
		return publisher;
	}

	@SuppressWarnings("unchecked")
	public void handleEvent(Event event) {
		Map properties = new HashMap();
		/**
		 * Alteracao 1
		 * Nao preciso do event.topics 
		 * 
		 * Alteracao 2
		 * Tem que ser um Map se nao terei de ter para cada servico de monitoramento um EventHandler diferente
		 * por exemplo, o Servico de Monitoramento de Disponibilidade nao teria Duration, e sim atributo X
		 * entao teria de construir outro handler
		 * 
		 * Alteracao 3
		 * o timeStamp deve vim como long
		 * o Esper nao aceita o formato Date.
		 * 
		 * A partir do long depois podemos criar uma 
		 * Date d = new Date(long);
		 * 
		 */
		String eventTypeName = (String) event.getProperty(MetaData.QOS_CHARACTERISTIC);
		System.out.println("EventType: " + eventTypeName);
		for(String propertyName:event.getPropertyNames()) {
			System.out.println("Name: " + propertyName);
			System.out.println("Value: " + event.getProperty(propertyName));
			properties.put(propertyName, event.getProperty(propertyName));
		}
		
		properties.remove("event.topics");
		
		getPublisher().publish(properties, eventTypeName);
	}
}
