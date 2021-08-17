package de.energy.optimax.trading.bot.service.bidder.impl;

import de.energy.optimax.trading.bot.data.BidRound;
import de.energy.optimax.trading.bot.service.bidder.BidderService;
import de.energy.optimax.trading.bot.service.predictor.Predictor;
import de.energy.optimax.trading.bot.util.ApproxUtil;
import de.energy.optimax.trading.bot.util.GenerateBidAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SmartBidderService implements BidderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private int initialCash;
    private int remainingCash;
    private int remainingOpponentCash;

    private int initialQuantity;
    private int quantityCounter;
    private int remainingQuantity;

    private GenerateBidAlgorithm generateBidAlgorithm;

    private final List<BidRound> bids = new LinkedList<>();
    private final List<Predictor> predictors;

    private final ApproxUtil approxUtil = new ApproxUtil();

    @Autowired
    public SmartBidderService(List<Predictor> predictors) {
        this.predictors = predictors;
    }

    @Override
    public int predictNextBid() {
        var predictedBid = predictNextBidWithoutCashAnalyze();

        return Math.min(predictedBid, remainingCash);
    }

    private int predictNextBidWithoutCashAnalyze() {
        if (bids.isEmpty()) {
            logger.debug("predictNextBidWithoutCashAnalyze() - Previous bids not found. Generating first bid.");
            return generateFirstBid();
        }
        var predictedOpponentBet = predictOpponentLogic();

        if (predictedOpponentBet.isEmpty()) {
            logger.debug("predictNextBidWithoutCashAnalyze() - Opponent's logic cannot be understood. Generating bid.");
            return generateBid();
        }
        if (predictedOpponentBet.get() < remainingOpponentCash) {
            return remainingOpponentCash + 1;
        }
        if (predictedOpponentBet.get() + 1 > remainingCash) {
            logger.debug("predictNextBidWithoutCashAnalyze() - Opponent's logic was understood. But we don't have MU to beat him. Bid 0.");
            return 0;
        } else {
            logger.debug("predictNextBidWithoutCashAnalyze() - Opponent's logic was understood. Increasing his bid up to 10%.");
            return predictedOpponentBet.get() + approxUtil.approx(initialQuantity, initialCash, 10);
        }
    }

    @Override
    public void addBidStatistics(int ownBid, int opponentBid) {
        var bidRound = new BidRound(ownBid, opponentBid);

        this.bids.add(bidRound);
        this.predictors.forEach(predictor -> predictor.addBidInfo(bidRound));
        this.remainingCash -= ownBid;
        this.remainingOpponentCash -= opponentBid;

        this.quantityCounter += bidRound.getProfit();
        this.remainingQuantity -= BidRound.QUANTITY_PER_ROUND;

        logger.info("addBidStatistics() - Remaining cash: [{}]. Number of bids won: [{}]",
                remainingCash,
                quantityCounter);
    }

    @Override
    public void init(int quantity, int cash) {
        this.quantityCounter = 0;
        this.initialQuantity = quantity;
        this.remainingQuantity = quantity;

        this.remainingCash = cash;
        this.initialCash = cash;
        this.remainingOpponentCash = cash;

        this.generateBidAlgorithm = new GenerateBidAlgorithm(bids, quantity, cash);
        logger.info("init() - Service was successfully initialized.");
    }

    @Override
    public int getResultCash() {
        return this.remainingCash;
    }

    @Override
    public int getResultQuantity() {
        return this.quantityCounter;
    }

    private Optional<Integer> predictOpponentLogic() {
        var intSummaryStatistic = predictors.stream()
                .filter(Predictor::canPredict)
                .map(Predictor::predict)
                .filter(Optional::isPresent)
                .collect(Collectors.summarizingInt(Optional::get));
        return intSummaryStatistic.getCount() == 0 ?
                Optional.empty() :
                Optional.of(intSummaryStatistic.getMax() + 1);
    }

    private int generateFirstBid() {
        return generateBidAlgorithm.generateAvgBid();
    }

    private int generateBid() {
        return generateBidAlgorithm.generateBid(remainingCash,
                remainingOpponentCash,
                remainingQuantity,
                quantityCounter);
    }
}