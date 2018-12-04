package com.rust.zkhaserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.rust.RMIService;
import com.rust.submit.Constants;
import com.rust.submit.WorkContext;

/**
 * @author Rust
 */
public class RMIServiceImpl extends UnicastRemoteObject implements RMIService {
	private static final long serialVersionUID = 1L;

	public RMIServiceImpl() throws RemoteException {
	}

	@Override
	@SuppressWarnings("unchecked")
	public String submit(String type, String id, String time, String account,
						 String pwd) {
		// type ,id ,time , account , pwd
		String[] params = new String[]{id, time};
		Object[] realObj = new Object[]{params, account, pwd};
		WorkContext context = null;
		try {
			context =
					(WorkContext) Constants.matchEnum(type).getLoader().doWork(realObj);
		} catch (Exception e) {
			e.printStackTrace();
			return "error" + e.getLocalizedMessage();
		}
		return String.format("your work submit ok, submit result :%s",
				context);
	}

}
