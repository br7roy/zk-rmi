 /*
  * Package com.rust
  * FileName: HelloService
  * Author:   Takho
  * Date:     18/11/30 1:20
  */
 package com.rust.zkha.service;

 import java.rmi.Remote;
 import java.rmi.RemoteException;

 /**
  * @author Takho
  */
 public interface HelloService extends Remote {
  String sayHello(String name) throws RemoteException;
 }

