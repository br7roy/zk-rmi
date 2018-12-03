package com.rust.zkhaserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Rust
 */
public class RMIServiceImpl extends UnicastRemoteObject implements RMIService {
	private static final long serialVersionUID = 1L;

	public RMIServiceImpl() throws RemoteException {
	}
	@Override
	public String submit(String[] param) {
		// TODO: 调用App逻辑，组装参数

		return null;
	}

}
