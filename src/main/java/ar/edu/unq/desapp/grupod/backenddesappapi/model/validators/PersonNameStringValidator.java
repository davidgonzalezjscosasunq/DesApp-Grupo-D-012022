package ar.edu.unq.desapp.grupod.backenddesappapi.model.validators;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.ModelException;

public class PersonNameStringValidator {

    public void assertIsValidLastName(String name) {
        if (!isValidName(name)) {
            throw new ModelException(String.format("User last name must have between %d and %d letters", minimunNumberOfCharacters(), maximumNumberOfCharacters()));
        }
    }

    public void assertIsValidFirstName(String name) {
        if (!isValidName(name)) {
            throw new ModelException(String.format("User first name must have between %d and %d letters", minimunNumberOfCharacters(), maximumNumberOfCharacters()));
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
