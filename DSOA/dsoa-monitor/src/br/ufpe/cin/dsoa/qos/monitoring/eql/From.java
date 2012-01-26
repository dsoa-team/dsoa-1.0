package br.ufpe.cin.dsoa.qos.monitoring.eql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class From {
	private String eventType;
	private List<Clause> filter;

	public From(String eventType) {
		this.eventType = eventType;
		this.filter    = new ArrayList<Clause>();
	}

	public String getEventType() {
		return eventType;
	}

	public List<Clause> getFilter() {
		return filter;
	}
	
	/*
	public String toString() {
		StringBuilder strBuilder = new StringBuilder(" from ");
		strBuilder.append(eventType);
		Iterator<Clause> itrClause = filter.iterator();
		
		if (itrClause.hasNext()) {
			strBuilder.append("(");
		}
		
		while(itrClause.hasNext()) {
			clau
		}
	}
	*/
}
