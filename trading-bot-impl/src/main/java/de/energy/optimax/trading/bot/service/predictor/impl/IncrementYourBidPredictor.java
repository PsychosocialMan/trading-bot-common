package de.energy.optimax.trading.bot.service.predictor.impl;

import de.energy.optimax.trading.bot.service.predictor.AbstractPredictor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IncrementYourBidPredictor extends AbstractPredictor {

    @Override
    public Optional<Integer> predict() {
        return Optional.empty();
    }
}
