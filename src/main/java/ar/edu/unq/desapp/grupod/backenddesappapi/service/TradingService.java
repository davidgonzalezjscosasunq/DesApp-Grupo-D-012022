package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.*;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.clock.Clock;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.AssetAdvertisementsRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.TransactionsRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TradingService {

    @Autowired
    Clock clock;

    @Autowired
    UserService userService;

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
        var assetAdvertisement = assetAdvertisementsRepository.findById(advertisementId).orElseThrow(() -> new ModelException("Advertisement not found"));

        var newTransaction = new Transaction(interestedUser, assetAdvertisement, quantityToTransfer, clock.now());

        return transactionsRepository.save(newTransaction);
    }

    public void confirmTransaction(Long userId, Long transactionToConfirmId) {
        var user = userService.findUserById(userId);
        var transaction = transactionsRepository.findById(transactionToConfirmId).orElseThrow(() -> new ModelException("Transaction not found"));

        transaction.confirmBy(user);
        giveReputationPointsForConfirmedTransaction(user, transaction);

        transactionsRepository.save(transaction);
        userRepository.save(user);
    }

    public void cancelTransaction(Long userId, Long transactionToCancelId) {
        var user = userRepository.findById(userId).get();
        var transaction = transactionsRepository.findById(transactionToCancelId).orElseThrow(() -> new ModelException("Transaction not found"));

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

    protected AssetAdvertisement postAdvertisement(AssetAdvertisementType assetAdvertisementType, Long publisherId, String assetSymbol, int quantity, double price) {
        var publisher = userService.findUserById(publisherId);

        var newAdvertisement = new AssetAdvertisement(assetAdvertisementType, assetSymbol, quantity, price, publisher);

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
