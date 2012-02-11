package br.ufpe.cin.dsoa.qos;

public class Slo {
	private String attribute;
	private Expression expression;
	private double value;
	private String operation;
	private String statistic;
	private long   weight;
	
	public Slo(String attribute, Expression expression, double value,
		String operation, String statistic, long weight) {
		this(attribute, value, operation, statistic);
		this.weight = weight;
		this.expression = expression;
	}
	
	public Slo(String attribute, double value,
			String operation, String statistic) {
			super();
			this.attribute = attribute;
			this.value = value;
			this.operation = operation;
			this.statistic = statistic;
	}
	
	public String getAttribute() {
		return attribute;
	}
	
	public Expression getExpression() {
		return expression;
	}

	public double getValue() {
		return value;
	}

	public String getOperation() {
		return operation;
	}

	public String getStatistic() {
		return statistic;
	}

	public long getWeight() {
		return weight;
	}

}
