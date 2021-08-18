package de.energy.optimax.trading.bot.service.predictor.impl;

import de.energy.optimax.trading.bot.data.BidRound;
import de.energy.optimax.trading.bot.service.predictor.AbstractPredictor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeltaBasedPredictor extends AbstractPredictor {

    @Override
    public Optional<Integer> predict() {
        logger.debug("predict() - Trying to predict Delta based algorithm (delta between our and opponent bids) bids.");

        Optional<Integer> result = Optional.empty();
        if (statistic.size() >= properties.getMinimalValuesForAnalysis()) {

            var descendingIterator = statistic.descendingIterator();

            var lastRound = descendingIterator.next();
            var roundBeforeLast = descendingIterator.next();

            // If he won, he increase our bid with delta.
            var ifOpponentWonCondition = roundBeforeLast.getBidStatus() == BidRound.Status.LOST &&
                    lastRound.getOpponentBid() - (roundBeforeLast.getYourBid() - roundBeforeLast.getDelta()) < properties.getAccuracy();

            // If he lost, he increase his bid with delta.
            var ifOpponentLostCondition = (roundBeforeLast.getBidStatus() == BidRound.Status.WON || roundBeforeLast.getBidStatus() == BidRound.Status.DRAW) &&
                    lastRound.getOpponentBid() - (roundBeforeLast.getOpponentBid() + roundBeforeLast.getDelta()) < properties.getAccuracy();

            var criteria = ifOpponentLostCondition || ifOpponentWonCondition;

            if (criteria) {
                if (lastRound.getBidStatus() == BidRound.Status.LOST) {
                    result = Optional.of(lastRound.getYourBid() - lastRound.getDelta());
                } else {
                    result = Optional.of(lastRound.getOpponentBid() + lastRound.getDelta());
                }
            }
            checkPrediction();

            lastPredictedValue = result;
        }

        result.ifPresentOrElse(resultValue -> logger.debug("predict() - Predict is successful. Offered bid value: [{}]", resultValue),
                () -> logger.debug("predict() - Predict is unsuccessful."));

        return result;
    }
}
