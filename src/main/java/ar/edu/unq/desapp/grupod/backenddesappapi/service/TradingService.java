package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.Transaction;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.CryptoAdvertisement;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.ModelException;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.CryptoAdvertisementsRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.TradingOrdersRepository;
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
    CryptoAdvertisementsRepository cryptoAdvertisementsRepository;

    @Autowired
    TradingOrdersRepository transactionsRepository;

    // TODO: modelar dinero
    public CryptoAdvertisement postSellAdvertisement(Long sellerId, String cryptoActiveSymbol, Integer quantityToSell, Double sellingPrice) {
        return postAdvertisement(CryptoAdvertisement.SELL_ADVERTISE_TYPE, sellerId, cryptoActiveSymbol, quantityToSell, sellingPrice);
    }

    public CryptoAdvertisement postBuyAdvertisement(Long buyerId, String cryptoActiveSymbol, int quantityToBuy, double buyPrice) {
        return postAdvertisement(CryptoAdvertisement.BUY_ADVERTISE_TYPE, buyerId, cryptoActiveSymbol, quantityToBuy, buyPrice);
    }

    public Transaction informTransaction(Long interestedUserId, Long advertisementId, int quantityToTransfer) {
        var interestedUser = userService.findUserById(interestedUserId);
        var cryptoAssertAdvertisement = cryptoAdvertisementsRepository.findById(advertisementId).orElseThrow(() -> new ModelException("Adverticement not found"));

        var newTransaction = new Transaction(interestedUser, cryptoAssertAdvertisement, quantityToTransfer);

        return transactionsRepository.save(newTransaction);
    }

    public void confirmTransaction(Long advertisePublisherId, Long transactionToConfirmId) {
        var publisher = userService.findUserById(advertisePublisherId);
        var transaction = transactionsRepository.findById(transactionToConfirmId).get();
        var advertisement = cryptoAdvertisementsRepository.findById(transaction.cryptoAssertAdvertisement).get();

        transaction.confirmBy(publisher, advertisement);

        transactionsRepository.save(transaction);
        cryptoAdvertisementsRepository.save(advertisement);
    }

    public List<CryptoAdvertisement> findSellAdvertisementsWithSymbol(String cryptoActiveSymbol) {
        return cryptoAdvertisementsRepository.findSellAdvertisementsWithSymbol(cryptoActiveSymbol);
    }

    public List<CryptoAdvertisement> findBuyAdvertisementsWithSymbol(String cryptoActiveSymbol) {
        return cryptoAdvertisementsRepository.findBuyAdvertisementsWithSymbol(cryptoActiveSymbol);
    }

    public List<Transaction> findTransactionsInformedBy(Long userId) {
        return transactionsRepository.findAllByBuyer(userId);
    }

    protected CryptoAdvertisement postAdvertisement(String advertisementType, Long publisherId, String cryptoActiveSymbol, int quantity, double price) {
        var publisher = userService.findUserById(publisherId);

        var newAdvertisement = new CryptoAdvertisement(advertisementType, cryptoActiveSymbol, quantity, price, publisher);

        return cryptoAdvertisementsRepository.save(newAdvertisement);
    }
}
