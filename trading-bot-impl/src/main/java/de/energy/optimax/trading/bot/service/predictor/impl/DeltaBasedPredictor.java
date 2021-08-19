package de.energy.optimax.trading.bot.service.predictor.impl;

import de.energy.optimax.trading.bot.data.BidRound;
import de.energy.optimax.trading.bot.service.predictor.AbstractPredictor;
import de.energy.optimax.trading.bot.util.ApproxUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeltaBasedPredictor extends AbstractPredictor {

    @Override
    public Optional<Integer> predict() {
        logger.debug("predict() - Trying to predict Delta based algorithm (delta between our and opponent bids) bids.");

        Optional<Integer> result = Optional.empty();
        if (checkPredictionAnalysis()) {

            var descendingIterator = statistic.descendingIterator();

            var lastRound = descendingIterator.next();
            var roundBeforeLast = descendingIterator.next();

            var criteria = true;

            while (descendingIterator.hasNext()) {
                // If he won, he increase our bid with delta.
                var ifOpponentWonCondition = roundBeforeLast.getBidStatus() == BidRound.Status.LOST &&
                        lastRound.getOpponentBid() - (
                                roundBeforeLast.getOpponentBid() - Math.abs(new ApproxUtil().divideAndCeil(roundBeforeLast.getDelta(), 2))
                        ) <= properties.getAccuracy();

                // If he lost, he increase his bid with delta.
                var ifOpponentLostCondition = (roundBeforeLast.getBidStatus() == BidRound.Status.WON || roundBeforeLast.getBidStatus() == BidRound.Status.DRAW) &&
                        lastRound.getOpponentBid() - (roundBeforeLast.getYourBid() + roundBeforeLast.getDelta()) <= properties.getAccuracy();

                criteria &= ifOpponentLostCondition || ifOpponentWonCondition;

                lastRound = roundBeforeLast;
                roundBeforeLast = descendingIterator.next();
            }
            if (criteria) {
                lastRound = statistic.getLast();
                if (lastRound.getBidStatus() == BidRound.Status.LOST) {
                    result = Optional.of(
                            lastRound.getOpponentBid() -
                                    new ApproxUtil().divideAndCeil(Math.abs(lastRound.getDelta()), 2)
                    );
                } else {
                    result = Optional.of(lastRound.getYourBid() + lastRound.getDelta());
                }
            }
            checkPrediction();

            lastPredictedValue = result;
        }

        logPrediction(result);

        return result;
    }
}
