package ar.edu.unq.desapp.grupod.backenddesappapi.model;

public class ModelException extends RuntimeException {

    private final transient Object[] messageArguments;

    public ModelException(String errorMessage) {
        super(errorMessage);
        messageArguments = new Object[0];
    }

    public ModelException(String errorMessage, Object[] messageArguments) {
        super(errorMessage);
        this.messageArguments = messageArguments;
    }

    public Object[] messageArguments() {
        return messageArguments;
    }

}
