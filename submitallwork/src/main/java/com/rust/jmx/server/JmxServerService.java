 /*
  * Package com.rust.jmx.server
  * FileName: JmxServerService
  * Author:   Rust
  * Date:     2018/8/3 23:27
  */
 package com.rust.jmx.server;

 import java.util.Collections;
 import java.util.Map;

 import com.rust.jmx.mbean.AppCtrl;
 import com.rust.jmx.mbean.AppCtrlImpl;


 /**
  * FileName:    JmxServerService
  * Author:      Rust
  * Date:        2018/8/3
  * Description:
  */
 public class JmxServerService {

	 public static JmxServerService ONE = new JmxServerService();

	 private final String jmxIp = "localhost";

	 private final JmxServer jmxServer;

	 private final int jmxPort = 50079;

	 private final String registryName = "sun$rmi";

	 private final String objectName;

	 public JmxServerService() {

		 objectName = "jmx:name=appCtrl";
		 AppCtrl appCtrl = new AppCtrlImpl();
		 Map<String, Object> mbMap = Collections.singletonMap(objectName, appCtrl);
		 jmxServer = new JmxServer(jmxPort, registryName, jmxPort, mbMap);

	 }


	 public boolean start() {
		 return jmxServer.start();
	 }

	 public void stop() {
		 jmxServer.stop();
	 }

	 public JmxServer getJmxServer() {
		 return jmxServer;
	 }

	 public int getJmxPort() {
		 return jmxPort;
	 }

	 public String getRegistryName() {
		 return registryName;
	 }

	 public String getJmxIp() {
		 return jmxIp;
	 }

	 public String getObjectName() {
		 return objectName;
	 }
 }
