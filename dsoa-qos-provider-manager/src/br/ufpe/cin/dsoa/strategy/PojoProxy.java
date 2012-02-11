package br.ufpe.cin.dsoa.strategy;

import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.apache.felix.ipojo.InstanceManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import br.ufpe.cin.dsoa.manager.metadata.Profile;
import br.ufpe.cin.dsoa.manager.metadata.ProviderMetadata;
import br.ufpe.cin.dsoa.manager.metadata.Resource;

public class PojoProxy implements InvocationHandler, ServiceTrackerCustomizer,
		NotificationListener {

	private InstanceManager m_manager;
	private Map<String, CreationPolicy> polices;
	private ProviderMetadata p_metadata;

	private Method hashCode;
	private Method toString;
	private Method equals;

	private CreationPolicy activePolicy;
	private Object servant;

	private static String OBJECT_NAME = "jmx.monitor:Type=PlatformMonitor";

	public PojoProxy(InstanceManager m_manager, ProviderMetadata p_metadata) {

		this.m_manager = m_manager;
		this.p_metadata = p_metadata;
		this.polices = new HashMap<String, CreationPolicy>();

		try {
			hashCode = Object.class.getMethod("hashCode", null);
			toString = Object.class.getMethod("toString", null);
			equals = Object.class.getMethod("equals",
					new Class[] { Object.class });
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		// open tracker
		new ServiceTracker(m_manager.getContext(),
				CreationPolicy.class.getName(), this).open();

		// register listener
		try {
			MBeanServer mbeanServer = ManagementFactory
					.getPlatformMBeanServer();
			mbeanServer.addNotificationListener(new ObjectName(OBJECT_NAME),
					this, null, null);
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}

	public Object addingService(ServiceReference reference) {
		CreationPolicy policy = (CreationPolicy) m_manager.getContext()
				.getService(reference);

		polices.put((String) reference.getProperty("policy.name"), policy);
		return policy;
	}

	public static Object createProxy(InstanceManager m_manager,
			ProviderMetadata p_metadata) {

		return Proxy.newProxyInstance(m_manager.getClazz().getClassLoader(),
				PojoProxy.getSpecifications(m_manager), new PojoProxy(
						m_manager, p_metadata));
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		if (method.getDeclaringClass() == Object.class) {
			if (method.equals(equals)) {
				return proxy == args[0] ? Boolean.TRUE : Boolean.FALSE;
			} else if (method.equals(hashCode)) {
				return new Integer(this.hashCode());
			} else if (method.equals(toString)) {
				return this.toString();
			}
		}

		if (existsActivePolicy()) {
			servant = getActivePolicy().createInstance(m_manager);
		} else if (!polices.isEmpty()) {
			servant = ((CreationPolicy) polices.values().iterator().next())
					.createInstance(m_manager);
		} else if (null == servant) {
			// default policy (static instance)
			servant = m_manager.createPojoObject();
		}
		return method.invoke(servant, args);
	}

	@Override
	public void modifiedService(ServiceReference reference, Object service) {
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
		polices.remove(reference.getProperty("policy.name"));
		m_manager.getContext().ungetService(reference);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleNotification(Notification notification, Object handback) {
		Map<String, Object> status = (Map<String, Object>) notification
				.getUserData();

		for (String key : status.keySet()) {
			parsePolicy(key, status.get(key));
		}
	}

	/**
	 * return all exported interfaces
	 * 
	 * @param m_manager
	 * @return
	 */
	private static Class<?>[] getSpecifications(InstanceManager m_manager) {

		String[] interfaces = m_manager.getFactory().getPojoMetadata()
				.getInterfaces();

		BundleContext ctx = m_manager.getContext();

		Class<?>[] classes = new Class[interfaces.length];
		int i = 0;
		for (String clazz : interfaces) {
			try {
				classes[i] = ctx.getBundle().loadClass(clazz);
			} catch (ClassNotFoundException e) {
			}
		}
		return classes;
	}

	private synchronized void setActivePolicy(CreationPolicy policy) {
		if (null == activePolicy
				|| !activePolicy.getClass().getName()
						.equals(policy.getClass().getName())) {
			activePolicy = policy;
		}
	}

	private synchronized CreationPolicy getActivePolicy() {
		return activePolicy;
	}

	private boolean existsActivePolicy() {
		return null != activePolicy;
	}

	private void parsePolicy(String key, Object value) {

		for (Profile p : p_metadata.getProfiles()) {
			for (Resource r : p.getResources()) {
				if (r.getAttribute().equalsIgnoreCase(key)) {
					if (parseExpression(r.getExpression(), (Double) value,
							r.getThreshold())) {
						// threshold limit exceeded
						this.setActivePolicy(polices.get(p.getPolicy()));
						System.out.println("CHANGE");
						System.out.println(polices.get(p.getPolicy())
								.getClass().getName());
						return;
					}
				}
			}
		}
	}

	private boolean parseExpression(String expression, Double arg1, Double arg2) {

		boolean retorno = false;

		if (expression.equalsIgnoreCase("EQ")) {
			retorno = arg1.equals(arg2);
		} else if (expression.equalsIgnoreCase("LT")) {
			retorno = arg1 < arg2;
		} else if (expression.equalsIgnoreCase("GT")) {
			retorno = arg1 > arg2;
		}

		return retorno;
	}
}
