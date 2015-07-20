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

    private PathType pathTypeFrom(List<Coordinate> coordinateList) {
        List<Path> paths = new ArrayList<>();
        Coordinate initialCoordinate = coordinateList.get(0);
        Direction lastDirection = guessDirection(initialCoordinate, coordinateList.get(1));
        List<Coordinate> coordinatesForDirection = new ArrayList<>();
        coordinatesForDirection.add(initialCoordinate);

        Coordinate previousCoordinate = initialCoordinate;
        for (int i = 2; i< coordinateList.size(); i++){
            Coordinate nextCoordinate = coordinateList.get(i);
            Direction thisDirection = guessDirection(previousCoordinate, nextCoordinate);
            if (changesDirection(lastDirection, thisDirection)){
                Orientation orientation = orientation(lastDirection, coordinatesForDirection);
                paths.add(new Path(lastDirection, orientation, coordinatesForDirection));
                lastDirection = thisDirection;
                coordinatesForDirection = new ArrayList<>();
            }
            coordinatesForDirection.add(nextCoordinate);
            previousCoordinate = nextCoordinate;
        }
        Orientation orientation = orientation(lastDirection, coordinatesForDirection);
        paths.add(new Path(lastDirection, orientation, coordinatesForDirection));
        return new PathType(paths, isLoop (paths.get(0), paths.get(paths.size() - 1)));
    }

    private Orientation orientation(Direction lastDirection, List<Coordinate> coordinatesForDirection) {
        if (coordinatesForDirection.size() == 1) return Orientation.NONE;
        Coordinate from = coordinatesForDirection.get(0);
        Coordinate to = coordinatesForDirection.get(1);
        if (lastDirection == Direction.HORIZONTAL){
            return to.getCol() > from.getCol() ? Orientation.EAST : Orientation.WEST;
        } else if (lastDirection == Direction.VERTICAL) {
            return to.getRow() > from.getRow() ? Orientation.NORTH : Orientation.SOUTH;
        } else if (lastDirection == Direction.DIAGONAL) {
            return to.getRow() > from.getRow() ?
                    to.getCol() > from.getCol() ? Orientation.NORTH_EAST : Orientation.NORTH_WEST :
                    to.getCol() > from.getCol() ? Orientation.SOUTH_EAST : Orientation.SOUTH_WEST;
        }
        throw new IllegalStateException();
    }

    private boolean isLoop(Path initialPath, Path lastPath) {
        return initialPath.getCoordinates().get(0).equals(lastPath.getCoordinates().get(lastPath.getCoordinates().size() - 1));
    }

    private boolean changesDirection(Direction lastDirection, Direction thisDirection) {
        return lastDirection == null || !(lastDirection == thisDirection);

    }

    private Direction guessDirection(Coordinate initialCoordinate, Coordinate nextCoordinate) {
        if (Objects.equals(initialCoordinate.getCol(), nextCoordinate.getCol())){
            return Direction.VERTICAL;
        }

        if (Objects.equals(initialCoordinate.getRow(), nextCoordinate.getRow())){
            return Direction.HORIZONTAL;
        }

        return Direction.DIAGONAL;
    }
}
