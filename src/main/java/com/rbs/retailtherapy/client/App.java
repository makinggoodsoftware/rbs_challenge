package com.rbs.retailtherapy.client;

import com.rbs.retailtherapy.impl.ParticipantImpl;
import com.rbs.retailtherapy.model.ParticipantParameters;
import org.apache.log4j.Logger;


public class App {

	private final static Logger logger = Logger.getLogger(App.class.getSimpleName());

	public static void main(String[] args) {
		String username = "Pulsy";
		String password = "testing";
		String baseUrl = "http://localhost:8081/RetailTherapy/jsonServices";
		try {
			ParticipantParameters participantParameter = new ParticipantParameters(username, password);
			MyAwesomeSolution client = new MyAwesomeSolution(baseUrl, new GameManager(new ParticipantImpl(baseUrl), participantParameter, baseUrl));
			client.start(username, password);
		} catch (Exception e) {
			logger.error(e.getStackTrace()[1].getClass().getSimpleName() + ": " + e.getMessage());
		}
	}
}
