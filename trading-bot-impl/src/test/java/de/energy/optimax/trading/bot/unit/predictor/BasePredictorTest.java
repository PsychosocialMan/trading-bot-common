package de.energy.optimax.trading.bot.unit.predictor;

import de.energy.optimax.trading.bot.BaseTest;
import de.energy.optimax.trading.bot.data.BidRound;
import de.energy.optimax.trading.bot.service.predictor.Predictor;
import org.springframework.test.context.TestPropertySource;
import org.testng.annotations.BeforeClass;

import java.util.List;

@TestPropertySource(properties = "bidder.predictor.successful-prediction-count-criteria=1")
public abstract class BasePredictorTest<T extends Predictor> extends BaseTest {
    protected T predictor;

    protected abstract List<BidRound> testData();

    @BeforeClass
    public void initPredictor() {
        testData().forEach(round -> predictor.addBidInfo(round));
    }

}
