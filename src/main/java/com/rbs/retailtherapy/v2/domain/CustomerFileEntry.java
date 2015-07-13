package com.rbs.retailtherapy.v2.domain;

import com.rbs.retailtherapy.model.Stock;

import java.util.List;

public class CustomerFileEntry {
    private Integer id;
    private List<Stock.StockType> stockCountCollection;
    private Double initialCash;
    private String GridPositions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Stock.StockType> getStockCountCollection() {
        return stockCountCollection;
    }

    public void setStockCountCollection(List<Stock.StockType> stockCountCollection) {
        this.stockCountCollection = stockCountCollection;
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
