package com.rbs.retailtherapy.client;

import com.rbs.retailtherapy.entity.RequestShopResponse;
import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.v3.client.HttpGameSession;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class RoundManager {
    private static final Logger LOGGER = Logger.getLogger(RoundManager.class.getSimpleName());

    private final KnowledgeProvider knowledgeProvider;
    private final ArtificialIntelligence ai;
    private final HttpGameSession httpGameSession;
    private final ExecutorService executorService;

    public RoundManager(ArtificialIntelligence ai, KnowledgeProvider knowledgeProvider, HttpGameSession httpGameSession, ExecutorService executorService) {
        this.knowledgeProvider = knowledgeProvider;
        this.ai = ai;
        this.httpGameSession = httpGameSession;
        this.executorService = executorService;
    }

    public void onRoundStart(RoundStateResponse roundState) {
        String msg = "***************************************************";
        String msg1 = "Starting new round: " + roundState.getRoundId();
        String msg2 = "Current state is: " + roundState.getCurrentGameState();
        String msg3 = "***************************************************";
        LOGGER.info(
                System.getProperty("line.separator") +
                        msg + System.getProperty("line.separator") +
                        msg1 + System.getProperty("line.separator") +
                        msg2 + System.getProperty("line.separator") +
                        msg3
        );
    }

    private void onNewRound(final HttpGameSession httpGameSession) {
        LOGGER.info("Trying to find out how much a shop costs");
        for (int i = 1; i<=5; i=i+2){
            for (int j = 1; j<=5; j=j+2){
                final int col = i;
                final int row = j;

                executorService.execute(new Runnable() {
                    public void run() {
                        LOGGER.info("Requesting shop " + col + " - " + row);
                        RequestShopResponse requestShopResponse = httpGameSession.requestShop(60, col, row);
                        if (requestShopResponse.getIsSuccess()){
                            knowledgeProvider.addExpectation(new Expectation(ExpectationType.SHOP_BID, row, col));
                        } else {
                            LOGGER.info("Shop denied!");
                        }
                    }
                });
            }
        }

    }

    public void tick(RoundStateResponse roundState) {
        List<GameEvent> events = knowledgeProvider.update(roundState, httpGameSession.getSelfState());
        for (GameEvent event : events) {
            LOGGER.info("Processing event " + event.getType());
            if (event.getType() == GameEventType.NEW_ROUND){
                onNewRound(httpGameSession);
                return;
            }

            if (event.getType() == GameEventType.SHOP_ALLOCATED){
                knowledgeProvider.addExpectation(event.getExpectation());
            }
        }
    }
}
