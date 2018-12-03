/**
 * 壹钱包
 * Copyright (c) 2013-2018 壹钱包版权所有.
 */
package com.rust.submit.loader;

/**
 * @author Rust
 */
public interface AppLoader<T,F> {
	 F doWork(T t) throws Exception;
}
