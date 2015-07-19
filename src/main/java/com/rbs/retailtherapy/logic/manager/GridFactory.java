package com.rbs.retailtherapy.logic.manager;

import com.rbs.retailtherapy.domain.Cell;
import com.rbs.retailtherapy.domain.Coordinate;
import com.rbs.retailtherapy.domain.Grid;
import com.rbs.retailtherapy.entity.ShopResponse;
import com.rbs.retailtherapy.model.GridCellWrapper;

import java.util.HashMap;
import java.util.Map;

public class GridFactory {
    public Grid from(GridCellWrapper[] allGrids, Map<Coordinate, ShopResponse> myShops) {
        Map<Coordinate, Cell> cells = new HashMap<>();
        for (GridCellWrapper gridCell : allGrids) {
            Coordinate cellCoordinate = new Coordinate(gridCell.getCol(), gridCell.getRow());
            Cell cell = new Cell(gridCell.getStatus(), myShops.get(cellCoordinate) != null);
            cells.put(cellCoordinate, cell);
        }
        return new Grid(cells);
    }
}
