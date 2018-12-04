 package com.rust.zkcli;

 /**
  * @author Takho
  */
 public interface Constant {
	 String ZK_CONNECTION_STRING = "10.28.11.159:2181";
	 int ZK_SESSION_TIME_OUT = 5000;
	 String ZK_REGISTRY_PATH = "/registry";
	 String ZK_PROVIDER_PATH = ZK_REGISTRY_PATH + "/provider";
 }
