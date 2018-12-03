package com.rust.ws.service.submit;

import javax.jws.*;

import java.util.*;

import com.rust.*;
import com.rust.jmx.bean.*;
import com.rust.jmx.mbean.*;
import com.rust.jmx.util.*;
import com.rust.jmx.workder.*;
import com.rust.zkcli.*;

/**
 * @author Rust
 */
@WebService
public class SubmitServiceImpl implements ISubmitService {
	@Override
	public RetBean submit(ReqBean reqBean) throws Exception {
		System.out.println("this is invoke,param:" + reqBean);
		// invokeWithJMX(reqBean);
		RetBean retBean = invokeWithRMI(reqBean);
		return retBean;
	}

	private RetBean invokeWithRMI(ReqBean reqBean) {
		RetBean retBean = new RetBean();
		ServiceConsumer serviceConsumer = new ServiceConsumer();
		RMIService lookup = serviceConsumer.lookup();
		String msg = null;
		try {
			msg = lookup.submit(reqBean.getWorkType(), reqBean.getId(), reqBean.getTime(), reqBean.getAccount(),reqBean.getPwd());
		} catch (Exception e) {
			e.printStackTrace();
		}
		retBean.setCode(2);
		retBean.setMsg(msg);
		return retBean;
	}

	private RetBean invokeWithJMX(ReqBean reqBean) throws Exception {
		Map<String, Object> pMap = new HashMap<>();
		// 使用appClassLoader.
		pMap.put("jmx.remote.x.client.connection.check.period", 0);
		// 这里模拟了一台本地机器，可以配置多个集群ip,数据库，缓存，zk
		List<WorkerInfomation> workers = WorkerInfomation.touchWorkers();
		RetBean submit = new RetBean();
		WorkerInfomation work = workers.get(0);
		AppCtrl appCtrl = JmxClientTools.makeRMIProxy(work.getJmxIp(), work.getJmxPort(), "sun$rmi", "jmx:name" +
						"=appCtrl",
				AppCtrl.class, pMap);
		try {
			submit = appCtrl.submit(reqBean);
		} catch (Exception e) {
			System.err.print("fail to invoke by RMI" + e);
			throw new Exception(e);
		}
		return submit;
	}
}
