package com.rust.submit.call;

import org.apache.http.client.methods.HttpGet;

import com.rust.submit.Constants.CallerTypeEnum;

/**
 *
 * @author Rust
 */
public interface HttpCaller {


	HttpGet getCallerByName(CallerTypeEnum queryIssList);


}
