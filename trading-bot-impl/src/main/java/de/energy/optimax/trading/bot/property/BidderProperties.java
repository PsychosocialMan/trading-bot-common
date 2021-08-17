package de.energy.optimax.trading.bot.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Bidder properties. Customize your auction bidder.
 *
 * @author Smirnov Kirill
 */
@ConfigurationProperties(prefix = "bidder")
public class BidderProperties {

    private BidderType type = BidderType.MIXED;

    /**
     * Gets type of the bidder. <br/> Can be these types:
     * <ul>
     *     <li>MIXED</li>
     *     <li>NEURAL</li>
     *     <li>GAME_THEORY</li>
     *     <li>DUMMY</li>
     * </ul>
     *
     * @return the Bidder type
     *
     * @see BidderType
     */
    public BidderType getType() {
        return type;
    }

    /**
     * Sets type of the bidder. Usually set by application.yaml.
     *
     * @param type the type
     *
     * @see BidderType
     */
    public void setType(BidderType type) {
        this.type = type;
    }

}
