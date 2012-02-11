package br.ufpe.cin.dsoa.manager.metadata;

import java.lang.reflect.Field;
import java.util.Dictionary;

import org.apache.felix.ipojo.metadata.Element;

public class Slo {

	private String metric;
	private String statistic;
	private double threshold = -1.0;
	private String operation;
	private String expression;
	private long weight = -1;

	public Slo() {
		super();
	}

	public Slo(String description) {
		String tuples[] = description.split(",");
		for (String tuple : tuples) {
			this.parseTuple(tuple);
		}
	}

	@SuppressWarnings("rawtypes")
	public void loadMetadata(Element metadata, Dictionary configuration) {

		if (metadata.containsAttribute(ProviderMetadata.SLO_EXPRESSION))
			this.expression = metadata
					.getAttribute(ProviderMetadata.SLO_EXPRESSION);

		if (metadata.containsAttribute(ProviderMetadata.SLO_METRIC))
			this.metric = metadata.getAttribute(ProviderMetadata.SLO_METRIC);

		if (metadata.containsAttribute(ProviderMetadata.SLO_OPERATION))
			this.operation = metadata
					.getAttribute(ProviderMetadata.SLO_OPERATION);

		if (metadata.containsAttribute(ProviderMetadata.SLO_STATISTIC))
			this.statistic = metadata
					.getAttribute(ProviderMetadata.SLO_STATISTIC);

		if (metadata.containsAttribute(ProviderMetadata.SLO_WEIGHT))
			this.weight = Long.parseLong(metadata
					.getAttribute(ProviderMetadata.SLO_WEIGHT));

		if (metadata.containsAttribute(ProviderMetadata.SLO_THRESHOLD))
			this.threshold = Double.parseDouble(metadata
					.getAttribute(ProviderMetadata.SLO_THRESHOLD));

	}

	private String createTuple(String key, String value) {
		return key + "=" + value;
	}

	private void parseTuple(String tuple) {

		String[] attribute = tuple.split("=");

		try {
			Field field = this.getClass().getDeclaredField(attribute[0]);
			String typeName = field.getType().getName();

			if (typeName.equals(String.class.getName())) {
				field.set(this, attribute[1]);
			} else if (typeName.equals(double.class.getName())) {
				field.set(this, Double.parseDouble(attribute[1]));
			} else if (typeName.equals(long.class.getName())) {
				field.set(this, Long.parseLong(attribute[1]));
			} else if (typeName.equals(int.class.getName())) {
				field.set(this, Integer.parseInt(attribute[1]));
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		StringBuffer description = new StringBuffer();

		if (this.expression != null) {
			description.append(createTuple(ProviderMetadata.SLO_EXPRESSION,
					expression) + ",");
		}

		if (this.operation != null) {
			description.append(createTuple(ProviderMetadata.SLO_OPERATION,
					operation) + ",");
		}

		if (this.statistic != null) {
			description.append(createTuple(ProviderMetadata.SLO_STATISTIC,
					statistic) + ",");
		}

		if (this.threshold > -1) {
			description.append(createTuple(ProviderMetadata.SLO_THRESHOLD,
					threshold + "") + ",");
		}

		if (this.weight > -1) {
			description.append(createTuple(ProviderMetadata.SLO_WEIGHT, weight
					+ "")
					+ ",");
		}

		if (this.metric != null) {
			description
					.append(createTuple(ProviderMetadata.SLO_METRIC, metric));
		}

		return description.toString();
	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public String getStatistic() {
		return statistic;
	}

	public void setStatistic(String statistic) {
		this.statistic = statistic;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public long getWeight() {
		return weight;
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}
}
