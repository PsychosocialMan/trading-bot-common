package de.energy.optimax.trading.bot.service.predictor.impl;

import de.energy.optimax.trading.bot.data.BidRound;
import de.energy.optimax.trading.bot.service.predictor.AbstractPredictor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type All the same bids predictor. Checks if opponent make his bids with the same value (with accuracy).
 *
 * @author Smirnov Kirill
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AllTheSameBidsPredictor extends AbstractPredictor {

    @Override
    public Optional<Integer> predict() {
        logger.debug("predict() - Trying to predict all the same bids.");

        Optional<Integer> result = Optional.empty();
        if (checkPredictionAnalysis()) {

            // Get all the opponent bids.
            var opponentStats = statistic.stream()
                    .collect(Collectors.summarizingInt(BidRound::getOpponentBid));

            // Compute average value from them.
            var avgOpponentBid = opponentStats.getAverage();

            // Checking the deviation from average value with accuracy from properties.
            var criteria = deviationCriteria(avgOpponentBid, statistic.stream()
                    .map(BidRound::getOpponentBid));

            // If all the elements in statistic passes, return max.
            if (criteria) {
                result = Optional.of(opponentStats.getMax());
            }
            checkPrediction();

            lastPredictedValue = result;
        }

        logPrediction(result);

        return result;
    }
}
