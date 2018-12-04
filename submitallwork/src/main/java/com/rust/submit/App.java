package com.rust.submit;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import com.rust.RMIService;
import com.rust.jmx.server.JmxServerService;
import com.rust.submit.util.ThreadUtil;
import com.rust.zkcli.ServiceProvider;
import com.rust.zkhaserver.Constant;
import com.rust.zkhaserver.RMIServiceImpl;


/**
 * @author Rust
 */
@Slf4j
public class App {
	public static void main(String[] args) throws Exception {

		CountDownLatch latch = new CountDownLatch(2);

		ThreadUtil.getEs().execute(() -> {
			// start JMX
			boolean ret = JmxServerService.ONE.start();
			if (!ret) {
				System.exit(-1);
				log.info("fail to start JmxServer");
				JmxServerService.ONE.stop();
			}
			latch.countDown();
		});

		ThreadUtil.getEs().execute(() -> {
			//	publish rmi service to zk server
			ServiceProvider provider = new ServiceProvider();
			RMIService rmiService = null;
			try {
				rmiService = new RMIServiceImpl();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			provider.publish(rmiService, Constant.ZK_IP, Constant.ZK_PORT);
			log.info("publish server -> zk , ok !");
			latch.countDown();
		});

		// add shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread(ThreadUtil::shutdownAll));

		latch.await();

		System.out.println("v3.0增加随机选举ID,可选日期         \r\n输入运行模式的数字编号		" + "	" + "	" + "\r\n" + "1,启动定时任务	" + "				\r\n" + "2,启动单次任务提交");
		Scanner scanner = new Scanner(new BufferedInputStream(System.in));
		String option = scanner.next();

		System.out.println("输入问题ID");
		String id = scanner.next();
		System.out.println("输入日期");
		String time = scanner.next();
		String[] param = new String[]{id, time};
		System.out.println("输入用户名");
		String account = scanner.next();
		System.out.println("输入密码");
		String password = scanner.next();
		Object[] objects = {param, account, password};

		//noinspection unchecked
		WorkContext context =
				(WorkContext) Constants.matchEnum(option).getLoader().doWork(objects);
		System.out.println("\r\n------>work submit result :" + context.isSingleSubmitRet() + "<------");
/*		while (1 == 1) {

		}*/
	}
}
