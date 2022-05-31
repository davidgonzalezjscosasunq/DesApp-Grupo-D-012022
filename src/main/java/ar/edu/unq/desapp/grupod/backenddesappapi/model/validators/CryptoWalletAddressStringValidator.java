package ar.edu.unq.desapp.grupod.backenddesappapi.model.validators;

public class CryptoWalletAddressStringValidator extends StringValidator {

    protected String validationRegex() {
        return "\\d{8}";
    }

    protected String invalidTextErrorMessage() {
        return "crypto_wallet_address.invalid_format";
    }

}
