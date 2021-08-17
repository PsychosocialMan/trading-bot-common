package de.energy.optimax.trading.bot.util;

import de.energy.optimax.trading.bot.data.BidRound;

import java.util.List;

public class GenerateBidAlgorithm {

    private final List<BidRound> bidRounds;
    private final int initialQuantity;

    public GenerateBidAlgorithm(List<BidRound> bidRounds, int initialQuantity) {
        this.bidRounds = bidRounds;
        this.initialQuantity = initialQuantity;
    }

    public int generateBid(int remainingCash, int remainingQuantity) {
        return 0;
    }
}
