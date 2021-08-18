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
        if (statistic.size() >= properties.getMinimalValuesForAnalysis()) {

            var opponentDeltaStream = getOpponentDeltaStream();
            var avgGrowth = opponentDeltaStream.collect(Collectors.summarizingInt(value -> value)).getAverage();
            var criteria = deviationCriteria(avgGrowth, opponentDeltaStream);

            if (criteria) {
                result = Optional.of(statistic.getLast().getOpponentBid() + (int) avgGrowth);
            }
            checkPrediction();

            lastPredictedValue = result;
        }

        result.ifPresentOrElse(resultValue -> logger.debug("predict() - Predict is successful. Offered bid value: [{}]", resultValue),
                () -> logger.debug("predict() - Predict is unsuccessful."));

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
