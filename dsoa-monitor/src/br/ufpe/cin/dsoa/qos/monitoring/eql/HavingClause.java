package br.ufpe.cin.dsoa.qos.monitoring.eql;

public class HavingClause{

	private Statistic statistic;

	public HavingClause(Statistic statistic, String expression, Object value) {
		this.statistic = statistic;
	}

	public Statistic getStatistic() {
		return statistic;
	}

}
