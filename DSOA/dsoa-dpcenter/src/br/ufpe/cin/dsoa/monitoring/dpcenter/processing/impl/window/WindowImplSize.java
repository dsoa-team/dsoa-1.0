package br.ufpe.cin.dsoa.qos.dpcenter.processing.impl.window;

import br.ufpe.cin.dsoa.qos.dpcenter.processing.api.WindowExp;
import br.ufpe.cin.dsoa.qos.management.MetaData;

import com.espertech.esper.client.soda.Expression;
import com.espertech.esper.client.soda.Expressions;
import com.espertech.esper.client.soda.View;

public class WindowImplSize implements WindowExp{

	public Expression getExpressions(int value) {
		return Expressions.constant(value);
	}

	@Override
	public View getView(int value) {
		return View.create(MetaData.NAMESPACE,MetaData.LENGTH_BATCH,getExpressions(value)); 
		
	}

}