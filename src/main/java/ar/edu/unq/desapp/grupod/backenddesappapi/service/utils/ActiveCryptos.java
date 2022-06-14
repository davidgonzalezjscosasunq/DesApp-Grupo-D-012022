package ar.edu.unq.desapp.grupod.backenddesappapi.service.utils;

import java.util.Arrays;
import java.util.List;

public class ActiveCryptos {
    public List<String> getActiveCryptosList() {
        List<String> activeCryptosSymbolList = Arrays.asList(
                "ALICEUSDT",
                "MATICUSDT",
                "AXSUSDT",
                "AAVEUSDT",
                "ATOMUSDT",
                "NEOUSDT",
                "DOTUSDT",
                "ETHUSDT",
                "CAKEUSDT",
                "BTCUSDT",
                "BNBUSDT",
                "ADAUSDT",
                "TRXUSDT",
                "AUDIOUSDT");
        return activeCryptosSymbolList;
        };
}