package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.BuyOrder;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.CryptoAdvertisement;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.ModelException;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.CryptoAdvertisementsRepository;
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

    // TODO: modelar dinero
    public CryptoAdvertisement postSellAdvertisement(Long sellerId, String cryptoActiveSymbol, Integer quantityToSell, Double sellingPrice) {
        var seller = userService.findUserById(sellerId);
        //var seller = userRepository.findByFirstNameAndLastName(sellerFirstName, sellerLastName).orElseThrow(() -> new ModelException("User not found"));

        var newSellAdvertisement = CryptoAdvertisement.sellAdvertise(cryptoActiveSymbol, quantityToSell, sellingPrice, seller);

        return cryptoAdvertisementsRepository.save(newSellAdvertisement);
    }

    public CryptoAdvertisement postBuyAdvertisement(Long buyerId, String cryptoActiveSymbol, int quantityToBuy, double buyPrice) {
        var buyer = userService.findUserById(buyerId);

        var newSellAdvertisement = CryptoAdvertisement.buyAdvertise(cryptoActiveSymbol, quantityToBuy, buyPrice, buyer);

        return cryptoAdvertisementsRepository.save(newSellAdvertisement);
    }

    public List<CryptoAdvertisement> findSellAdvertisementsWithSymbol(String cryptoActiveSymbol) {
        return cryptoAdvertisementsRepository.findSellAdvertisementsWithSymbol(cryptoActiveSymbol);
    }

    public List<CryptoAdvertisement> findBuyAdvertisementsWithSymbol(String cryptoActiveSymbol) {
        return cryptoAdvertisementsRepository.findBuyAdvertisementsWithSymbol(cryptoActiveSymbol);
    }

    public BuyOrder placeBuyOrder(Long buyerId, Long assetSymbol, int quantityToBuy) {
        var buyer = userService.findUserById(buyerId);
        var cryptoAssertAdverticement = cryptoAdvertisementsRepository.findById(assetSymbol).orElseThrow(() -> new ModelException("Adverticement not found"));

        return new BuyOrder(buyer, cryptoAssertAdverticement, quantityToBuy);
    }
}
