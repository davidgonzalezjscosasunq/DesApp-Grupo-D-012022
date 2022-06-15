package ar.edu.unq.desapp.grupod.backenddesappapi.model.validators;

public class EmailStringValidator extends StringValidator {

    protected String validationRegex() {
        return "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    }

    protected String invalidTextErrorMessage() {
        return "email.invalid_format";
    }

}
