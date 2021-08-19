package de.energy.optimax.trading.bot.unit.predictor;

import de.energy.optimax.trading.bot.data.BidRound;
import de.energy.optimax.trading.bot.service.predictor.impl.AllTheSameBidsPredictor;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class AllTheSameBidsPredictorTest extends BasePredictorTest<AllTheSameBidsPredictor> {

    @Autowired
    public void setPredictor(AllTheSameBidsPredictor predictor) {
        this.predictor = predictor;
    }

    @Override
    protected List<BidRound> testData() {
        return List.of(
                new BidRound(7, 8),
                new BidRound(6, 8),
                new BidRound(10, 7),
                new BidRound(11, 8),
                new BidRound(12, 9)
        );
    }


    @Test(testName = "Check AllTheSameBidsPredictor predict()")
    public void checkPrediction() {
        var predictedValue = predictor.predict();
        Assert.assertTrue(predictedValue.isPresent());
        Assert.assertEquals((int) predictedValue.get(), 9);
    }
}
