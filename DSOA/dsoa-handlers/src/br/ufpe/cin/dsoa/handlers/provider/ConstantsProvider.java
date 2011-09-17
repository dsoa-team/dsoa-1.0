package br.ufpe.cin.dsoa.handlers.provider;

public interface ConstantsProvider {
	/** 
	 * Tag OFFER
	 */
	public static final String PROVIDER_NAME_ATTRIBUTE           = "name";
	public static final String PROVIDER_PID_ATTRIBUTE            = "pid";
	public static final String PROVIDER_DURATION_VALUE_ATTRIBUTE = "duration.value";
	public static final String PROVIDER_DURATION_UNIT_ATTRIBUTE  = "duration.unit";
	
	/** 
	 * Tag SLO
	 */
	public static final String SLO_ELEMENT              = "slo";
	public static final String SLO_NAME_ATTRIBUTE       = "name";
	public static final String SLO_VALUE_ATTRIBUTE      = "value";
	public static final String SLO_TARGET_ATTRIBUTE     = "target";
	
	/**
	 * Tag handler
	 */
	public static final String NAMESPACE = "br.ufpe.cin.dsoa.qos.prohandler";
	public static final String NAME      = "provides";
	
	/**
	 * properties name
	 */
	public static final String PROVIDER_NAME          = "provider.name";
	public static final String PROVIDER_PID           = "provider.pid";
    
    public static final String DURATION_VALUE              = "duration.value";
    public static final String DURATION_UNIT               = "duration.unit";
    
    
    public static final String IPOJO_PROVIDED_SERVICE_HANDLER = "org.apache.felix.ipojo:provides";
    
}
