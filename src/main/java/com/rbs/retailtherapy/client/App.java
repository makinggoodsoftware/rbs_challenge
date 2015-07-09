package com.rbs.retailtherapy.client;

import com.rbs.retailtherapy.impl.HttpGameClient;
import com.rbs.retailtherapy.model.ParticipantParameters;
import org.apache.log4j.Logger;


public class App {

	private final static Logger logger = Logger.getLogger(App.class.getSimpleName());

	public static void main(String[] args) {
		String username = "Pulsy";
		String password = "testing";
		String baseUrl = "http://localhost:8081/RetailTherapy/jsonServices";
		try {
			ParticipantParameters credentials = new ParticipantParameters(username, password);
            HttpGameClient httpGameClient = new HttpGameClient(baseUrl);
            GameManager heartbeat = new GameManager(
                    httpGameClient,
                    new RoundManagerProvider(
                            new RoundManagerFactory(
                                    httpGameClient, credentials)
                    )
            );
            heartbeat.start();
		} catch (Exception e) {
			logger.error(e.getStackTrace()[1].getClass().getSimpleName() + ": " + e.getMessage());
		}
	}
}
