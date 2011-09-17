package br.ufpe.cin.dsoa.qos.dpcenter.processing.impl.dimension;

import com.espertech.esper.client.soda.Expression;
import com.espertech.esper.client.soda.Expressions;

import br.ufpe.cin.dsoa.qos.dpcenter.processing.api.DimensionExp;

public class DimensionExpImplCount implements DimensionExp {

	@Override
	public Expression getExpressions(String value) {
		return Expressions.count(value);
	}

}
