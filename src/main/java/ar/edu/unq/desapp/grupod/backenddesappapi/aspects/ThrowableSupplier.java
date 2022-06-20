package ar.edu.unq.desapp.grupod.backenddesappapi.aspects;

public interface ThrowableSupplier<T> {

    T get() throws Throwable;

}
