package br.ufpe.cin.dsoa.qos.monitoring.eql;


public class Statement {
	private Select select;
	private From from;// from
	private Clause whereClause; // where (filter)
	private HavingClause havingClause;
	private Window window;
	
	public Statement(Select select, From from,
			HavingClause havingClause, Window window) {
		super();
		this.select = select;
		this.from = from;
		this.havingClause = havingClause;
		this.window = window;
	}

	public Select getSelect() {
		return select;
	}

	public From getFrom() {
		return from;
	}

	public Clause getWhereClause() {
		return whereClause;
	}

	public void setWhereClause(Clause whereClause) {
		this.whereClause = whereClause;
	}

	public HavingClause getHavingClause() {
		return havingClause;
	}

	public Window getWindow() {
		return window;
	}

	public void setSelect(Select select) {
		this.select = select;
	}

	public void setFrom(From from) {
		this.from = from;
	}

	public void setHavingClause(HavingClause havingClause) {
		this.havingClause = havingClause;
	}

	@Override
	public String toString() {
		
		StringBuilder strBuilder= new StringBuilder();
		strBuilder.append(select.toString());
		
		
		return super.toString();
	}
	
	
	
}
