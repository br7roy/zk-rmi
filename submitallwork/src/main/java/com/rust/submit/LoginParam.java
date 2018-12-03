package com.rust.submit;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.google.common.collect.Lists;

/**
 * @author Takho
 */
public class LoginParam {
	private String username;//
	private String password;//
	// private String autologin;
	private String authenticity_token;
	private String back_url;
	private String utf8;
	private String login;
	private List<BasicNameValuePair> list;

	public LoginParam(LoginParamBuilder builder) {
		list = Lists.newArrayList();
		BasicNameValuePair	bnv = new BasicNameValuePair("username", builder.username);
		list.add(bnv);
		bnv = new BasicNameValuePair("password", builder.password);
		list.add(bnv);
		bnv = new BasicNameValuePair("username", builder.username);
		list.add(bnv);
		bnv = new BasicNameValuePair("authenticity_token", builder.authenticity_token);
		list.add(bnv);
		bnv = new BasicNameValuePair("back_url", builder.back_url);
		list.add(bnv);
		bnv = new BasicNameValuePair("utf8", builder.utf8);
		list.add(bnv);
		bnv = new BasicNameValuePair("login", builder.login);
		list.add(bnv);
	}
	public static LoginParamBuilder newBuilder() {
		return new LoginParamBuilder();
	}

	public List<BasicNameValuePair> getList() {
		return list;
	}

	public static class LoginParamBuilder {
		private String username;//
		private String password;//
		// private String autologin;
		private String authenticity_token;
		private String back_url;
		private String utf8;
		private String login;

		public LoginParamBuilder addUserName(String username) {
			this.username = username;
			return this;
		}

		public LoginParamBuilder addPassword(String password) {
			this.password = password;
			return this;
		}


		public LoginParam build() {
			// autologin = StringUtils.defaultIfBlank(autologin, "1");
			authenticity_token = "PWFsGMP65zUnUMvQTWvP6FqKehfIdfTAfa2whcDhnNbzzLClogoUS/vNZN59o4w5E8C7R1sYzGmtmXhMvxhyVA==";
			back_url = "http://badao.pinganfu.net/login?back_url=http://badao.pinganfu.net/";
			utf8 = "✓";
			login = "登录 »";
			return new LoginParam(this);
		}
	}
}
