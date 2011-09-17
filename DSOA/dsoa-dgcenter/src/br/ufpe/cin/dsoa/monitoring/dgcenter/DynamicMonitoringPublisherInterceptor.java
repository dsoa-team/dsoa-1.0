package br.ufpe.cin.dsoa.qos.dgcenter;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.Service;
import org.apache.cxf.service.model.OperationInfo;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import br.ufpe.cin.dsoa.management.MetaData;
import br.ufpe.cin.dsoa.management.MonitoringConfiguration;

public class DynamicMonitoringPublisherInterceptor extends
		DynamicMonitoringAbstractInterceptor {

	private BundleContext ctx;
	private Long serviceId;

	public DynamicMonitoringPublisherInterceptor(BundleContext ctx,
			Long serviceId, boolean isIn, boolean isFault) {
		super(serviceId, isIn ? Phase.PRE_INVOKE : Phase.SEND, ctx);
		this.ctx = ctx;
		this.serviceId = serviceId;
	}

	@Override
	public void handleMessage(Message message) {
		DataGatheringCenterMXBean dgCenter = (DataGatheringCenterMXBean) getDataGatheringCenterTracker()
				.getService();
		if (null != dgCenter && dgCenter.isEnabled()) {
			Exchange ex = message.getExchange();
			Map<String, Object> defaultMonitoringData = null;
			System.out.println("==> Publisher: " + getMonitoringConfigurationTracker()
					.getServiceReferences());
			if (null != getMonitoringConfigurationTracker()
					.getServiceReferences()) {
				for (ServiceReference serviceReference : getMonitoringConfigurationTracker()
						.getServiceReferences()) {
					MonitoringConfiguration monitoringConfiguration = (MonitoringConfiguration) ctx
							.getService(serviceReference);
					System.out.println(monitoringConfiguration);
					String eventTopic = monitoringConfiguration.getEventTopic();
					System.out.println("Topic: " + eventTopic);
					String qosAttribute = monitoringConfiguration
							.getQosCharacteristic();
					System.out.println(qosAttribute);
					Map<String, Object> monitoringData = (Map<String, Object>) message
							.get(eventTopic);
					System.out.println(monitoringData);
					if (null != monitoringData) {
						if (null == defaultMonitoringData) {
							defaultMonitoringData = collectDefaultData(ex);
						}
						monitoringData.putAll(defaultMonitoringData);
						 monitoringData.put(MetaData.QOS_CHARACTERISTIC,qosAttribute);
						this.notifyMonitoringHandlers(eventTopic,
								monitoringData);
					}
				}
			}
		}
	}

	private Map<String, Object> collectDefaultData(Exchange ex) {
		Map<String, Object> defaultMonitoringData = null;
		if (null != ex) {
			defaultMonitoringData = new HashMap<String, Object>();
			/*
			 * Message in = ex.getInMessage(); HttpServletRequest request =
			 * (HttpServletRequest) in .get("HTTP.REQUEST"); String ipClient,
			 * ipServer, uri = null; Integer portClient, portServer = null; if
			 * (null != request) { uri = request.getRequestURI(); ipServer =
			 * request.getLocalAddr(); portServer = request.getLocalPort();
			 * ipClient = request.getRemoteAddr(); portClient =
			 * request.getRemotePort();
			 * defaultMonitoringData.put(MonitoringEventHeader.URI, uri);
			 * defaultMonitoringData.put(MonitoringEventHeader.CLIENT_IP,
			 * ipClient);
			 * defaultMonitoringData.put(MonitoringEventHeader.CLIENT_PORT,
			 * portClient);
			 * defaultMonitoringData.put(MonitoringEventHeader.SERVER_IP,
			 * ipServer);
			 * defaultMonitoringData.put(MonitoringEventHeader.SERVER_PORT,
			 * portServer); }
			 */

			Service service = ex.get(Service.class);
			String serviceName = String.valueOf(service.getName());
			OperationInfo opInfo = ex.get(OperationInfo.class);
			String operationName = opInfo == null ? null : opInfo.getName()
					.getLocalPart();
			if (operationName == null) {
				Object nameProperty = ex
						.get("org.apache.cxf.resource.operation.name");
				if (nameProperty != null) {
					operationName = "\"" + nameProperty.toString() + "\"";
				}
			}
			defaultMonitoringData.put(MetaData.SERVICE_REF_NAME, serviceId.toString());
			defaultMonitoringData.put(MetaData.OPERATION_REF_NAME,
					operationName);
			/*
			 * defaultMonitoringData.put(MonitoringEventHeader.TIMESTAMP, new
			 * Date()); defaultMonitoringData.put(MonitoringEventHeader.ADDRESS,
			 * address);
			 * defaultMonitoringData.put(MonitoringEventHeader.INTERFACE, sei);
			 */
		}
		return defaultMonitoringData;
	}

	private void log(String direction, Message message) {
		System.out.println("Direction: " + direction);
		if (null != message) {
			System.out.println("\t" + message.get(Message.TRANSPORT));
			System.out.println("\t" + message.get(Message.INBOUND_MESSAGE));
			System.out.println("\t" + message.get(Message.INVOCATION_CONTEXT));
			System.out.println("\t" + message.get(Message.HTTP_REQUEST_METHOD));
			System.out.println("\t" + message.get(Message.REQUEST_URI));
			System.out.println("\t" + message.get(Message.RESPONSE_CODE));
			System.out.println("\t" + message.get(Message.ENDPOINT_ADDRESS));
			System.out.println("\t" + message.get(Message.PATH_INFO));
			System.out.println("\t" + message.get(Message.QUERY_STRING));
			System.out.println("\t" + message.get(Message.WSDL_DESCRIPTION));
			System.out.println("\t" + message.get(Message.WSDL_SERVICE));
			System.out.println("\t" + message.get(Message.WSDL_PORT));
			System.out.println("\t" + message.get(Message.WSDL_INTERFACE));
			System.out.println("\t" + message.get(Message.WSDL_OPERATION));
		}
	}

	private void notifyMonitoringHandlers(String monitoringTopic,
			Map<String, Object> monitoringData) {
		ServiceReference ref = ctx.getServiceReference(EventAdmin.class
				.getName());
		if (ref != null) {
			EventAdmin eventAdmin = (EventAdmin) ctx.getService(ref);
			Event monitoringEvent = new Event(monitoringTopic, monitoringData);
			eventAdmin.postEvent(monitoringEvent);
		}
	}
}
