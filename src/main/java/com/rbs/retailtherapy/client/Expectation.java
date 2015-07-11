package com.rbs.retailtherapy.client;

/**
 * Created by esthertorras on 10/07/2015.
 */
public class Expectation {
    private final ExpectationType expectationType;
    private final int row;
    private final int column;

    public Expectation(ExpectationType expectationType, int row, int column) {
        this.expectationType = expectationType;
        this.row = row;
        this.column = column;
    }

    public ExpectationType getExpectationType() {
        return expectationType;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
