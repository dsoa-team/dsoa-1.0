package br.ufpe.cin.dsoa.qos.management.impl;

public class QoSAttribute {
	private String qosCategory;
	private String qosCharacteristic;
	
	
	public QoSAttribute(String qosCategory, String qosCharacteristic) {
		super();
		this.qosCategory = qosCategory;
		this.qosCharacteristic = qosCharacteristic;
	}
	
	public String getQosCategory() {
		return qosCategory;
	}
	public String getQosCharacteristic() {
		return qosCharacteristic;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((qosCategory == null) ? 0 : qosCategory.hashCode());
		result = prime
				* result
				+ ((qosCharacteristic == null) ? 0 : qosCharacteristic
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QoSAttribute other = (QoSAttribute) obj;
		if (qosCategory == null) {
			if (other.qosCategory != null)
				return false;
		} else if (!qosCategory.equals(other.qosCategory))
			return false;
		if (qosCharacteristic == null) {
			if (other.qosCharacteristic != null)
				return false;
		} else if (!qosCharacteristic.equals(other.qosCharacteristic))
			return false;
		return true;
	}
	
	
}
