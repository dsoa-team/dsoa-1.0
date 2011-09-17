package consumer.test.service;

import provider.serasa.service.ServiceSerasa;
import provider.spc.service.ServiceSpc;

public class CompTeste implements Runnable {

	private ServiceSpc serviceSpc;
	private ServiceSerasa serviceSerasa;
	boolean end;
	long delay = 3000;

	public CompTeste() {
	}

	public void start() {
		end = false;
		System.out.println("test started");
		Thread t = new Thread(this);
		t.setName("Test-consumer");
		t.start();
	}

	public void stop() {
		end = true;
		System.out.println("test stopped");
	}

	public void run() {
		while (!end) {
			callService();
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void callService() {

		if (serviceSerasa.searchStateSerasa("Fabio")) {
			System.out.println("");
			System.out.println("Resposta ServiceSERASA: "
					+ serviceSerasa.searchCauseSerasa("Fabio"));
		}

		if (serviceSpc.searchStateSpc("Jie")) {
			System.out.println("");
			System.out.println("Resposta ServiceSPC: "
					+ serviceSpc.searchCauseSpc("Jie"));
		}

	}

}
