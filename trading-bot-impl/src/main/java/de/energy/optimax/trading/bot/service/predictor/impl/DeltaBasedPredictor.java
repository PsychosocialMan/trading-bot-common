package de.energy.optimax.trading.bot.service.predictor.impl;

import de.energy.optimax.trading.bot.data.BidRound;
import de.energy.optimax.trading.bot.service.predictor.AbstractPredictor;
import de.energy.optimax.trading.bot.util.ApproxUtil;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The type Delta based predictor. Algorithm is based on delta between our and opponent bids.
 *
 * <ol>
 *     <li>If he won last round, he will decrease his last bid with half of a delta between our bids</li>
 *     <li>If it was draw or opponent lost, he will increase our last bid with delta between our bids</li>
 * </ol>
 *
 * @author Smirnov Kirill
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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
                // If he won, he decrease his bid with half of a delta.
                var ifOpponentWonCondition = roundBeforeLast.getBidStatus() == BidRound.Status.LOST &&
                        lastRound.getOpponentBid() - (
                                roundBeforeLast.getOpponentBid() - Math.abs(new ApproxUtil().divideAndCeil(roundBeforeLast.getDelta(), 2))
                        ) <= properties.getAccuracy();

                // If he lost, he increase our bid with delta.
                var ifOpponentLostCondition = (roundBeforeLast.getBidStatus() == BidRound.Status.WON || roundBeforeLast.getBidStatus() == BidRound.Status.DRAW) &&
                        lastRound.getOpponentBid() - (roundBeforeLast.getYourBid() + roundBeforeLast.getDelta()) <= properties.getAccuracy();

                criteria &= ifOpponentLostCondition || ifOpponentWonCondition;

                lastRound = roundBeforeLast;
                roundBeforeLast = descendingIterator.next();
            }
            // If success we can predict his next move.
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
