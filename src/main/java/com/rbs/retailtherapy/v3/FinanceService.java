package com.rbs.retailtherapy.v3;

import com.rbs.retailtherapy.model.Stock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinanceService {
    public Double averageProfits (int numberOfShoppers, List<Customer> typeOfCustomer, List<Stock> stocks){
        Map<Stock.StockType, Double> profitMargins = calculateMargins(stocks);
        int instanceOfShoppers = numberOfShoppers / typeOfCustomer.size();


        Double totalProfit = 0d;
        for (Customer customer : typeOfCustomer) {
            double customerProfit = getCustomerAverageProfit(customer, profitMargins);
            totalProfit = customerProfit  * instanceOfShoppers;
        }
        return totalProfit;
    }

    private double getCustomerAverageProfit(Customer customer, Map<Stock.StockType, Double> profitMargins) {
        double customerProfit = 0d;
        for (Stock.StockType stockType : customer.getShoppingList()) {
            Double profitMargin = profitMargins.get(stockType);
            Double profitItemType = profitMargin * customer.getInitialCash() / customer.getShoppingList().size();
            customerProfit += profitItemType;
        }
        return customerProfit;
    }

    private Map<Stock.StockType, Double> calculateMargins(List<Stock> stocks) {
        Map<Stock.StockType, Double> profitMargins = new HashMap<>();
        for (Stock stock : stocks) {
            profitMargins.put(stock.getStockType(), stock.getWholesalePrice() / stock.getRetailPrice());
        }
        return profitMargins;
    }
}
