package br.ufpe.cin.dsoa.manager.metadata;

import java.util.Dictionary;

import org.apache.felix.ipojo.metadata.Element;


public class Resource {

	private String type;
	private String attribute;
	private double threshold;
	private String expression;

	public Resource() {
		super();
	}

	@SuppressWarnings("rawtypes")
	public void loadMetadata(Element metadata, Dictionary configuration) {
		this.type = metadata.getAttribute(ProviderMetadata.RESOURCE_TYPE);
		this.attribute = metadata.getAttribute(ProviderMetadata.RESOURCE_ATTRIBUTE);
		this.threshold = Double.parseDouble(metadata.getAttribute(ProviderMetadata.RESOURCE_THRESHOLD));
		this.expression = metadata.getAttribute(ProviderMetadata.RESOURCE_EXPRESSION);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

}
