package com.rbs.retailtherapy.domain;

import com.rbs.retailtherapy.model.Stock;

import java.util.List;

public class CustomerFileEntry {
    private Integer id;
    private List<Stock.StockType> stocks;
    private Double initialCash;
    private String GridPositions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Stock.StockType> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock.StockType> stocks) {
        this.stocks = stocks;
    }

    public Double getInitialCash() {
        return initialCash;
    }

    public void setInitialCash(Double initialCash) {
        this.initialCash = initialCash;
    }

    public String getGridPositions() {
        return GridPositions;
    }

    public void setGridPositions(String gridPositions) {
        this.GridPositions = gridPositions;
    }
}
