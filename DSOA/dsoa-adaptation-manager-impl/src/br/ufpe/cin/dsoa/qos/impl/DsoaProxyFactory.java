package br.ufpe.cin.dsoa.qos.impl;

import java.lang.reflect.Proxy;

import br.ufpe.cin.dsoa.manager.DsoaDependencyManager;

public class DsoaProxyFactory {

	public static Object createProxy(DsoaDependency dependency) {
		DsoaDependencyManager manager = new DsoaDependencyManager(dependency);
		Object proxy = Proxy.newProxyInstance(dependency.getSpecification().getClassLoader(),
				new Class[] {dependency.getSpecification()}, manager );

		return proxy;
	}
}
