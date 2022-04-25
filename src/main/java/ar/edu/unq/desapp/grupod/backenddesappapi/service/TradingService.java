package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.Transaction;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.AssetAdvertisement;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.ModelException;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.AssetAdvertisementsRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.TransactionsRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradingService {

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
        return postAdvertisement(AssetAdvertisement.SELL_ADVERTISE_TYPE, sellerId, assetSymbol, quantityToSell, sellingPrice);
    }

    public AssetAdvertisement postBuyAdvertisement(Long buyerId, String assetSymbol, int quantityToBuy, double buyPrice) {
        return postAdvertisement(AssetAdvertisement.BUY_ADVERTISE_TYPE, buyerId, assetSymbol, quantityToBuy, buyPrice);
    }

    public Transaction informTransaction(Long interestedUserId, Long advertisementId, int quantityToTransfer) {
        var interestedUser = userService.findUserById(interestedUserId);
        var assetAdvertisement = assetAdvertisementsRepository.findById(advertisementId).orElseThrow(() -> new ModelException("Advertisement not found"));

        var newTransaction = new Transaction(interestedUser, assetAdvertisement, quantityToTransfer);

        return transactionsRepository.save(newTransaction);
    }

    public void confirmTransaction(Long userId, Long transactionToConfirmId) {
        var user = userService.findUserById(userId);
        var transaction = transactionsRepository.findById(transactionToConfirmId).get();
        var advertisement = assetAdvertisementsRepository.findById(transaction.cryptoAssertAdvertisementId()).get();

        transaction.confirmBy(user, advertisement);

        transactionsRepository.save(transaction);
        assetAdvertisementsRepository.save(advertisement);
    }

    public List<AssetAdvertisement> findSellAdvertisementsWithSymbol(String assetSymbol) {
        return assetAdvertisementsRepository.findSellAdvertisementsWithSymbol(assetSymbol);
    }

    public List<AssetAdvertisement> findBuyAdvertisementsWithSymbol(String assetSymbol) {
        return assetAdvertisementsRepository.findBuyAdvertisementsWithSymbol(assetSymbol);
    }

    public List<Transaction> findTransactionsInformedBy(Long userId) {
        return transactionsRepository.findAllByInterestedUser(userId);
    }

    protected AssetAdvertisement postAdvertisement(String advertisementType, Long publisherId, String assetSymbol, int quantity, double price) {
        var publisher = userService.findUserById(publisherId);

        var newAdvertisement = new AssetAdvertisement(advertisementType, assetSymbol, quantity, price, publisher);

        return assetAdvertisementsRepository.save(newAdvertisement);
    }
}
