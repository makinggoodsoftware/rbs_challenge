package com.rbs.retailtherapy.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHelper {

	private final String baseUrl;
	private final Gson gson;
	private final static Logger logger = Logger.getLogger(JsonHelper.class.getName());

	public JsonHelper(String baseUrl) {
		this.baseUrl = baseUrl;
		this.gson = new GsonBuilder().create();
	}

	public <T> T processGetRequest(String url, Class<T> clazz) {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(baseUrl + url);
		T result = null;
		String json = null;
		try {
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new IOException(response.getStatusLine().toString());
			}
			json = readResponse(response.getEntity().getContent());
			// System.out.println("Result in JSON: " + clazz.getSimpleName() +
			// ": " + json);
			result = gson.fromJson(json, clazz);
		} catch (IOException e) {
			logger.error(this.getClass().getSimpleName() + ": " + getStackTrace(e));
		}
		return result;
	}

	public <T, P> T processPostRequest(String url, P arg, Class<T> clazz) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(baseUrl + url);
		T result = null;
		try {
			if (arg != null) {
				String message = gson.toJson(arg);
				// System.out.println("Message out in JSON: " +
				// clazz.getSimpleName() + " - "
				// + message);
				post.setHeader("Content-Type", "application/json; charset=utf-8");
				post.setEntity(new StringEntity(message));
			}
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new IOException(response.getStatusLine().toString());
			}
			String json = readResponse(response.getEntity().getContent());
			// System.out.println("Result in JSON: " + clazz.getSimpleName() +
			// ": " + json);
			result = gson.fromJson(json, clazz);
		} catch (IOException e) {
			logger.error(this.getClass().getSimpleName() + ": " + getStackTrace(e));
		}
		return result;
	}

	private String readResponse(InputStream inputStream) throws IOException {
		StringBuilder responseText = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while ((line = reader.readLine()) != null) {
			responseText.append(line);
		}
		return responseText.toString();
	}

	public static String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}

}
