package com.rust.submit.support;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.apache.http.Header;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.rust.submit.Constants;
import com.rust.submit.LoginParam;
import com.rust.submit.WorkContext;
import com.rust.submit.WorkContext.SingleSubmitParam;
import com.rust.submit.WorkException;
import com.rust.submit.WorkParam;
import com.rust.submit.util.DateUtil;

import static com.rust.submit.Constants.Vip.VIP;

/**
 *
 * @author  Takho
 */
@Slf4j
public class WorkSupport {

	public static SingleSubmitParam resolvingHourParamByContext(WorkContext workContext,
																float floats) {
		String[] params = workContext.getParams();
		SingleSubmitParam singleSubmitParam = new SingleSubmitParam();
		switch (workContext.getServiceTypeEnm()) {
			case SINGLE:
				//
				singleSubmitParam.setIssId(params[0]);
				singleSubmitParam.setSpendOn(params[1]);
				singleSubmitParam.setHour(String.valueOf(floats));
				return singleSubmitParam;
			case SCHEDULE:
				//
				List<String> singleSubmitList =
						workContext.getSingleSubmitList();
				singleSubmitParam.setSpendOn(DateUtil.formatLocalDateTime8bit(LocalDateTime.now()));
				String issId = shuffleListAndGetRandomOne(singleSubmitList);
				singleSubmitParam.setIssId(issId);
				singleSubmitParam.setHour(String.valueOf(floats));
				return singleSubmitParam;
			default:
				throw new WorkException("not such serviceType:" + workContext.getServiceTypeEnm());
		}
	}

	public static <T>T shuffleListAndGetRandomOne(List<T> singleSubmitList) {
		Collections.shuffle(singleSubmitList);
		int i = ThreadLocalRandom.current().nextInt(singleSubmitList.size());
		return singleSubmitList.get(i);
	}
	public static  WorkParam buildSubmitWorkParam(SingleSubmitParam args) {
		return WorkParam.newBuilder().addId(args.getIssId()).addHours(args.getHour()).addSpent_on(args.getSpendOn()).build();
	}

	public static String triggerTimeWithContext(WorkContext context) {
		switch (context.getServiceTypeEnm()) {
			case SCHEDULE:
				return DateUtil.formatLocalDateTime8bit(LocalDateTime.now());

			case SINGLE:
				if (context.getParams() != null && !Strings.isNullOrEmpty(context.getParams()[1])) {
					return context.getParams()[1];
				} else if (context.getParams() != null && Strings.isNullOrEmpty(context.getParams()[1])) {
					return Constants.ISSUE_ID;
				} else {
					log.error("should not be here ever");
					throw new RuntimeException("params is null");
				}
			default:
				throw new RuntimeException("Service type cannot be " +
						"trigger:" + context.getServiceTypeEnm());
		}
	}

	public static String fetchUserInfo(String account,
									   String password)throws Exception {
		boolean flg = checkIfVip();
		if (flg){
			System.out.println("欢迎，VIP！");
			return VIP.cookie;
		}
		String cookie = doLogin(account, password);
		return cookie;
	}

	public static void main(String[] args) throws Exception {
		doLogin("futanghang004", "fth19ooOO");
	}
	private static String doLogin(String account, String password) throws Exception{

		// String pc = doPreLogin();


		String loginUrl ="http://badao.pinganfu" +
				".net/login";

		LoginParam param = WorkSupport.buildLoginParam(account,password);


		HttpPost httpPost = new HttpPost(loginUrl);
		// httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
/*		httpPost.setHeader("Accept-Encoding", "gzip, deflate");
		httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
		httpPost.setHeader("Cache-Control","max-age=0");
		httpPost.setHeader("Connection", "keep-alive");
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");*/
		httpPost.setHeader("Cookie","K2lkNEdrRmpndWxHNGJxWGYwVjRRaWZPT0V4ZFJrQk1oSFFvQWErYlorMnNWbFc1WDhCYUx6bDNwaTJEZE5ibk03cEp3bGR4dHg0N2xsRnNiNTZWZjllVldRdXJITVlyRG5yWnlJNGpPV2pOZjAyamNjMTRFM1c5N1BDM1JMWndCc29ZM082b0EwWFpFYi9vbE5rRWZ3NzhhVXhBVzl0ZkE4OE1iNXRjZzRlZGpST282OUMyblJ0eXAvRU1BSVFELS0wS1dOTi8wcW5Fd0NTQTRYWEJUN2hRPT0%3D--fb2ce026785659fc3563a608beda1304c903932d");
/*		httpPost.setHeader("Host", "badao.pinganfu.net");
		httpPost.setHeader("Origin", "http://badao.pinganfu.net");

		httpPost.setHeader("Pragma", "no-cache");
		httpPost.setHeader("Upgrade-Insecure-Requests", "1");
		httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");*/

		CloseableHttpClient httpClient = HttpClients.createDefault();
		UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(param.getList(),
				StandardCharsets.UTF_8);
		   JSONObject jsonParam = new JSONObject();
		    jsonParam.put("username", "futanghang004");
		     jsonParam.put("password", "fth19ooOO");
		jsonParam.put("authenticity_token", "PWFsGMP65zUnUMvQTWvP6FqKehfIdfTAfa2whcDhnNbzzLClogoUS/vNZN59o4w5E8C7R1sYzGmtmXhMvxhyVA==");
		jsonParam.put("back_url", "http://badao.pinganfu.net/login?back_url=http://badao.pinganfu.net/");
		jsonParam.put("utf8", "✓");
		jsonParam.put("login", "登录 »");
		StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/x-www-form-urlencoded");
		httpPost.setEntity(urlEncodedFormEntity);

		CloseableHttpResponse response = httpClient.execute(httpPost);
		Header[] headers = response.getHeaders("Set-Cookie");
		for (Header header : headers) {
			System.out.println(header.getName() + "::" + header.getValue());
		}
		return null;
	}

	private static String doPreLogin() throws  Exception{
		HttpGet oriGet = new HttpGet("http://badao.pinganfu.net/login?back_url=http%3A%2F%2Fbadao.pinganfu.net%2F");
		CloseableHttpClient httpClient= HttpClients.createDefault();
		CloseableHttpResponse response = httpClient.execute(oriGet);
		String pc =
				Stream.of(response.getAllHeaders()).filter(header -> header.getName().equals("Set-Cookie")).findFirst().get().getValue();

		return pc;
	}

	private static LoginParam buildLoginParam(String account, String password) {
		return LoginParam.newBuilder().addUserName(account).addPassword(password).build();
	}

	private static boolean checkIfVip() throws Exception {
		InetAddress inetAddress = InetAddress.getLocalHost();
		String hostAddress = inetAddress.getHostAddress();
		return VIP.ip.equals(hostAddress);
	}

}
