package ar.edu.unq.desapp.grupod.backenddesappapi.model.validators;

public class EmailStringValidator extends StringValidator {

    protected String validationRegex() {
        return "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$";
    }

    protected String invalidTextErrorMessage() {
        return "Invalid email format";
    }

}
