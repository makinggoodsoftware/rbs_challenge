package com.rbs.retailtherapy.client;

public class ArtificialIntelligenceConfig {
    private final int idealShopToGridRatio;
    private int willStoSpendInShopsInitially;

    public ArtificialIntelligenceConfig(int idealShopToGridRatio) {
        this.idealShopToGridRatio = idealShopToGridRatio;
    }

    public int getIdealShopToGridRatio() {
        return idealShopToGridRatio;
    }

    public int getPercentageWillingToSpendInShopsInitially() {
        return willStoSpendInShopsInitially;
    }
}
