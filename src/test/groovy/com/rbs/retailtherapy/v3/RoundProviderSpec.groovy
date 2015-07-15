package com.rbs.retailtherapy.v3

import com.rbs.retailtherapy.entity.RoundStateResponse
import spock.lang.Specification

class RoundProviderSpec extends Specification {
    RoundProvider testObj
    GameState gameStateMock = Mock (GameState)
    GameManager gameManagerMock =  Mock (GameManager)
    RoundMonitor roundManagerMock = Mock (RoundMonitor)
    RoundStateResponse stateMock = Mock(RoundStateResponse)

    def "setup" (){
        testObj = new RoundProvider(gameManagerMock, gameStateMock)
    }

    def "should create a new round if in new round" (){
        given:
        gameStateMock.getRoundManager (stateMock) >> null
        gameManagerMock.onNewRound (stateMock) >> roundManagerMock

        when:
        RoundMonitor result = testObj.tick (stateMock)

        then:
        result.is(roundManagerMock)
    }
}
