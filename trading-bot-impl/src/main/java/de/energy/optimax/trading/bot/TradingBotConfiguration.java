package de.energy.optimax.trading.bot;

import de.energy.optimax.trading.bot.property.BidderProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

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

    private final Logger logger = LoggerFactory.getLogger(TradingBotConfiguration.class);

    private final BidderProperties properties;

    @Autowired
    public TradingBotConfiguration(BidderProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        logger.info("Context was initialized. Application properties: [{}]", properties);
    }

}
