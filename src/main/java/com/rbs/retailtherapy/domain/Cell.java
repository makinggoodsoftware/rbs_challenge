package com.rbs.retailtherapy.domain;

import com.rbs.retailtherapy.model.CellStatus;

public class Cell {
    private final CellStatus type;
    private final boolean mine;

    public Cell(CellStatus type, boolean mine) {
        this.type = type;
        this.mine = mine;
    }

    public CellStatus getType() {
        return type;
    }

    public boolean isMine() {
        return mine;
    }
}
