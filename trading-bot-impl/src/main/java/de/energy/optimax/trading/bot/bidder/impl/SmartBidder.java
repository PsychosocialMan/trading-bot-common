package de.energy.optimax.trading.bot.bidder.impl;

import de.energy.optimax.trading.bot.bidder.AbstractBidder;
import de.energy.optimax.trading.bot.service.bidder.impl.SmartBidderService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class SmartBidder extends AbstractBidder {

    public SmartBidder(SmartBidderService bidderService) {
        super(bidderService);
    }
}
