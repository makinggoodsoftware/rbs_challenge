package com.rbs.retailtherapy.v2.service;

import com.google.gson.Gson;
import com.rbs.retailtherapy.client.CustomersFile;
import com.rbs.retailtherapy.model.Stock;
import com.rbs.retailtherapy.v3.domain.Customer;
import com.rbs.retailtherapy.v2.domain.CustomerFileEntry;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private final Gson gson;
    private final CoordinatesService coordinatesService;

    public CustomerService(Gson gson, CoordinatesService coordinatesService) {
        this.gson = gson;
        this.coordinatesService = coordinatesService;
    }


    public List<Customer> loadCustomersFromCpFileName (String customersFileName){
        List<Customer> customers = new ArrayList<>();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(customersFileName);
        Reader reader = new InputStreamReader(in);
        CustomersFile customersFile = gson.fromJson(reader, CustomersFile.class);
        for (CustomerFileEntry customerFileEntry : customersFile.getShoppers()) {
            List<Stock.StockType> stockCountCollection = customerFileEntry.getStockCountCollection();
            Integer id = customerFileEntry.getId();
            if (stockCountCollection == null) throw new IllegalStateException("Can't process the stocks for customer with ID " + id);
            customers.add(new Customer(
                    id,
                    stockCountCollection,
                    customerFileEntry.getInitialCash(),
                    null
            ));
        }
        return customers;
    }
}
