/**
 * 壹钱包
 * Copyright (c) 2013-2018 壹钱包版权所有.
 */
package com.rust.submit.call;

import java.util.EnumMap;

import org.apache.http.client.methods.HttpGet;

import com.rust.submit.Constants.CallerTypeEnum;
import com.rust.submit.WorkException;
import com.rust.submit.util.HttpUtil;

/**
 *
 * @author Rust
 */
public class DefaultHttpCaller implements HttpCaller{


	private static final EnumMap<CallerTypeEnum, HttpGet> callerMap = new EnumMap<>(CallerTypeEnum.class);

	public DefaultHttpCaller(String cookie) {
		this.putDelegeteCaller(CallerTypeEnum.QUERY_ISS_LIST,
				findCallerWithType(CallerTypeEnum.QUERY_ISS_LIST,cookie));
	}

	private HttpGet findCallerWithType(CallerTypeEnum queryIssList,
									   String cookie) {
		switch (queryIssList) {
			case QUERY_ISS_LIST:
				return HttpUtil.touchHttpGetFetchIssList(cookie);
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




}
