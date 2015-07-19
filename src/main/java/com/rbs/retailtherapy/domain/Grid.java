package com.rbs.retailtherapy.domain;

import java.util.Map;

public class Grid {
    private final Map<Coordinate, Cell> cells;

    public Grid(Map<Coordinate, Cell> cells) {
        this.cells = cells;
    }

    public Cell getCell(Coordinate coordinate) {
        return cells.get(coordinate);
    }
}
