package br.ufpe.cin.dsoa.qos.dpcenter.processing.api;

import com.espertech.esper.client.soda.Expression;

public interface DimensionExp {
	
	public Expression getExpressions(String value);

}
