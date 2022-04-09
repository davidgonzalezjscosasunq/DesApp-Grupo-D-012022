package ar.edu.unq.desapp.grupod.backenddesappapi.model.validators;

public class CVUStringValidator extends StringValidator {

    public String validationRegex() {
        return "[0-9]{22}";
    }

    protected String invalidTextErrorMessage() {
        return "Invalid CVU";
    }

}
