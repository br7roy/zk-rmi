 /*
  * Package com.rust
  * FileName: AppStart
  * Author:   Rust
  * Date:     2018/8/4 1:11
  */
 package com.rust;

 import javax.xml.ws.*;

import java.lang.Exception;
import java.util.*;

import com.rust.jmx.bean.RetBean;
import com.rust.ws.service.submit.*;
import com.rust.ws.service.submit.SubmitServiceImpl;
import com.rust.ws.service.wsimport.*;

 /**
  * FileName:    AppStart
  * Author:      Rust
  * Date:        2018/8/4
  * Description:
  */
 public class AppStart {
	 public static void main(String[] args) throws Exception {
		 String address = "http://localhost:9000/services/submitService";
		 ISubmitService submitService = new SubmitServiceImpl();
		 Endpoint.publish(address, submitService);
		 System.out.println("submit submit work start ok,please use wsimport use it or visit address: \r\n" + address+"?wsdl");

		 SubmitServiceImplService factory = new SubmitServiceImplService();
		 SubmitServiceImpl submitServiceImplPort = factory.getSubmitServiceImplPort();
		 Scanner scanner = new Scanner(System.in);
		 System.out.println("输入类型，1定时，2单次");
		 String type = scanner.next();
		 System.out.println("输入id,如果类型为2的话，必填");
		 String id = scanner.next();
		 System.out.println("输入时间，yyyy-MM-dd");
		 String time = scanner.next();
		 System.out.println("输入账号");
		 String account = scanner.next();
		 // 输入密码
		 System.out.println("输入密码");
		 String pwd = scanner.next();
		 com.rust.jmx.bean.ReqBean reqBean = new com.rust.jmx.bean.ReqBean();
		 reqBean.setWorkType(type);
		 reqBean.setId(id);
		 reqBean.setTime(time);
		 reqBean.setAccount(account);
		 reqBean.setPwd(pwd);
		 RetBean resResult = submitServiceImplPort.submit(reqBean);
		 System.out.println(resResult);



	 }
 }
