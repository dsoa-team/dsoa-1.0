package br.ufpe.cin.dsoa.qos.monitoring.eql;

public class Statistic {
	private String name;
	private String field;
	
	public Statistic(String name, String field) {
		this.name = name;
		this.field = field;
	}

	public String getName() {
		return name;
	}

	public String getField() {
		return field;
	}
	
	public String toString() {
		return name + "(" + field + ")";
	}
}
