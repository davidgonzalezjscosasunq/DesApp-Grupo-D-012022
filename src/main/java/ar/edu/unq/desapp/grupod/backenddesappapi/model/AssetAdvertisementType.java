package ar.edu.unq.desapp.grupod.backenddesappapi.model;

public enum AssetAdvertisementType {
    BUY_ADVERTISEMENT {
        public String paymentAddressFor(User user) {
            return user.cryptoActiveWalletAddress();
        }
    },

    SELL_ADVERTISEMENT {
        public String paymentAddressFor(User user) {
            return user.cvu();
        }
    };

    public abstract String paymentAddressFor(User user);
}
