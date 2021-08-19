package de.energy.optimax.trading.bot.property;

/**
 * The Predictor properties. In this bidder's algorithm we use patterns to predict opponent's behavior.
 *
 * @author Smirnov Kirill
 */
public class PredictorProperties {

    /**
     * Predictors can guess the result and can make a mistake. This property is used to determine the number of
     * predicted successively opponent's bids so that we listen to him.
     */
    private int successfulPredictionCountCriteria = 3;
    /**
     * If you need to predict values by patterns, you have to analyze previous bids. This property is used to specify
     * minimal number of statistic that must be presented in auction.
     */
    private int minimalValuesForAnalysis = 5;

    /**
     * Accuracy for predictors. If the predicted value is in the vicinity of this delta, then the prediction is
     * successful. Unit of measure - QU (Quantity unit).
     */
    private int accuracy = 1;

    /**
     * Gets successful prediction count criteria.
     *
     * @return the successful prediction count criteria
     */
    public int getSuccessfulPredictionCountCriteria() {
        return successfulPredictionCountCriteria;
    }

    /**
     * Sets successful prediction count criteria.
     *
     * @param successfulPredictionCountCriteria the successful prediction count criteria
     */
    public void setSuccessfulPredictionCountCriteria(int successfulPredictionCountCriteria) {
        this.successfulPredictionCountCriteria = successfulPredictionCountCriteria;
    }

    /**
     * Gets accuracy.
     *
     * @return the accuracy
     */
    public int getAccuracy() {
        return accuracy;
    }

    /**
     * Sets accuracy.
     *
     * @param accuracy the accuracy
     */
    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * Gets minimal values for analysis.
     *
     * @return the minimal values for analysis
     */
    public int getMinimalValuesForAnalysis() {
        return minimalValuesForAnalysis;
    }

    /**
     * Sets minimal values for analysis.
     *
     * @param minimalValuesForAnalysis the minimal values for analysis
     */
    public void setMinimalValuesForAnalysis(int minimalValuesForAnalysis) {
        this.minimalValuesForAnalysis = minimalValuesForAnalysis;
    }

    @Override
    public String toString() {
        return "PredictorProperties{" +
                "retries=" + successfulPredictionCountCriteria +
                ", accuracy=" + accuracy +
                '}';
    }
}
