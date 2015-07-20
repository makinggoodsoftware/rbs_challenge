package com.rbs.retailtherapy.logic.meta;

import com.google.gson.Gson;
import com.rbs.retailtherapy.domain.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class CustomersLoader {
    private final Gson gson;

    public CustomersLoader(Gson gson) {
        this.gson = gson;
    }

    public Map<Integer, Customer> loadCustomersFromCpFileName (String customersFileName){
        Map<Integer, Customer> customers = new HashMap<>();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(customersFileName);
        Reader reader = new InputStreamReader(in);
        CustomersFile customersFile = gson.fromJson(reader, CustomersFile.class);
        for (CustomerFileEntry customerFileEntry : customersFile.getShoppers()) {
            if (customerFileEntry.getStocks() == null){
                throw new IllegalStateException("This customer doesn't have a list of shopping items");
            }
            List<Coordinate> coordinateList = asCoordenatesList(customerFileEntry.getGridPositions());
            Customer customer = new Customer(
                    customerFileEntry.getId(),
                    customerFileEntry.getStocks(),
                    customerFileEntry.getInitialCash(),
                    coordinateList,
                    pathTypeFrom(coordinateList));
            customers.put(customerFileEntry.getId(), customer);
        }
        return customers;
    }

    private List<Coordinate> asCoordenatesList(String gridPositions) {
        List<Coordinate> coordinates= new ArrayList<>();
        String base = gridPositions.
                trim();
        String[] rawCoordinates = base.
                substring(1, base.length() - 1).
                replaceAll(" \\[", "[").
                split("\\]\\[");
        for (String rawCoordinate : rawCoordinates) {
            String[] coordinatesAsArray = rawCoordinate.split(",");
            if (coordinatesAsArray.length != 2){
                throw new IllegalStateException();
            }
            Integer col = Integer.valueOf(coordinatesAsArray[0]);
            Integer row = Integer.valueOf(coordinatesAsArray[1]);
            Coordinate coordinate = new Coordinate(col, row);
            coordinates.add(coordinate);
        }
        return coordinates;
    }

    private List<Direction> pathTypeFrom(List<Coordinate> coordinateList) {
        List<Direction> directions = new ArrayList<>();
        Coordinate initialCoordinate = coordinateList.get(0);
        Coordinate previousCoordinate = initialCoordinate;
        for (int i = 1; i< coordinateList.size(); i++){
            Coordinate nextCoordinate = coordinateList.get(i);
            Direction thisDirection = guessDirection(previousCoordinate, nextCoordinate);
            if (changesDirection(directions, thisDirection)){
                directions.add(thisDirection);
            }
            previousCoordinate = nextCoordinate;
        }
        return directions;
    }

    private boolean changesDirection(List<Direction> directions, Direction thisDirection) {
        if (directions.size() == 0) return true;

        return ! (directions.get(directions.size() -1) == thisDirection);
    }

    private Direction guessDirection(Coordinate initialCoordinate, Coordinate nextCoordinate) {
        if (Objects.equals(initialCoordinate.getCol(), nextCoordinate.getCol())){
            return Direction.HORIZONTAL;
        }

        if (Objects.equals(initialCoordinate.getRow(), nextCoordinate.getRow())){
            return Direction.VERTICAL;
        }

        return Direction.DIAGONAL;
    }
}
