package com.rust.submit.support;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.apache.http.Header;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.common.base.Strings;
import com.rust.submit.Constants;
import com.rust.submit.LoginParam;
import com.rust.submit.WorkContext;
import com.rust.submit.WorkContext.SingleSubmitParam;
import com.rust.submit.WorkException;
import com.rust.submit.WorkParam;
import com.rust.submit.util.DateUtil;

import static com.rust.submit.Constants.VIP_COOKIE;
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
	public static String doLogin(String account, String password) throws Exception{
		if ("VIP".equals(account)) {
			return VIP_COOKIE;
		}
		SimpleEntry<String, String> remindToken = doPreLogin();


		String loginUrl ="http://badao.pinganfu" +
				".net/login";

		LoginParam param = WorkSupport.buildLoginParam(account,password,
				remindToken.getValue());


		HttpPost httpPost = new HttpPost(loginUrl);
		// httpPost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		httpPost.setHeader("Accept-Encoding", "gzip, deflate");
		httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
		httpPost.setHeader("Cache-Control","max-age=0");
		httpPost.setHeader("Connection", "keep-alive");
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httpPost.setHeader("Cookie",
				"SL_GWPT_Show_Hide_tmp=1; "+remindToken.getKey()+"SL_wptGlobTipTmp=1");
	httpPost.setHeader("Host", "badao.pinganfu.net");
		httpPost.setHeader("Origin", "http://badao.pinganfu.net");

		httpPost.setHeader("Pragma", "no-cache");
		httpPost.setHeader("Upgrade-Insecure-Requests", "1");
		httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");

		CloseableHttpClient httpClient = HttpClients.createDefault();
		UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(param.getList(),
				StandardCharsets.UTF_8);
/*		   JSONObject jsonParam = new JSONObject();
		    jsonParam.put("username", "futanghang004");
		     jsonParam.put("password", "fth19ooOO");
		jsonParam.put("authenticity_token", remindToken.getValue());
		jsonParam.put("back_url", "http://badao.pinganfu.net/");
		jsonParam.put("utf8", "✓");
		jsonParam.put("autologin", "1");
		jsonParam.put("login", "登录 »");
		StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/x-www-form-urlencoded");
		httpPost.setEntity(urlEncodedFormEntity);*/



		/////////////////////////////

		httpPost.setEntity(urlEncodedFormEntity);





		CloseableHttpResponse response = httpClient.execute(httpPost);
		Header[] headers = response.getHeaders("Set-Cookie");
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Header header : headers) {
				String value = header.getValue();
				int end = value.indexOf(";");
				String va = value.substring(0, end);
				sb.append(va);
			if (i == 0) {
				sb.append("; ");
				sb.append("sidebar_hide=hide; ");
				i++;
			}
			System.out.println(header.getName() + "::" + header.getValue());
		}
		String tick = sb.toString();
		System.out.println(tick);
		return tick;
	}

	private static SimpleEntry<String,String> doPreLogin() throws  Exception{
		HttpGet oriGet = new HttpGet("http://badao.pinganfu.net/login");
		CloseableHttpClient httpClient= HttpClients.createDefault();
		CloseableHttpResponse response = httpClient.execute(oriGet);
		String pc =
				Stream.of(response.getAllHeaders()).filter(header -> header.getName().equals("Set-Cookie")).findFirst().get().getValue();

		String remind = pc.substring(0, pc.indexOf("path=/; HttpOnly"));
		response.getEntity();
		String string = EntityUtils.toString(response.getEntity());
		int start = string.indexOf("<meta name=\"csrf-token\" content=\"") +
				"<meta name=\"csrf-token\" content=\"".length();
		int end = string.indexOf("\" />", start);
		String token = string.substring(start, end);

		return new SimpleEntry<>(remind, token);
	}

	private static LoginParam buildLoginParam(String account, String password,
											  String token) {
		return LoginParam.newBuilder().addUserName(account).addPassword(password).addToken(token).build();
	}

	private static boolean checkIfVip() throws Exception {
		InetAddress inetAddress = InetAddress.getLocalHost();
		String hostAddress = inetAddress.getHostAddress();
		return VIP.ip.equals(hostAddress);
	}

	public static String touchToken()  {
		SimpleEntry<String, String> stringStringSimpleEntry = null;
		try {
			stringStringSimpleEntry = doPreLogin();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringStringSimpleEntry.getValue();
	}
}
