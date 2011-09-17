package br.ufpe.cin.dsoa.qos.slamanager.agreement;

public enum Expression {
	GT("GT"), LT("LT"), EQ("EQ");
	
	private String operator;
	
	Expression(String exp) {
		this.operator = exp;
	}
	
	public boolean evaluate(double leftOp, double rightOp) {
		switch(this) {
			case GT: {
				return leftOp > rightOp;
			}
			case LT: {
				return leftOp < rightOp;
			}
			case EQ: {
				return leftOp == rightOp;
			}
		}
		return false;
	}
	
	public String getOperator() {
		return operator;
	}
	
	public static Expression getExpression(String exp) {
		for(Expression expression : values()) {
			if (expression.getOperator().equals(exp)) {
				return expression;
			}
		}
		return null;
	}
}
