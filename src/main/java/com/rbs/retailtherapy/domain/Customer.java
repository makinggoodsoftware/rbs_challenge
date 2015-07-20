package com.rbs.retailtherapy.domain;

import com.rbs.retailtherapy.model.Stock;

import java.util.List;

public class Customer {
    private final Integer id;
    private final List<Stock.StockType> shoppingList;
    private final Double initialCash;
    private final List<Coordinate> path;
    private final PathType pathType;


    public Customer(Integer id, List<Stock.StockType> shoppingList, Double initialCash, List<Coordinate> path, PathType pathType) {
        this.id = id;
        this.shoppingList = shoppingList;
        this.initialCash = initialCash;
        this.path = path;
        this.pathType = pathType;
    }

    public Integer getId() {
        return id;
    }

    public List<Stock.StockType> getShoppingList() {
        return shoppingList;
    }

    public Double getInitialCash() {
        return initialCash;
    }

    public List<Coordinate> getPath() {
        return path;
    }

    public PathType getPathType() {
        return pathType;
    }
}
