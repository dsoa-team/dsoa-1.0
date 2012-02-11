package br.ufpe.cin.dsoa.monitoring.dpcenter.processing.api;

import com.espertech.esper.client.soda.View;

public interface WindowExp {
	
	public View getView(int value);

}
