/**
 * 壹钱包
 * Copyright (c) 2013-2018 壹钱包版权所有.
 */
package com.rust.submit.call;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.google.common.collect.Lists;
import com.rust.submit.Constants.CallerTypeEnum;
import com.rust.submit.WorkException;
import com.rust.submit.util.HttpUtil;

/**
 *
 * @author Rust
 */
public class DefaultHttpCaller implements HttpCaller{


	private static final EnumMap<CallerTypeEnum, HttpGet> callerMap = new EnumMap<>(CallerTypeEnum.class);

	public DefaultHttpCaller() {
		this.putDelegeteCaller(CallerTypeEnum.QUERY_ISS_LIST,
				findCallerWithType(CallerTypeEnum.QUERY_ISS_LIST));
	}

	private HttpGet findCallerWithType(CallerTypeEnum queryIssList) {
		switch (queryIssList) {
			case QUERY_ISS_LIST:
				return HttpUtil.touchHttpGetFetchIssList();
				default:
					throw new WorkException("no comfortable http method found" +
							".");
		}
	}

	protected void putDelegeteCaller(CallerTypeEnum callName, HttpGet defination) {
		callerMap.putIfAbsent(callName, defination);
	}

	@Override
	public HttpGet getCallerByName(CallerTypeEnum callerTypeEnum) {
		HttpGet httpGet = callerMap.get(callerTypeEnum);
		if (httpGet == null) {
			throw new WorkException("caller not found");
		}
		return httpGet;
	}

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
