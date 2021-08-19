package de.energy.optimax.trading.bot.integration;

import de.energy.optimax.trading.bot.BaseTest;
import de.energy.optimax.trading.bot.auctioneer.Auctioneer;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IntegrationTest extends BaseTest {

    @Autowired
    private Auctioneer auctioneer;

    @DataProvider(name = "testBidderMappingDataProvider")
    public Object[][] testBidderMappingDataProvider() {
        return new Object[][]{
                {Paths.get("/src/test/resources/bidders/1_usual_mapping.json")},
                {Paths.get("/src/test/resources/bidders/2_all_the_same_bid.json")},
                {Paths.get("/src/test/resources/bidders/3_all_the_same_bid_with_accuracy.json")},
                {Paths.get("/src/test/resources/bidders/4_arithmetic_progression.json")},
                {Paths.get("/src/test/resources/bidders/5_one_bid.json")},
                {Paths.get("/src/test/resources/bidders/6_few_bids.json")},
                {Paths.get("/src/test/resources/bidders/7_delta_based.json")},
                {Paths.get("/src/test/resources/bidders/8_increment_your_bid.json")},
        };
    }

    @Test(testName = "Check that our smart bidder will win test bidder",
            dataProvider = "testBidderMappingDataProvider")
    public void checkSmartBidderWillWin(Path testBidderMappingFilePath) throws IOException {
        auctioneer.init(testBidderMappingFilePath);
        auctioneer.play();
        Assert.assertTrue(auctioneer.isOurBidderWon(), "Check if our bidder won");
    }
}
