package de.energy.optimax.trading.bot.service.predictor.impl;

import de.energy.optimax.trading.bot.service.predictor.AbstractPredictor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class ArithmeticProgressionPredictor extends AbstractPredictor {

    @Override
    public Optional<Integer> predict() {
        logger.debug("predict() - Trying to predict Arithmetic Progression bids.");

        Optional<Integer> result = Optional.empty();
        if (checkPredictionAnalysis()) {

            var opponentDeltaStream = getOpponentDeltaStream();
            var avgGrowth = opponentDeltaStream.collect(Collectors.summarizingInt(value -> value)).getAverage();
            var criteria = deviationCriteria(avgGrowth, getOpponentDeltaStream());

            if (criteria) {
                result = Optional.of(statistic.getLast().getOpponentBid() + (int) Math.ceil(avgGrowth));
            }
            checkPrediction();

            lastPredictedValue = result;
        }

        logPrediction(result);

        return result;
    }


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
