package ar.edu.unq.desapp.grupod.backenddesappapi.model.validators;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.ModelException;

public abstract class StringValidator {

    public void assertIsValid(String textToValidate) {
        if (!textToValidate.matches(validationRegex())) {
            throw new ModelException(invalidTextErrorMessage());
        }
    }

    protected abstract String validationRegex();

    protected abstract String invalidTextErrorMessage();
}
