package com.rust.jmx.workder;

import java.util.*;

/**
 * @author Rust
 */
public class WorkerInfomation {
	private static List<WorkerInfomation> workers = new ArrayList<>();

	static {
		WorkerInfomation worker = new WorkerInfomation();
		worker.setJmxIp("10.28.11.159");
		worker.setJmxPort(50079);
		workers.add(worker);
	}

	public static List<WorkerInfomation> touchWorkers() {
		return workers;
	}
	private String jmxIp;
	private int jmxPort;

	public String getJmxIp() {
		return jmxIp;
	}

	public void setJmxIp(String jmxIp) {
		this.jmxIp = jmxIp;
	}

	public int getJmxPort() {
		return jmxPort;
	}

	public void setJmxPort(int jmxPort) {
		this.jmxPort = jmxPort;
	}
}
