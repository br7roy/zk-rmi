package com.rust.jmx.bean;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * @author Rust
 */
public class RetBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", RetBean.class.getSimpleName() + "[",
				"]").add("code=" + code).add("msg='" + msg + "'").toString();
	}
}
