package br.ufpe.cin.dsoa.manager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import br.ufpe.cin.dsoa.broker.Broker;
import br.ufpe.cin.dsoa.broker.impl.BrokerImpl;
import br.ufpe.cin.dsoa.qos.QosDependencyListener;
import br.ufpe.cin.dsoa.qos.Slo;
import br.ufpe.cin.dsoa.qos.impl.DsoaDependency;
import br.ufpe.cin.dsoa.qos.monitoring.service.MonitoringConfiguration;
import br.ufpe.cin.dsoa.qos.monitoring.service.MonitoringConfigurationItem;
import br.ufpe.cin.dsoa.qos.monitoring.service.MonitoringListener;
import br.ufpe.cin.dsoa.qos.monitoring.service.MonitoringService;
import br.ufpe.cin.dsoa.qos.monitoring.service.events.ArrivalEvent;
import br.ufpe.cin.dsoa.qos.monitoring.service.events.DepartureEvent;
import br.ufpe.cin.dsoa.qos.monitoring.service.events.ErrorEvent;
import br.ufpe.cin.dsoa.qos.monitoring.service.events.Event;
import br.ufpe.cin.dsoa.qos.monitoring.service.events.RequestEvent;
import br.ufpe.cin.dsoa.qos.monitoring.service.events.ResponseEvent;
import br.ufpe.cin.log.generator.service.LogGenerator;

public class DsoaDependencyManager implements InvocationHandler,
		QosDependencyListener, MonitoringListener, ServiceTrackerCustomizer {
	private final Random random;
	private final BundleContext ctx;
	private final DsoaDependency dependency;
	private Object service;
	private ServiceReference serviceReference;
	private final Broker broker;
	private final ServiceTracker monitorTracker;
	private ServiceTracker serviceTracker;
	private MonitoringConfiguration configuration;
	private MonitoringService monitoringService;
	private final List<ServiceReference> blackList;
	private LogGenerator logGenerator;

	public DsoaDependencyManager(DsoaDependency dependency) {
		this.dependency = dependency;
		this.ctx = dependency.getContext();
		this.broker = new BrokerImpl(ctx);
		this.monitorTracker = new ServiceTracker(ctx,
				MonitoringService.class.getName(), this);
		this.monitorTracker.open();
		this.random = new Random();
		this.blackList = new ArrayList<ServiceReference>();

		broker.getBestService(dependency.getSpecification().getName(),
				dependency.getSlos(), this, blackList);
		
		ServiceReference reference = ctx.getServiceReference(LogGenerator.class.getName());
		if (reference != null) {
			this.logGenerator = (LogGenerator) ctx.getService(reference);
		}
		
		
	}

	public synchronized void setSelected(final ServiceReference reference) {
		if (null != monitoringService) {
			this.serviceReference = reference;
			this.service = ctx.getService(reference);
			System.out.println("Receiving a NEW reference..." + reference.getProperty("provider.pid"));

			this.configuration = new MonitoringConfiguration(dependency.getConsumerPID(),
			reference.getProperty("provider.pid").toString(), this);
			
			for (Slo slo : this.dependency.getSlos()) {
				MonitoringConfigurationItem item = new MonitoringConfigurationItem(slo.getOperation(), 
																				   slo.getAttribute(), 
																				   slo.getExpression().getOperator(),
																				   slo.getValue(),
																				   slo.getStatistic(), configuration);
				this.configuration.addConfigurationItem(item);
			}
			monitoringService.startMonitoring(configuration);

			// sendArrivalEvent(reference);
			Filter filter = null;
			try {
				filter = ctx.createFilter("(" + "provider.pid" + "="
						+ reference.getProperty("provider.pid").toString()
						+ ")");
			} catch (InvalidSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				IllegalArgumentException iae = new IllegalArgumentException(
						"unexpected InvalidSyntaxException: " + e.getMessage());
				iae.initCause(e);
				throw iae;
			}

			serviceTracker = new ServiceTracker(ctx, filter, null) {
				@Override
				public Object addingService(ServiceReference reference) {
					service = ctx.getService(reference);
					dependency.setValid(true);
					sendArrivalEvent(reference);
					return service;
				}

				@Override
				public void removedService(ServiceReference reference,
						Object object) {
					dependency.setValid(false);
					service = null;
					sendDepartureEvent(reference);
					ctx.ungetService(reference);
				}
			};
			serviceTracker.open();
			this.dependency.setValid(true);
		} else {
			this.serviceReference = reference;
		}

	}

	public synchronized Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;
		Long correlationId = random.nextLong();
		sendRequestEvent(correlationId, method, args);
		try {
			if (null != service) {
				result = method.invoke(service, args);
			} else {
				throw new IllegalStateException(
						"Required service is not available!");
			}
		} catch (Exception exception) {
			sendErrorEvent(correlationId, method, exception);
			throw exception;
		} 
		sendResponseEvent(correlationId, method, result);
		return result;
	}

	public synchronized void listen(Map result, Object userObject,
			String statementName) {
		this.dependency.setValid(false);
		System.out.println("A monitoring event occurred: " + statementName);
		System.out.println("\tClause: "
				+ ((MonitoringConfigurationItem) userObject).getStatement());
		for (Object key : result.keySet()) {
			System.out.println("\tKey: " + key);
			System.out.println("\tValue: " + result.get(key));
			System.out.println("");
			System.out.println("");
		}
		
		//linhas adicionadas para considerar efetivas trocas
		this.blackList.removeAll(blackList);
		
		
		this.blackList.add(this.serviceReference);
		this.service = null;
		this.ctx.ungetService(this.serviceReference);
		this.serviceReference = null;
		broker.getBestService(dependency.getSpecification().getName(),dependency.getSlos(), this, this.blackList);
	}

	private void sendEvent(Event event) {
		MonitoringService monitor = (MonitoringService) monitorTracker
				.getService();
		if (null != monitor) {
			monitor.publishMonitoringEvent(event);
		}
	}

	private void sendRequestEvent(Long correlationId, Method method,
			Object[] args) {
		sendEvent(new RequestEvent(System.nanoTime(),
				configuration.getClientId(), configuration.getServiceId(),
				correlationId.toString(), method.getName(), null, null, args));
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
		StringBuilder builder = new StringBuilder(1000);
		builder.append("Request").append(" - ");
		builder.append(df.format(new Date(System.currentTimeMillis()))).append(" - ");
		builder.append(correlationId.toString()).append(" - ");
		builder.append(configuration.getClientId()).append(" - ");
		builder.append(configuration.getServiceId()).append(" - ");
		builder.append(method.getName());
		logGenerator.log(Level.INFO, builder.toString());
		
	}

	private void sendResponseEvent(Long correlationId, Method method,
			Object result) {
		sendEvent(new ResponseEvent(System.nanoTime(),
				configuration.getClientId(), configuration.getServiceId(),
				correlationId.toString(), method.getName(), null, result));
	}

	private void sendErrorEvent(Long correlationId, Method method,
			Exception exception) {
		Throwable rootCause = exception;
		Throwable cause = rootCause.getCause();
		while (cause != null) {
			rootCause = cause;
			cause = rootCause.getCause();
		}
		sendEvent(new ErrorEvent(System.nanoTime(),
				configuration.getClientId(), configuration.getServiceId(),
				correlationId.toString(), method.getName(), rootCause
						.getClass().getName(), rootCause.getMessage()));
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
		StringBuilder builder = new StringBuilder(1000);
		builder.append("Error").append(" - ");
		builder.append(df.format(new Date(System.currentTimeMillis()))).append(" - ");
		builder.append(correlationId.toString()).append(" - ");
		builder.append(configuration.getClientId()).append(" - ");
		builder.append(configuration.getServiceId()).append(" - ");
		builder.append(method.getName());
		logGenerator.log(Level.INFO, builder.toString());
		
	}

	private void sendArrivalEvent(ServiceReference reference) {
		sendEvent(new ArrivalEvent(System.nanoTime(), reference
				.getProperty("provider.pid").toString(),
				(reference.getProperty("provider.pid") != null ? reference
						.getProperty("provider.pid") : reference
						.getProperty(Constants.OBJECTCLASS)).toString(),
				reference.getProperty(Constants.OBJECTCLASS).toString()));
	}

	private void sendDepartureEvent(ServiceReference reference) {
		sendEvent(new DepartureEvent(System.nanoTime(), reference
				.getProperty("provider.pid").toString(),
				(reference.getProperty("provider.pid") != null ? reference
						.getProperty("provider.pid") : reference
						.getProperty(Constants.OBJECTCLASS)).toString(),
				reference.getProperty(Constants.OBJECTCLASS).toString()));
	}

	public Object addingService(ServiceReference reference) {
		// TODO Auto-generated method stub
		this.monitoringService = (MonitoringService) ctx.getService(reference);
		return monitoringService;
	}

	public void modifiedService(ServiceReference reference, Object service) {

	}

	public void removedService(ServiceReference reference, Object service) {
		this.monitoringService = null;
		ctx.ungetService(reference);
	}

}
