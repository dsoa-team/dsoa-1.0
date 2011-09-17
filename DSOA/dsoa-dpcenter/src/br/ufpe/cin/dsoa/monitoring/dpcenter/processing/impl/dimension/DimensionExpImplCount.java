package br.ufpe.cin.dsoa.monitoring.dpcenter.processing.impl.dimension;

import br.ufpe.cin.dsoa.monitoring.dpcenter.processing.api.DimensionExp;

import com.espertech.esper.client.soda.Expression;
import com.espertech.esper.client.soda.Expressions;

public class DimensionExpImplCount implements DimensionExp {

	@Override
	public Expression getExpressions(String value) {
		return Expressions.count(value);
	}

}
