package com.rbs.retailtherapy.v2.service;

import com.rbs.retailtherapy.v2.domain.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class CoordinatesService {
    public List<Coordinate> coordinates(String gridPositions) {
        String[] coordinatesAsString = explode(gridPositions);
        return coordinates(coordinatesAsString);
    }

    private List<Coordinate> coordinates(String[] coordinatesAsString) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (String coordinateAsString : coordinatesAsString) {
            coordinates.add(coordinate(coordinateAsString));
        }
        return coordinates;
    }

    private Coordinate coordinate(String coordinateAsString) {
        String[] split = coordinateAsString.split(",");
        return new Coordinate(
                Integer.valueOf(split[0]),
                Integer.valueOf(split[1])
        );
    }

    private String[] explode(String gridPositions) {
        return gridPositions.substring(1, gridPositions.length() - 2).replaceAll(" ", "").split("\\]\\[");
    }
}
