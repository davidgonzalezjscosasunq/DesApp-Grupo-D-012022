package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.BinanceRatesResponse;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.CoinRate;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.UsdResponse;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.apiCalls.BCRARequest;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.apiCalls.BinanceRequest;


@Service
public class RateService {

    @Autowired
    private BCRARequest bcraRequest;

    @Autowired
    private BinanceRequest binanceRequest;

    public CoinRate getCoinRate (String symbol) {
        BinanceRatesResponse rate = binanceRequest.getBinanceRate(symbol);

        List<UsdResponse> usdList = bcraRequest.getUsdConvertionToPesos();

        Float lastDollarValueInPesos = usdList.get(usdList.size() - 1).getV();

        Float priceInPesos = rate.getPrice() * lastDollarValueInPesos;

        return new CoinRate(rate.getPrice(), priceInPesos);
    }
}
