package de.energy.optimax.trading.bot.service.predictor;

import de.energy.optimax.trading.bot.data.BidRound;
import de.energy.optimax.trading.bot.property.BidderProperties;
import de.energy.optimax.trading.bot.property.PredictorProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

/**
 * The type Abstract predictor.
 *
 * @author Smirnov Kirill
 */
public abstract class AbstractPredictor implements Predictor {

    protected List<BidRound> statistic = new LinkedList<>();
    protected PredictorProperties properties = new PredictorProperties();

    @Autowired
    public void setProperties(BidderProperties properties) {
        this.properties = properties.getPredictor();
    }

    @Override
    public void addBidInfo(BidRound round) {
        statistic.add(round);
    }
}
