package com.rbs.retailtherapy.v2.ai;

import com.rbs.retailtherapy.v2.domain.Coordinate;
import com.rbs.retailtherapy.v3.Customer;
import com.rbs.retailtherapy.v2.domain.GameState;
import com.rbs.retailtherapy.v2.domain.Grid;
import com.rbs.retailtherapy.v2.service.GridService;

import java.awt.*;
import java.util.List;

public class Simulator {
    private final GridService gridService;

    public Simulator(GridService gridService) {
        this.gridService = gridService;
    }

    public GameState startSimulation (List<Customer> customers, Dimension dimension){
        Grid grid = gridService.create (dimension);
        for (Customer customer : customers) {
            Coordinate coordinateAtTurn = null;
            grid.get(coordinateAtTurn).setCustomer(customer);
        }
        return new GameState(grid);
    }
}
