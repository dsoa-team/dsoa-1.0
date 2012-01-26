package br.ufpe.cin.dsoa.jmx.monitor;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import javax.management.ReflectionException;

public class PlatformMonitor extends NotificationBroadcasterSupport implements
		PlatformMonitorMBean, Runnable {

	public static final String RESOURCE_STATUS_TYPE = "resource.status";

	private static String OBJECT_NAME = "jmx.monitor:Type=PlatformMonitor";
	private static final String SYSTEM_LOAD = "SystemLoadAverage";
	private static final String FREE_PHYSICAL_MEM = "FreePhysicalMemorySize";
	private static final String TOTAL_PHYSICAL_MEM = "TotalPhysicalMemorySize";
	private static final String TOTAL_SWAP_MEM = "TotalSwapSpaceSize";
	private static final String FREE_SWAP_MEM = "FreeSwapSpaceSize";
	private static final String SHARED_MEM = "CommittedVirtualMemorySize";

	private long sequenceNumber = 0;
	private long interval = 3000;

	private Thread monitoringThread;

	private MBeanServer mbeanServer;
	private ObjectName platformMonitor;

	public void start() {
		this.mbeanServer = ManagementFactory.getPlatformMBeanServer();

		try {
			platformMonitor = new ObjectName(OBJECT_NAME);
			this.mbeanServer.registerMBean(this, platformMonitor);

		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (InstanceAlreadyExistsException e) {
			e.printStackTrace();
		} catch (MBeanRegistrationException e) {
			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			this.mbeanServer.unregisterMBean(platformMonitor);
		} catch (MBeanRegistrationException e) {
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startMonitoring() {

		if (monitoringThread == null) {
			monitoringThread = new Thread(this);
		}
		if (!monitoringThread.isAlive()) {
			monitoringThread.start();
		}
	}

	@Override
	public void stopMonitoring() {
		monitoringThread.interrupt();
	}

	@Override
	public void setInterval(long interval) {
		this.interval = interval;
	}

	@Override
	public long getInterval() {
		return this.interval;
	}

	@Override
	public void run() {
		this.poolingSystemProperties();
	}

	private void poolingSystemProperties() {
		try {
			ObjectName name = new ObjectName(
					ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME);

			while (true) {

				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
				}

				Object systemLoad = mbeanServer.getAttribute(name, SYSTEM_LOAD);
				Long freePhysicalMemory = (Long) mbeanServer.getAttribute(name,
						FREE_PHYSICAL_MEM);
				Long freeSwapMemory = (Long) mbeanServer.getAttribute(name,
						FREE_SWAP_MEM);
				Long sharedMemory = (Long) mbeanServer.getAttribute(name,
						SHARED_MEM);

				Long totalSwapMemory = (Long) mbeanServer.getAttribute(name, TOTAL_SWAP_MEM);
				Long totalPhysicalMemory = (Long) mbeanServer.getAttribute(name, TOTAL_PHYSICAL_MEM);
				
				
				Double percentSwap = (double) ((freeSwapMemory*100)/totalSwapMemory);
				Double percentPhysical = (double) ((freePhysicalMemory*100/totalPhysicalMemory));
				Long sharedMbyteMemory = sharedMemory / 1000000;
				
				Map<String, Object> status = new HashMap<String, Object>();
				
				status.put(SYSTEM_LOAD, systemLoad);
				status.put(FREE_PHYSICAL_MEM, percentPhysical);
				status.put(FREE_SWAP_MEM, percentSwap);
				status.put(SHARED_MEM,
						Double.parseDouble(sharedMbyteMemory.toString()));

				this.sendNotification(RESOURCE_STATUS_TYPE, status);
			}

		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (AttributeNotFoundException e) {
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		} catch (MBeanException e) {
			e.printStackTrace();
		} catch (ReflectionException e) {
			e.printStackTrace();
		}
	}

	private void sendNotification(String message, Map<String, Object> status) {

		Notification notification = new Notification(
				PlatformMonitor.RESOURCE_STATUS_TYPE, platformMonitor,
				this.sequenceNumber++, message);

		notification.setUserData(status);

		super.sendNotification(notification);
	}
}
