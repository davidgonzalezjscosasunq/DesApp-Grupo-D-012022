package ar.edu.unq.desapp.grupod.backenddesappapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityProperties {

 @Value("${security.bcra_token}")
    public String bcraToken;
}
