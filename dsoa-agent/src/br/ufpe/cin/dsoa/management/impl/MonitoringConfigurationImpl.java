package br.ufpe.cin.dsoa.management.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufpe.cin.dsoa.management.MonitoringConfiguration;
import br.ufpe.cin.dsoa.management.MonitoringService;


class MonitoringConfigurationImpl implements MonitoringConfiguration {
	@Override
	public String toString() {
		String retorno =   getServiceName() + " " + 
						   getOperationName() + " " +
						   getQosCharacteristic()+ " " +
						   getWindowType() + " " +
						   getWindowSize()+ " " +
						   getQosStatistic();
		return retorno;
	}

	private static int idGenerator = 1; 
	
	private int id;
	private String serviceName;
	private String operationName;
	private String qosCategory;
	private String qosCharacteristic; 
	private String qosStatistic;
	private String qosUnit; 
	private String windowType;
	private int windowSize;
	private String windowUnit; 
	private Date startTime;
	private Date stopTime; 
	private List<String> handlers;

	private MonitoringService monitoringService;
	
	
	public MonitoringConfigurationImpl(String serviceName,
			String operationName, String qosCategory, String qosCharacteristic,
			String qosStatistic, String qosUnit, String windowType,
			int windowSize, String windowUnit, Date startTime, Date stopTime,
			String[] handlers) {
		super();
		this.id = idGenerator++;
		this.serviceName = serviceName;
		this.operationName = operationName;
		this.qosCategory = qosCategory;
		this.qosCharacteristic = qosCharacteristic;
		this.qosStatistic = qosStatistic;
		this.qosUnit = qosUnit;
		this.windowType = windowType;
		this.windowSize = windowSize;
		this.windowUnit = windowUnit;
		this.startTime = startTime;
		this.stopTime = stopTime;
		this.handlers = new ArrayList<String>();
		for (String handler : handlers) {
			this.handlers.add(handler);
			System.out.println("Handler: " + handler);
		}
	}

	/* (non-Javadoc)
	 * @see br.ufpe.cin.dsoa.qos.management.MonitoringConfiguration#getId()
	 */
	@Override
	public int getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see br.ufpe.cin.dsoa.qos.management.MonitoringConfiguration#getServiceName()
	 */
	@Override
	public String getServiceName() {
		return serviceName;
	}


	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}


	/* (non-Javadoc)
	 * @see br.ufpe.cin.dsoa.qos.management.MonitoringConfiguration#getOperationName()
	 */
	@Override
	public String getOperationName() {
		return operationName;
	}


	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}


	/* (non-Javadoc)
	 * @see br.ufpe.cin.dsoa.qos.management.MonitoringConfiguration#getQosCategory()
	 */
	@Override
	public String getQosCategory() {
		return qosCategory;
	}


	public void setQosCategory(String qosCategory) {
		this.qosCategory = qosCategory;
	}


	/* (non-Javadoc)
	 * @see br.ufpe.cin.dsoa.qos.management.MonitoringConfiguration#getQosCharacteristic()
	 */
	@Override
	public String getQosCharacteristic() {
		return qosCharacteristic;
	}


	public void setQosCharacteristic(String qosCharacteristic) {
		this.qosCharacteristic = qosCharacteristic;
	}


	/* (non-Javadoc)
	 * @see br.ufpe.cin.dsoa.qos.management.MonitoringConfiguration#getQosStatistic()
	 */
	@Override
	public String getQosStatistic() {
		return qosStatistic;
	}


	public void setQosStatistic(String qosStatistic) {
		this.qosStatistic = qosStatistic;
	}


	/* (non-Javadoc)
	 * @see br.ufpe.cin.dsoa.qos.management.MonitoringConfiguration#getQosUnit()
	 */
	@Override
	public String getQosUnit() {
		return qosUnit;
	}


	public void setQosUnit(String qosUnit) {
		this.qosUnit = qosUnit;
	}


	/* (non-Javadoc)
	 * @see br.ufpe.cin.dsoa.qos.management.MonitoringConfiguration#getWindowType()
	 */
	@Override
	public String getWindowType() {
		return windowType;
	}


	public void setWindowType(String windowType) {
		this.windowType = windowType;
	}


	/* (non-Javadoc)
	 * @see br.ufpe.cin.dsoa.qos.management.MonitoringConfiguration#getWindowSize()
	 */
	@Override
	public int getWindowSize() {
		return windowSize;
	}


	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}


	/* (non-Javadoc)
	 * @see br.ufpe.cin.dsoa.qos.management.MonitoringConfiguration#getWindowUnit()
	 */
	@Override
	public String getWindowUnit() {
		return windowUnit;
	}


	public void setWindowUnit(String windowUnit) {
		this.windowUnit = windowUnit;
	}


	/* (non-Javadoc)
	 * @see br.ufpe.cin.dsoa.qos.management.MonitoringConfiguration#getStartTime()
	 */
	@Override
	public Date getStartTime() {
		return startTime;
	}


	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}


	/* (non-Javadoc)
	 * @see br.ufpe.cin.dsoa.qos.management.MonitoringConfiguration#getStopTime()
	 */
	@Override
	public Date getStopTime() {
		return stopTime;
	}


	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}


	/* (non-Javadoc)
	 * @see br.ufpe.cin.dsoa.qos.management.MonitoringConfiguration#getHandlers()
	 */
	@Override
	public List<String> getHandlers() {
		return handlers;
	}


	public void setHandlers(List<String> handlers) {
		this.handlers = handlers;
	}

	public void setMonitoringService(MonitoringService monitoringService) {
		this.monitoringService = monitoringService;
	}

	/* (non-Javadoc)
	 * @see br.ufpe.cin.dsoa.qos.management.MonitoringConfiguration#getEventTopic()
	 */
	@Override
	public String getEventTopic() {
		return qosCategory + "/" + qosCharacteristic;
	}

	public Object getInInterceptor() {
		return (monitoringService == null) ? null : monitoringService.getInInterceptor();
	}

	public Object getInFaultInterceptor() {
		return (monitoringService == null) ? null : monitoringService.getInFaultInterceptor();
	}

	public Object getOutInterceptor() {
		return (monitoringService == null) ? null : monitoringService.getOutInterceptor();
	}

	public Object getOutFaultInterceptor() {
		return (monitoringService == null) ? null : monitoringService.getOutFaultInterceptor();
	}

	
}
