package com.rbs.retailtherapy.logic.meta;

import com.google.gson.Gson;
import com.rbs.retailtherapy.domain.CustomersFile;
import com.rbs.retailtherapy.domain.Customer;
import com.rbs.retailtherapy.domain.CustomerFileEntry;

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
            if (customerFileEntry.getStocks() == null){
                throw new IllegalStateException("This customer doesn't have a list of shopping items");
            }
            customers.add(new Customer(
                    customerFileEntry.getId(),
                    customerFileEntry.getStocks(),
                    customerFileEntry.getInitialCash(),
                    null
            ));
        }
        return customers;
    }
}
