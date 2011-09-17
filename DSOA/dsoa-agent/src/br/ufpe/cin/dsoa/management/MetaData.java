package br.ufpe.cin.dsoa.qos.management;


public interface MetaData {
	String	ID = "Id";
	String SERVICE_NAME = "service.id";
	String SERVICE_REF_NAME = "ServiceName";
	String OPERATION_REF_NAME = "OperationName";
	String HANDLER_NAME = "HandlerName";
	
	String QOS_CATEGORY = "Category";
	String QOS_CHARACTERISTIC = "Characteristic";
	String QOS_PROPERTIES = "Properties";
	String QOS_VALUE = "value";
	String QOS_TOPIC = "Topic";
	
	String NAMESPACE    = "win";
	String LENGTH_BATCH = "length_batch";
	String TIME_BATCH   = "time_batch";
	
	String TIME = "Time Window";
	String SIZE = "Package Window";
	String MIN  = "min";
	String AVG  = "avg";
	String MAX  = "max";
}
