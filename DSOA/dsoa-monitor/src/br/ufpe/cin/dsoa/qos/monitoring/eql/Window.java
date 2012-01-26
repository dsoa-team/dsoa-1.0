package br.ufpe.cin.dsoa.qos.monitoring.eql;

public class Window {
	private String windowType;
	private int windowSize;
	private String windowUnit;
	public Window(String windowType, int windowSize, String windowUnit) {
		super();
		this.windowType = windowType;
		this.windowSize = windowSize;
		this.windowUnit = windowUnit;
	}
	public String getWindowType() {
		return windowType;
	}
	public int getWindowSize() {
		return windowSize;
	}
	public String getWindowUnit() {
		return windowUnit;
	}
	
}
