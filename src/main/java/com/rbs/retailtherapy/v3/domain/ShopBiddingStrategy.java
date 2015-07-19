package com.rbs.retailtherapy.v3.domain;

public class ShopBiddingStrategy {
    private final double amountToInvest;
    private final InvestmentConfiguration investmentConfiguration;

    public ShopBiddingStrategy(double amountToInvest, InvestmentConfiguration investmentConfiguration) {
        this.amountToInvest = amountToInvest;
        this.investmentConfiguration = investmentConfiguration;
    }

    public double getAmountToInvest() {
        return amountToInvest;
    }

    public InvestmentConfiguration getInvestmentConfiguration() {
        return investmentConfiguration;
    }
}
