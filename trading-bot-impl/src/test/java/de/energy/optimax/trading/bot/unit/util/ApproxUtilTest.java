package de.energy.optimax.trading.bot.unit.util;

import de.energy.optimax.trading.bot.util.ApproxUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class ApproxUtilTest {

    private final ApproxUtil approxUtil = new ApproxUtil();

    @Test(testName = "Check approx method")
    public void checkApprox() {
        Assert.assertEquals(approxUtil.approx(50, 74, 10), 1);
        Assert.assertEquals(approxUtil.approx(500, 7402, 18), 6);
    }

    @Test(testName = "Check random percents approx method")
    public void checkRandomPercents() {
        var percent = approxUtil.getRandomApproxPercents();
        Assert.assertTrue(percent <= 20 && percent >= 0);
    }

    @Test(testName = "Check divideAndCeil method")
    public void checkDivideAndCeil() {
        Assert.assertEquals(approxUtil.divideAndCeil(8, 10), 1);
        Assert.assertEquals(approxUtil.divideAndCeil(4, 3), 2);
        Assert.assertEquals(approxUtil.divideAndCeil(74, 10), 8);
    }

}
