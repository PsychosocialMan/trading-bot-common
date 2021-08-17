package de.energy.optimax.trading.bot.property;

public class PredictorProperties {

    private int retries = 5;
    private int accuracy = 10;

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public String toString() {
        return "PredictorProperties{" +
                "retries=" + retries +
                ", accuracy=" + accuracy +
                '}';
    }
}
