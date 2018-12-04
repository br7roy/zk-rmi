package com.rust.submit.loader;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

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
import static com.rust.submit.Constants.VIP_COOKIE;
import static com.rust.submit.util.ComputeUtil.minus;

/**
 * @author Rust
 */
@Slf4j
public class ServiceLoader implements AppLoader<Object[], WorkContext> {


	private static final String URI = "http://badao.pinganfu.net/time_entries";


	private static ServiceLoader ONE = new ServiceLoader();

	private ServiceLoader() {
	}

	public static ServiceLoader getONE() {
		return ONE;
	}


	public WorkContext doWork(Object[] args) throws Exception {
		WorkContext workContext = null;
			// demand current hour
		String cookie =	WorkSupport.doLogin((String) args[1], (String) args[2]);
			workContext = new WorkContext(System.currentTimeMillis(), (String[]) args[0], ServiceTypeEnm.SINGLE,cookie);
		workContext.setCookie(cookie);
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
				submit(trueArgs,context);
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
		String cookie = context.getCookie();
		realRequest.addHeader("Cookie", cookie);
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

	public void submit(SingleSubmitParam args,WorkContext workContext) throws IOException {
		WorkParam param = WorkSupport.buildSubmitWorkParam(args);
		HttpPost httpPost = new HttpPost(URI);
		String cookie = workContext.getCookie();
		httpPost.addHeader("Cookie", VIP_COOKIE);

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
