package br.ufpe.cin.dsoa.qos.reqhandler;

public interface Constants {
	/** 
	 * Tag DEMAND
	 */
	public static final String SERVICE_ELEMENT                   = "service";
	public static final String SERVICE_FIELD_ATTRIBUTE           = "field";
	public static final String CONSUMER_NAME_ATTRIBUTE           = "name";
	public static final String CONSUMER_PID_ATTRIBUTE            = "pid";
	
	
	/** 
	 * Tag SLO
	 */
	public static final String SLO_ELEMENT              = "slo";
	public static final String SLO_NAME_ATTRIBUTE       = "name";
	public static final String SLO_EXPRESSION_ATTRIBUTE = "expression";
	public static final String SLO_VALUE_ATTRIBUTE      = "value";
	public static final String SLO_TARGET_ATTRIBUTE     = "target";
	public static final String SLO_WEIGHT_ATTRIBUTE     = "weight";
	
	/**
	 * Tag handler
	 */
	public static final String NAMESPACE = "br.ufpe.cin.dsoa.qos.reqhandler";
	public static final String NAME      = "requires";
	
	/**
	 * properties name
	 */
	public static final String CONSUMER_NAME          = "consumer.name";
    public static final String CONSUMER_PID           = "consumer.pid";
    
}
