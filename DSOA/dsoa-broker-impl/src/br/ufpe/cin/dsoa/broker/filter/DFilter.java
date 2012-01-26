package br.ufpe.cin.dsoa.broker.filter;

import br.ufpe.cin.dsoa.qos.Expression;



public class DFilter extends FilterBuilder{
	
	private final String name;
	private final double value;
	private final Expression expression;
	
	public DFilter(String name, Expression expression, double value) {
		super();
		this.name = name;
		this.value = value;
		this.expression = expression;
	}

	@Override
	public StringBuilder append(StringBuilder builder) {
		// TODO Auto-generated method stub
		return builder.append('(').append(name).append(expression.getOperator())
		.append(value).append(')');
	}
}
