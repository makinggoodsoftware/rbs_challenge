package com.rbs.retailtherapy.v3;

import com.google.common.base.Objects;

import java.util.HashSet;
import java.util.Set;

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

    public Set<Coordinate> outterRingCoordinates(){
        Set<Coordinate> ring = new HashSet<>();
        ring.addAll(lineToEast(top()));
        ring.addAll(lineToSouth(right()));
        ring.addAll(lineToWest(bottom()));
        ring.addAll(lineToNorth(left()));
        return ring;
    }

    private Integer right() {
        return rows - 1;
    }

    private Integer top() {
        return rows - 1;
    }

    private Integer left() {
        return 0;
    }

    private Integer bottom() {
        return 0;
    }

    private Set<Coordinate> lineToEast(Integer onRow) {
        return horizontalLine(onRow);
    }

    private Set<Coordinate> lineToSouth(Integer onCol) {
        return verticalLine(onCol);
    }

    private Set<Coordinate> lineToWest(Integer onRow) {
        return horizontalLine(onRow);
    }

    private Set<Coordinate> lineToNorth(Integer onCol) {
        return verticalLine(onCol);
    }

    private Set<Coordinate> horizontalLine(Integer onRow) {
        HashSet<Coordinate> coordinates = new HashSet<>();
        for (int i = 0; i < cols; i++){
            coordinates.add(new Coordinate(i, onRow));
        }
        return coordinates;
    }

    private Set<Coordinate> verticalLine(Integer onCol) {
        Set<Coordinate> coordinates = new HashSet<>();
        for (int i = 0; i < rows; i++){
            coordinates.add(new Coordinate(onCol, i));
        }
        return coordinates;
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
