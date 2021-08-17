package de.energy.optimax.trading.bot.util;

import de.energy.optimax.trading.bot.data.BidRound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class GenerateBidAlgorithm {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final List<BidRound> bidRounds;
    private final int initialQuantity;
    private final int initialCash;

    public GenerateBidAlgorithm(List<BidRound> bidRounds, int initialQuantity, int initialCash) {
        this.bidRounds = bidRounds;
        this.initialQuantity = initialQuantity;
        this.initialCash = initialCash;
    }

    public int generateBid(int remainingCash,
                           int remainingOpponentCash,
                           int remainingQuantity,
                           int wonQuantity) {
        var quantityToDraw = initialQuantity / 2 - wonQuantity;
        var opponentQuantityToDraw = initialQuantity / 2 - (initialQuantity - wonQuantity);

        // No risk
        if (quantityToDraw <= 0) {
            return 0;
        }

        if (remainingQuantity - quantityToDraw <= 0) {
            return remainingOpponentCash / opponentQuantityToDraw + 1;
        } else {
            var avgBid = generateAvgBid() + middleDelta();
            return remainingCash - avgBid >= 0 ? avgBid : remainingCash;
        }

    }

    public int generateAvgBid() {
        var approxUtil = new ApproxUtil();

        var avgBid = 2 * initialCash / initialQuantity + approxUtil.approx(
                initialQuantity,
                initialCash,
                approxUtil.getRandomApproxPercents()
        );

        logger.debug("generateFirstBid() - First big generated: [{}]", avgBid);

        return avgBid;
    }

    private int middleDelta() {
        return (int) Math.round(bidRounds.stream()
                .collect(Collectors.summarizingInt(BidRound::getDelta))
                .getAverage());
    }

}
