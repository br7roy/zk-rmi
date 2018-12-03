package com.rust.submit;

import java.io.BufferedInputStream;
import java.rmi.RemoteException;
import java.util.Scanner;

import com.rust.jmx.server.JmxServerService;
import com.rust.submit.util.ThreadUtil;
import com.rust.zkhaserver.Constant;
import com.rust.zkhaserver.RMIService;
import com.rust.zkhaserver.RMIServiceImpl;
import com.rust.zkhaserver.provider.ServiceProvider;


/**
 * @author Rust
 */
public class App {
	public static void main(String[] args) throws Exception {

		ThreadUtil.getEs().execute(() -> {
			// start JMX
			boolean ret = JmxServerService.ONE.start();
			if (!ret) {
				System.exit(-1);
				System.out.println("fail to start JmxServer");
				JmxServerService.ONE.stop();
			}
		});

		ThreadUtil.getEs().execute(() -> {
			//	start zk server
			ServiceProvider provider = new ServiceProvider();
			RMIService rmiService = null;
			try {
				rmiService = new RMIServiceImpl();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			provider.publish(rmiService, Constant.ZK_IP, Constant.ZK_PORT);
			System.out.println("publish server -> zk , ok .");
		});

		// add shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread(ThreadUtil::shutdownAll));

		System.out.println("v3.0增加随机选举ID,可选日期         \r\n输入运行模式的数字编号		" + "	" + "	" + "\r\n" + "1,启动定时任务	" + "				\r\n" + "2,启动单次任务提交");
		Scanner scanner = new Scanner(new BufferedInputStream(System.in));
		String option = scanner.next();
		//noinspection unchecked
		WorkContext context =
				(WorkContext) Constants.matchEnum(option).getLoader().doWork(new String[]{option});
		System.out.println("\r\n------>work submit result :" + context.isSingleSubmitRet() + "<------");
/*		while (1 == 1) {

		}*/
	}
}
