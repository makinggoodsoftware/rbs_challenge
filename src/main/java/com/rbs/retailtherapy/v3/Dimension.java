package com.rbs.retailtherapy.v3;

import com.google.common.base.Objects;

public class Dimension {
    private final Integer cols;
    private final Integer rows;

    public Dimension(Integer cols, Integer rows) {
        this.cols = cols;
        this.rows = rows;
    }

    public Dimension reduce(int i) {
        return new Dimension(cols - i, rows - i);
    }

    public Dimension shops() {
        return new Dimension(cols/2, rows/2);
    }

    public Integer perimeter() {
        return ((2 * (rows - 2)) + (2 *(cols - 2))) + 4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dimension)) return false;
        Dimension dimension = (Dimension) o;
        return Objects.equal(cols, dimension.cols) &&
                Objects.equal(rows, dimension.rows);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cols, rows);
    }
}