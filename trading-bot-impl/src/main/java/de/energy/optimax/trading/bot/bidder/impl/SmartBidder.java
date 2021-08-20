package de.energy.optimax.trading.bot.bidder.impl;

import de.energy.optimax.trading.bot.bidder.AbstractBidder;
import de.energy.optimax.trading.bot.service.bidder.BidderService;
import de.energy.optimax.trading.bot.service.bidder.impl.SmartBidderService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The type Smart bidder. Main and at this moment the only one implementation of bidder. Main logic is delegated to
 * <code>SmartBidderService</code>. <code>@Primary</code> annotation was used to prevent conflicts.
 * Prototype is used because of single-thread behaviour of the Bidder.
 *
 * @author Smirnov Kirill
 * @see SmartBidderService
 */
@Component
@Primary
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SmartBidder extends AbstractBidder {

    /**
     * Instantiates a new Smart bidder.
     *
     * @param bidderService the bidder service
     *
     * @see AbstractBidder#AbstractBidder(BidderService)
     */
    public SmartBidder(SmartBidderService bidderService) {
        super(bidderService);
    }
}
