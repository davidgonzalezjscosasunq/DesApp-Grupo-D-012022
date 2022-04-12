package ar.edu.unq.desapp.grupod.backenddesappapi.persistence;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.CryptoAdvertisement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CryptoAdvertisementsRepository extends CrudRepository<CryptoAdvertisement, Long> {

    List<CryptoAdvertisement> findByCryptoActiveSymbol(String cryptoActiveSymbol);

    Boolean existsByCryptoActiveSymbol(String cryptoActiveSymbol);

    default List<CryptoAdvertisement> findBuyAdvertisementsWithSymbol(String cryptoActiveSymbol) {
        return findByCryptoActiveSymbolAndType(cryptoActiveSymbol, CryptoAdvertisement.BUY_ADVERTISE_TYPE);
    }

    default List<CryptoAdvertisement> findSellAdvertisementsWithSymbol(String cryptoActiveSymbol) {
        return findByCryptoActiveSymbolAndType(cryptoActiveSymbol, CryptoAdvertisement.SELL_ADVERTISE_TYPE);
    }

    List<CryptoAdvertisement> findByCryptoActiveSymbolAndType(String cryptoActiveSymbol, String cellAdvertise);

}