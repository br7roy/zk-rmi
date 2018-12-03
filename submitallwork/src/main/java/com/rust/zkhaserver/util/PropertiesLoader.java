package com.rust.zkhaserver.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Rust
 */
public class PropertiesLoader {
	private static InputStream resourceAsStream = null;
	private static Properties properties = null;
	static {
		InputStream resourceAsStream =
				Thread.currentThread().getContextClassLoader().getResourceAsStream("env.properties");
		PropertiesLoader.resourceAsStream = resourceAsStream;
		load();
	}

	public static void load(){
		Properties properties = null;

		properties = new Properties();
		try {
			properties.load(resourceAsStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PropertiesLoader.properties = properties;
	}

	public static String getZkIp() {
		return properties.getProperty("zk.server.address");
	}

	public static int getZkPort() {
		return Integer.valueOf(properties.getProperty("zk.server.port"));
	}
}
