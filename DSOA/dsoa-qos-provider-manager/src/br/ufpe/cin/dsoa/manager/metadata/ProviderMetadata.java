package br.ufpe.cin.dsoa.manager.metadata;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.apache.felix.ipojo.metadata.Element;

public class ProviderMetadata {

	private String pid;
	private String name;
	private double duration;
	private String unit;

	private List<Slo> slos;
	private List<Profile> profiles;

	public ProviderMetadata() {
		slos = new ArrayList<Slo>();
		profiles = new ArrayList<Profile>();
	}

	@SuppressWarnings("rawtypes")
	public void loadMetadata(Element metadata, Dictionary configuration) {

		Element[] elements = metadata
				.getElements(HANDLE_NAME, HANDLE_NAMESPACE);

		this.pid = elements[0].getAttribute(PROVIDER_PID_ATTR);
		this.name = elements[0].getAttribute(PROVIDER_NAME_ATTR);
		this.duration = Double.parseDouble(elements[0]
				.getAttribute(PROVIDER_DURATION_ATTR));
		this.unit = elements[0].getAttribute(PROVIDER_UNIT_ATTR);

		// slo
		if (elements[0].containsElement(SLO)) {
			Element[] tagSlo = elements[0].getElements(SLO);
			for (Element e_slo : tagSlo) {
				Slo slo = new Slo();
				slo.loadMetadata(e_slo, configuration);
				slos.add(slo);
			}
		}

		// profile
		if (elements[0].containsElement(PROFILES)) {
			Element[] tagProfiles = elements[0].getElements(PROFILES);
			if (tagProfiles[0].containsElement(PROFILE)) {
				Element[] tagProfile = tagProfiles[0].getElements(PROFILE);
				for (Element e_profile : tagProfile) {
					Profile profile = new Profile();
					profile.loadMetadata(e_profile, configuration);
					profiles.add(profile);
				}
			}
		}

	}

	public Dictionary<String, Object> getRegisterProperties() {

		Dictionary<String, Object> properties = new Hashtable<String, Object>();

		properties.put(PROVIDER_NAME_ATTR, name);
		properties.put(PROVIDER_PID_ATTR, pid);
		properties.put(PROVIDER_DURATION_ATTR, duration);

		for (Slo slo : slos) {

			if (slo.getOperation() != null) {
				// METRIC.OPERATION :: VALUE
				properties.put(slo.getMetric() + "." + slo.getOperation(),
						slo.getThreshold());
			} else {
				// METRIC :: VALUE
				properties.put(slo.getMetric(), slo.getThreshold());
			}
		}

		return properties;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<Slo> getSlos() {
		return slos;
	}

	public void setSlos(List<Slo> slos) {
		this.slos = slos;
	}

	public List<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

	// Constants//
	public static final String PROVIDER_METADATA = "provider.metadata";
	public static final String HANDLE_NAME = "provider-manager";
	public static final String HANDLE_NAMESPACE = "br.ufpe.cin.dsoa.manager";

	// provides
	public static final String PROVIDER_NAME_ATTR = "name";
	public static final String PROVIDER_PID_ATTR = "pid";
	public static final String PROVIDER_DURATION_ATTR = "duration.value";
	public static final String PROVIDER_UNIT_ATTR = "duration.unit";

	// tags
	public static final String SLO = "slo";
	public static final String PROFILES = "profiles";
	public static final String PROFILE = "profile";
	public static final String RESOURCE = "resource";

	// slo
	public static final String SLO_METRIC = "metric";
	public static final String SLO_STATISTIC = "statistic";
	public static final String SLO_THRESHOLD = "threshold";
	public static final String SLO_OPERATION = "operation";
	public static final String SLO_EXPRESSION = "expression";
	public static final String SLO_WEIGHT = "weight";

	// profile
	public static final String PROFILE_POLICY = "policy";

	// resource
	public static final String RESOURCE_TYPE = "type";
	public static final String RESOURCE_ATTRIBUTE = "attribute";
	public static final String RESOURCE_THRESHOLD = "threshold";
	public static final String RESOURCE_EXPRESSION = "expression";
}
