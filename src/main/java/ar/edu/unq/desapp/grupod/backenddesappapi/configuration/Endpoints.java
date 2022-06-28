package ar.edu.unq.desapp.grupod.backenddesappapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Endpoints {
    @Value("${api_binance_base_url}")
    public String apiBinanceBaseURL;

    public String apiBinancePriceURL =  "/api/v3/ticker/price?symbol=";

    @Value("${api_dollar_to_peso_conversion_ratio_url}")
    public String apiDollarToPesoConversionRatioURL;

}
