package de.energy.optimax.trading.bot.service.predictor;

import de.energy.optimax.trading.bot.data.BidRound;

import java.util.Optional;

// You can implement another predictor and add it to the context.
public interface Predictor {

    boolean canPredict();

    Optional<Integer> predict();

    void addBidInfo(BidRound round);

    void clear();
}
