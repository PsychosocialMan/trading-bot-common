package de.energy.optimax.trading.bot.unit;

import auction.Bidder;
import de.energy.optimax.trading.bot.BaseTest;
import de.energy.optimax.trading.bot.TradingBotFactory;
import de.energy.optimax.trading.bot.bidder.impl.SmartBidder;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;


public class InterfaceTest extends BaseTest {

    @Autowired
    private Bidder targetBidder;

    @Test(testName = "Check non Spring API")
    public void checkFactoryTest() {
        Assert.assertEquals(TradingBotFactory.getBidder().getClass(), SmartBidder.class);
    }

    @Test(testName = "Check if context can be autowired")
    public void checkSpringApi() {
        Assert.assertNotNull(targetBidder);
        Assert.assertEquals(targetBidder.getClass(), SmartBidder.class);
    }
}
