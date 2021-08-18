package de.energy.optimax.trading.bot.service.predictor.impl;

import de.energy.optimax.trading.bot.data.BidRound;
import de.energy.optimax.trading.bot.service.predictor.AbstractPredictor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AllTheSameBidsPredictor extends AbstractPredictor {

    @Override
    public Optional<Integer> predict() {
        logger.debug("predict() - Trying to predict all the same bids.");

        Optional<Integer> result = Optional.empty();
        if (statistic.size() >= properties.getMinimalValuesForAnalysis()) {
            var opponentStats = statistic.stream()
                    .collect(Collectors.summarizingInt(BidRound::getOpponentBid));

            var avgOpponentBid = opponentStats.getAverage();

            var criteria = deviationCriteria(avgOpponentBid, statistic.stream()
                    .map(BidRound::getOpponentBid));

            if (criteria) {
                result = Optional.of(opponentStats.getMax());
            }
            checkPrediction();

            lastPredictedValue = result;
        }

        result.ifPresentOrElse(resultValue -> logger.debug("predict() - Predict is successful. Offered bid value: [{}]", resultValue),
                () -> logger.debug("predict() - Predict is unsuccessful."));

        return result;
    }
}
