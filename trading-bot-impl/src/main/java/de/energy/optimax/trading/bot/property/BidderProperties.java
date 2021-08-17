package de.energy.optimax.trading.bot.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Bidder properties. Customize your auction bidder.
 *
 * @author Smirnov Kirill
 */
@ConfigurationProperties(prefix = "bidder")
public class BidderProperties {

    @NestedConfigurationProperty
    private PredictorProperties predictor = new PredictorProperties();


    public PredictorProperties getPredictor() {
        return predictor;
    }

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
