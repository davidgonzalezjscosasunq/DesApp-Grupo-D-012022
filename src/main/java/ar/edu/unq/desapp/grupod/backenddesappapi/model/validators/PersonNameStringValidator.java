package ar.edu.unq.desapp.grupod.backenddesappapi.model.validators;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.ModelException;

public class PersonNameStringValidator {

    public void assertIsValidLastName(String name) {
        if (!isValidName(name)) {
            throw new ModelException("user.last_name.invalid_length", new Integer[] {minimunNumberOfCharacters(), maximumNumberOfCharacters()});
        }
    }

    public void assertIsValidFirstName(String name) {
        if (!isValidName(name)) {
            throw new ModelException("user.first_name.invalid_length", new Integer[]{minimunNumberOfCharacters(), maximumNumberOfCharacters()});
        }
    }

    private boolean isValidName(String name) {
        return name.matches(validationRegex());
    }

    private String validationRegex() {
        return String.format(".{%d,%d}", minimunNumberOfCharacters(), maximumNumberOfCharacters());
    }

    private Integer minimunNumberOfCharacters() {
        return 3;
    }

    private Integer maximumNumberOfCharacters() {
        return 30;
    }

}
