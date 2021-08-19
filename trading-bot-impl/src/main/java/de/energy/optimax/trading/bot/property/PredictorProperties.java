package de.energy.optimax.trading.bot.property;

public class PredictorProperties {

    private int successfulPredictionCountCriteria = 3;
    private int minimalValuesForAnalysis = 5;

    // in QU
    private int accuracy = 1;

    public int getSuccessfulPredictionCountCriteria() {
        return successfulPredictionCountCriteria;
    }

    public void setSuccessfulPredictionCountCriteria(int successfulPredictionCountCriteria) {
        this.successfulPredictionCountCriteria = successfulPredictionCountCriteria;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public int getMinimalValuesForAnalysis() {
        return minimalValuesForAnalysis;
    }

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
