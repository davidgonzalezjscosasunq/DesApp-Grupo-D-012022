package ar.edu.unq.desapp.grupod.backenddesappapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StaticProperties {
    @Value("${api_binance_url}")
    public String apiBinanceURL;

    @Value("${api_estadisticasbcra_base_url}")
    public String apiEstadisticasbcraBaseURL;
}
