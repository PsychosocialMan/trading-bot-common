package de.energy.optimax.trading.bot.service.predictor.impl;

import de.energy.optimax.trading.bot.service.predictor.AbstractPredictor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The type Arithmetic progression predictor. Checks if our opponent increase his bids with the same value (with
 * accuracy).
 *
 * @author Smirnov Kirill
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ArithmeticProgressionPredictor extends AbstractPredictor {

    @Override
    public Optional<Integer> predict() {
        logger.debug("predict() - Trying to predict Arithmetic Progression bids.");

        Optional<Integer> result = Optional.empty();
        if (checkPredictionAnalysis()) {

            // Get opponent deltas between his bids.
            var opponentDeltaStream = getOpponentDeltaStream();
            // Calculating average delta.
            var avgGrowth = opponentDeltaStream.collect(Collectors.summarizingInt(value -> value)).getAverage();
            // Check if all the deltas has deviation from avg with accuracy.
            var criteria = deviationCriteria(avgGrowth, getOpponentDeltaStream());

            // If yes, just increase his last bid with the average delta.
            if (criteria) {
                result = Optional.of(statistic.getLast().getOpponentBid() + (int) Math.ceil(avgGrowth));
            }
            checkPrediction();

            lastPredictedValue = result;
        }

        logPrediction(result);

        return result;
    }


    /**
     * Gets opponent delta stream.
     *
     * @return the opponent delta stream
     */
    private Stream<Integer> getOpponentDeltaStream() {
        var streamBuilder = IntStream.builder();

        var previousBid = statistic.getFirst().getOpponentBid();

        for (var statIndex = 1; statIndex < statistic.size(); statIndex++) {
            var currentBid = statistic.get(statIndex).getOpponentBid();

            streamBuilder.add(currentBid - previousBid);
            previousBid = currentBid;
        }
        return streamBuilder.build().boxed();
    }
}
