package ar.edu.unq.desapp.grupod.backenddesappapi.model.validators;

public class AddressStringValidator extends StringValidator {

    protected String validationRegex() {
        return ".{10,30}";
    }

    protected String invalidTextErrorMessage() {
        return "address.invalid_length";
    }
}
