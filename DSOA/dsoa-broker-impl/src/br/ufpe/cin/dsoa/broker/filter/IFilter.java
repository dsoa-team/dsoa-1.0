package br.ufpe.cin.dsoa.broker.filter;



public class IFilter extends FilterBuilder {
	
	private final String name;
	private final String op = "=";
	private final String value;
	
	public IFilter(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	@Override
	public StringBuilder append(StringBuilder builder) {
		// TODO Auto-generated method stub
		return builder.append('(').append(name).append(op)
		.append(value).append(')');
	}

}
