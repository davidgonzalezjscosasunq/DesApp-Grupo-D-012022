package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.CryptoAdvertisement;
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
    public CryptoAdvertisement postSellAdvertisement(String cryptoActiveSymbol, Integer quantityToSell, Double sellingPrice, String sellerFirstName, String sellerLastName) {
        var seller = userService.findUserNamed(sellerFirstName, sellerLastName);
        //var seller = userRepository.findByFirstNameAndLastName(sellerFirstName, sellerLastName).orElseThrow(() -> new ModelException("User not found"));

        var newSellAdvertisement = CryptoAdvertisement.sellAdvertise(cryptoActiveSymbol, quantityToSell, sellingPrice, seller);

        return cryptoAdvertisementsRepository.save(newSellAdvertisement);
    }

    public CryptoAdvertisement postBuyAdvertisement(String cryptoActiveSymbol, int quantityToBuy, double buyPrice, String buyerFirstName, String buyerLastName) {
        var buyer = userService.findUserNamed(buyerFirstName, buyerLastName);

        var newSellAdvertisement = CryptoAdvertisement.buyAdvertise(cryptoActiveSymbol, quantityToBuy, buyPrice, buyer);

        return cryptoAdvertisementsRepository.save(newSellAdvertisement);
    }

    public List<CryptoAdvertisement> findSellAdvertisementsWithSymbol(String cryptoActiveSymbol) {
        return cryptoAdvertisementsRepository.findSellAdvertisementsWithSymbol(cryptoActiveSymbol);
    }

    public List<CryptoAdvertisement> findBuyAdvertisementsWithSymbol(String cryptoActiveSymbol) {
        return cryptoAdvertisementsRepository.findBuyAdvertisementsWithSymbol(cryptoActiveSymbol);
    }

}