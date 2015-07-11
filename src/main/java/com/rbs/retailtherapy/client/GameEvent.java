package com.rbs.retailtherapy.client;

public class GameEvent {
    private GameEventType gameEventType;
    private Expectation expectation;

    public GameEvent(GameEventType gameEventType, Expectation expectation) {
        this.gameEventType = gameEventType;
        this.expectation = expectation;
    }

    public GameEventType getType() {
        return gameEventType;
    }

    public Expectation getExpectation() {
        return expectation;
    }
}
