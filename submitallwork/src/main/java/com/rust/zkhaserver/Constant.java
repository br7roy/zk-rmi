 /*
  * Package com.rust.zkha.util
  * FileName: Constant
  * Author:   Takho
  * Date:     18/11/30 0:58
  */
 package com.rust.zkhaserver;

 import com.rust.zkhaserver.util.PropertiesLoader;

 /**
  * @author Takho
  */
 public interface Constant {
	 String ZK_CONNECTION_STRING = "10.28.11.159:2181";
	 int ZK_SESSION_TIME_OUT = 5000;
	 String ZK_REGISTRY_PATH = "/registry";
	 String ZK_PROVIDER_PATH = ZK_REGISTRY_PATH + "/provider";
	 String ZK_IP = PropertiesLoader.getZkIp();
	 int ZK_PORT = PropertiesLoader.getZkPort();
 }
