package com.rust.ws.service.submit;

import javax.jws.*;

import com.rust.jmx.bean.*;

/**
 *
 * @author Rust
 */
@WebService
public interface ISubmitService {

	@WebMethod
	RetBean submit(ReqBean reqBean)throws Exception;
}
