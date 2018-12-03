package com.rust.jmx.bean;

import java.io.*;

/**
 * @author Rust
 */
public class RetBean implements Serializable {
	private static final long serialVersionUID = 1;
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
}
