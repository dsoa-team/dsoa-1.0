package br.ufpe.cin.dsoa.manager.metadata;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.apache.felix.ipojo.metadata.Element;


public class Profile {

	private String policy;
	private List<Resource> resources;

	public Profile() {
		resources = new ArrayList<Resource>();
	}

	@SuppressWarnings("rawtypes")
	public void loadMetadata(Element metadata, Dictionary configuration) {

		if (metadata.containsAttribute(ProviderMetadata.PROFILE_POLICY)) {
			this.policy = metadata
					.getAttribute(ProviderMetadata.PROFILE_POLICY);
		}

		if (metadata.containsElement(ProviderMetadata.RESOURCE)) {
			Element[] tagResource = metadata
					.getElements(ProviderMetadata.RESOURCE);
			for (Element e_resource : tagResource) {
				Resource resource = new Resource();
				resource.loadMetadata(e_resource, configuration);
				resources.add(resource);
			}
		}
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

}
