package com.rbs.retailtherapy.v3.domain;

import com.google.common.base.Objects;

public class Coordinate {
    private final Integer col;
    private final Integer row;

    public Coordinate(Integer col, Integer row) {
        this.col = col;
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public Integer getRow() {
        return row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        Coordinate that = (Coordinate) o;
        return Objects.equal(getCol(), that.getCol()) &&
                Objects.equal(getRow(), that.getRow());
    }

    public Coordinate adjust(int deltaCol, int deltaRow) {
        return new Coordinate(col + deltaCol, row + deltaRow);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCol(), getRow());
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "col=" + col +
                ", row=" + row +
                '}';
    }
}
