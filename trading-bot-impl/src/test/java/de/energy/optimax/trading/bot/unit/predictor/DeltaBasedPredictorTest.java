package de.energy.optimax.trading.bot.unit.predictor;

import de.energy.optimax.trading.bot.data.BidRound;
import de.energy.optimax.trading.bot.service.predictor.impl.DeltaBasedPredictor;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class DeltaBasedPredictorTest extends BasePredictorTest<DeltaBasedPredictor> {

    @Autowired
    public void setPredictor(DeltaBasedPredictor predictor) {
        this.predictor = predictor;
    }

    @Override
    protected List<BidRound> testData() {
        return List.of(
                new BidRound(8, 8),
                new BidRound(9, 11),
                new BidRound(18, 11),
                new BidRound(2, 25),
                new BidRound(5, 12),
                new BidRound(8, 4),
                new BidRound(7, 12)
        );
    }


    @Test(testName = "Check DeltaBasedPredictor predict()")
    public void checkPrediction() {
        var predictedValue = predictor.predict();
        Assert.assertTrue(predictedValue.isPresent());
        Assert.assertEquals((int) predictedValue.get(), 9);

        predictor.addBidInfo(new BidRound(10, 9));

        predictedValue = predictor.predict();
        Assert.assertTrue(predictedValue.isPresent());
        Assert.assertEquals((int) predictedValue.get(), 11);
    }
}
