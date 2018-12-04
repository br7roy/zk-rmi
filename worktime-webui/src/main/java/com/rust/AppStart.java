 /*
  * Package com.rust
  * FileName: AppStart
  * Author:   Rust
  * Date:     2018/8/4 1:11
  */
 package com.rust;

 import javax.xml.ws.*;

import com.rust.ws.service.submit.*;

 /**
  * FileName:    AppStart
  * Author:      Rust
  * Date:        2018/8/4
  * Description:
  */
 public class AppStart {
	 public static void main(String[] args) throws Exception {
		 String address = "http://10.28.11.159:9000/services/submitService";
		 ISubmitService submitService = new SubmitServiceImpl();
		 Endpoint.publish(address, submitService);
		 System.out.println("submit submit work start ok,please use wsimport use it or visit address: \r\n" + address+"?wsdl");


	 }
 }
