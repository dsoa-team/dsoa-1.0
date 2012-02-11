package br.ufpe.cin.dsoa.broker.filter;

public abstract class FilterBuilder {
	
	@Override
	public final String toString() {
		return append(new StringBuilder()).toString();
	}
	
	public abstract StringBuilder append(StringBuilder builder);
	
}
