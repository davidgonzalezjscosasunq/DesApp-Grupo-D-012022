package ar.edu.unq.desapp.grupod.backenddesappapi.persistence;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.AssetAdvertisement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetAdvertisementsRepository extends CrudRepository<AssetAdvertisement, Long> {

    Boolean existsByAssetSymbol(String assetSymbol);

    default List<AssetAdvertisement> findBuyAdvertisementsWithSymbol(String assetSymbol) {
        return findByAssetSymbolAndType(assetSymbol, AssetAdvertisement.BUY_ADVERTISE_TYPE);
    }

    default List<AssetAdvertisement> findSellAdvertisementsWithSymbol(String assetSymbol) {
        return findByAssetSymbolAndTypeAndQuantityNot(assetSymbol, AssetAdvertisement.SELL_ADVERTISE_TYPE, 0);
    }

    List<AssetAdvertisement> findByAssetSymbolAndType(String assetSymbol, String assetAdvertisement);

    List<AssetAdvertisement> findByAssetSymbolAndTypeAndQuantityNot(String assetSymbol, String cellAdvertise, Integer quantity);

}
