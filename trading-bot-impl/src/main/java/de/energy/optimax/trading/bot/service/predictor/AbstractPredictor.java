package de.energy.optimax.trading.bot.service.predictor;

import de.energy.optimax.trading.bot.data.BidRound;
import de.energy.optimax.trading.bot.property.BidderProperties;
import de.energy.optimax.trading.bot.property.PredictorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The type Abstract predictor.
 *
 * @author Smirnov Kirill
 */
public abstract class AbstractPredictor implements Predictor {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    protected LinkedList<BidRound> statistic = new LinkedList<>();
    protected PredictorProperties properties = new PredictorProperties();
    protected Integer predictCounter = 0;
    protected Optional<Integer> lastPredictedValue = Optional.empty();

    @Autowired
    public void setProperties(BidderProperties properties) {
        this.properties = properties.getPredictor();
    }

    @Override
    public void addBidInfo(BidRound round) {
        statistic.add(round);
    }

    @Override
    public boolean canPredict() {
        return predictCounter > properties.getSuccessfulPredictionCountCriteria();
    }

    @Override
    public void clear() {
        statistic.clear();
        predictCounter = 0;
        lastPredictedValue = Optional.empty();
    }

    protected boolean deviationCriteria(double avgValue, Stream<Integer> target) {
        return target
                .allMatch(opponentBid -> Math.abs(opponentBid - avgValue) <= properties.getAccuracy());
    }

    protected void checkPrediction() {
        if (lastPredictedValue.isPresent()) {
            if (lastPredictedValue.get() - statistic.getLast().getOpponentBid() < properties.getAccuracy()) {
                predictCounter++;
            } else {
                predictCounter = 0;
            }
        }
    }

    protected boolean checkPredictionAnalysis() {
        return statistic.size() >= properties.getMinimalValuesForAnalysis();
    }

    protected void logPrediction(Optional<Integer> prediction) {
        prediction.ifPresentOrElse(resultValue -> logger.debug("predict() - Predict is successful. Offered bid value: [{}]", resultValue),
                () -> logger.debug("predict() - Predict is unsuccessful."));
    }
}
