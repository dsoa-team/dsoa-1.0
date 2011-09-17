package br.ufpe.cin.dsoa.qos.dpcenter.processing.impl.dimension;

import br.ufpe.cin.dsoa.qos.dpcenter.processing.api.DimensionExp;

import com.espertech.esper.client.soda.Expression;
import com.espertech.esper.client.soda.Expressions;

public class DimensionExpImplMin implements DimensionExp{

	public Expression getExpressions(String value) {
		return Expressions.min(value);
	}

}
