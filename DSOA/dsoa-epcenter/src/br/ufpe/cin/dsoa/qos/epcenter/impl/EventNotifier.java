package br.ufpe.cin.dsoa.qos.epcenter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ufpe.cin.dsoa.qos.epcenter.service.EventConsumer;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;
import com.espertech.esper.event.map.MapEventBean;

public class EventNotifier implements StatementAwareUpdateListener{

	private List<EventConsumer> consumers;

	public EventNotifier() {
		this.consumers = new ArrayList<EventConsumer>();
	}

	public void addEventConsumer(EventConsumer eventConsumer) {
		this.consumers.add(eventConsumer);
	}

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents,
			EPStatement statement, EPServiceProvider epServiceProvider) {
		
		for(EventConsumer consumer : consumers) {
			for (int i = 0; i < newEvents.length; i++) {
				MapEventBean eventBean = (MapEventBean) newEvents[i];
				Map<String,Object> result = eventBean.getProperties();
				consumer.receive(result,statement.getUserObject(),statement.getName());
			}
		}
		
	}

	public void removeEventConsumer(EventConsumer eventConsumer) {
		// TODO Auto-generated method stub
		
	}

	public boolean hasEventConsumers() {
		boolean result = false;
		if (null != consumers && consumers.size() != 0) {
			result = true;
		}
		return result;
	}

}
