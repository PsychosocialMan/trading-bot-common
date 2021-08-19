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
 * The type Abstract predictor. Implements all the methods except <code>predict()</code>.
 *
 * @author Smirnov Kirill
 */
public abstract class AbstractPredictor implements Predictor {

    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * The Statistic of the auction.
     */
    protected LinkedList<BidRound> statistic = new LinkedList<>();
    /**
     * The Properties.
     */
    protected PredictorProperties properties = new PredictorProperties();
    /**
     * The Predict counter. How many times in a row predictor successfully did his job.
     */
    protected Integer predictCounter = 0;
    /**
     * The Last predicted value.
     */
    protected Optional<Integer> lastPredictedValue = Optional.empty();

    /**
     * Sets properties.
     *
     * @param properties the properties
     */
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

    /**
     * Deviation criteria. Checks target by the deviation from avgValue.
     *
     * @param avgValue the average value
     * @param target   the target
     *
     * @return is target stream is ok.
     */
    protected boolean deviationCriteria(double avgValue, Stream<Integer> target) {
        return target
                .allMatch(opponentBid -> Math.abs(opponentBid - avgValue) <= properties.getAccuracy());
    }

    /**
     * Check prediction. Increments counter of successful prediction.
     */
    protected void checkPrediction() {
        if (lastPredictedValue.isPresent()) {
            if (lastPredictedValue.get() - statistic.getLast().getOpponentBid() < properties.getAccuracy()) {
                predictCounter++;
            } else {
                predictCounter = 0;
            }
        }
    }

    /**
     * Check prediction analysis available. For example, if we set up property <code>bidder.predictor.minimal-values-for-analysis:
     * 3</code>
     * <p>
     * Then only statistic with at least 3 elements will be analyzed.
     *
     * @return the boolean
     */
    protected boolean checkPredictionAnalysis() {
        return statistic.size() >= properties.getMinimalValuesForAnalysis();
    }

    /**
     * Log prediction.
     *
     * @param prediction the prediction
     */
    protected void logPrediction(Optional<Integer> prediction) {
        prediction.ifPresentOrElse(resultValue -> logger.debug("predict() - Predict is successful. Offered bid value: [{}]", resultValue),
                () -> logger.debug("predict() - Predict is unsuccessful."));
    }
}
