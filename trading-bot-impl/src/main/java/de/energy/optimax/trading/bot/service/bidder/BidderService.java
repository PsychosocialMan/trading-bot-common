package de.energy.optimax.trading.bot.service.bidder;

public interface BidderService {
    int predictNextBid();

    void addBidStatistics(int ownBid, int opponentBid);

    void init(int quantity, int cash);

    int getResultCash();

    int getResultQuantity();
}
