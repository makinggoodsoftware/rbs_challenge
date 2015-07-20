package com.rbs.retailtherapy.logic.meta

import com.google.gson.GsonBuilder
import com.rbs.retailtherapy.domain.Customer
import com.rbs.retailtherapy.domain.Dimension
import spock.lang.Specification

class CustomersLoaderSpec extends Specification {

    def "should load customers file" (){
        when:
        Map<Integer, Customer> customers = new CustomersLoader(new Dimension(41, 41), new GsonBuilder().create()).loadCustomersFromCpFileName("customers.json")

        then:
        customers.size() == 100
    }
}
