package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.BinanceRatesResponse;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.CoinRate;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.UsdResponse;
import ar.edu.unq.desapp.grupod.backenddesappapi.configuration.SecurityProperties;
import ar.edu.unq.desapp.grupod.backenddesappapi.configuration.Endpoints;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.CoinRateDetail;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.clock.Clock;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.utils.ActiveCryptos;


@Service
public class RateService {

    @Autowired
    private Endpoints endpoints;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    Clock clock;

    private List<String> activeCryptos = new ActiveCryptos().getActiveCryptosList();

    public List<CoinRateDetail> getActiveCoinRates() {
        List<CoinRateDetail> coinRatesCost = new ArrayList<>();
        for(String symbol: activeCryptos){
            CoinRate coinRate = getCoinRate(symbol);
            coinRatesCost.add(new CoinRateDetail(coinRate.usdPrice(), coinRate.pesosPrice(), symbol, clock.now()));
        }
        return coinRatesCost;
    }

    public CoinRate getCoinRate(String symbol) {
        String url = endpoints.apiBinanceBaseURL + endpoints.apiBinancePriceURL +  symbol;
        var assetRate = new RestTemplate().getForObject(url, BinanceRatesResponse.class);
        var priceInPesos = assetRate.priceInDollars() * dollarToPesoConversionRate();

        return new CoinRate(assetRate.priceInDollars(), priceInPesos);
    }

    public Float dollarToPesoConversionRate(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + securityProperties.bcraToken);

        try {
            final ResponseEntity<List<UsdResponse>> response = new RestTemplate().exchange(
                    endpoints.apiEstadisticasbcraBaseURL + "/usd",
                    HttpMethod.GET,
                    new HttpEntity(headers),
                    new ParameterizedTypeReference<List<UsdResponse>>(){});

            // TODO: la peticion a la API trae mas de 5000 precios. Ver como hacer para pedirle el ultimo o buscar otra API
            return response.getBody().get(response.getBody().size() - 1).dollarToPesoConversionRate();
        } catch (Exception exception) {
            throw new Error(exception);
        }
    }
}
