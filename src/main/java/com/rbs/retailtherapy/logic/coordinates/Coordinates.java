package com.rbs.retailtherapy.logic.coordinates;

import com.rbs.retailtherapy.domain.Coordinate;

import java.util.*;

public class Coordinates {
    public List<Coordinate> sortSquare(Set<Coordinate> toSort, Coordinate topLeft, Coordinate bottomRight) {
        Coordinate topRight = new Coordinate(bottomRight.getCol(), topLeft.getRow());
        Coordinate bottomLeft = new Coordinate(topLeft.getCol(), bottomRight.getRow());
        if (
                !toSort.contains(topLeft) ||
                        !toSort.contains(bottomRight) ||
                        !toSort.contains(topRight) ||
                        !toSort.contains(bottomLeft)
                ) {
            throw new IllegalStateException();
        }

        List<Coordinate> topMiddleCoordinates = new ArrayList<>();
        List<Coordinate> rightMiddleCoordinates = new ArrayList<>();
        List<Coordinate> bottomMiddleCoordinates = new ArrayList<>();
        List<Coordinate> leftMiddleCoordinates = new ArrayList<>();

        for (Coordinate toSortCoordinate : toSort) {
            if (
                    !toSortCoordinate.equals(topLeft) &&
                    !toSortCoordinate.equals(topRight) &&
                    !toSortCoordinate.equals(bottomLeft) &&
                    !toSortCoordinate.equals(bottomRight)
            ) {
                if (Objects.equals(toSortCoordinate.getRow(), topLeft.getRow())){
                    topMiddleCoordinates.add(toSortCoordinate);
                } else if (Objects.equals(toSortCoordinate.getRow(), bottomLeft.getRow())){
                    bottomMiddleCoordinates.add(toSortCoordinate);
                } else if (Objects.equals(toSortCoordinate.getCol(), bottomLeft.getCol())){
                    leftMiddleCoordinates.add(toSortCoordinate);
                } else if (Objects.equals(toSortCoordinate.getCol(), topRight.getCol())){
                    rightMiddleCoordinates.add(toSortCoordinate);
                }
            }
        }

        List<Coordinate> squareCoordinates = new ArrayList<>();
        squareCoordinates.add(topLeft);
        squareCoordinates.addAll(leftToRight(topMiddleCoordinates));
        squareCoordinates.add(topRight);
        squareCoordinates.addAll(topToBottom(rightMiddleCoordinates));
        squareCoordinates.add(bottomRight);
        squareCoordinates.addAll(rightToLeft(bottomMiddleCoordinates));
        squareCoordinates.add(bottomLeft);
        squareCoordinates.addAll(bottomToTop(leftMiddleCoordinates));
        return squareCoordinates;
    }

    public Collection<? extends Coordinate> topToBottom(List<Coordinate> toSort) {
        return vertical(toSort, 1);

    }

    public Collection<? extends Coordinate> bottomToTop(List<Coordinate> toSort) {
        return vertical(toSort, -1);

    }

    public Collection<? extends Coordinate> leftToRight(List<Coordinate> toSort) {
        return horizontal(toSort, -1);
    }

    public Collection<? extends Coordinate> rightToLeft(List<Coordinate> toSort) {
        return horizontal(toSort, 1);
    }

    private Collection<? extends Coordinate> vertical(List<Coordinate> toSort, final int onLeftRowIsSmaller) {
        ArrayList<Coordinate> list = new ArrayList<>(toSort);
        Collections.sort(list, new Comparator<Coordinate>() {
            @Override
            public int compare(Coordinate left, Coordinate right) {

                if (left.getRow() < right.getRow()) return onLeftRowIsSmaller;
                if (left.getRow() > right.getRow()) return onLeftRowIsSmaller * -1;
                return 0;
            }
        });

        return list;
    }

    private Collection<? extends Coordinate> horizontal(List<Coordinate> toSort, final int onLeftColIsSmaller) {
        ArrayList<Coordinate> list = new ArrayList<>(toSort);
        Collections.sort(list, new Comparator<Coordinate>() {
            @Override
            public int compare(Coordinate left, Coordinate right) {

                if (left.getCol() < right.getCol()) return onLeftColIsSmaller;
                if (left.getCol() > right.getCol()) return onLeftColIsSmaller * -1;
                return 0;
            }
        });

        return list;
    }

    public List<Coordinate> adjustCoordinates(List<Coordinate> toAdjust, int deltaCol, int deltaRow) {
        List<Coordinate> adjustedCoordinates = new ArrayList<>();
        for (Coordinate toAdjustIter : toAdjust) {
            adjustedCoordinates.add(toAdjustIter.adjust(deltaCol, deltaRow));
        }
        return adjustedCoordinates;
    }
}
