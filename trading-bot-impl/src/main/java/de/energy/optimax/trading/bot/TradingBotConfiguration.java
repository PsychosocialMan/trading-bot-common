package de.energy.optimax.trading.bot;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Main Trading bot Spring configuration. <br/> Scans all inner packages to put them in context. <br/> Scans properties
 * in application yaml anf bind them to <code>@ConfigurationProperties</code>.
 *
 * @author Smirnov Kirill
 */
@Configuration
@ComponentScan
@ConfigurationPropertiesScan
public class TradingBotConfiguration {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String... args) {
        // Will be implemented later
        throw new UnsupportedOperationException();
    }
}
