package br.ufpe.cin.dsoa.monitoring.dpcenter.processing.impl;
import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;

import br.ufpe.cin.dsoa.management.MonitoringEvent;
import br.ufpe.cin.dsoa.management.MonitoringHandler;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.event.map.MapEventBean;

public class Notifier implements UpdateListener{
	
	private List<String> monitoringHandlers = new ArrayList<String>();
	private BundleContext bundleContext;
	private MonitoringHandlerServiceTracker monitoringHandlerServiceTracker;
	
	public Notifier(BundleContext bundleContext,List<String> handlers) {
		this.monitoringHandlers = handlers;
		this.bundleContext = bundleContext;
		this.monitoringHandlerServiceTracker = new MonitoringHandlerServiceTracker(bundleContext, MonitoringHandler.class.getName(), null);
		this.monitoringHandlerServiceTracker.open();
	}

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if(newEvents == null || newEvents.length == 0)
            return;
		System.out.println("======  UPDATE   =======");
        MonitoringEvent[] events = new MonitoringEvent[newEvents.length];
        
        
        for(int i = 0; i < newEvents.length; i++){
        	System.out.println("-----------******---------");
        	MapEventBean propertiesMap = (MapEventBean) newEvents[i];
        	MonitoringEvent event = new MonitoringEvent();
        	event.setProperties(propertiesMap.getProperties());
            events[i] = event;
            System.out.println(event.toString());
        }
        
        
        for (String handler : getMonitoringHandlers()) {
        	System.out.println("==> Handler: " + handler);
        	MonitoringHandler han = getMonitoringHandlerServiceTracker().getHandler(handler);
        	System.out.println("Handler: " + han);
        	han.handleEvents(events);
		}
        
		
	}
	
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public void setMonitoringHandlers(List<String> handlers) {
		this.monitoringHandlers = handlers;
	}

	public List<String> getMonitoringHandlers() {
		return monitoringHandlers;
	}

	public MonitoringHandlerServiceTracker getMonitoringHandlerServiceTracker() {
		return monitoringHandlerServiceTracker;
	}
	
	
}
