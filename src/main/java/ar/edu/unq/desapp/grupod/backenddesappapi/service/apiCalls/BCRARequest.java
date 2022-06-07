package ar.edu.unq.desapp.grupod.backenddesappapi.service.apiCalls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

import ar.edu.unq.desapp.grupod.backenddesappapi.configuration.SecurityProperties;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.UsdResponse;


@Service
public class BCRARequest {

    @Autowired
    private SecurityProperties securityProperties;

    public List<UsdResponse> getUsdConvertionToPesos (){
        String url = securityProperties.bcra_api_url;

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

            return conversions;

        } catch (Exception exception) {
            throw new Error(exception);
        }
    }
}
