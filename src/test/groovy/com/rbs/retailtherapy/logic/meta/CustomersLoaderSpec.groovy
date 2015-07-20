package com.rbs.retailtherapy.logic.meta

import com.google.gson.GsonBuilder
import com.rbs.retailtherapy.domain.Customer
import spock.lang.Specification

class CustomersLoaderSpec extends Specification {

    def "should load customers file" (){
        when:
        Map<Integer, Customer> customers = new CustomersLoader(new GsonBuilder().create()).loadCustomersFromCpFileName("customers.json")

        then:
        customers.size() == 100
    }
}
