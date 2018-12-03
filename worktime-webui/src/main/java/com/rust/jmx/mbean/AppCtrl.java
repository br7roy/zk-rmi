 /*
  * Package com.rust.jmx.mbean
  * FileName: AppCtrl
  * Author:   Rust
  * Date:     2018/8/3 23:30
  */
 package com.rust.jmx.mbean;

 import com.rust.jmx.bean.*;

 /**
  * FileName:    AppCtrl
  * Author:      Rust
  * Date:        2018/8/3
  * Description:
  */
 // @MXBean
 public interface AppCtrl {

  RetBean submit(ReqBean reqBean) throws Exception;

  String test(String param) throws Exception;
 }
