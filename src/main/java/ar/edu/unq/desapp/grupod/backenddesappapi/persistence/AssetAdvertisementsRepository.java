package ar.edu.unq.desapp.grupod.backenddesappapi.persistence;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.AssetAdvertisementType;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.AssetAdvertisement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetAdvertisementsRepository extends CrudRepository<AssetAdvertisement, Long> {

    Boolean existsByAssetSymbol(String assetSymbol);

    default List<AssetAdvertisement> findBuyAdvertisementsWithSymbol(String assetSymbol) {
        return findByAssetSymbolAndType(assetSymbol, AssetAdvertisementType.BUY_ADVERTISEMENT);
    }

    default List<AssetAdvertisement> findSellAdvertisementsWithSymbol(String assetSymbol) {
        return findByAssetSymbolAndTypeAndQuantityNot(assetSymbol, AssetAdvertisementType.SELL_ADVERTISEMENT, 0);
    }

    default List<AssetAdvertisement> findAllByAssetSymbol(String assetSymbol) {
        return findByAssetSymbolAndQuantityNot(assetSymbol, 0);
    }

    List<AssetAdvertisement> findByAssetSymbolAndQuantityNot(String assetSymbol, Integer quantity);

    List<AssetAdvertisement> findByAssetSymbolAndType(String assetSymbol, AssetAdvertisementType assetAdvertisement);

    List<AssetAdvertisement> findByAssetSymbolAndTypeAndQuantityNot(String assetSymbol, AssetAdvertisementType assetAdvertisementType, Integer quantity);

}
