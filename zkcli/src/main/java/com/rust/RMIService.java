package com.rust;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Rust
 */
public interface RMIService extends Remote {
	String submit(String type, String id, String time, String account, String pwd) throws RemoteException;
}
