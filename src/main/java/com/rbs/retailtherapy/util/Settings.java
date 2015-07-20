package com.rbs.retailtherapy.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {
	private static Properties config;
	
	public static void init() throws IOException {
		config = new Properties();
		InputStream input = new FileInputStream("config.properties");
		config.load(input);
	}
	
	public static boolean getBoolean(String property) {
		String value = config.getProperty(property);
		return Boolean.parseBoolean(value);
	}
	
	public static double getDouble(String property) {
		String value = config.getProperty(property);
		return Double.parseDouble(value);
	}

	public static float getFloat(String property) {
		String value = config.getProperty(property);
		return Float.parseFloat(value);
	}

	public static int getInt(String property) {
		String value = config.getProperty(property);
		return Integer.parseInt(value);
	}

	public static String getString(String property) {
		String value = config.getProperty(property);
		return value;
	}
}
