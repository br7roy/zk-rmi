package com.rust.jmx.bean;

import java.io.*;
import java.util.*;

/**
 * @author Rust
 */
public class ReqBean implements Serializable {
  private static final long serialVersionUID = 1L;
  private String id;
  private String time;
  private String workType;
  private String account;
  private String pwd;
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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ReqBean.class.getSimpleName() + "[", "]")
            .add("id='" + id + "'")
            .add("time='" + time + "'")
            .add("workType='" + workType + "'")
            .add("account='" + account + "'")
            .add("pwd='" + pwd + "'")
            .toString();
  }
}
