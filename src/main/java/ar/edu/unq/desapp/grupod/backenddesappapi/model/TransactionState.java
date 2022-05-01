package ar.edu.unq.desapp.grupod.backenddesappapi.model;

import java.util.Optional;

public enum TransactionState {
    PENDING_STATE,
    CANCELLED_STATE,
    CONFIRMED_STATE {
        public Optional<String> paymentAddressFor(AssetAdvertisement assetAdvertisement) {
            return Optional.of(assetAdvertisement.paymentAddress());
        }
    };

    public Optional<String> paymentAddressFor(AssetAdvertisement assetAdvertisement) {
        return Optional.empty();
    }
}
