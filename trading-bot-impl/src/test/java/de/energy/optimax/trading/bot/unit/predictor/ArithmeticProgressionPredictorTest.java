package de.energy.optimax.trading.bot.unit.predictor;

import de.energy.optimax.trading.bot.data.BidRound;
import de.energy.optimax.trading.bot.service.predictor.impl.ArithmeticProgressionPredictor;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class ArithmeticProgressionPredictorTest extends BasePredictorTest<ArithmeticProgressionPredictor> {

    @Autowired
    public void setPredictor(ArithmeticProgressionPredictor predictor) {
        this.predictor = predictor;
    }

    @Override
    protected List<BidRound> testData() {
        return List.of(
                new BidRound(8, 1),
                new BidRound(9, 3),
                new BidRound(18, 5),
                new BidRound(2, 7),
                new BidRound(5, 8),
                new BidRound(8, 10),
                new BidRound(7, 12)
        );
    }


    @Test(testName = "Check ArithmeticProgressionPredictor predict()")
    public void checkPrediction() {
        var predictedValue = predictor.predict();
        Assert.assertTrue(predictedValue.isPresent());
        Assert.assertEquals((int) predictedValue.get(), 14);
    }
}
