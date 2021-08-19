package de.energy.optimax.trading.bot.annotation;

import de.energy.optimax.trading.bot.TradingBotConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Import smart bidder. Imports all required components and beans for the autowiring <code>Bidder</code>.
 * Main entry point for Spring applications.
 *
 * @author Smirnov Kirill
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Import(TradingBotConfiguration.class)
public @interface ImportSmartBidder {
}
