 /*
  * Package com.rust.zkha.client
  * FileName: Client
  * Author:   Takho
  * Date:     18/11/30 1:25
  */
 package com.rust.zkha.client;

 import com.rust.zkha.consumer.ServiceConsumer;
 import com.rust.zkha.service.HelloService;

 /**
  * @author Takho
  */
 public class Client {
  public static void main(String[] args) throws Exception {
   ServiceConsumer consumer = new ServiceConsumer();

   while (true) {
    HelloService helloService = consumer.lookup();
    String result = helloService.sayHello("Jack");
    System.out.println(result);
    Thread.sleep(3000);
   }
  }
 }
