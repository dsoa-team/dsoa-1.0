package br.ufpe.cin.dsoa.broker.impl;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import br.ufpe.cin.dsoa.broker.Broker;
import br.ufpe.cin.dsoa.broker.filter.AndFilter;
import br.ufpe.cin.dsoa.broker.filter.DFilter;
import br.ufpe.cin.dsoa.broker.filter.FilterBuilder;
import br.ufpe.cin.dsoa.broker.filter.IFilter;
import br.ufpe.cin.dsoa.qos.QosDependencyListener;
import br.ufpe.cin.dsoa.qos.Slo;
import br.ufpe.cin.dsoa.rank.Rank;


public class BrokerImpl implements Broker {
	
	private BundleContext context;
	private ServiceTracker tracker;
	private ServiceReference reference, ranking;
	private ServiceReference[] candidates;
	private Filter filter;
	private Rank rank;
	

	/**
	 * Constructor
	 * 
	 **/
	
	public BrokerImpl(BundleContext context) {
		this.context = context;
	}
	
	private List<FilterBuilder> getFilters(String spe, List<Slo> slos) {
		
		List<FilterBuilder> filter = new ArrayList<FilterBuilder>();
		
		filter.add(new IFilter(Constants.OBJECTCLASS, spe));
		
			for(Slo slo: slos) {
				
				if(slo.getOperation() != null) {
					
					filter.add(new DFilter(slo.getAttribute() + "." + slo.getOperation(), 
							slo.getExpression(), slo.getValue()));
				} else {
					
					filter.add(new DFilter(slo.getAttribute(), slo.getExpression(), slo.getValue()));
				}
			}
		//}
		/*
		else {
			
			for(Slo slo: slos) {
				
				if(slo.getTarget() != null) {
					
					filter.add(new IFilter(slo.getName() + "." + slo.getTarget(), "*"));
				} else {
					
					filter.add(new IFilter(slo.getName(), "*"));
				}
			}
		}
		*/
		return filter;
	}

	/*
	@Override
	public void getBestService(String spe, List<Slo> slos,
			QosDependencyListener dep) {
		//Tinha um mode;
		// TODO Auto-generated method stub
		
		setQdl(dep);
		setSlos(slos);
		
		try {
			filter = context.createFilter(new AndFilter(getFilters(
					spe, slos)).toString());
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
		try {
			candidates = context.getServiceReferences(spe, new AndFilter(getFilters(
						spe, slos)).toString());
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
		if(candidates == null) {
			
			tracker = new BrockerTracker(this, context, filter);
			tracker.open();
		} else {
			
			reference = findBestService(candidates);
			service = bestService(reference);
			System.out.println("Service::" + service.getService());
			//getQdl().setSelected(service);
		}
	}

*/

	private ServiceReference findBestService(List<Slo> slos, ServiceReference... candidates) {
		// TODO Auto-generated method stub
		
		ServiceReference service = null;
		
		ranking = context.getServiceReference(Rank.class.getName());
		
		if(ranking == null) {
			
			service = candidates[0];
			
			/*	
			for(ServiceReference reference: candidates) {
				
				System.out.println(service.getProperty(Constants.SERVICE_RANKING));
				int high = Integer.parseInt((String) service.getProperty(Constants.SERVICE_RANKING));
				int low = Integer.parseInt((String) reference.getProperty(Constants.SERVICE_RANKING));
				
				if(low > high) {
					service = reference;
				} else {
					if(low == high) {
						long id1 = Long.parseLong((String) service.getProperty(Constants.SERVICE_ID));
						long id2 = Long.parseLong((String) reference.getProperty(Constants.SERVICE_ID));
						
						if(id2 < id1) {
							service = reference;
						}
					}
				}
			}
		
		*/
			
		} else {
			
			rank = (Rank) context.getService(ranking);
			service = rank.ranking(slos, candidates);
		}
		
		return service;
	}

	@Override
	public void getBestService(String spe, List<Slo> slos,
			QosDependencyListener dep) {
		
		try {
			filter = context.createFilter(new AndFilter(getFilters(
					spe, slos)).toString());
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		System.out.println("Filter:" + filter.toString());
		try {
			candidates = context.getServiceReferences(spe, new AndFilter(getFilters(
						spe, slos)).toString());
			System.out.println("Candidates" + candidates);
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
		if(candidates == null) {
			tracker = new BrokerTracker(dep, context, filter);
			tracker.open();
		} else {
			reference = findBestService(slos, candidates);
			dep.setSelected(reference);
		}
		
	}
}
