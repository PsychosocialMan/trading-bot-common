package de.energy.optimax.trading.bot;

import de.energy.optimax.trading.bot.property.BidderProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * <h2>Main Trading bot Spring configuration. </h2>
 * <p>Scans all inner packages to put them in context.</p>
 * <p>Scans properties
 * in application yaml anf bind them to <code>@ConfigurationProperties</code>.</p>
 *
 * @author Smirnov Kirill
 */
@Configuration
@ComponentScan
@ConfigurationPropertiesScan
public class TradingBotConfiguration {

    /**
     * The Logger to log init and destroy main entry point events.
     */
    private final Logger logger = LoggerFactory.getLogger(TradingBotConfiguration.class);

    /**
     * Application properties.
     */
    private final BidderProperties properties;

    /**
     * Instantiates a new Trading bot configuration.
     *
     * @param properties the properties
     */
    @Autowired
    public TradingBotConfiguration(BidderProperties properties) {
        this.properties = properties;
    }

    /**
     * Init event. Logs initialization of the main entry point bean with the application properties.
     */
    @PostConstruct
    public void init() {
        logger.info("Context was initialized. Application properties: [{}]", properties);
    }

    /**
     * Destroy event. Logs shutting down the context.
     */
    @PreDestroy
    public void destroy() {
        logger.info("Context is shutting down...");
    }

}
