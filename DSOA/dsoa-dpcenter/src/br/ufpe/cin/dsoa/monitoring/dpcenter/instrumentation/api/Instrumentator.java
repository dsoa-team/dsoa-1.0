package br.ufpe.cin.dsoa.qos.dpcenter.instrumentation.api;

import java.util.Map;

public interface Instrumentator {
	
	@SuppressWarnings("unchecked")
	public void    createInstrumentationCell(String eventTypeName, Map typeMap,String topic);
	public boolean destroyInstrumentationCell(String eventTypeName);

}
