package de.energy.optimax.trading.bot.util;

import java.security.SecureRandom;

/**
 * The type Approximation utils.
 *
 * @author Smirnov Kirill
 */
public class ApproxUtil {

    /**
     * Instantiates a new Approx util.
     */
    public ApproxUtil() {
        // For easily mocking in tests static util classes are not recommended.
    }

    /**
     * Approximate average value of auction MU per round.
     *
     * @param quantity         the quantity
     * @param cash             the cash
     * @param percentsToApprox the percents to approx
     *
     * @return approximated percent of the average round MU.
     */
    public int approx(int quantity, int cash, int percentsToApprox) {
        return divideAndCeil(divideAndCeil(2 * cash, quantity) * percentsToApprox, 100);
    }

    /**
     * Gets random approx percents (from 0 to 20).
     *
     * @return the random approx percents
     */
    public int getRandomApproxPercents() {
        return getRandomApproxPercents(20);
    }

    /**
     * Gets random approx percents.
     *
     * @param percentsBound the percents bound
     *
     * @return the random approx percents
     */
    public int getRandomApproxPercents(int percentsBound) {
        return new SecureRandom().nextInt(percentsBound);
    }


    /**
     * Divide and ceil int.
     *
     * @param first  the first
     * @param second the second
     *
     * @return the int
     */
    public int divideAndCeil(int first, int second) {
        return (int) Math.ceil(first / (double) second);
    }

}
