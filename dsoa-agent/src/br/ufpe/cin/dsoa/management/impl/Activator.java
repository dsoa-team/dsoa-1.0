package br.ufpe.cin.dsoa.management.impl;

import javax.management.ObjectName;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import br.ufpe.cin.dsoa.management.util.JMXUtil;

public class Activator implements BundleActivator {
	
	private BundleContext ctx;
	private static final String MANAGEMENT_AGENT_MXBEAN_NAME = "management:type=Agent";
	private ObjectName name;
	private ManagementAgent agent;
	
	@Override
	public void start(BundleContext ctx) throws Exception {
		this.ctx = ctx;
		this.name = JMXUtil.buildObjectName(MANAGEMENT_AGENT_MXBEAN_NAME);
		this.agent = new ManagementAgent(ctx);
		this.agent.start();
		this.registerManagementAgentMXBean();
	}

	@Override
	public void stop(BundleContext ctx) throws Exception {
		this.agent.stop();
		this.agent = null;
		this.removeManagementAgentMXBean();
	}
	
	private void registerManagementAgentMXBean() {
		JMXUtil.registerMXBean(agent, name);
	}
	
	private void removeManagementAgentMXBean() {
		JMXUtil.removeMXBean(name);
	}

}
