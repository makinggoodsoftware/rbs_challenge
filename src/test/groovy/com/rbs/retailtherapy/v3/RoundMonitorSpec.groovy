package com.rbs.retailtherapy.v3

import com.rbs.retailtherapy.entity.RoundStateResponse
import com.rbs.retailtherapy.v3.domain.BidStatus
import com.rbs.retailtherapy.v3.domain.RoundState
import com.rbs.retailtherapy.v3.logic.clock.RoundMonitor
import com.rbs.retailtherapy.v3.logic.manager.RoundManager
import com.rbs.retailtherapy.v3.logic.manager.RoundStateFactory
import spock.lang.Specification

class RoundMonitorSpec extends Specification {
    RoundMonitor testObj
    RoundState previousRoundStateMock = Mock(RoundState)
    RoundState roundStateMock = Mock(RoundState)
    RoundState processedRound = Mock(RoundState)
    RoundStateFactory roundStateFactoryMock = Mock (RoundStateFactory)
    RoundStateResponse httpStateMock = Mock(RoundStateResponse)
    RoundManager roundManagerMock = Mock (RoundManager)

    def "setup" (){
        testObj = new RoundMonitor(roundStateFactoryMock, roundManagerMock)
    }

    def "if game is not opened" (){
        given:
        roundStateFactoryMock.from (httpStateMock, null) >> roundStateMock
        roundStateMock.isBiddingOpen >> false
        roundStateMock.isTradeOpen >> false
        roundStateMock.bidStatus >> BidStatus.NOT_BID

        when:
        RoundState result = testObj.tick(httpStateMock, null)

        then:
        1 * roundManagerMock.onWaitingForGameToStart(roundStateMock) >> processedRound
        result.is(processedRound)
    }


    def "if never bade for shops previously, bidding is open, and trading is closed, bid only" (){
        given:
        roundStateFactoryMock.from (httpStateMock, null) >> roundStateMock
        roundStateMock.isBiddingOpen() >> true
        roundStateMock.isTradeOpen() >> false
        roundStateMock.bidStatus >> BidStatus.NOT_BID

        when:
        RoundState result = testObj.tick(httpStateMock, null)

        then:
        1 * roundManagerMock.onBiddingOpened(roundStateMock) >> processedRound
        result.is(processedRound)
    }

    def "if waiting for bids, and all the bids are inconclusive, wait" (){
        given:
        roundStateFactoryMock.from (httpStateMock, previousRoundStateMock) >> roundStateMock
        roundStateMock.isBiddingOpen() >> true
        roundStateMock.isTradeOpen() >> false
        roundStateMock.bidStatus >> BidStatus.BID_SENT

        when:
        RoundState result = testObj.tick(httpStateMock, previousRoundStateMock)

        then:
        1 * roundManagerMock.onWaitingForFirstBidResult(roundStateMock) >> processedRound
        result.is(processedRound)
    }

    def "if waiting for bids, and all the bids are completed, notify" (){
        given:
        roundStateFactoryMock.from (httpStateMock, previousRoundStateMock) >> roundStateMock
        roundStateMock.isBiddingOpen() >> true
        roundStateMock.isTradeOpen() >> false
        roundStateMock.bidStatus >> BidStatus.BID_COMPLETE

        when:
        RoundState result = testObj.tick(httpStateMock, previousRoundStateMock)

        then:
        1 * roundManagerMock.onBiddingCompleted(roundStateMock) >> processedRound
        result.is(processedRound)
    }

    def "if trading is opened, cotinue trading" (){
        given:
        roundStateFactoryMock.from (httpStateMock, previousRoundStateMock) >> roundStateMock
        roundStateMock.isBiddingOpen() >> true
        roundStateMock.isTradeOpen() >> true
        roundStateMock.bidStatus >> BidStatus.BID_COMPLETE

        when:
        RoundState result = testObj.tick(httpStateMock, previousRoundStateMock)

        then:
        1 * roundManagerMock.onTrading(roundStateMock) >> processedRound
        result.is(processedRound)
    }


}
