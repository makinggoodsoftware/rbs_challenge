package com.rbs.retailtherapy.v3.logic.meta;

import com.google.gson.Gson;
import com.rbs.retailtherapy.client.CustomersFile;
import com.rbs.retailtherapy.v2.domain.CustomerFileEntry;
import com.rbs.retailtherapy.v3.domain.Customer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CustomersLoader {
    private final Gson gson;

    public CustomersLoader(Gson gson) {
        this.gson = gson;
    }

    public List<Customer> loadCustomersFromCpFileName (String customersFileName){
        List<Customer> customers = new ArrayList<>();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(customersFileName);
        Reader reader = new InputStreamReader(in);
        CustomersFile customersFile = gson.fromJson(reader, CustomersFile.class);
        for (CustomerFileEntry customerFileEntry : customersFile.getShoppers()) {
            customers.add(new Customer(
                    customerFileEntry.getId(),
                    customerFileEntry.getStockCountCollection(),
                    customerFileEntry.getInitialCash(),
                    null
            ));
        }
        return customers;
    }
}
