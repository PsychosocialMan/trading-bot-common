package de.energy.optimax.trading.bot.util;

import java.security.SecureRandom;

public class ApproxUtil {

    public ApproxUtil() {
        // For easily mocking in tests static util classes are not recommended.
    }

    public int approx(int quantity, int cash, int percentsToApprox) {
        return 2 * cash / quantity * percentsToApprox / 100;
    }

    public int getRandomApproxPercents() {
        return new SecureRandom().nextInt(20);
    }

}
