package com.rbs.retailtherapy.impl;

import com.rbs.retailtherapy.entity.BuyResponse;
import com.rbs.retailtherapy.entity.BuyShopParameters;
import com.rbs.retailtherapy.entity.BuyStockParameters;
import com.rbs.retailtherapy.entity.ClockCheck;
import com.rbs.retailtherapy.entity.JoinGameResponse;
import com.rbs.retailtherapy.entity.ParticipantGameParametersResponse;
import com.rbs.retailtherapy.entity.PlaceAdvertParameters;
import com.rbs.retailtherapy.entity.PlaceAdvertResponse;
import com.rbs.retailtherapy.entity.PlaceBlockerParameters;
import com.rbs.retailtherapy.entity.PlaceBlockerResponse;
import com.rbs.retailtherapy.entity.RequestShopResponse;
import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.entity.SelfStateResponse;
import com.rbs.retailtherapy.interfaces.ParticipantInterface;
import com.rbs.retailtherapy.model.ParticipantParameters;

public class ParticipantImpl implements ParticipantInterface {

	private final JsonHelper helper;

	public ParticipantImpl(String connectionUrl) {
		helper = new JsonHelper(connectionUrl);
	}

	public ClockCheck checkClock(ClockCheck arg) {
		return helper.processPostRequest("/checkClock", arg, ClockCheck.class);
	}

	public RoundStateResponse getRoundState() {
		return helper.processGetRequest("/getRoundState", RoundStateResponse.class);
	}

	public SelfStateResponse getSelfState(int arg) {
		String resource = "/getSelfState/" + arg;
		return helper.processGetRequest(resource, SelfStateResponse.class);
	}

	public JoinGameResponse joinGame(ParticipantParameters arg) {
		return helper.processPostRequest("/joinGame", arg, JoinGameResponse.class);
	}

	public PlaceAdvertResponse placeAdvert(PlaceAdvertParameters arg) {
		return helper.processPostRequest("/placeAdvert", arg, PlaceAdvertResponse.class);
	}

	public PlaceBlockerResponse placeBlocker(PlaceBlockerParameters arg) {
		return helper.processPostRequest("/placeBlocker", arg, PlaceBlockerResponse.class);
	}

	public RequestShopResponse requestShop(BuyShopParameters arg) {
		return helper.processPostRequest("/requestShop", arg, RequestShopResponse.class);
	}

	public void setConnectionDetails(String arg0, int arg1) {
	}

	public void setLogRequests(boolean arg0) {
	}

	public BuyResponse buyStock(BuyStockParameters arg) {
		return helper.processPostRequest("/buyStock", arg, BuyResponse.class);
	}

	public ParticipantGameParametersResponse getGameParameters() {
		return helper.processGetRequest("/getGameState", ParticipantGameParametersResponse.class);
	}
}
