package com.rbs.retailtherapy.v3.domain;

public class FixedSpacingInvestmentConfiguration {
    private final double investmentPercentage;
    private final int desiredSpacing;

    public FixedSpacingInvestmentConfiguration(double investmentPercentage, int desiredSpacing) {
        this.investmentPercentage = investmentPercentage;
        this.desiredSpacing = desiredSpacing;
    }

    public double getInvestmentPercentage() {
        return investmentPercentage;
    }

    public int getDesiredSpacing() {
        return desiredSpacing;
    }
}
