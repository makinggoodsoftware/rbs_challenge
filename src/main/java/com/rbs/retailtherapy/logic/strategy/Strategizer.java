package com.rbs.retailtherapy.logic.strategy;

import com.rbs.retailtherapy.domain.GameState;
import com.rbs.retailtherapy.domain.InvestmentConfiguration;
import com.rbs.retailtherapy.domain.RoundState;
import com.rbs.retailtherapy.domain.ShopBiddingStrategy;

public class Strategizer {
    private final FinanceService financeService;
    private final InvestmentConfiguration baseInvestmentConfiguration;

    public Strategizer(FinanceService financeService, InvestmentConfiguration baseInvestmentConfiguration) {
        this.financeService = financeService;
        this.baseInvestmentConfiguration = baseInvestmentConfiguration;
    }

    public ShopBiddingStrategy shopBiddingStrategy(GameState gameState, RoundState roundState) {
        Double averageAvailableProfit = financeService.averageProfits(
                roundState.getNumberOfShoppers(),
                gameState.getTypeOfShoppers(),
                roundState.getStocks()
        );

        double profitTarget = averageAvailableProfit * 3;
        double willingToSpend = profitTarget / 2;
        double maximumToInvest = roundState.getInitialMoney() * gameState.getMaximumToInvest();
        double amountToInvest = willingToSpend > roundState.getInitialMoney() ? maximumToInvest : willingToSpend;
        return new ShopBiddingStrategy(amountToInvest, baseInvestmentConfiguration);
    }
}
