 /*
  * Package com.rust.zkha.service
  * FileName: HelloServiceImpl
  * Author:   Takho
  * Date:     18/11/30 1:22
  */
 package com.rust.zkha.service;

 import java.rmi.RemoteException;
 import java.rmi.server.UnicastRemoteObject;

 /**
  * @author Takho
  */
 public class HelloServiceImpl extends UnicastRemoteObject implements HelloService {
  public HelloServiceImpl() throws RemoteException {
  }

  @Override
  public String sayHello(String name) throws RemoteException {
   return String.format("Hello %s", name);
  }
 }
