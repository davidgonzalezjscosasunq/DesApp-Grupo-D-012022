package ar.edu.unq.desapp.grupod.backenddesappapi.persistence;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.CryptoAdvertisement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeAdvertisementRepository extends CrudRepository<CryptoAdvertisement, Long> {

    List<CryptoAdvertisement> findByCryptoActiveSymbol(String cryptoActiveSymbol);

    Boolean existsByCryptoActiveSymbol(String cryptoActiveSymbol);

    List<CryptoAdvertisement> findByCryptoActiveSymbolAndTypeLike(String cryptoActiveSymbol, String cellAdvertise);
}
