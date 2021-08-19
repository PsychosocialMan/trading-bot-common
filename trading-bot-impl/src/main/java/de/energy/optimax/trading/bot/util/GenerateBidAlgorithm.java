package de.energy.optimax.trading.bot.util;

import de.energy.optimax.trading.bot.data.BidRound;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Generate bid algorithm. If predictors can't recognize model of opponent's behavior, we have to generate our
 * bid. Algorithm is:
 * <ol>
 *     <li>If we need first bid, we generate average MU per round and add to it random percent of itself</li>
 *     <li>Else we have to generate bid based on statistic</li>
 * </ol>
 *
 * @author Smirnov Kirill
 * @see #generateBid(int, int, int, int) #generateBid(int, int, int, int)#generateBid(int, int, int,
 * int)#generateBid(int, int, int, int)Description of generating algorithm.
 */
public class GenerateBidAlgorithm {

    /**
     * The Bid rounds.
     */
    private final List<BidRound> bidRounds;
    /**
     * The Initial quantity in QU.
     */
    private final int initialQuantity;
    /**
     * The Initial cash in MU.
     */
    private final int initialCash;
    /**
     * The Approx util.
     */
    private final ApproxUtil approxUtil;

    /**
     * Instantiates a new Generate bid algorithm.
     *
     * @param bidRounds       the bid rounds statistic
     * @param initialQuantity the initial quantity
     * @param initialCash     the initial cash
     */
    public GenerateBidAlgorithm(List<BidRound> bidRounds, int initialQuantity, int initialCash) {
        this.bidRounds = bidRounds;
        this.initialQuantity = initialQuantity;
        this.initialCash = initialCash;
        this.approxUtil = new ApproxUtil();
    }

    /**
     * Generate bid. At least we need to take half of the units from auction.
     * <ol>
     *  <li>So if we have more, we can just pass.</li>
     *  <li>If we have draw, we have to grab at least one unit. For this situation we divide our remaining cash between half
     * of remaining QU + 1.</li>
     *  <li>If we have critical situation and till draw we have not free rounds, just separate opponent's cash and increment it.</li>
     *  <li>Else we compute middle delta. Adding default value to our average bid.
     *  <ol>
     *      <li>
     *          If middle delta is less than 60% from average bid for round, add middle delta.
     *      </li>
     *      <li>
     *          Else decrease average bid with the 60% of middle delta. Not 100% because of risk.
     *      </li>
     *      <li>
     *          Else we have the same situation with the opponent, add to average bid 70% of average bid.
     *      </li>
     *  </ol>
     *  </li>
     *  <li>Checking if we have enough budget.</li>
     * </ol>
     *
     * @param remainingCash         our remaining cash
     * @param remainingOpponentCash opponent remaining cash
     * @param remainingQuantity     remaining quantity in round
     * @param wonQuantity           our won quantity
     *
     * @return generated bid
     */
    public int generateBid(int remainingCash,
                           int remainingOpponentCash,
                           int remainingQuantity,
                           int wonQuantity) {
        var quantityToDraw = initialQuantity / 2 - wonQuantity;
        var opponentQuantityToDraw = initialQuantity / 2 - (initialQuantity - remainingQuantity - wonQuantity);

        // We won, just pass.
        if (quantityToDraw < 0 && remainingQuantity + quantityToDraw <= 0) {
            return 0;
        }

        // Separate cash between half of the remaining bids + 1.
        if (quantityToDraw == 0) {
            return approxUtil.divideAndCeil(remainingCash, (remainingQuantity / 2 + 1));
        }

        // If we have critical situation and till draw we have not free rounds, just separate opponent's cash and increment it.
        if (remainingQuantity - quantityToDraw <= 0) {
            return approxUtil.divideAndCeil(remainingOpponentCash, opponentQuantityToDraw) + 1;
        } else {

            // Else we compute middle delta.
            var middleDelta = middleDelta();

            // Adding default value to our average bid.
            var avgBid = generateAvgBid() - middleDelta / 2;

            // If middle delta < 60% from average bid for round, add middle delta.
            if (middleDelta < approxUtil.approx(initialQuantity, initialCash, 60) && middleDelta > 0) {
                avgBid = generateAvgBid() + middleDelta;
            } else if (middleDelta < 0) {
                // Else decrease average bid with the 60% of middle delta. Not 100% because of risk.
                avgBid = generateAvgBid() - approxUtil.divideAndCeil(middleDelta * 60, 100);
            } else if (middleDelta == 0) {
                // Else we have the same situation with the opponent, add to average bid 70% of average bid.
                avgBid = generateAvgBid() + approxUtil.approx(
                        initialQuantity,
                        initialCash,
                        approxUtil.getRandomApproxPercents(70)
                );
            }

            // Checking if we have enough budget.
            return remainingCash - avgBid >= 0 ? avgBid : remainingCash;
        }

    }

    /**
     * Generate average bid. We generate average MU per round and add to it random percent of itself
     *
     * @return bid in MU
     */
    public int generateAvgBid() {

        return approxUtil.divideAndCeil(2 * initialCash, initialQuantity) + approxUtil.approx(
                initialQuantity,
                initialCash,
                approxUtil.getRandomApproxPercents()
        );
    }

    /**
     * Average delta between our and opponent bids.
     *
     * @return middle delta
     */
    private int middleDelta() {
        return (int) Math.round(bidRounds.stream()
                .collect(Collectors.summarizingInt(BidRound::getDelta))
                .getAverage());
    }

}
