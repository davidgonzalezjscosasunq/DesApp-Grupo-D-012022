package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.BinanceRatesResponse;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.CoinRate;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.UsdResponse;
import ar.edu.unq.desapp.grupod.backenddesappapi.configuration.SecurityProperties;


@Service
public class RateService {

    @Value("${api_estadisticasbcra_base_url}")
    public String apiEstadisticasbcraBaseURL;

    @Autowired
    private SecurityProperties securityProperties;

    public CoinRate getCoinRate (String symbol) {
        String url = "https://api1.binance.com/api/v3/ticker/price?symbol=" + symbol;
        RestTemplate restTemplate = new RestTemplate();

        BinanceRatesResponse rate = restTemplate.getForObject(url, BinanceRatesResponse.class);

        Float lastDollarValueInPesos = this.dollarToPesoConversionRate();

        Float priceInPesos = rate.price * lastDollarValueInPesos;

        return new CoinRate(rate.price, priceInPesos);
    }


    public Float dollarToPesoConversionRate(){
        String url = apiEstadisticasbcraBaseURL + "/usd";

        String base64Creds = securityProperties.bcraToken;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + base64Creds);

        HttpEntity request = new HttpEntity(headers);

        final RestTemplate restTemplate = new RestTemplate();

        try {

            final ResponseEntity<List<UsdResponse>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    new ParameterizedTypeReference<List<UsdResponse>>(){});

            List<UsdResponse> conversions = response.getBody();

            return conversions.get(conversions.size() - 1).v;

        } catch (Exception exception) {
            throw new Error(exception);
        }
    }
}
