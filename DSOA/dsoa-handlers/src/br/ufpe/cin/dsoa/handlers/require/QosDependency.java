package br.ufpe.cin.dsoa.qos.reqhandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import org.apache.felix.ipojo.FieldInterceptor;
import org.apache.felix.ipojo.MethodInterceptor;
import org.apache.felix.ipojo.Nullable;
import org.apache.felix.ipojo.handlers.dependency.NullableObject;
import org.apache.felix.ipojo.util.DependencyModel;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

import br.ufpe.cin.dsoa.qos.slamanager.agreement.Slo;
import br.ufpe.dsoa.broker.service.ServiceDescription;

public class QosDependency extends DependencyModel implements
FieldInterceptor, MethodInterceptor,QosDependencyListener{
	
	private QosDependencyHandler m_handler;
	private String               m_specification;
	private List<Slo>            slos;
	private BundleContext        context;
	private Object               m_nullableObject;
	private Object               m_svcObject;
	private boolean              stateDep;
	
	//provider in use
	private String providerPid;
	private String providerName;
	private long   duration;
	
	@SuppressWarnings("rawtypes")
	public QosDependency(Class specification,BundleContext context,
			QosDependencyHandler handler,List<Slo> slos) {
	
		
		super(specification, /*aggregate*/false, /*optional*/false, /*filter*/null, /*comparator*/null, DependencyModel.DYNAMIC_BINDING_POLICY,
		context, handler, handler.getInstanceManager());
		
		this.m_handler        = handler;
		this.slos             = slos;
		this.context          = context;
		this.m_specification  = specification.getName();
		this.setStateDep(false);
	}
	
	public void start() {
        // creation of the Nullable Object
        m_nullableObject = Proxy.newProxyInstance(m_handler
        .getInstanceManager().getClazz().getClassLoader(),
        new Class[] { getSpecification(), Nullable.class },
        new NullableObject());
        
        super.start();
	}
	
	 @Override
     public void stop() {
             super.stop();
             m_nullableObject = null;
     }
	 
	 @Override
     public synchronized void onServiceArrival(ServiceReference svcRef) {
		 String pid = (String) svcRef.getProperty(Constants.SERVICE_PID);
		 
         // update the service object
         m_svcObject = super.getService(svcRef);
         
	 }
	
	

	@Override
	public void onEntry(Object arg0, Method arg1, Object[] arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Object arg0, Method arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Fabions
	 
	@Override
	public boolean match(ServiceReference ref) {
		return m_handler.getSlaManagerService().matchDemand((String)ref.getProperty(ConstantsSLA.PROVIDER_PID),getSlos());
	}
*/
	@Override
	public void onExit(Object arg0, Method arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinally(Object arg0, Method arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object onGet(Object arg0, String arg1, Object arg2) {
		return m_svcObject;
	}

	@Override
	public void onSet(Object arg0, String arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDependencyReconfiguration(ServiceReference[] arg0,
			ServiceReference[] arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServiceDeparture(ServiceReference arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServiceModification(ServiceReference arg0) {
		// TODO Auto-generated method stub
		
	}

	public QosDependencyHandler getHandler() {
		return m_handler;
	}

	public void setHandler(QosDependencyHandler handler) {
		this.m_handler = handler;
	}

	public String getM_specification() {
		return m_specification;
	}

	public void setM_specification(String m_specification) {
		this.m_specification = m_specification;
	}

	
	public List<Slo> getSlos() {
		return slos;
	}

	public BundleContext getContext() {
		return context;
	}

	public void setContext(BundleContext context) {
		this.context = context;
	}

	public Object getNullableObject() {
		return m_nullableObject;
	}

	public void setM_nullableObject(Object m_nullableObject) {
		this.m_nullableObject = m_nullableObject;
	}

	public Object getSvcObject() {
		return m_svcObject;
	}

	public void setSvcObject(Object m_svcObject) {
		this.m_svcObject = m_svcObject;
	}

	@Override
	public void setSelected(ServiceDescription serviceDescription) {
		
		setSvcObject(serviceDescription.getService());
		setProviderName(serviceDescription.getProviderName());
		setProviderPid(serviceDescription.getProviderId());
		setDuration(serviceDescription.getDuration());

		setStateDep(true);
		getHandler().checkValidate();
		
	}

	public void setStateDep(boolean stateDep) {
		this.stateDep = stateDep;
	}

	public boolean isStateDep() {
		return stateDep;
	}

	public String getProviderPid() {
		return providerPid;
	}

	public void setProviderPid(String providerPid) {
		this.providerPid = providerPid;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getDuration() {
		return duration;
	}
	
}
