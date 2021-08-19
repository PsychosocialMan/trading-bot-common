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

/**
 * Smart bidder service. Main application service.
 * <p>
 * <p>
 * The work algorithm is as follows:
 *     <ol>
 *         <li>If this is the first bid, we generate the average value for the bid, taking into account the approximation.</li>
 *         <li>Otherwise, we try to predict the logic of the opponent. For this, many predictors are used.
 *         After each attempt to predict the behavior, the maximum is taken from the results obtained and incremented (the calculated accuracy is used for the increment).</li>
 *         <li>If none of the predictors recognized the opponent's logic, then a bid is generated based on the statistics of the previous rounds of the auction.</li>
 *         <li>However, if the predicted rate is greater than the opponent's remaining cash, the value of the opponent's remaining cash is incremented.</li>
 *         <li>Accordingly, if the predicted rate is greater than our remaining cash, we pass.</li>
 *     </ol>
 * <p>
 * Predictors' and bid-generator's algorithm will be covered in the documentation of the respective classes and packages.
 *
 * @author Smirnov Kirill
 * @see GenerateBidAlgorithm
 * @see Predictor
 */
@Service
public class SmartBidderService implements BidderService {

    /**
     * The Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The Initial cash (in MU).
     */
    private int initialCash;
    /**
     * The Remaining cash (after N rounds).
     */
    private int remainingCash;
    /**
     * The Remaining opponent cash (after N rounds).
     */
    private int remainingOpponentCash;

    /**
     * The Initial quantity (in QU).
     */
    private int initialQuantity;
    /**
     * The Quantity counter (in QU). Counter of won QU.
     */
    private int quantityCounter;
    /**
     * The Remaining quantity in round.
     */
    private int remainingQuantity;

    /**
     * The Generate bid algorithm.
     *
     * @see GenerateBidAlgorithm
     */
    private GenerateBidAlgorithm generateBidAlgorithm;

    /**
     * The Bids. List of bids in round. Implemented as <code>LinkedList</code> as we will use elements by order and by
     * descending.
     */
    private final List<BidRound> bids = new LinkedList<>();
    /**
     * The Predictors. All the predictors which marked as Spring <code>@Component</code> annotation (or other ways to
     * create Spring bean)
     */
    private final List<Predictor> predictors;

    /**
     * The Approximation util.
     */
    private final ApproxUtil approxUtil = new ApproxUtil();

    /**
     * Instantiates a new Smart bidder service.
     *
     * @param predictors the predictors (filled bu Spring Application Context)
     */
    @Autowired
    public SmartBidderService(List<Predictor> predictors) {
        this.predictors = predictors;
    }

    @Override
    public int predictNextBid() {
        // Generate new bid by our algorithm.
        var predictedBid = predictNextBidWithoutCashAnalyze();

        // If generated bid is greater than our remaining budget, chose our remaining budget as a bid.
        return Math.min(predictedBid, remainingCash);
    }

    /**
     * Predict next bid without remaining cash analyze.
     *
     * @return bid in MU
     *
     * @see GenerateBidAlgorithm
     * @see #predictOpponentLogic()
     */
    private int predictNextBidWithoutCashAnalyze() {

        // If it is our first bid, generate average bid.
        if (bids.isEmpty()) {
            logger.debug("predictNextBidWithoutCashAnalyze() - Previous bids not found. Generating first bid.");
            return generateFirstBid();
        }

        // Try to predict opponent's logic.
        var predictedOpponentBid = predictOpponentLogic();

        // If prediction is unsuccessful, generate bid by our specific algorithm.
        if (predictedOpponentBid.isEmpty()) {
            logger.debug("predictNextBidWithoutCashAnalyze() - Opponent's logic cannot be understood. Generating bid.");
            return generateBid();
        }

        // If opponent's cash is lower opponent's bid, just increment opponent's remaining cash.
        if (predictedOpponentBid.get() >= remainingOpponentCash) {
            return remainingOpponentCash + 1;
        }

        // Compute 10% of average bid for a round to use it as approximation.
        var incrementAccuracy = approxUtil.approx(initialQuantity, initialCash, 10);

        // If we don't have enough MU to beat predicted bid, just pass.
        if (predictedOpponentBid.get() + incrementAccuracy > remainingCash) {
            logger.debug("predictNextBidWithoutCashAnalyze() - Opponent's logic was understood. But we don't have MU to beat him. Bid 0.");
            return 0;
        } else {
            // Else just increment our bid with accuracy (to win 100%)
            logger.debug("predictNextBidWithoutCashAnalyze() - Opponent's logic was understood. Increasing his bid up to 10%.");
            return predictedOpponentBid.get() + incrementAccuracy;
        }
    }

    @Override
    public void addBidStatistics(int ownBid, int opponentBid) {
        var bidRound = new BidRound(ownBid, opponentBid);

        // Adding bid round to our statistic.
        this.bids.add(bidRound);
        this.predictors.forEach(predictor -> predictor.addBidInfo(bidRound));

        // Decreasing our and opponent's cash.
        this.remainingCash -= ownBid;
        this.remainingOpponentCash -= opponentBid;

        // Increasing our profit (+2 if victory, +1 if draw, 0 if defeat).
        this.quantityCounter += bidRound.getProfit();

        // Decreasing remaining quantity/rounds
        this.remainingQuantity -= BidRound.QUANTITY_PER_ROUND;

        logger.info("addBidStatistics() - Remaining cash: [{}]. Number of bids won: [{}]",
                remainingCash,
                quantityCounter);
    }

    @Override
    public void init(int quantity, int cash) {
        // Quantity initializing.
        this.quantityCounter = 0;
        this.initialQuantity = quantity;
        this.remainingQuantity = quantity;

        // Cash initializing.
        this.remainingCash = cash;
        this.initialCash = cash;
        this.remainingOpponentCash = cash;

        // Generating algorithm.
        this.generateBidAlgorithm = new GenerateBidAlgorithm(bids, quantity, cash);

        // Clear all the predictors.
        this.predictors.forEach(Predictor::clear);
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

    /**
     * Predict opponent logic.
     *
     * @return the optional
     */
    private Optional<Integer> predictOpponentLogic() {
        var intSummaryStatistic = predictors.parallelStream()
                // Invoke prediction
                .filter(predictor -> predictor.predict().isPresent())
                // Check if predictor can predict and we can trust him
                .filter(Predictor::canPredict)
                .map(Predictor::predict)
                .filter(Optional::isPresent)
                // Getting stats about predicted values
                .collect(Collectors.summarizingInt(Optional::get));
        // If predicted values are presented, take max.
        return intSummaryStatistic.getCount() == 0 ?
                Optional.empty() :
                Optional.of(intSummaryStatistic.getMax() + 1);
    }

    /**
     * Generate first bid.
     *
     * @return bid in MU
     */
    private int generateFirstBid() {
        return generateBidAlgorithm.generateAvgBid();
    }

    /**
     * Generate bid.
     *
     * @return bid in MU
     */
    private int generateBid() {
        var generatedBid = generateBidAlgorithm.generateBid(remainingCash,
                remainingOpponentCash,
                remainingQuantity,
                quantityCounter);
        logger.debug("generateBid() - Generated bid: [{}]", generatedBid);
        return generatedBid;
    }
}