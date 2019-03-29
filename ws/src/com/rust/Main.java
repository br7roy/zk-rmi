

package com.rust;


import com.rust.ws.service.submit.Exception_Exception;
import com.rust.ws.service.submit.ReqBean;
import com.rust.ws.service.submit.RetBean;
import com.rust.ws.service.submit.SubmitServiceImpl;
import com.rust.ws.service.submit.SubmitServiceImplService;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception_Exception {
		// write your code here
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
		ReqBean reqBean = new ReqBean();
		reqBean.setWorkType(type);
		reqBean.setId(id);
		reqBean.setTime(time);
		reqBean.setAccount(account);
		reqBean.setPwd(pwd);
		RetBean resResult = submitServiceImplPort.submit(reqBean);
		System.out.println(resResult.getMsg());
	}
}