package de.energy.optimax.trading.bot.unit.predictor;

import de.energy.optimax.trading.bot.data.BidRound;
import de.energy.optimax.trading.bot.service.predictor.impl.IncrementYourBidPredictor;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class IncrementYourBidPredictorTest extends BasePredictorTest<IncrementYourBidPredictor> {

    @Autowired
    public void setPredictor(IncrementYourBidPredictor predictor) {
        this.predictor = predictor;
    }

    @Override
    protected List<BidRound> testData() {
        return List.of(
                new BidRound(8, 8),
                new BidRound(10, 9),
                new BidRound(8, 11),
                new BidRound(15, 9)
        );
    }


    @Test(testName = "Check IncrementYourBidPredictor predict()")
    public void checkPrediction() {
        var predictedValue = predictor.predict();
        Assert.assertTrue(predictedValue.isPresent());
        Assert.assertEquals((int) predictedValue.get(), 16);
    }
}
