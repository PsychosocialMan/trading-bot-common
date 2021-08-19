package de.energy.optimax.trading.bot.service.predictor.impl;

import de.energy.optimax.trading.bot.service.predictor.AbstractPredictor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IncrementYourBidPredictor extends AbstractPredictor {

    @Override
    public Optional<Integer> predict() {
        logger.debug("predict() - Trying to predict Increment-our-bid algorithm bids.");

        Optional<Integer> result = Optional.empty();
        if (checkPredictionAnalysis()) {

            var descendingIterator = statistic.descendingIterator();
            var previousRound = descendingIterator.next();

            var criteria = true;

            while (descendingIterator.hasNext()) {
                var roundBeforeLast = descendingIterator.next();
                criteria &= Math.abs(previousRound.getOpponentBid() - roundBeforeLast.getYourBid() - 1) < properties.getAccuracy();
                previousRound = roundBeforeLast;
            }

            if (criteria) {
                result = Optional.of(statistic.getLast().getYourBid() + 1);
            }

            checkPrediction();

            lastPredictedValue = result;
        }

        logPrediction(result);

        return result;
    }
}
