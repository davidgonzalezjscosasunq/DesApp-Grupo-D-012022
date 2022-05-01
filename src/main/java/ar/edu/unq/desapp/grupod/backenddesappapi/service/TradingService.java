package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.GetCoinRateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.*;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.clock.Clock;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.AssetAdvertisementsRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.TransactionsRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.UserRepository;

@Service
@Transactional
public class TradingService {

    @Autowired
    Clock clock;

    @Autowired
    UserService userService;

    @Autowired
    RateService rateService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AssetAdvertisementsRepository assetAdvertisementsRepository;

    @Autowired
    TransactionsRepository transactionsRepository;

    // TODO: modelar dinero
    public AssetAdvertisement postSellAdvertisement(Long sellerId, String assetSymbol, Integer quantityToSell, Double sellingPrice) {
        return postAdvertisement(AssetAdvertisement.SELL_ADVERTISE_TYPE, sellerId, assetSymbol, quantityToSell, sellingPrice);
    }

    public AssetAdvertisement postBuyAdvertisement(Long buyerId, String assetSymbol, int quantityToBuy, double buyPrice) {
        return postAdvertisement(AssetAdvertisement.BUY_ADVERTISE_TYPE, buyerId, assetSymbol, quantityToBuy, buyPrice);
    }

    public Transaction informTransaction(Long interestedUserId, Long advertisementId, int quantityToTransfer) {
        var interestedUser = userService.findUserById(interestedUserId);
        var assetAdvertisement = assetAdvertisementsRepository.findById(advertisementId).orElseThrow(() -> new ModelException("Advertisement not found"));

        var newTransaction = new Transaction(interestedUser, assetAdvertisement, quantityToTransfer, clock.now());

        return transactionsRepository.save(newTransaction);
    }

    public void confirmTransaction(Long userId, Long transactionToConfirmId) {
        var user = userService.findUserById(userId);
        var transaction = transactionsRepository.findById(transactionToConfirmId).get();

        transaction.confirmBy(user);
        giveReputationPointsForConfirmedTransaction(user, transaction);

        transactionsRepository.save(transaction);
        userRepository.save(user);
    }

    public void cancelTransaction(Long userId, Long transactionToCancelId) {
        var user = userRepository.findById(userId).get();
        var transaction = transactionsRepository.findById(transactionToCancelId).get();

        transaction.cancelBy(user);

        looseReputationPointsForCancelTransaction(transaction.publisher());

        userRepository.save(user);
    }

    public List<AssetAdvertisement> findSellAdvertisementsWithSymbol(String assetSymbol) {
        return assetAdvertisementsRepository.findSellAdvertisementsWithSymbol(assetSymbol);
    }

    public List<AssetAdvertisement> findBuyAdvertisementsWithSymbol(String assetSymbol) {
        return assetAdvertisementsRepository.findBuyAdvertisementsWithSymbol(assetSymbol);
    }

    public List<AssetAdvertisement> findAdvertisementsWithSymbol(String assetSymbol) {
        return assetAdvertisementsRepository.findAllByAssetSymbol(assetSymbol);
    }

    public List<Transaction> findTransactionsInformedBy(Long userId) {
        return transactionsRepository.findAllByInterestedUserId(userId);
    }

    public Volume getTradedVolumeBetweenDatesForUser(Long userId, LocalDateTime start, LocalDateTime end){
       List<Transaction> transactionsBetweenDates = transactionsRepository.findAllBetweenDatesByUserId(userId, start, end);
       List<Transaction> completedTransactions = (List<Transaction>) transactionsBetweenDates.stream().filter(transaction -> transaction.isConfirmed());
       User user = userRepository.findById(userId).get();
       LocalDateTime dateAndTimeRequest = clock.now();
       List<ActiveCrypto> assets = getActiveCryptos(completedTransactions);
       Double tradedValueInUsd = (assets.stream().mapToDouble(asset -> asset.nominalAmount * asset.currentPriceInUsd).sum());
       Double tradedValueInPesos = (assets.stream().mapToDouble(asset -> asset.nominalAmount * asset.currentPriceInPesos).sum());

       return new Volume(user, dateAndTimeRequest, tradedValueInUsd.floatValue(), tradedValueInPesos.floatValue(), assets);
    }

    protected List<ActiveCrypto> getActiveCryptos(List<Transaction> transactions) {
        List<ActiveCrypto> result = new ArrayList<>();
        for(Transaction transaction: transactions){
            String symbol = transaction.assetSymbol();
            Float quantity = transaction.quantity().floatValue();
            GetCoinRateResponse rate = rateService.getCoinRate(symbol);
            result.add(new ActiveCrypto(symbol, quantity, rate.usdPrice, rate.pesosPrice));
        }
        return result;
    }

    protected AssetAdvertisement postAdvertisement(String advertisementType, Long publisherId, String assetSymbol, int quantity, double price) {
        var publisher = userService.findUserById(publisherId);

        var newAdvertisement = new AssetAdvertisement(advertisementType, assetSymbol, quantity, price, publisher);

        return assetAdvertisementsRepository.save(newAdvertisement);
    }

    protected void giveReputationPointsForConfirmedTransaction(User user, Transaction transaction) {
        var now = clock.now();
        var limitTime = transaction.startLocalDateTime().plusMinutes(30);

        if (now.isBefore(limitTime)) {
            user.receiveReputationPoints(10);
        } else {
            user.receiveReputationPoints(5);
        }
    }

    private void looseReputationPointsForCancelTransaction(User user) {
        user.looseReputationPoints(20);
    }

}
