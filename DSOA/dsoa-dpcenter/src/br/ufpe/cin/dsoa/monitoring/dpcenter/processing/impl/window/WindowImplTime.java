package br.ufpe.cin.dsoa.monitoring.dpcenter.processing.impl.window;

import br.ufpe.cin.dsoa.management.MetaData;
import br.ufpe.cin.dsoa.monitoring.dpcenter.processing.api.WindowExp;

import com.espertech.esper.client.soda.Expression;
import com.espertech.esper.client.soda.Expressions;
import com.espertech.esper.client.soda.View;

public class WindowImplTime implements WindowExp{

	public Expression getExpressions(int value) {
		return Expressions.timePeriod(null, null, null, null, value);
	}

	@Override
	public View getView(int value) {
		return View.create(MetaData.NAMESPACE,MetaData.TIME_BATCH,getExpressions(value));
	}

}
