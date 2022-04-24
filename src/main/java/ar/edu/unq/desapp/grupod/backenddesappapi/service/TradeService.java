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
        return postAdvertisement(CryptoAdvertisement.SELL_ADVERTISE_TYPE, sellerId, cryptoActiveSymbol, quantityToSell, sellingPrice);
    }

    public CryptoAdvertisement postBuyAdvertisement(Long buyerId, String cryptoActiveSymbol, int quantityToBuy, double buyPrice) {
        return postAdvertisement(CryptoAdvertisement.BUY_ADVERTISE_TYPE, buyerId, cryptoActiveSymbol, quantityToBuy, buyPrice);
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
        var advertisement = cryptoAdvertisementsRepository.findById(buyOrderToConfirm.cryptoAssertAdvertisement).get();

        buyOrderToConfirm.confirmBy(seller, advertisement);

        tradingOrdersRepository.save(buyOrderToConfirm);
        cryptoAdvertisementsRepository.save(advertisement);
    }

    public List<CryptoAdvertisement> findSellAdvertisementsWithSymbol(String cryptoActiveSymbol) {
        return cryptoAdvertisementsRepository.findSellAdvertisementsWithSymbol(cryptoActiveSymbol);
    }

    public List<CryptoAdvertisement> findBuyAdvertisementsWithSymbol(String cryptoActiveSymbol) {
        return cryptoAdvertisementsRepository.findBuyAdvertisementsWithSymbol(cryptoActiveSymbol);
    }

    public List<BuyOrder> ordersOf(Long userId) {
        return tradingOrdersRepository.findAllByBuyer(userId);
    }

    protected CryptoAdvertisement postAdvertisement(String advertisementType, Long publisherId, String cryptoActiveSymbol, int quantity, double price) {
        var publisher = userService.findUserById(publisherId);

        var newBuyAdvertisement = new CryptoAdvertisement(advertisementType, cryptoActiveSymbol, quantity, price, publisher);

        return cryptoAdvertisementsRepository.save(newBuyAdvertisement);
    }
}
