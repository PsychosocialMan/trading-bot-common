package de.energy.optimax.trading.bot;

import auction.Bidder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TradingBotFactory {
    private TradingBotFactory() {
    }

    public static Bidder getBidder() {
        return new AnnotationConfigApplicationContext(TradingBotConfiguration.class)
                .getBean(Bidder.class);
    }
}
