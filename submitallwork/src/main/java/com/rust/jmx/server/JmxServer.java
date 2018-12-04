 /*
  * Package com.rust.jmx.server
  * FileName: JmxServer
  * Author:   Rust
  * Date:     2018/8/3 23:27
  */
 package com.rust.jmx.server;

 import javax.management.MBeanServer;
 import javax.management.ObjectName;
 import javax.management.remote.JMXConnectorServer;
 import javax.management.remote.JMXConnectorServerFactory;
 import javax.management.remote.JMXServiceURL;
 import lombok.extern.slf4j.Slf4j;

 import java.io.IOException;
 import java.lang.management.ManagementFactory;
 import java.rmi.registry.LocateRegistry;
 import java.rmi.registry.Registry;
 import java.util.Arrays;
 import java.util.Map;
 import java.util.Map.Entry;

 /**
  * FileName:    JmxServer
  * Author:      Rust
  * Date:        2018/8/3
  * Description:
  */
 @Slf4j
 public class JmxServer {

	 private final int rmiRegistryPort;

	 private final int rmiPort;

	 private final String rmiRegistryName;

	 private final Map<String, Object> env;

	 private final Map<String, Object> mbName;

	 private final String jmxServiceUrl;

	 private JMXConnectorServer jmxConnectorServer;

	 private Registry registry;


	 public JmxServer(int rmiRegistryPort, String rmiRegistryName, int rmiPort
			 , Map<String, Object> mbMap) {
		 this(rmiRegistryPort, rmiRegistryName, rmiPort, null, mbMap);
	 }

	 public JmxServer(int rmiRegistryPort, String rmiRegistryName, int rmiPort
			 , Map<String, Object> env, Map<String, Object> mbMap) {
		 this.rmiRegistryPort = rmiRegistryPort;
		 this.rmiRegistryName = rmiRegistryName;
		 this.rmiPort = rmiPort;
		 this.env = env;
		 this.mbName = mbMap;
		 jmxServiceUrl = "service:jmx:rmi://localhost:" + this.rmiPort +
				 "/jndi/rmi://localhost:" + this.rmiRegistryPort + "/" + this.rmiRegistryName;
		 log.info("JmxServer.JmxServer.url=" + jmxServiceUrl);

	 }


	 public boolean start() {

		 try {
			 registry = LocateRegistry.createRegistry(rmiRegistryPort);

			 MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

			 for (Entry<String, Object> mbEntry : mbName.entrySet()) {
				 String mbName = mbEntry.getKey();
				 Object mbObject = mbEntry.getValue();
				 ObjectName mbObjectName = new ObjectName(mbName);
				 mbs.registerMBean(mbObject, mbObjectName);
			 }

			 JMXServiceURL jmxServiceURL = new JMXServiceURL(jmxServiceUrl);

			 jmxConnectorServer =
					 JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceURL, this.env, mbs);

			 jmxConnectorServer.start();

			 log.info("start JmxServer is ok.");

			 return true;
		 } catch (Exception e) {
			 log.info("fail to start JmxServer." + e);
		 }
		 return false;
	 }

	 public void stop() {
		 try {
			 if (registry != null) {
				 System.out.println("registry address:" + Arrays.asList(registry.list()));
			 }
			 if (jmxConnectorServer != null) {
				 jmxConnectorServer.stop();
			 }
			 System.out.println("stop JmxServer is ok.");
		 } catch (IOException e) {
			 System.err.print("fail to stop JmxServer." + e);
		 }
	 }
 }
