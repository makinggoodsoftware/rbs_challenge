package com.rbs.retailtherapy.client;

import com.rbs.retailtherapy.v2.domain.CustomerFileEntry;

import java.util.List;

public class CustomersFile {
    private List<CustomerFileEntry> shoppers;

    public List<CustomerFileEntry> getShoppers() {
        return shoppers;
    }

    public void setShoppers(List<CustomerFileEntry> shoppers) {
        this.shoppers = shoppers;
    }
}