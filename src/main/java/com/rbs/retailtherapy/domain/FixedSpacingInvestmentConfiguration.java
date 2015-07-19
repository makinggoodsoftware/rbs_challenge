package com.rbs.retailtherapy.domain;

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
