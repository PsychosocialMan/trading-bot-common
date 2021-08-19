package de.energy.optimax.trading.bot.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Bidder properties. Customize your auction bidder. Deserialized properties from application.yaml.
 *
 * @author Smirnov Kirill
 */
@ConfigurationProperties(prefix = "bidder")
public class BidderProperties {

    /**
     * The Predictor properties. In this bidder's algorithm we use patterns to predict opponent's behavior.
     */
    @NestedConfigurationProperty
    private PredictorProperties predictor = new PredictorProperties();


    /**
     * Gets predictor's properties.
     *
     * @return the predictor
     */
    public PredictorProperties getPredictor() {
        return predictor;
    }

    /**
     * Sets predictor's properties. Is used by Spring.
     *
     * @param predictor the predictor
     */
    public void setPredictor(PredictorProperties predictor) {
        this.predictor = predictor;
    }


    @Override
    public String toString() {
        return "BidderProperties{" +
                "predictor=" + predictor +
                '}';
    }

}
