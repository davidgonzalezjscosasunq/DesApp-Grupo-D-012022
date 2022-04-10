package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.ModelException;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.SellAdvertisement;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.TradeAdvertisementRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TradeService {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TradeAdvertisementRepository tradeAdvertisementRepository;

    // TODO: modelar dinero
    public SellAdvertisement postSellAdvertisement(String cryptoActiveSymbol, Integer quantityToSell, Double sellingPrice, String sellerFirstName, String sellerLastName) {
        var seller = userService.findUserNamed(sellerFirstName, sellerLastName);
        //var seller = userRepository.findByFirstNameAndLastName(sellerFirstName, sellerLastName).orElseThrow(() -> new ModelException("User not found"));

        var newSellAdvertisement = new SellAdvertisement(cryptoActiveSymbol, quantityToSell, sellingPrice, seller);

        return tradeAdvertisementRepository.save(newSellAdvertisement);
    }

    public List<SellAdvertisement> findSellAdvertisementsWithSymbol(String cryptoActiveSymbol) {
        return tradeAdvertisementRepository.findByCryptoActiveSymbol(cryptoActiveSymbol);
    }

}
