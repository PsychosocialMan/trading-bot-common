package de.energy.optimax.trading.bot.service.predictor.impl;

import de.energy.optimax.trading.bot.service.predictor.AbstractPredictor;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Zero to avg and vice versa
@Service
public class SwitchPredictor extends AbstractPredictor {
    @Override
    public boolean canPredict() {
        return false;
    }

    @Override
    public Optional<Integer> predict() {
        return Optional.empty();
    }
}
