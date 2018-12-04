package com.rust.submit.call;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.common.collect.Lists;
import com.rust.submit.Constants.CallerTypeEnum;

/**
 * @author Rust
 */
public class DefaultHttpCallback implements HttpCallback{
	@Override
	public Object parseResult(HttpResponse response,
							  CallerTypeEnum callerTypeEnum) throws IOException {
		String string = EntityUtils.toString(response.getEntity());
		List<String> list = Lists.newArrayList();
		StringBuilder sb = new StringBuilder();
		sb.append(string);
		String[] split = string.split("\n");
		int start = 0;
		for (String s : split) {
			start =
					sb.indexOf("<a href=\"/issues/") + "<a href=\"/issues/".length();
			if (start < 0)
				continue;
			String res = sb.substring(start, start + 6);
			if (!res.matches("[0-9]{6}"))
				break;
			sb.delete(0, start);
			list.add(res);
		}
		return list;
	}
}
