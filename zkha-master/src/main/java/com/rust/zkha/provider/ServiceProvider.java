 /*
  * Package com.rust
  * FileName: ServiceProvider
  * Author:   Takho
  * Date:     18/11/30 0:46
  */
 package com.rust.zkha.provider;

 import com.rust.zkha.Constant;
 import org.apache.zookeeper.CreateMode;
 import org.apache.zookeeper.KeeperException;
 import org.apache.zookeeper.WatchedEvent;
 import org.apache.zookeeper.Watcher;
 import org.apache.zookeeper.Watcher.Event.KeeperState;
 import org.apache.zookeeper.ZooDefs.Ids;
 import org.apache.zookeeper.ZooKeeper;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;

 import java.io.IOException;
 import java.net.MalformedURLException;
 import java.rmi.Naming;
 import java.rmi.Remote;
 import java.rmi.RemoteException;
 import java.rmi.registry.LocateRegistry;
 import java.util.concurrent.CountDownLatch;

 /**
  * @author Takho
  */
 public class ServiceProvider {
	 private static final Logger LOGGER = LoggerFactory.getLogger(ServiceProvider.class);

	 private CountDownLatch latch = new CountDownLatch(1);

	 /**
	  * 发布 RMI服务并注册 RMI 地址到ZooKeeper 中
	  */
	 public void publish(Remote remote, String host, int port) {
		 String url = publishService(remote, host, port);// 发布RMI服务并返回RMI地址
		 if (url != null) {
			 ZooKeeper zk = connectServer(); // 连接ZooKeeper 服务器并获取ZooKeeper对象
			 if (zk != null) {
				 createZNode(zk, url); // 创建ZNode并将 RMI地址放入ZNode 上
			 }
		 }
	 }

	 private void createZNode(ZooKeeper zk, String url) {
		 try {
			 byte[] data = url.getBytes();
			 String path = zk.create(Constant.ZK_PROVIDER_PATH, data, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			 LOGGER.debug("create zookeeper node ({} => {})", path, url);
		 } catch (KeeperException | InterruptedException e) {
			 e.printStackTrace();
		 }

	 }

	 /**
	  * 连接ZK
	  *
	  * @return
	  */
	 private ZooKeeper connectServer() {
		 ZooKeeper zk = null;
		 try {
			 zk = new ZooKeeper(Constant.ZK_CONNECTION_STRING, Constant.ZK_SESSION_TIME_OUT, new Watcher() {
				 @Override
				 public void process(WatchedEvent event) {
					 if (event.getState() == KeeperState.SyncConnected) {
						 latch.countDown(); // 唤醒当前正在执行的线程
					 }
				 }
			 });
			 latch.await(); // 使当前线程处于等待状态
		 } catch (IOException | InterruptedException e) {
			 e.printStackTrace();
		 }

		 return zk;
	 }

	 /**
	  * 发布RMI服务并返回RMI地址
	  */
	 private String publishService(Remote remote, String host, int port) {
		 String url = null;
		 try {
			 url = String.format("rmi://%s:%d/%s", host, port, remote.getClass().getName());
			 LocateRegistry.createRegistry(port);
			 Naming.rebind(url, remote);
			 LOGGER.debug("publish rmi service (url: {})", url);
		 } catch (RemoteException | MalformedURLException e) {
			 LOGGER.error("", e);
		 }
		 return url;
	 }


 }
