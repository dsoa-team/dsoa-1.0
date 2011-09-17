package br.ufpe.cin.dsoa.qos.dgcenter;

import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.InterceptorChain;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.model.OperationInfo;
import org.osgi.framework.BundleContext;

import br.ufpe.cin.dsoa.management.MonitoringConfiguration;

public class DynamicMonitoringChainerInterceptor extends
		DynamicMonitoringAbstractInterceptor {

	private BundleContext ctx;

	private boolean isInInterceptor;
	private boolean isFaultInterceptor;

	public DynamicMonitoringChainerInterceptor(BundleContext ctx,
			Long serviceId, boolean isIn, boolean isFault) {
		super(serviceId, isIn ? Phase.RECEIVE : Phase.SETUP, ctx);
		this.ctx = ctx;
		this.isInInterceptor = isIn;
		this.isFaultInterceptor = isFault;
	}

	@Override
	public void handleMessage(Message message) {
		DataGatheringCenterMXBean dgCenter = (DataGatheringCenterMXBean) getDataGatheringCenterTracker()
				.getService();
		System.out.println("==> Chainer");
		System.out.println(dgCenter);
		if (null != dgCenter) {
			System.out.println(dgCenter.isEnabled());
		}
		if (null != dgCenter && dgCenter.isEnabled()) {
			InterceptorChain chain = message.getInterceptorChain();
			if (null != getMonitoringConfigurationTracker().getServices()) {
				for (Object monitoringConfigurationObj : getMonitoringConfigurationTracker()
						.getServices()) {
					MonitoringConfiguration monitoringConfiguration = (MonitoringConfiguration) monitoringConfigurationObj;
					System.out.println(monitoringConfiguration);
					Exchange ex = message.getExchange();
					OperationInfo opInfo = ex.get(OperationInfo.class);
					String operationName = opInfo == null ? null : opInfo
							.getName().getLocalPart();
					if (operationName == null) {
						Object nameProperty = ex
								.get("org.apache.cxf.resource.operation.name");
						if (nameProperty != null) {
							operationName = "\"" + nameProperty.toString()
									+ "\"";
						}
					}
					System.out.println("Operation name: " + operationName);
					if (null != monitoringConfiguration
							/*&& monitoringConfiguration.getOperationName()
									.equals(operationName)*/) {
						Interceptor<Message> interceptor = null;
						if (isInInterceptor) {
							if (isFaultInterceptor) {
								System.out.println("InFault: ");
								System.out.println(monitoringConfiguration.getInFaultInterceptor());
								interceptor = (Interceptor<Message>) monitoringConfiguration
										.getInFaultInterceptor();
							} else {
								System.out.println("In: ");
								System.out.println(monitoringConfiguration.getInInterceptor());
								interceptor = (Interceptor<Message>) monitoringConfiguration
										.getInInterceptor();
							}
						} else {
							if (isFaultInterceptor) {
								System.out.println("OutFault: ");
								System.out.println(monitoringConfiguration.getOutFaultInterceptor());
								interceptor = (Interceptor<Message>) monitoringConfiguration
										.getOutFaultInterceptor();
							} else {
								System.out.println("Out: ");
								System.out.println(monitoringConfiguration.getOutInterceptor());
								interceptor = (Interceptor<Message>) monitoringConfiguration
										.getOutInterceptor();
							}
						}

						if (interceptor != null) {
							chain.add(interceptor);
						}
					}
				}
			}
		}
		System.out.println("==> Chainer <==");
	}

}
