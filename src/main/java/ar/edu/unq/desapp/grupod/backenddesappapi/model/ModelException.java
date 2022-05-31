package ar.edu.unq.desapp.grupod.backenddesappapi.model;

public class ModelException extends RuntimeException {

    private Object[] messageArguments;

    public ModelException(String errorMessage) {
        super(errorMessage);
    }

    public ModelException(String errorMessage, Object[] messageArguments) {
        super(errorMessage);
        this.messageArguments = messageArguments;
    }

    public Object[] messageArguments() {
        return messageArguments;
    }

}
