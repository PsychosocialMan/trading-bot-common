package de.energy.optimax.trading.bot.unit.service;

import de.energy.optimax.trading.bot.BaseTest;
import de.energy.optimax.trading.bot.data.BidRound;
import de.energy.optimax.trading.bot.service.bidder.BidderService;
import de.energy.optimax.trading.bot.service.predictor.impl.AllTheSameBidsPredictor;
import de.energy.optimax.trading.bot.service.predictor.impl.ArithmeticProgressionPredictor;
import de.energy.optimax.trading.bot.service.predictor.impl.DeltaBasedPredictor;
import de.energy.optimax.trading.bot.service.predictor.impl.IncrementYourBidPredictor;
import de.energy.optimax.trading.bot.util.ApproxUtil;
import de.energy.optimax.trading.bot.util.GenerateBidAlgorithm;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class SmartBidderServiceTest extends BaseTest {

    @MockBean
    @Autowired
    private AllTheSameBidsPredictor predictor1;

    @MockBean
    @Autowired
    private ArithmeticProgressionPredictor predictor2;

    @MockBean
    @Autowired
    private DeltaBasedPredictor predictor3;

    @MockBean
    @Autowired
    private IncrementYourBidPredictor predictor4;

    @Autowired
    private BidderService bidderService;

    private int remainingCash;
    private int remainingQuantity;
    private int wonQuantity;
    private int remainingOpponentCash;
    private int initialCash;
    private int initialQuantity;
    private GenerateBidAlgorithm generateBidAlgorithm;
    final List<BidRound> bids = new LinkedList<>();

    @BeforeMethod
    public void setInitValues() {
        initialQuantity = 20;
        initialCash = 85;

        remainingCash = initialCash;
        remainingQuantity = initialQuantity;
        wonQuantity = 0;
        remainingOpponentCash = initialCash;

        bidderService.init(initialQuantity, initialCash);

        bids.clear();
        generateBidAlgorithm = new GenerateBidAlgorithm(bids, initialQuantity, initialCash);

    }

    @AfterMethod
    public void reset() {
        Mockito.reset(predictor1, predictor2, predictor3, predictor4);
    }

    @Test(testName = "Check bidder service work when bidders disabled")
    public void checkIfPredictorsDisabled() {


        List.of(predictor1, predictor2, predictor3, predictor4).forEach(predictor -> {
            when(predictor.canPredict()).thenReturn(false);
            when(predictor.predict()).thenReturn(Optional.empty());
        });

        Assert.assertEquals(bidderService.getResultCash(), initialCash);
        Assert.assertEquals(bidderService.getResultQuantity(), 0);


        var firstBid = bidderService.predictNextBid();
        // 2 cause of random percents
        Assert.assertTrue(Math.abs(firstBid - generateBidAlgorithm.generateAvgBid()) <= 2);

        remainingQuantity -= BidRound.QUANTITY_PER_ROUND;
        remainingCash -= firstBid;
        remainingOpponentCash -= 2;
        wonQuantity += incrementWon(firstBid, 2);

        bidderService.addBidStatistics(firstBid, 2);


        bids.add(new BidRound(firstBid, 2));

        // -1 cause last bid
        for (int i = 1; i < initialQuantity / 2 - 1; i++) {

            var bid = bidderService.predictNextBid();

            var expectedBid = generateBidAlgorithm.generateBid(
                    remainingCash,
                    remainingOpponentCash,
                    remainingQuantity,
                    wonQuantity
            );

            // 2 cause of random percents
            Assert.assertTrue(Math.abs(bid - expectedBid) <= 2);

            var otherBid = 5;

            remainingQuantity -= BidRound.QUANTITY_PER_ROUND;
            remainingCash -= firstBid;
            remainingOpponentCash -= otherBid;
            wonQuantity += incrementWon(firstBid, otherBid);

            bidderService.addBidStatistics(bid, otherBid);


            bids.add(new BidRound(bid, otherBid));
        }
    }

    @Test(testName = "Check bidder service work when bidders return correct values")
    public void checkIfPredictorsReturnedCorrectValue() {
        List.of(predictor1, predictor2, predictor3, predictor4).forEach(predictor -> {
            when(predictor.canPredict()).thenReturn(true);
            when(predictor.predict()).thenReturn(Optional.of(8));
        });

        Assert.assertEquals(bidderService.getResultCash(), initialCash);
        Assert.assertEquals(bidderService.getResultQuantity(), 0);

        var firstBid = bidderService.predictNextBid();
        // 2 cause of random percents
        Assert.assertTrue(Math.abs(firstBid - generateBidAlgorithm.generateAvgBid()) <= 2);

        remainingQuantity -= BidRound.QUANTITY_PER_ROUND;
        remainingCash -= firstBid;
        remainingOpponentCash -= 8;
        wonQuantity += incrementWon(firstBid, 8);

        bidderService.addBidStatistics(firstBid, 8);


        bids.add(new BidRound(firstBid, 8));

        for (int i = 1; i < initialQuantity / 2; i++) {

            var bid = bidderService.predictNextBid();

            var expectedBid = 8 + new ApproxUtil().approx(initialQuantity, initialCash, 10);

            expectedBid = remainingCash < expectedBid ? 0 : expectedBid;
            Assert.assertTrue(Math.abs(bid - expectedBid) <= 1);

            Assert.assertTrue(Math.abs(firstBid - generateBidAlgorithm.generateAvgBid()) <= 2);
            var otherBid = 8;

            remainingQuantity -= BidRound.QUANTITY_PER_ROUND;
            remainingCash -= firstBid;
            remainingOpponentCash -= otherBid;
            wonQuantity += incrementWon(firstBid, otherBid);

            bidderService.addBidStatistics(bid, otherBid);

            bids.add(new BidRound(bid, otherBid));
        }
    }

    private int incrementWon(int yourBid, int otherBid) {
        int profit;

        if (yourBid > otherBid) {
            profit = 2;
        } else if (yourBid == otherBid) {
            profit = 1;
        } else {
            profit = 0;
        }

        return profit;
    }

}
