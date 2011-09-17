package br.ufpe.cin.dsoa.slamanager.agreement;

public class Party {

	private String UID = null;
	private String name = null;

	public Party(String uid, String name) {
		this.UID = uid;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getUID() {
		return UID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUID(String uid) {
		UID = uid;
	}

}
