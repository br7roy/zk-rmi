package com.rust.submit.loader;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.common.base.Strings;
import com.rust.submit.WorkContext;
import com.rust.submit.WorkContext.SingleSubmitParam;
import com.rust.submit.WorkException;
import com.rust.submit.WorkParam;
import com.rust.submit.support.WorkSupport;
import com.rust.submit.util.DateUtil;

import static com.rust.submit.Constants.SPLIT_LIMIT_HOURS;
import static com.rust.submit.util.ComputeUtil.minus;

/**
 * @author Rust
 */
@Slf4j
public class ServiceLoader implements AppLoader<String[], WorkContext> {


	private static final String URI = "http://badao.pinganfu.net/time_entries";


	private static ServiceLoader ONE = new ServiceLoader();

	private ServiceLoader() {
	}

	public static ServiceLoader getONE() {
		return ONE;
	}


	public WorkContext doWork(String[] args) throws Exception {
		WorkContext workContext = null;
			// demand current hour
			System.out.println("输入问题ID");
			Scanner scanner = new Scanner(new BufferedInputStream(System.in));
			String id = scanner.next();
			System.out.println("输入日期");
			String time = scanner.next();
			String[] param = new String[]{id, time};
		System.out.println("输入用户名");
		String account = scanner.next();
		System.out.println("输入密码");
		String password = scanner.next();
		String cookie = WorkSupport.fetchUserInfo(account, password);
			workContext = new WorkContext(System.currentTimeMillis(),
					param, ServiceTypeEnm.SINGLE,cookie);
			workContext.setSingleSubmitRet(runningWithTrigger(workContext));
		return workContext;
	}

	public Boolean runningWithTrigger(WorkContext context) {
		try {
		float[] floats = query(context);
			for (float aFloat : floats) {
				SingleSubmitParam trueArgs =
						WorkSupport.resolvingHourParamByContext(context,
								aFloat);
				submit(trueArgs);
			}
		} catch (Exception e) {
			log.error("work running fail" + e);
			return false;
		}
		return true;
	}


	private float[] query(WorkContext context) throws Exception {
		String time =
				WorkSupport.triggerTimeWithContext(context);
		String now = DateUtil.formatLocalDateTime8bit(LocalDateTime.now());
		String param = "utf8=%E2%9C%93&f%5B%5D=spent_on&op" +
				"%5Bspent_on%5D=%3D&f%5B%5D=user_id&op%5Buser_id%5D=%3D&v" +
				"%5Buser_id%5D%5B%5D=me&f%5B%5D=&c%5B%5D=project&c%5B%5D" +
				"=spent_on&c%5B%5D=user&c%5B%5D=activity&c%5B%5D=issue&c%5B%5D=comments&c%5B%5D=hours&v%5Bspent_on%5D%5B%5D="+now;

		HttpGet oriGet = new HttpGet(URI +
				"?" + param);
		URIBuilder newBuilder = new URIBuilder(oriGet.getURI());
		if (!Strings.isNullOrEmpty(time)) {
			newBuilder.setParameter("v[spent_on][]", time);
		}
		List<NameValuePair> params = newBuilder.getQueryParams();
		String str = EntityUtils.toString(new UrlEncodedFormEntity(params,
				Consts.UTF_8));
		HttpGet request = new HttpGet(URI + "?" + str);

		URIBuilder bb = new URIBuilder(request.getURI());
		//获取键值对列表
		List<NameValuePair> asd = bb.getQueryParams();
		//转换为键值对字符串
		String tempStr = EntityUtils.toString(new UrlEncodedFormEntity(asd,
				Consts.UTF_8));

		HttpGet realRequest = new HttpGet(URI + "?" + tempStr);
		realRequest.addHeader("Cookie", "autologin=09d7c2e22e061cc160ff4c6b3e257611c27459f5; sidebar_hide=hide; " +
				"_redmine_session" +
				"=UVFDNkZhTnBEZGZUWlFjeEZPVHlrbDJPc0dlQ25vTjA1WVhhWHpXZEcwY3EwbEJ2TGVubjVVbUlUd2xYYzA3TEwySUlsN3pQKzRDMVZ3Q1pzeVFnSmxDZ3ZkYnNhaFFKb3ZoNDFSamFaMHd2WW5nNUJCSHBBZS9JOGVYdTQ0VGowYWk3b0ZzbUNMN2dQNXJHSHJhQ2JGVHRlTE5EdDNHSXJHZ2ZEVHVIbmdieFZkYm05dkdHRmJWMEpGQU1UQUd6YjFONnMvYmdKaTN0bDJ6ZUxiUGJKcWQ5dGxLanNJVTlaWnVmc0xWTFltUGpWVXJVdVRJYVJYT2gwam1IeDBwNjZiRjJ0VXdWemhmdEt3aWIwTGZhYm0weWdEMU5VMUNZSDVkdEdNWjMvUzZVL01CaExYK0xVRERORG9hQzVNQkQ3RXFhOXQ4SG1GNVNLc1Y1K2JKa3Z4cnNZTTZESzZOQ2JvOG9WRVNva1JnVjBWRmVjUW4wcmhpb3pQeVUrTFVwTVdkM0Q4YjlnNThLaG83WFF3djQ5cG94Zm0rZVBlakpiUWJHa09oV2hZZmZMYUV3UGtEdDZPcHlmYW1JZndSNTJtaENYWkR0d0NsUkt4cEtQVkRrNlpvMVI0Si9IeUVQTmhWaStadXMybTdwcWFKcktrVlhLWnlNMzNFSjdxR0dYdy9pazBGSWxlcE9XVzJwbS9DUlp3c3oyUlc3VWI0eXdTR3Jwd0ZBaWlzUVEwanNYZnJZZ2xCS216NXZqWXVyOUxDZjZrcFZWdTlFQzF6RjJkWS9SQT09LS1zbTQ1QjlqMnhUMXNuOUxZenVCdm9RPT0%3D--d5f6e2c06f5b5a11c6c1f594e671e5609555facd");
		realRequest.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpResponse response = httpClient.execute(realRequest);
		StatusLine statusLine = response.getStatusLine();
		if (null == statusLine) {
			throw new RuntimeException("http request fail, no status line");
		}
		if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
			String string = EntityUtils.toString(response.getEntity());
			String sLine = "class=\"hours hours-int\"";

			int sIdx = string.indexOf(sLine) + sLine.length() + 1;
			int eIdx = string.indexOf("<", sIdx);

			String dLine = "class=\"hours hours-dec\"";
			int sIdx2 = string.indexOf(dLine) + dLine.length() + 1;
			int eIdx2 = string.indexOf("<", sIdx2);

			String hourI = string.substring(sIdx, eIdx);

			String hourDesc = string.substring(sIdx2, eIdx2);

			float wt = Float.valueOf(hourI + hourDesc);

			log.info("current_work_time:" + wt);

			// TODO:  do dynamic
			float[] ints = computeHours(wt);
			return ints;
		}

		throw new WorkException("httpResponse Code is not OK.");
	}

	private float[] computeHours(float wt) {
		float remain = minus(8, wt).floatValue();
		if (remain <= 0) {
			log.warn("no need to submit");
			return new float[0];
		}
		boolean trigger = true;
		float f;
		if ((f = remain % SPLIT_LIMIT_HOURS) == 0) {
			trigger = false;
		}

		int len = 0;
		if (remain <= 1) {
			len = 1;
		} else if (trigger) {
			len = (int) remain / 2 + 1;
		} else {
			len = (int) remain / 2;
		}
		float[] floats = new float[len];
		for (int i = 0; i < floats.length; i++) {
			if (i == floats.length - 1 && trigger) {
				floats[i] = new BigDecimal(f).setScale(2, RoundingMode.HALF_UP).floatValue();
			} else {
				floats[i] = SPLIT_LIMIT_HOURS;
			}
		}
		return floats;
	}

	public void submit(SingleSubmitParam args) throws IOException {
		WorkParam param = WorkSupport.buildSubmitWorkParam(args);
		HttpPost httpPost = new HttpPost(URI);
		httpPost.addHeader("Cookie", "autologin=09d7c2e22e061cc160ff4c6b3e257611c27459f5; " + "_redmine_session"
				+
				"=MXdSbDRUVG54MTVsVEc5Zy9vOEV5WmQxSy9MclpVUjQ3dDgyb2QvSlowYUpDYjlIbGhPNjdzUno0Y2cvWVg5UVFpbmpyZ1pyZVdjREZsYTN6NzdEeXBQOGg5dHJXS3h4ZUJSNFVvaFBnQjdab201Q3pEbmE4Tm9KNnhwbk8vdlJtNUx5dEhjcUZJLzZLRGhtb21nZ0JOZnAvOXJER3kyaFNPMUovRVZxOTdYMFBpUnpCdlZQMWJzR2hRZDFwVVQyVkQwcjZ5T1VCSWh4NFZaZkIwaCtDQjJlRUVLSlFoUDl1T1dWcWQxRG0vOHJHMExLcDhVbTdUVnFZRW1DeUtVeVZ6VnNVV0RaNk5VZmJaRUkrUEF0NmxPME5ZUDFWWUR6MFNLNzk5RlV5eDlnUUxzbHg1cnRHMWJtYmwvZGExdVJGem81dEc3T1ZXRXhIZlY4SUE2UkhnUHdzZG1ZcSswRVB6YkRWRW9MUHdnT1AwNzV0UFlsZTVoalM3ZkYxVXNNenV0dGFqVDloM2Yrd1Y5dUdNd3VzS3Qvam4rd0lrNGcxNE0xdlE5NjlYSDdqckFaSFRKU1BFaUx6djhRakVOeWRNUkpmN1FlYXI0WnYxZlg4TWhFTG5HanhNRUVEZ05lazA1WjN0aExYQlY4QmJXb2dPSytYVWltaVEzbWl4Nys3YlFSUmtjc09UU2FRVG9Fb1dLZU5BPT0tLXNmeHJOZjc0aGxhMnptdi9TQ3Vla3c9PQ%3D%3D--5d9e234049412526bb821daa34bf1e62c33fb240");

		CloseableHttpClient httpClient = HttpClients.createDefault();


		UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(param.getList(),
				StandardCharsets.UTF_8);
		httpPost.setEntity(urlEncodedFormEntity);

		CloseableHttpResponse response = httpClient.execute(httpPost);
		log.info("status code:" + response.getStatusLine().getStatusCode());
	}


public 	enum ServiceTypeEnm {

		SCHEDULE("1") {
			@Override
			public AppLoader getLoader() {
				return TimerTaskLoader.getONE();
			}
		},
		SINGLE("2") {
			@Override
			public AppLoader getLoader() {
				return ServiceLoader.getONE();
			}
		},
		;
		private String mode;

		ServiceTypeEnm(String mode) {
			this.mode = mode;
		}

		public abstract AppLoader getLoader();
	}
}
