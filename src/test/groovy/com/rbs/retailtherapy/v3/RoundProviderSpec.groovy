package com.rbs.retailtherapy.v3

import com.rbs.retailtherapy.entity.RoundStateResponse
import com.rbs.retailtherapy.v3.domain.GameState
import com.rbs.retailtherapy.v3.logic.clock.RoundMonitor
import com.rbs.retailtherapy.v3.logic.clock.RoundProvider
import com.rbs.retailtherapy.v3.logic.manager.GameManager
import spock.lang.Specification

class RoundProviderSpec extends Specification {
    RoundProvider testObj
    GameState gameStateMock = Mock (GameState)
    GameManager gameManagerMock =  Mock (GameManager)
    RoundMonitor roundMonitorMock = Mock (RoundMonitor)
    RoundStateResponse stateMock = Mock(RoundStateResponse)

    def "setup" (){
        testObj = new RoundProvider(gameManagerMock, gameStateMock)
    }

    def "should create a new round if in new round" (){
        given:
        gameStateMock.getRoundMonitor (stateMock) >> null
        gameManagerMock.onNewRound (stateMock) >> roundMonitorMock

        when:
        RoundMonitor result = testObj.retrieve (stateMock)

        then:
        result.is(roundMonitorMock)
    }
}
