package br.ufpe.cin.dsoa.monitoring.dpcenter.processing.impl;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;

import br.ufpe.cin.dsoa.management.MetaData;
import br.ufpe.cin.dsoa.management.MonitoringConfiguration;
import br.ufpe.cin.dsoa.monitoring.dpcenter.processing.api.DimensionExp;
import br.ufpe.cin.dsoa.monitoring.dpcenter.processing.api.Processor;
import br.ufpe.cin.dsoa.monitoring.dpcenter.processing.api.WindowExp;
import br.ufpe.cin.dsoa.monitoring.dpcenter.processing.impl.dimension.DimensionExpImplCount;
import br.ufpe.cin.dsoa.monitoring.dpcenter.processing.impl.dimension.DimensionExpImplMax;
import br.ufpe.cin.dsoa.monitoring.dpcenter.processing.impl.dimension.DimensionExpImplMean;
import br.ufpe.cin.dsoa.monitoring.dpcenter.processing.impl.dimension.DimensionExpImplMin;
import br.ufpe.cin.dsoa.monitoring.dpcenter.processing.impl.window.WindowImplSize;
import br.ufpe.cin.dsoa.monitoring.dpcenter.processing.impl.window.WindowImplTime;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.soda.EPStatementObjectModel;
import com.espertech.esper.client.soda.Expression;
import com.espertech.esper.client.soda.Expressions;
import com.espertech.esper.client.soda.FilterStream;
import com.espertech.esper.client.soda.FromClause;
import com.espertech.esper.client.soda.SelectClause;
import com.espertech.esper.client.soda.View;

public class ProcessorImpl implements Processor {
	
	private EPServiceProvider epServiceProvider;
	private Map<String, DimensionExp> mapDimensionExp = new HashMap<String,DimensionExp>();
	private Map<String, WindowExp>    mapWindowExp    = new HashMap<String, WindowExp>();
	private BundleContext         bundleContext;
	
	public Map<String, WindowExp> getMapWindowExp() {
		return mapWindowExp;
	}

	public ProcessorImpl(EPServiceProvider epServiceProvider,BundleContext bundleContext) {
		this.epServiceProvider = epServiceProvider;
		this.bundleContext = bundleContext;
		
		loader();
	}

	
	public EPServiceProvider getEpServiceProvider() {
		return epServiceProvider;
	}

	@Override
	public void addMonitoringConfiguration(MonitoringConfiguration monitoringConfiguration) {
		
		EPStatementObjectModel epStamodel = new EPStatementObjectModel();

		//define select clause
		DimensionExp dimExp = mapDimensionExp.get(monitoringConfiguration.getQosStatistic());
		epStamodel.setSelectClause(SelectClause.create().add(dimExp.getExpressions(MetaData.QOS_VALUE)));
		
		//define filter
		Expression expressionFilter = Expressions.eq(MetaData.SERVICE_REF_NAME, monitoringConfiguration.getServiceName());
		if (monitoringConfiguration.getOperationName() != null) {
			expressionFilter = Expressions.and().add(Expressions.eq(MetaData.SERVICE_REF_NAME, monitoringConfiguration.getServiceName())).
			// deveria ser: add(expressionFilter);
            add(Expressions.eq(MetaData.OPERATION_REF_NAME, monitoringConfiguration.getOperationName()));
		}
		
		//define view
		WindowExp windowExp = getMapWindowExp().get(monitoringConfiguration.getWindowType());
		View view = windowExp.getView(monitoringConfiguration.getWindowSize());
		
		//define from
		epStamodel.setFromClause(FromClause.create(FilterStream.create(
		monitoringConfiguration.getQosCharacteristic(),expressionFilter).addView(view)));
		
	//	epStamodel.setFromClause(FromClause.create(FilterStream.create(
	//	monitoringConfiguration.getQosCharacteristic())));
		
		//create statement
		EPStatement stmt = getEpServiceProvider().getEPAdministrator().create(epStamodel);
		
		//define handlers
		Notifier notifier = new Notifier(getBundleContext(),monitoringConfiguration.getHandlers());
		stmt.addListener(notifier);
			
	}

	public Map<String, DimensionExp> getMapDimensionExp() {
		return mapDimensionExp;
	}
	
	public void loader() {
		getMapDimensionExp().put(MetaData.MIN,   new DimensionExpImplMin());
		getMapDimensionExp().put(MetaData.AVG,   new DimensionExpImplMean());
		getMapDimensionExp().put(MetaData.MAX,   new DimensionExpImplMax());
		getMapDimensionExp().put(MetaData.COUNT, new DimensionExpImplCount());
		getMapWindowExp().put(MetaData.TIME,     new WindowImplTime());
		getMapWindowExp().put(MetaData.SIZE,     new WindowImplSize());
		
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public BundleContext getBundleContext() {
		return bundleContext;
	}

}
