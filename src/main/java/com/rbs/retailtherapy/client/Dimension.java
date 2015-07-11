package com.rbs.retailtherapy.client;

public class Dimension {
    private final int numberOfRows;
    private final int numberOfCols;

    public Dimension(int numberOfRows, int numberOfCols) {
        this.numberOfRows = numberOfRows;
        this.numberOfCols = numberOfCols;
    }

    public int indexOf(int row, int column) {
        return (column * numberOfCols + row) - 1;
    }
}
