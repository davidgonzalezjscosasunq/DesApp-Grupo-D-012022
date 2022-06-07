package ar.edu.unq.desapp.grupod.backenddesappapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityProperties {

 @Value("${security.bcra_token}")
    public String bcraToken;

 @Value("${external.server.bcraUrl}")
    public String bcra_api_url;

 @Value("${external.server.binanceUrl}")
    public String binance_api_url;

}
