package br.ufpe.cin.dsoa.qos.management.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import br.ufpe.cin.dsoa.qos.management.ManagementAgentMXBean;
import br.ufpe.cin.dsoa.qos.management.MetaData;
import br.ufpe.cin.dsoa.qos.management.MonitoringConfiguration;
import br.ufpe.cin.dsoa.qos.management.MonitoringHandler;
import br.ufpe.cin.dsoa.qos.management.MonitoringService;

class ManagementAgent implements ManagementAgentMXBean {

	private BundleContext ctx;
	private ServiceTracker monitoringTracker;

	private Map<QoSAttribute, List<MonitoringService>> qosToMonitorMap;

	private Map<MonitoringService, List<Map<MonitoringConfiguration, ServiceRegistration>>> resolvedConfigurationsMap;
	private Map<QoSAttribute, List<MonitoringConfiguration>> pendingConfigurationsMap;

	public ManagementAgent(BundleContext ctx) {
		this.ctx = ctx;
	}

	public void start() {
		pendingConfigurationsMap = new HashMap<QoSAttribute, List<MonitoringConfiguration>>();
		resolvedConfigurationsMap = new HashMap<MonitoringService, List<Map<MonitoringConfiguration, ServiceRegistration>>>();
		qosToMonitorMap = new HashMap<QoSAttribute, List<MonitoringService>>();
		monitoringTracker = new MonitoringServiceTracker(ctx);
		monitoringTracker.open();
	}

	public void stop() {
		monitoringTracker.close();
		monitoringTracker = null;
	}

	public String[] getServices() {
		List<String> serviceNames = new ArrayList<String>();
		Bundle[] bundles = ctx.getBundles();
		for (Bundle bundle : bundles) {
			ServiceReference[] serviceRefs = bundle.getRegisteredServices();
			if (serviceRefs != null) {
				for (ServiceReference serviceRef : serviceRefs) {
					if (null != serviceRef.getProperty("service.exported.interfaces")) {
						String serviceName = serviceRef.getProperty(
								MetaData.SERVICE_NAME).toString();
						if (null != serviceName) {
							serviceNames.add(serviceName);
						}
					}
				}
			}
		}
		return transforms(serviceNames);
	}

	public String[] getOperations(String serviceName) {
		Set<String> operationNames = new HashSet<String>();
		Bundle[] bundles = ctx.getBundles();
		for (Bundle bundle : bundles) {
			ServiceReference[] serviceRefs = bundle.getRegisteredServices();
			if (serviceRefs != null) {
				for (ServiceReference serviceRef : serviceRefs) {
					if (serviceRef.getProperty(MetaData.SERVICE_NAME).toString().equals(serviceName)) {
						Object service = ctx.getService(serviceRef);
						Class clazz = service.getClass();
						for (Method method : clazz.getMethods()) {
							String methodName = method.getName();
							if (null != methodName) {
								operationNames.add(methodName);
							}
						}
					}
				}
			}
		}
		return transforms(operationNames);
	}

	public String[] getHandlers() {
		ServiceReference[] references = null;
		List<String> handlerNames = new ArrayList<String>();
		try {
			references = ctx.getServiceReferences(MonitoringHandler.class
					.getName(), null);
			String handlerName;
			if (references != null) {
				for (ServiceReference reference : references) {
					handlerName = (String) reference
							.getProperty(MetaData.HANDLER_NAME);
					if (null != handlerName && !handlerName.trim().equals("")) {
						handlerNames.add(handlerName);
					}
				}
			}
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return transforms(handlerNames);
	}

	public String[] transforms(Collection<String> lista) {
		String[] ret = new String[lista.size()];
		Iterator itr = lista.iterator();
		int i = 0;
		while (itr.hasNext()) {
			ret[i++] = (String)itr.next();
		}
		return ret;
	}

	public int createMonitoringConfiguration(String serviceName,
			String operationName, String qosCategory, String qosCharacteristic,
			String qosStatistic, String qosUnit, String windowType,
			int windowSize, String windowUnit, Date startTime, Date stopTime,
			String[] handlers) {
		
		QoSAttribute qosAtt = new QoSAttribute(qosCategory, qosCharacteristic);
		MonitoringConfigurationImpl config = new MonitoringConfigurationImpl(
				serviceName, operationName, qosCategory, qosCharacteristic,
				qosStatistic, qosUnit, windowType, windowSize, windowUnit,
				startTime, stopTime, getHandlers());
		System.out.println("==> Agent: ");
		if (qosToMonitorMap.containsKey(qosAtt)) {
			List<MonitoringService> monitorList = qosToMonitorMap.get(qosAtt);
			if (null != monitorList && !monitorList.isEmpty()) {
				MonitoringService monitor = monitorList.get(0);
				config.setMonitoringService(monitor);
				Dictionary properties = new Hashtable();
				properties.put(MetaData.ID, config.getId());
				properties.put(MetaData.SERVICE_REF_NAME, config
						.getServiceName());
				properties.put(MetaData.OPERATION_REF_NAME, config
						.getOperationName());
				ServiceRegistration registration = ctx.registerService(
						MonitoringConfiguration.class.getName(), config,
						properties);
				Map<MonitoringConfiguration, ServiceRegistration> map = new HashMap<MonitoringConfiguration, ServiceRegistration>();
				map.put(config, registration);
				List<Map<MonitoringConfiguration, ServiceRegistration>> resolvedList = resolvedConfigurationsMap
						.get(monitor);
				if (null == resolvedList) {
					resolvedList = new ArrayList<Map<MonitoringConfiguration, ServiceRegistration>>();
					resolvedConfigurationsMap.put(monitor, resolvedList);
				}
				resolvedList.add(map);
			}
		} else {
			List<MonitoringConfiguration> configList = pendingConfigurationsMap
					.get(qosAtt);
			if (null == configList) {
				configList = new ArrayList<MonitoringConfiguration>();
				pendingConfigurationsMap.put(qosAtt, configList);
			}
			configList.add(config);
		}
		System.out.println(config.toString());
		return config.getId();
	}

	class MonitoringServiceTracker extends ServiceTracker {

		MonitoringServiceTracker(BundleContext ctx) {
			super(ctx, MonitoringService.class.getName(), null);
		}

		public Object addingService(ServiceReference reference) {
			String category = (String) reference
					.getProperty(MetaData.QOS_CATEGORY);
			String characteristic = (String) reference
					.getProperty(MetaData.QOS_CHARACTERISTIC);
			QoSAttribute qosAtt = new QoSAttribute(category, characteristic);
			MonitoringService monitoringService = (MonitoringService) ctx.getService(reference);
			// Mapear qos para MonitoringService
			if (qosToMonitorMap.containsKey(qosAtt)) {
				qosToMonitorMap.get(qosAtt).add(monitoringService);
			} else {
				List<MonitoringService> monitList = new ArrayList<MonitoringService>();
				monitList.add(monitoringService);
				qosToMonitorMap.put(qosAtt, monitList);

				// Verificar servicos pendentes
				List<Map<MonitoringConfiguration, ServiceRegistration>> registrations = null;
				if (null != pendingConfigurationsMap
						.get(qosAtt)) {
					for (MonitoringConfiguration config : pendingConfigurationsMap
							.get(qosAtt)) {
						((MonitoringConfigurationImpl) config)
								.setMonitoringService(monitoringService);
						Dictionary properties = new Hashtable();
						properties.put(MetaData.ID, config.getId());
						properties.put(MetaData.SERVICE_REF_NAME, config
								.getServiceName());
						properties.put(MetaData.OPERATION_REF_NAME, config
								.getOperationName());
						ServiceRegistration registration = ctx.registerService(
								MonitoringConfiguration.class.getName(), config,
								properties);
						if (null == registrations) {
							registrations = new ArrayList<Map<MonitoringConfiguration, ServiceRegistration>>();
						}
						Map<MonitoringConfiguration, ServiceRegistration> configToRegistrationMap = new HashMap<MonitoringConfiguration, ServiceRegistration>();
						configToRegistrationMap.put(config, registration);
						registrations.add(configToRegistrationMap);
					}
				}
				// Remover da relacao de configuracoes pendentes
				pendingConfigurationsMap.remove(qosAtt);

				// Adicionar na relacao de configuracoes resolvidas
				if (null != registrations) {
					resolvedConfigurationsMap.put(monitoringService,
							registrations);
				}
			}

			return reference;
		}

		public void removedService(ServiceReference reference, Object obj) {
			String category = (String) reference
					.getProperty(MetaData.QOS_CATEGORY);
			String characteristic = (String) reference
					.getProperty(MetaData.QOS_CHARACTERISTIC);
			QoSAttribute qosAtt = new QoSAttribute(category, characteristic);
			
			MonitoringService monitor = (MonitoringService) ctx.getService(reference);
			// Remover o monitor da lista de monitores disponiveis para o
			// atributo de qualidade
			List<MonitoringService> monitorList = qosToMonitorMap.get(qosAtt);
			if (null != monitorList) {
				monitorList.remove(monitor);
				if (monitorList.isEmpty()) {
					qosToMonitorMap.remove(qosAtt);
				}
			}

			if (resolvedConfigurationsMap.containsKey(monitor)) {
				List<Map<MonitoringConfiguration, ServiceRegistration>> configList = resolvedConfigurationsMap
						.get(monitor);
				for (Map<MonitoringConfiguration, ServiceRegistration> map : configList) {
					for (Map.Entry<MonitoringConfiguration, ServiceRegistration> entry : map
							.entrySet()) {
						MonitoringConfiguration config = entry.getKey();
						if (!monitorList.isEmpty()) {
							((MonitoringConfigurationImpl) config)
									.setMonitoringService(monitorList.get(0));
						} else {
							ServiceRegistration reg = entry.getValue();
							reg.unregister();
							((MonitoringConfigurationImpl) config)
									.setMonitoringService(null);
							List<MonitoringConfiguration> pendingList = pendingConfigurationsMap
									.get(qosAtt);
							if (null == pendingList) {
								pendingList = new ArrayList<MonitoringConfiguration>();
								pendingConfigurationsMap.put(qosAtt,
										pendingList);
							}
							pendingList.add(config);
						}
					}
				}
			}
			super.removedService(reference, obj);
		}
	}

}
