package de.energy.optimax.trading.bot.util;

import java.security.SecureRandom;

public class ApproxUtil {

    public ApproxUtil() {
        // For easily mocking in tests static util classes are not recommended.
    }

    public int approx(int quantity, int cash, int percentsToApprox) {
        return divideAndCeil(divideAndCeil(2 * cash, quantity) * percentsToApprox, 100);
    }

    public int getRandomApproxPercents() {
        return getRandomApproxPercents(20);
    }

    public int getRandomApproxPercents(int percentsBound) {
        return new SecureRandom().nextInt(percentsBound);
    }


    public int divideAndCeil(int first, int second) {
        return (int) Math.ceil(first / (double) second);
    }

}
