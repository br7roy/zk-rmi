package com.rust.submit.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import static com.rust.submit.Constants.VIP_COOKIE;


/**
 * @author Rust
 */
public class HttpUtil {
	public static final String FETCH_ISS_LIST_URI = "http://badao.pinganfu" +
			".net/issue_badge/load_badge_contents";

	public static HttpGet touchHttpGetFetchIssList(String cookie) {
		String param = "utf8=%E2%9C%93&f%5B%5D=spent_on&op%5Bspent_on%5D=t&f" +
				"%5B%5D=user_id&op%5Buser_id%5D=%3D&v" + "%5Buser_id%5D%5B%5D" +
				"=me&f%5B%5D=&c%5B%5D=project&c%5B%5D=spent_on&c%5B%5D=user&c" +
				"%5B%5D=activity&c%5B" + "%5D=issue&c%5B%5D=comments&c%5B%5D" +
				"=hours";
		HttpGet request = new HttpGet(FETCH_ISS_LIST_URI + "?" + param);
		request.addHeader("Cookie", cookie);
		return request;
	}

	public static void main(String[] args) throws Exception {
		HttpGet httpGet = touchHttpGetFetchIssList(VIP_COOKIE);
		CloseableHttpClient aDefault = HttpClients.createDefault();
		HttpResponse response = aDefault.execute(httpGet);

		String string = EntityUtils.toString(response.getEntity());
		System.out.println(string);

		StringBuilder sb = new StringBuilder();
		sb.append(string);
		String[] split = string.split("\n");
		int start = 0;
		int end = 0;
		for (String s : split) {
			start =
					sb.indexOf("<a href=\"/issues/") + "<a href=\"/issues/".length();
			if (start < 0)
				continue;
			String res = sb.substring(start, end = start + 6);
			if (!res.matches("[0-9]{6}"))
				break;
			sb.delete(0, start);
			System.out.println(res);
		}


	}
}
