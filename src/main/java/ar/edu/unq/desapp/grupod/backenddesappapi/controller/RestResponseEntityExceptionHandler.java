package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.EntityNotFoundException;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.ModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private ResourceBundleMessageSource messageSource;

    public String findMessage(ModelException modelException, Locale locale) {
        return messageSource.getMessage(modelException.getMessage(), modelException.messageArguments(), locale);
    }

    @ExceptionHandler(value = { EntityNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(Locale locale, EntityNotFoundException exception, WebRequest request) {
        return handleExceptionInternal(exception, findMessage(exception, locale), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { ModelException.class })
    protected ResponseEntity<Object> handeBadRequest(Locale locale, ModelException exception, WebRequest request) {
        return handleExceptionInternal(exception, findMessage(exception, locale), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}