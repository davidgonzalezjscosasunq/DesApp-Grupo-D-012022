package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.BinanceRatesResponse;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.CoinRate;
import ar.edu.unq.desapp.grupod.backenddesappapi.configuration.Endpoints;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.clock.Clock;


@Service
public class RateService {

    @Autowired
    private Endpoints endpoints;

    @Autowired
    Clock clock;

    private List<String> activeCryptos = Arrays.asList(
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

    public List<CoinRate> getActiveCoinRates() {
        Float dollarToPesoConversionRatio = dollarToPesoConversionRate();

        return activeCryptos.stream()
                .map(symbol -> getCoinRateWithDollarToPesoConversionRatio(symbol, dollarToPesoConversionRatio))
                .collect(Collectors.toList());
    }

    public CoinRate getCoinRate(String symbol) {
        return getCoinRateWithDollarToPesoConversionRatio(symbol, dollarToPesoConversionRate());
    }

    private CoinRate getCoinRateWithDollarToPesoConversionRatio(String symbol, Float dollarToPesoConversionRatio) {
        String url = endpoints.apiBinanceBaseURL + endpoints.apiBinancePriceURL +  symbol;
        var assetRate = new RestTemplate().getForObject(url, BinanceRatesResponse.class);

        var priceInPesos = assetRate.priceInDollars() * dollarToPesoConversionRatio;

        return new CoinRate(symbol, clock.now(), assetRate.priceInDollars(), priceInPesos);
    }

    public Float dollarToPesoConversionRate(){
        var response = new RestTemplate().exchange(
                endpoints.apiDollarToPesoConversionRatioURL,
                HttpMethod.GET,
                new HttpEntity(new HttpHeaders()),
                new ParameterizedTypeReference<List<JsonNode>>(){});

        return response.getBody().stream()
                .filter(objectNode -> isDollarBlue(objectNode))
                .map(objectNode -> parseSellPrice(objectNode))
                .findFirst()
                .get();
    }

    private Boolean isDollarBlue(JsonNode objectNode) {
        return objectNode.get("casa").get("nombre").asText().equals("Blue");
    }

    private Float parseSellPrice(JsonNode objectNode) {
        var priceAsString = objectNode.get("casa").get("venta").asText().replace(',', '.');
        return Float.parseFloat(priceAsString);
    }

}
