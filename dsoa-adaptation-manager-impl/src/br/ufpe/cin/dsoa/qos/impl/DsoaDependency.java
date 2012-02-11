package br.ufpe.cin.dsoa.qos.impl;

import java.util.Collections;
import java.util.List;

import org.apache.felix.ipojo.FieldInterceptor;
import org.osgi.framework.BundleContext;

import br.ufpe.cin.dsoa.qos.Slo;

/**
 * 
 * @author Danilo - em caso de uso do DependencyModel
 * 
 * 
 *         public class DsoaDependency extends DependencyModel implements
 *         FieldInterceptor, MethodInterceptor,QosDependencyListener{
 */

public class DsoaDependency implements FieldInterceptor {

	private DsoaDependencyHandler handler;
	private Class specification;
	private List<Slo> slos;
	private boolean valid;
	private Object serviceProxy;


	@SuppressWarnings("rawtypes")
	public DsoaDependency(DsoaDependencyHandler handler, Class specification, List<Slo> slos) {
		this.handler = handler;
		this.specification = specification;
		this.slos = slos;
		this.valid = false;
	}

	public void start() {
		this.serviceProxy = DsoaProxyFactory.createProxy(this);
	}

	/**
	 * necessario somente se usarmos o DependencyModel
	 * 
	 * @Override public synchronized void onServiceArrival(ServiceReference
	 *           svcRef) { String pid = (String)
	 *           svcRef.getProperty(Constants.SERVICE_PID);
	 * 
	 *           // update the serviceProxy object serviceProxy =
	 *           super.getService(svcRef);
	 * 
	 *           }
	 */

	public Object onGet(Object arg0, String arg1, Object arg2) {
		return serviceProxy;
	}

	public void onSet(Object arg0, String arg1, Object arg2) {

	}

	public BundleContext getContext() {
		return handler.getInstanceManager().getContext();
	}
	
	public Class getSpecification() {
		return specification;
	}

	public List<Slo> getSlos() {
		return Collections.unmodifiableList(slos);
	}
	
	public String getConsumerPID() {
		return handler.getConsumerPID();
	}

	public String getConsumerName() {
		return handler.getConsumerName();
	}
	
	public String getQoSMode() {
		return handler.getQosMode();
	}

	public void setValid(boolean stateDep) {
		this.valid = stateDep;
		this.handler.checkValidate();
	}

	public boolean isValid() {
		return valid;
	}

}
