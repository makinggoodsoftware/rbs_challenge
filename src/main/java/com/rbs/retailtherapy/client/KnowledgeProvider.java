package com.rbs.retailtherapy.client;

import com.rbs.retailtherapy.entity.RoundStateResponse;
import com.rbs.retailtherapy.entity.SelfStateResponse;
import com.rbs.retailtherapy.entity.ShopResponse;
import com.rbs.retailtherapy.model.CellStatus;
import com.rbs.retailtherapy.model.GridCellWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.rbs.retailtherapy.client.GameEventType.*;

public class KnowledgeProvider {
    private Knowledge currentKnowledge = null;
    private List<Expectation> expectations = Collections.synchronizedList(new ArrayList<Expectation>());

    public List<GameEvent> update(RoundStateResponse roundState, SelfStateResponse selfStateResponse) {
        List<GameEvent> events = new ArrayList<GameEvent>();
        if (currentKnowledge == null){
            currentKnowledge = onFirstUpdate(roundState, events);
        }

        processExpectations(roundState, selfStateResponse, events);
        return events;
    }

    private void processExpectations(RoundStateResponse roundState, SelfStateResponse selfStateResponse, List<GameEvent> events) {
        for (Expectation expectation : expectations) {
            int row = expectation.getRow();
            int column = expectation.getColumn();
            int cellIndex = currentKnowledge.getDimension().indexOf(row, column);
            GridCellWrapper currentGrid = roundState.getGridCells() [cellIndex];
            if (currentGrid.getStatus() == CellStatus.AllocatedForShop){
                events.add(new GameEvent(SHOP_ALLOCATED, expectation));
            } else if (currentGrid.getStatus() == CellStatus.Shop){
                ShopResponse[] shops = selfStateResponse.getShops();
                if (doIHaveAShopIn(shops, row, column)){
                    events.add(new GameEvent(SHOP_OWNED, expectation));
                } else {
                    events.add(new GameEvent(SHOP_STOLEN, expectation));
                }
            } else {
                events.add(new GameEvent(SHOP_REQUEST_IGNORED, expectation));
            }
        }
        expectations = Collections.synchronizedList(new ArrayList<Expectation>());
    }

    private boolean doIHaveAShopIn(ShopResponse[] shops, int row, int column) {
        for (ShopResponse shop : shops) {
            if (shop.getPosition().getRow() == row && shop.getPosition().getCol() == column){
                return true;
            }
        }
        return false;
    }

    private Knowledge onFirstUpdate(RoundStateResponse roundState, List<GameEvent> events) {
        events.add(new GameEvent(NEW_ROUND, null));
        Dimension dimension = findDimension(roundState.getGridCells());
        return new Knowledge(dimension, roundState);
    }

    private Dimension findDimension(GridCellWrapper[] gridCells) {
        int numberOfCols = 0;
        for (GridCellWrapper gridCell : gridCells) {
            numberOfCols++;
            if (gridCell.getRow() > 0){
                break;
            }
        }
        int numberOfRows = gridCells.length / numberOfCols;
        return new Dimension(numberOfRows, numberOfCols);
    }

    public Knowledge retrieve() {
        return currentKnowledge;
    }

    public void addExpectation(Expectation expectation) {
        expectations.add(expectation);
    }
}
