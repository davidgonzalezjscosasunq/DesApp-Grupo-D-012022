package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.BinanceRatesResponse;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.CoinRate;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.UsdResponse;
import ar.edu.unq.desapp.grupod.backenddesappapi.configuration.SecurityProperties;
import ar.edu.unq.desapp.grupod.backenddesappapi.configuration.StaticProperties;


@Service
public class RateService {

    @Autowired
    private StaticProperties staticProperties;

    @Autowired
    private SecurityProperties securityProperties;

    public CoinRate getCoinRate(String symbol) {
        String url = staticProperties.apiBinanceURL  + symbol;
        var assetRate = new RestTemplate().getForObject(url, BinanceRatesResponse.class);
        var priceInPesos = assetRate.priceInDollars() * dollarToPesoConversionRate();

        return new CoinRate(assetRate.priceInDollars(), priceInPesos);
    }

    public Float dollarToPesoConversionRate(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + securityProperties.bcraToken);

        try {
            final ResponseEntity<List<UsdResponse>> response = new RestTemplate().exchange(
                    staticProperties.apiEstadisticasbcraBaseURL + "/usd",
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
