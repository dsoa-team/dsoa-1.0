package br.ufpe.cin.dsoa.qos.management;

import java.util.Map;

public class MonitoringEvent {
	
	private String eventType;
    private Map propertyTypes;
	private Map properties;
	
	public MonitoringEvent(String eventType, Map propertyTypes, Map properties) {
		super();
		this.eventType = eventType;
		this.propertyTypes = propertyTypes;
		this.properties = properties;
	}
	
    
    public MonitoringEvent() {
	}



	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

    public Map getPropertyTypes() {
		return propertyTypes;
	}

	public void setPropertyTypes(Map propertyTypes) {
		this.propertyTypes = propertyTypes;
	}

	public Map getProperties() {
        return properties;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }
    
    public Object getProperty(String propertyName) {
    	return properties.get(propertyName);
    }
    
    @Override
    public String toString() {
    	StringBuffer buf = new StringBuffer();
    	buf.append("[eventType: ").append(eventType).append(" ");
    	for (Object key : properties.keySet()) {
    		buf.append("Key: ").append(key);
    		buf.append("Value: ").append(properties.get(key));
    	}
    	buf.append("]");
    	return buf.toString();
    }
}
