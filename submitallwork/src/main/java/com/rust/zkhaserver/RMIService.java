package com.rust.zkhaserver;

import java.rmi.Remote;

/**
 * @author Rust
 */
public interface RMIService extends Remote {
	String submit(String[] param);
}
