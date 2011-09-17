package br.ufpe.cin.dsoa.slamanager.agreement;

public class Slo {
	private String name;
	private Expression expression;
	private double value;
	private String target;
	private long   weight;
	
	public Slo() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public long getWeight() {
		return weight;
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}

/**
 * Fabions
 
	public boolean satisfies(Slo sloRequired) {
		// TODO Auto-generated method stub
		if (this.getName().equals(sloRequired.getName())) {
			this.expression.evaluate(this.getValue(), rightOp);
		}
		return false;
	}
*/	
}
