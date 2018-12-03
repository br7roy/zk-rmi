package com.rust.jmx.mbean;

import com.rust.jmx.bean.ReqBean;
import com.rust.jmx.bean.RetBean;
import com.rust.submit.Constants;
import com.rust.submit.WorkContext;

/**
 * @author Rust
 */
public class AppCtrlImpl implements AppCtrl {

	@Override
	public RetBean submit(ReqBean reqBean) throws Exception {
		WorkContext context =
				(WorkContext) Constants.matchEnum(reqBean.getWorkType()).getLoader().doWork(new String[]{reqBean.getWorkType()});
		RetBean retBean = new RetBean();
		return retBean;
	}

	@Override
	public String test(String param) throws Exception {
		return "good";
	}
}
