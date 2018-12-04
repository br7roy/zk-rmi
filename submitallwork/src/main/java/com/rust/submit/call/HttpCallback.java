package com.rust.submit.call;

import java.io.IOException;

import org.apache.http.HttpResponse;

import com.rust.submit.Constants.CallerTypeEnum;

/**
 * @author Rust
 */
public interface HttpCallback {
	HttpCallback DEFAULT = new DefaultHttpCallback();
	Object parseResult(HttpResponse response, CallerTypeEnum callerTypeEnum) throws IOException;
}
