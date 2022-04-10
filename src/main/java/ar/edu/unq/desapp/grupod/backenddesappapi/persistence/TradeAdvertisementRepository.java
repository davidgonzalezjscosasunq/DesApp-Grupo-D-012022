package ar.edu.unq.desapp.grupod.backenddesappapi.persistence;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.SellAdvertisement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeAdvertisementRepository extends CrudRepository<SellAdvertisement, Long> {

    List<SellAdvertisement> findByCryptoActiveSymbol(String cryptoActiveSymbol);

    Boolean existsByCryptoActiveSymbol(String cryptoActiveSymbol);

}
