package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.CoinRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        return postAdvertisement(AssetAdvertisementType.SELL_ADVERTISEMENT, sellerId, assetSymbol, quantityToSell, sellingPrice);
    }

    public AssetAdvertisement postBuyAdvertisement(Long buyerId, String assetSymbol, int quantityToBuy, double buyPrice) {
        return postAdvertisement(AssetAdvertisementType.BUY_ADVERTISEMENT, buyerId, assetSymbol, quantityToBuy, buyPrice);
    }

    public Transaction informTransaction(Long interestedUserId, Long advertisementId, int quantityToTransfer) {
        var interestedUser = userService.findUserById(interestedUserId);
        var assetAdvertisement = assetAdvertisementsRepository.findById(advertisementId).orElseThrow(() -> new EntityNotFoundException("advertisement.not_found"));

        var newTransaction = new Transaction(interestedUser, assetAdvertisement, quantityToTransfer, clock.now());

        return transactionsRepository.save(newTransaction);
    }

    public Transaction confirmTransaction(Long userId, Long transactionToConfirmId) {
        var user = userService.findUserById(userId);
        var transaction = transactionsRepository.findById(transactionToConfirmId).orElseThrow(() -> new EntityNotFoundException("transaction.not_found"));

        transaction.confirmBy(user);
        giveReputationPointsForConfirmedTransaction(user, transaction);

        transactionsRepository.save(transaction);
        userRepository.save(user);

        return transaction;
    }

    public Transaction cancelTransaction(Long userId, Long transactionToCancelId) {
        var user = userService.findUserById(userId);
        var transaction = transactionsRepository.findById(transactionToCancelId).orElseThrow(() -> new EntityNotFoundException("transaction.not_found"));

        transaction.cancelBy(user);
        looseReputationPointsForCancelTransaction(transaction.publisher());

        userRepository.save(user);

        return transaction;
    }

    public List<Transaction> findTransactionsInformedBy(Long userId) {
        return transactionsRepository.findAllByInterestedUserId(userId);
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

    public TradedVolume getTradedVolumeBetweenDatesForUser(Long userId, LocalDateTime start, LocalDateTime end){
        User user = userService.findUserById(userId);

        List<Transaction> completedTransactions = transactionsRepository.findConfirmedTransactionsBetweenDatesFor(userId, start, end);
        List<ActiveCrypto> activeCryptos = getActiveCryptos(completedTransactions);

        Double tradedValueInUsd = activeCryptos.stream().mapToDouble(activeCrypto -> activeCrypto.getFinalPriceInUSD()).sum();
        Double tradedValueInPesos = activeCryptos.stream().mapToDouble(activeCrypto -> activeCrypto.getFinalPriceInPesos()).sum();

        return new TradedVolume(user, clock.now(), tradedValueInUsd.floatValue(), tradedValueInPesos.floatValue(), activeCryptos);
    }

    protected List<ActiveCrypto> getActiveCryptos(List<Transaction> transactions) {
        return transactions.stream().map(transaction -> {
            CoinRate coinRate = rateService.getCoinRate(transaction.assetSymbol());

            return new ActiveCrypto(
                    transaction.assetSymbol(),
                    transaction.quantity().floatValue(),
                    coinRate.usdPrice(),
                    coinRate.pesosPrice()
            );
        }).collect(Collectors.toList());
    }

    public AssetAdvertisement postAdvertisement(AssetAdvertisementType assetAdvertisementType, Long publisherId, String assetSymbol, int quantity, double price) {
        var publisher = userService.findUserById(publisherId);

        var newAdvertisement = new AssetAdvertisement(assetAdvertisementType, assetSymbol, quantity, price, publisher, clock.now());

        return assetAdvertisementsRepository.save(newAdvertisement);
    }

    protected void giveReputationPointsForConfirmedTransaction(User user, Transaction transaction) {
        var now = clock.now();
        var limitTime = transaction.startLocalDateTime().plusMinutes(30);

        var pointsToGive = now.isBefore(limitTime) ? 10 : 5;

        user.receiveReputationPoints(pointsToGive);
    }

    private void looseReputationPointsForCancelTransaction(User user) {
        user.looseReputationPoints(20);
    }

}
