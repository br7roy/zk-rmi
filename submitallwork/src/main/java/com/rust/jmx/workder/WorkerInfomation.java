package com.rust.jmx.workder;

import java.util.ArrayList;
import java.util.List;

import static com.rust.jmx.server.JmxServerService.ONE;


/**
 * @author Rust
 */
public class WorkerInfomation {
	private static List<WorkerInfomation> workers = new ArrayList<>();

	static {
		WorkerInfomation worker = new WorkerInfomation();
		worker.setJmxIp(ONE.getJmxIp());
		worker.setJmxPort(ONE.getJmxPort());
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
