package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.Transaction;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.TradingService;

public enum UpdateTransactionType {
    CONFIRM {
        public Transaction applyTo(TradingService tradingService, Long userId, Long transactionId) {
            return tradingService.confirmTransaction(userId, transactionId);
        }
    },
    CANCEL {
        public Transaction applyTo(TradingService tradingService, Long userId, Long transactionId) {
            return tradingService.cancelTransaction(userId, transactionId);
        }
    };

    public abstract Transaction applyTo(TradingService tradingService, Long userId, Long transactionId);
}
