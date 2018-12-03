package com.rust.submit.call;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import com.rust.submit.Constants.CallerTypeEnum;

/**
 *
 * @author Rust
 */
public interface HttpCaller {

	HttpCaller DEFAULT = new DefaultHttpCaller();

	HttpGet getCallerByName(CallerTypeEnum queryIssList);

	Object parseResult(HttpResponse response, CallerTypeEnum callerTypeEnum) throws IOException;

}
