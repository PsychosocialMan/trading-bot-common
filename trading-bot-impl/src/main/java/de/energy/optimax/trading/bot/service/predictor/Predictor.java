package de.energy.optimax.trading.bot.service.predictor;

import de.energy.optimax.trading.bot.data.BidRound;

import java.util.Optional;

/**
 * The interface Predictor. Trying to predict opponent's logic in different ways. For the further development there is
 * an opportunity to create another implementations of Predictor interface.
 *
 * @author Smirnov Kirill
 */
public interface Predictor {

    /**
     * Can predictor predict? Did he recognized the opponent's behavior pattern? If he successfully predicted opponent's
     * bid several times, we can trust him.
     *
     * @return the boolean
     */
    boolean canPredict();

    /**
     * Predicted bid value.
     *
     * @return the optional
     */
    Optional<Integer> predict();

    /**
     * Add bid info for previous bids analysis.
     *
     * @param round the round
     */
    void addBidInfo(BidRound round);

    /**
     * Clear the predictor's infrastructure.
     */
    void clear();
}
