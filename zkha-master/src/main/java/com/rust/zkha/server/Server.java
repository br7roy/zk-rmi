 /*
  * Package com.rust.zkha.server
  * FileName: Server
  * Author:   Takho
  * Date:     18/11/30 1:18
  */
 package com.rust.zkha.server;

 import com.rust.zkha.provider.ServiceProvider;
 import com.rust.zkha.service.HelloService;
 import com.rust.zkha.service.HelloServiceImpl;

 import java.rmi.RemoteException;
 import java.util.concurrent.TimeUnit;

 /**
  * @author Takho
  */
 public class Server {
  public static void main(String[] args) throws InterruptedException, RemoteException {
   if (args.length != 2) {
    System.err.println("please using command: java Server <rmi_host> <rmi_port>");
    System.exit(-1);
   }

   String host = args[0];
   int port = Integer.parseInt(args[1]);

   ServiceProvider provider = new ServiceProvider();

   HelloService helloService = new HelloServiceImpl();
   provider.publish(helloService, host, port);

   TimeUnit.DAYS.sleep(1);
  }
 }
