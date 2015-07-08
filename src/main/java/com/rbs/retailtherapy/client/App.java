package com.rbs.retailtherapy.client;

import org.apache.log4j.Logger;


public class App {

	private final static Logger logger = Logger.getLogger(App.class.getSimpleName());

	public static void main(String[] args) {
		String username = "Pulsy";
		String password = "testing";
		String baseUrl = "http://localhost:8081/RetailTherapy/jsonServices";
		try {
			MyAwesomeSolution client = new MyAwesomeSolution(baseUrl, gameAI);
			client.start(username, password);
		} catch (Exception e) {
			logger.error(e.getStackTrace()[1].getClass().getSimpleName() + ": " + e.getMessage());
		}
	}
}
