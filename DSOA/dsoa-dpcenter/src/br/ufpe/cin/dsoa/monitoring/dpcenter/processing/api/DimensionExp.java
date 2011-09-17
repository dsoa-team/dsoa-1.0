package br.ufpe.cin.dsoa.monitoring.dpcenter.processing.api;

import com.espertech.esper.client.soda.Expression;

public interface DimensionExp {
	
	public Expression getExpressions(String value);

}
