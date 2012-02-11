package br.ufpe.cin.dsoa.qos.monitoring.eql;

import java.util.Iterator;
import java.util.List;

public class Select {
	
	private Statistic statistic;
	private List<String> fields;

	public Select(Statistic statistic, List<String> fields) {
		this.statistic = statistic;
		this.fields = fields;
	}
	
	public Select(List<String> fields) {
		this.fields = fields;
	}
	
	public Select(Statistic statistic) {
		this.statistic = statistic;
	}

	public Statistic getStatistic() {
		return statistic;
	}

	public List<String> getFields() {
		return fields;
	}

	public String toString() {
		Iterator<String> itrFields = fields.iterator();
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("Select ");
		if (null != statistic) {
			strBuilder.append(statistic.toString());
			if (itrFields.hasNext()) {
				strBuilder.append(",");
			}
		}
		
		while(itrFields.hasNext()) {
			strBuilder.append(itrFields.next());
			if (itrFields.hasNext()) {
				strBuilder.append(itrFields.next());
			}
		}
		
		return strBuilder.toString();
	}
}
