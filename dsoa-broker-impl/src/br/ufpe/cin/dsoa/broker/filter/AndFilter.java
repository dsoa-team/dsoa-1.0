package br.ufpe.cin.dsoa.broker.filter;

import java.util.List;

public class AndFilter extends FilterBuilder {
	
	private final List<FilterBuilder> f;

	public AndFilter(List<FilterBuilder> f) {
		super();
		this.f = f;
	}

	@Override
	public StringBuilder append(StringBuilder buffer) {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		builder.append("(&");
		
		for(FilterBuilder filter: f) {
			filter.append(builder);
		}
		
		builder.append(")");
		return builder;
	}
}
