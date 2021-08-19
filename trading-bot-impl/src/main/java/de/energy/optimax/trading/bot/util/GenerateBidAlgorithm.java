package de.energy.optimax.trading.bot.util;

import de.energy.optimax.trading.bot.data.BidRound;

import java.util.List;
import java.util.stream.Collectors;

public class GenerateBidAlgorithm {

    private final List<BidRound> bidRounds;
    private final int initialQuantity;
    private final int initialCash;
    private final ApproxUtil approxUtil;

    public GenerateBidAlgorithm(List<BidRound> bidRounds, int initialQuantity, int initialCash) {
        this.bidRounds = bidRounds;
        this.initialQuantity = initialQuantity;
        this.initialCash = initialCash;
        this.approxUtil = new ApproxUtil();
    }

    public int generateBid(int remainingCash,
                           int remainingOpponentCash,
                           int remainingQuantity,
                           int wonQuantity) {
        var quantityToDraw = initialQuantity / 2 - wonQuantity;
        var opponentQuantityToDraw = initialQuantity / 2 - (initialQuantity - remainingQuantity - wonQuantity);

        // We won
        if (quantityToDraw < 0 && remainingQuantity + quantityToDraw <= 0) {
            return 0;
        }

        // Separate cash between other bids.
        if (quantityToDraw == 0) {
            return approxUtil.divideAndCeil(remainingCash, (remainingQuantity / 2 + 1));
        }

        if (remainingQuantity - quantityToDraw <= 0) {
            return approxUtil.divideAndCeil(remainingOpponentCash, opponentQuantityToDraw) + 1;
        } else {
            var middleDelta = middleDelta();

            var avgBid = generateAvgBid() - middleDelta / 2;

            // If middle delta < 60% from average bid for round, add middle delta.
            // Else
            if (middleDelta < approxUtil.approx(initialQuantity, initialCash, 60) && middleDelta > 0) {
                avgBid = generateAvgBid() + middleDelta;
            } else if (middleDelta < 0) {
                avgBid = generateAvgBid() - approxUtil.divideAndCeil(middleDelta * 60, 100);
            } else if (middleDelta == 0) {
                avgBid = generateAvgBid() + approxUtil.approx(
                        initialQuantity,
                        initialCash,
                        approxUtil.getRandomApproxPercents(70)
                );
            }

            return remainingCash - avgBid >= 0 ? avgBid : remainingCash;
        }

    }

    public int generateAvgBid() {

        return approxUtil.divideAndCeil(2 * initialCash, initialQuantity) + approxUtil.approx(
                initialQuantity,
                initialCash,
                approxUtil.getRandomApproxPercents()
        );
    }

    private int middleDelta() {
        return (int) Math.round(bidRounds.stream()
                .collect(Collectors.summarizingInt(BidRound::getDelta))
                .getAverage());
    }

}
