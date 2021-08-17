package de.energy.optimax.trading.bot.service.predictor.impl;

import de.energy.optimax.trading.bot.service.predictor.AbstractPredictor;

import java.util.Optional;

public class AllTheSameBidsPredictor extends AbstractPredictor {
    @Override
    public boolean canPredict() {
        return false;
    }

    @Override
    public Optional<Integer> predict() {
        return Optional.empty();
    }
}
