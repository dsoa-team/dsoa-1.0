package br.ufpe.cin.dsoa.qos.dpcenter;

import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import br.ufpe.cin.dsoa.qos.dpcenter.instrumentation.api.Instrumentator;
import br.ufpe.cin.dsoa.qos.dpcenter.instrumentation.impl.InstrumentatorImpl;
import br.ufpe.cin.dsoa.qos.dpcenter.processing.api.Processor;
import br.ufpe.cin.dsoa.qos.dpcenter.processing.impl.ProcessorImpl;
import br.ufpe.cin.dsoa.qos.management.MetaData;
import br.ufpe.cin.dsoa.qos.management.MonitoringConfiguration;
import br.ufpe.cin.dsoa.qos.management.MonitoringService;

import com.espertech.esper.client.EPServiceProvider;

public class DataProcessingCenterImpl implements DataProcessingCenter{

	private Instrumentator instrumentator;
	private Processor      processor;
	private ServiceTracker monitoringServiceTracker;
	private ServiceTracker monitoringConfigurationTracker;
	private BundleContext  bundleContext;
	
	public DataProcessingCenterImpl(EPServiceProvider epServiceProvider, BundleContext bundleContext) {
		this.instrumentator = new InstrumentatorImpl(epServiceProvider,bundleContext);
		this.processor      = new ProcessorImpl(epServiceProvider,bundleContext);
		this.bundleContext  = bundleContext;
		this.monitoringServiceTracker = new ServiceTracker(bundleContext, MonitoringService.class.getName(), new MonitoringServiceTrackerCustomizer());
		this.monitoringServiceTracker.open();
		this.monitoringConfigurationTracker = new ServiceTracker(bundleContext, MonitoringConfiguration.class.getName(), new MonitoringConfigurationTrackerCustomizer());
		this.monitoringConfigurationTracker.open();
	}

	public Instrumentator getInstrumentator() {
		return instrumentator;
	}

	public Processor getProcessor() {
		return processor;
	}

	@Override
	public void addMonitoringCell(MonitoringConfiguration monitoringConfiguration) {
		getProcessor().addMonitoringConfiguration(monitoringConfiguration);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void createInstrumentationCell(String eventTypeName, Map typeMap,String topic) {
		getInstrumentator().createInstrumentationCell(eventTypeName, typeMap, topic);
	}

	@Override
	public boolean destroyInstrumentationCell(String eventTypeName) {
		return getInstrumentator().destroyInstrumentationCell(eventTypeName);
	}
	
	public ServiceTracker getMonitoringServiceTracker() {
		return monitoringServiceTracker;
	}

	public ServiceTracker getMonitoringConfigurationTracker() {
		return monitoringConfigurationTracker;
	}



	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public BundleContext getBundleContext() {
		return bundleContext;
	}



	class MonitoringServiceTrackerCustomizer implements ServiceTrackerCustomizer {
		public Object addingService(ServiceReference reference) {

			String characteristic  = (String)reference.getProperty(MetaData.QOS_CHARACTERISTIC);
			String topic           = (String)reference.getProperty(MetaData.QOS_TOPIC);
			@SuppressWarnings("rawtypes")
			Map properties         = (Map)reference.getProperty(MetaData.QOS_PROPERTIES);

			getInstrumentator().createInstrumentationCell(characteristic, properties, topic);
			return reference;   
		}   


		public void removedService(ServiceReference reference, Object service) {   

		}


		@Override
		public void modifiedService(ServiceReference reference, Object service) {
			// TODO Auto-generated method stub
			
		}
	}
	
	class MonitoringConfigurationTrackerCustomizer implements ServiceTrackerCustomizer {

		@Override
		public Object addingService(ServiceReference reference) {
			MonitoringConfiguration monCon = (MonitoringConfiguration)getBundleContext().getService(reference);
			getProcessor().addMonitoringConfiguration(monCon);
			return reference;
		}

		@Override
		public void modifiedService(ServiceReference reference, Object service) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removedService(ServiceReference reference, Object service) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
