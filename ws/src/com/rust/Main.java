

package com.rust;


import java.util.Scanner;

import com.rust.ws.service.submit.*;

public class Main {

    public static void main(String[] args) throws Exception_Exception {
	// write your code here
        SubmitServiceImplService factory = new SubmitServiceImplService();
        SubmitServiceImpl submitServiceImplPort = factory.getSubmitServiceImplPort();
        Scanner scanner = new Scanner(System.in);
        System.out.println("�������ͣ�1��ʱ��2����");
        String type = scanner.next();
        System.out.println("����id,�������Ϊ2�Ļ�������");
        String id = scanner.next();
        System.out.println("����ʱ�䣬yyyy-MM-dd");
        String time = scanner.next();
        System.out.println("�����˺�");
        String account = scanner.next();
        // ��������
        System.out.println("��������");
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