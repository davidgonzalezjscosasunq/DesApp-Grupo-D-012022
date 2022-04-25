package ar.edu.unq.desapp.grupod.backenddesappapi.service;


import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.UsdResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class RateService {

public Object getCoinRates (String symbol) {
    String url = "https://api1.binance.com/api/v3/ticker/price?symbol=" + symbol;
    RestTemplate restTemplate = new RestTemplate();

    Object rates = restTemplate.getForObject(url, Object.class);

    List<UsdResponse> usd = this.getUsdPriceInPesos();

    //to do: get last usd value and get rate in pesos.

    System.out.print(usd);

    return rates;
}


public <T> List<T> getUsdPriceInPesos (){
    try {
        String url = "https://api.estadisticasbcra.com/usd";

        //to do: move token.

        String base64Creds = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODIwMzA2NzMsInR5cGUiOiJleHRlcm5hbCIsInVzZXIiOiJlbXJvbWVybzUwMEBnbWFpbC5jb20ifQ.zOA2_ULqpw-aLjqVM5bogpqVDlRGDqxIBi3nnsewdJG1ZwQ8sIqCNHLaqM5XPLJUJcjTj2BE7e3BnpDpkLuDTg";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + base64Creds);

        HttpEntity request = new HttpEntity(headers);

        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<List<T>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<T>>(){});

        List<T> conversions = response.getBody();

        return conversions;

    } catch (Exception exception) {
        throw new Error(exception);
    }
}
}
