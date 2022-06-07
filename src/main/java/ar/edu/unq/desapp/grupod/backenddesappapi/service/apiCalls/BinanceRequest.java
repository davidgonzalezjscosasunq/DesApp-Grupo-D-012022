package ar.edu.unq.desapp.grupod.backenddesappapi.service.apiCalls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.edu.unq.desapp.grupod.backenddesappapi.configuration.SecurityProperties;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.BinanceRatesResponse;

@Service
public class BinanceRequest {

    @Autowired
    private SecurityProperties securityProperties;

    public BinanceRatesResponse getBinanceRate(String symbol){
        String url = securityProperties.binance_api_url + symbol;
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(url, BinanceRatesResponse.class);
    }
}
