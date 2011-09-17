package provider.serasa.service.impl;

import provider.serasa.service.ServiceSerasa;

public class ServiceSerasaImpl implements ServiceSerasa{

	@Override
	public boolean searchStateSerasa(String name) {
		return true;
	}

	@Override
	public String searchCauseSerasa(String name) {
		return name + " pagou a divida das bolachas (graças a Andrea)";
	}


}
