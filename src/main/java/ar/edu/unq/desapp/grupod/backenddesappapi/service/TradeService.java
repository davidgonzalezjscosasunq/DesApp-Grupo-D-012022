package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.BuyOrder;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.CryptoAdvertisement;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.ModelException;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.CryptoAdvertisementsRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.TradingOrdersRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CryptoAdvertisementsRepository cryptoAdvertisementsRepository;

    @Autowired
    TradingOrdersRepository tradingOrdersRepository;
    
    // TODO: modelar dinero
    public CryptoAdvertisement postSellAdvertisement(Long sellerId, String cryptoActiveSymbol, Integer quantityToSell, Double sellingPrice) {
        var seller = userService.findUserById(sellerId);
        //var seller = userRepository.findByFirstNameAndLastName(sellerFirstName, sellerLastName).orElseThrow(() -> new ModelException("User not found"));

        var newSellAdvertisement = CryptoAdvertisement.sellAdvertise(cryptoActiveSymbol, quantityToSell, sellingPrice, seller);

        return cryptoAdvertisementsRepository.save(newSellAdvertisement);
    }

    public CryptoAdvertisement postBuyAdvertisement(Long buyerId, String cryptoActiveSymbol, int quantityToBuy, double buyPrice) {
        var buyer = userService.findUserById(buyerId);

        var newBuyAdvertisement = CryptoAdvertisement.buyAdvertise(cryptoActiveSymbol, quantityToBuy, buyPrice, buyer);

        return cryptoAdvertisementsRepository.save(newBuyAdvertisement);
    }

    public List<CryptoAdvertisement> findSellAdvertisementsWithSymbol(String cryptoActiveSymbol) {
        return cryptoAdvertisementsRepository.findSellAdvertisementsWithSymbol(cryptoActiveSymbol);
    }

    public List<CryptoAdvertisement> findBuyAdvertisementsWithSymbol(String cryptoActiveSymbol) {
        return cryptoAdvertisementsRepository.findBuyAdvertisementsWithSymbol(cryptoActiveSymbol);
    }

    public BuyOrder placeBuyOrder(Long buyerId, Long advertisementId, int quantityToBuy) {
        var buyer = userService.findUserById(buyerId);
        var cryptoAssertAdverticement = cryptoAdvertisementsRepository.findById(advertisementId).orElseThrow(() -> new ModelException("Adverticement not found"));

        var newBuyOrder = new BuyOrder(buyer, cryptoAssertAdverticement, quantityToBuy);

        return tradingOrdersRepository.save(newBuyOrder);
    }

    public void confirmSuccessfulSell(Long sellerId, Long ifOfBuyOrderToConfirm) {
        var seller = userService.findUserById(sellerId);
        var buyOrderToConfirm = tradingOrdersRepository.findById(ifOfBuyOrderToConfirm).get();

        var advertisement = cryptoAdvertisementsRepository.findById(buyOrderToConfirm.cryptoAssertAdverticement).get();

        buyOrderToConfirm.confirmFor(seller, advertisement);

        cryptoAdvertisementsRepository.save(advertisement);
    }
}
