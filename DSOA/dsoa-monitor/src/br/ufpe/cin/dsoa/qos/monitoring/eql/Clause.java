package br.ufpe.cin.dsoa.qos.monitoring.eql;

public class Clause {
	private String atribute;
	private String expression;
	private Object value;
	
	public Clause(String atribute, String expression, Object value) {
		super();
		this.atribute = atribute;
		this.expression = expression;
		this.value = value;
	}
	
	public String getAtribute() {
		return atribute;
	}
	public String getExpression() {
		return expression;
	}
	public Object getValue() {
		return value;
	}
	
	
}
