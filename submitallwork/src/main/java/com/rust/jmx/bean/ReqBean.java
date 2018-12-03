package com.rust.jmx.bean;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * @author Rust
 */
public class ReqBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String account;
	private String pwd;
	private String workType;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ReqBean.class.getSimpleName() + "[", "]")
				.add("account='" + account + "'")
				.add("pwd='" + pwd + "'")
				.add("workType='" + workType + "'")
				.toString();
	}
}
