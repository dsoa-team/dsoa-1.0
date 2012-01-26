package br.ufpe.cin.dsoa.qos.monitoring.service;


public class MonitoringConfigurationItem{

	//private Statement statetement;

	private static long idGenerator = 1;
	
	
	private long id;
	private String operation;
	private String attribute;
	private String expression;
	private double value;
	private String statistic;
	private String statement;
	private MonitoringConfiguration monitoringConfiguration;
	
	
	private synchronized static long getNextId() {
		return idGenerator++;
	}
	
	public MonitoringConfigurationItem(String operation, String attribute,
			String expression, double value, String statistic, MonitoringConfiguration config) {
		this.id = getNextId();
		
		this.operation = operation;
		this.attribute     = attribute;
		this.expression    = expression;
		this.value         = value;
		this.statistic     = statistic;
		this.monitoringConfiguration = config;
		//this.createStatement();
		this.create();
	}


	private void create() {

		if(this.operation != null){
			if(this.attribute.equalsIgnoreCase("ErrorEvent") == true){
				//Accuracy
				this.statement = "select " + "count(serviceId)*100/30 as erros" + " from " + this.attribute + "(serviceId=?" + 
				", consumerId=" + "'" +
				this.monitoringConfiguration.getClientId() + "'" + 
				", operationName=" + "'" + this.operation + "')" +
				".win:length(30)" + " having " + " count(serviceId)" +
				"*100/30 > " + this.value;
				
			}
			else{
				//ResponseTime and ProcessnigTime - Aggregate
				if(this.statistic != null){
					//ResponseTime
					if(this.monitoringConfiguration.getClientId() != null){
						this.statement = "select " + this.statistic + "(value) from " + this.attribute + "(serviceId=?" + 
						", consumerId=" + "'" + 
						this.monitoringConfiguration.getClientId() + "'" + 
						", operationName=" + "'" + this.operation + "')" +
						".win:length_batch(2)" + " having not " + this.statistic + "(value)" + this.expression + this.value;
						System.out.println("-------+++++  " + this.statement);
					}
					//ProcessingTime
					else{
						this.statement = "select " + this.statistic + "(value) from " + this.attribute + "(serviceId=?" + 
						", operationName=" + "'" + this.operation + "')" +
						".win:time(30 sec)" + " having not'('" + this.statistic + "(value)" + this.expression + this.value +"')'";
					}
				}
				//ResponseTime and ProcessnigTime - no Aggreaged
				else{
					this.statement = "select value from " + this.attribute + "(serviceId=?, value" +
					this.expression + this.value+ ")";
				}
			}
		}
		else{
			//Avaliability
			this.statement = "select " + "sum(value)*100/30 as disponibilidade" + " from " + this.attribute + "(serviceId=?)" +
			".win:time(30 sec)" + " having " + " sum(value)" +
			"*100/30 < " + this.value;
		}
		System.out.println(this.statement);
	}


/*	private void createStatement() {
		//select
		Select select = null;
		Statistic statistic = null;
		if (null != this.statisticName) {
			statistic = new Statistic(this.statisticName, "value");
			select = new Select(statistic);
		} else {
			List<String> fields = new ArrayList<String>();
			fields.add("value");
			select = new Select(fields);
		}

		//from
		From from = new From(this.attribute);
		Clause clause1 = new Clause("serviceId","=", this.monitoringConfiguration.getServiceId());
		Clause clause2 = new Clause("clientId", "=", this.monitoringConfiguration.getClientId());
		from.getFilter().add(clause1);
		from.getFilter().add(clause2);
		if(this.operationName != null){
			Clause clause3 = new Clause("operationName", "=", this.operationName);
			from.getFilter().add(clause3);
		}

		this.statetement.setSelect(select);
		this.statetement.setFrom(from);


		//having
		if(this.statisticName != null){
			HavingClause havingClause = new HavingClause(statistic, this.expression, this.value);
			this.statetement.setHavingClause(havingClause);
		}

		//where
		else{
			Clause whereClause = new Clause("value", this.expression, this.value);
			this.statetement.setWhereClause(whereClause);
		}



	}

*/





	public String getOperation() {
		return operation;
	}


	public String getAttribute() {
		return attribute;
	}


	public String getExpression() {
		return expression;
	}


	public double getValue() {
		return value;
	}


	public String getStatistic() {
		return statistic;
	}

	public long getId() {
		return id;
	}

	public String getStatement() {
		return statement;
	}
	
}
