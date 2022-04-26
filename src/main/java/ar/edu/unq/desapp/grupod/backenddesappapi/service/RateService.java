package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.BinanceRatesResponse;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.GetCoinRateResponse;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.UsdResponse;

@Service
public class RateService {

public Object getCoinRate (String symbol) {
    String url = "https://api1.binance.com/api/v3/ticker/price?symbol=" + symbol;
    RestTemplate restTemplate = new RestTemplate();

    BinanceRatesResponse rate = restTemplate.getForObject(url, BinanceRatesResponse.class);

    List<UsdResponse> usdList = this.getUsdConvertionToPesos();

    Float lastDollarValueInPesos = usdList.get(usdList.size() - 1).v;

    Float priceInPesos = rate.price * lastDollarValueInPesos;

    GetCoinRateResponse response = new GetCoinRateResponse(rate.price, priceInPesos);

    return response;
}


public List<UsdResponse> getUsdConvertionToPesos (){
    try {
        String url = "https://api.estadisticasbcra.com/usd";

        //to do: move token.
        String base64Creds = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODIwMzA2NzMsInR5cGUiOiJleHRlcm5hbCIsInVzZXIiOiJlbXJvbWVybzUwMEBnbWFpbC5jb20ifQ.zOA2_ULqpw-aLjqVM5bogpqVDlRGDqxIBi3nnsewdJG1ZwQ8sIqCNHLaqM5XPLJUJcjTj2BE7e3BnpDpkLuDTg";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + base64Creds);

        HttpEntity request = new HttpEntity(headers);

        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<List<UsdResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<UsdResponse>>(){});

        List<UsdResponse> conversions = response.getBody();

        return conversions;

    } catch (Exception exception) {
        throw new Error(exception);
    }
}
}
