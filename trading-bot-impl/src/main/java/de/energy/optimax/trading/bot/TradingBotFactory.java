package de.energy.optimax.trading.bot;

import auction.Bidder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * The type Trading bot factory. Entry point for non-Spring applications. Usage example:
 * <p>
 * <code>
 * var bidder = TradingBotFactory.getBidder();
 * </code>
 * </p>
 *
 * @author Smirnov Kirill
 */
public class TradingBotFactory {
    /**
     * Instantiates a new Trading bot factory. Private cause Factory is static.
     */
    private TradingBotFactory() {
    }

    /**
     * Gets target bidder.
     *
     * @return the bidder.
     */
    public static Bidder getBidder() {
        return new AnnotationConfigApplicationContext(TradingBotConfiguration.class)
                .getBean(Bidder.class);
    }
}
