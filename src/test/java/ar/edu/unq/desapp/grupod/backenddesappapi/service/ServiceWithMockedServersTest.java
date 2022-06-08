package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ServiceWithMockedServersTest extends ServiceTest {

    private static WireMockServer binanceMockServer;
    private static WireMockServer estadisticasBCRAMockServer;

    @Value("${security.bcra_token}")
    public String bcraToken;

    @BeforeAll
    static void beforeAll() {
        startMockedServers();
    }

    @AfterAll
    static void afterAll() {
        stopMockedServers();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("api_estadisticasbcra_base_url", estadisticasBCRAMockServer::baseUrl);
        dynamicPropertyRegistry.add("api_binance_base_url", binanceMockServer::baseUrl);
    }

    static public void startMockedServers() {
        binanceMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        binanceMockServer.start();

        estadisticasBCRAMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        estadisticasBCRAMockServer.start();
    }

    static public void stopMockedServers() {
        binanceMockServer.stop();
        estadisticasBCRAMockServer.stop();
    }

    public void mockServerToRespondWithDollarToPesoRatioOf(Float dollarToPesoRatioToReturn) {
        estadisticasBCRAMockServer.stubFor(get("/usd")
                .withHeader("Authorization", equalTo("Bearer " + bcraToken))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("[{\"d\": \"2003-01-03\", \"v\": " + dollarToPesoRatioToReturn + "}]")));
    }

    public void mockServerToRespondWithSymbolPrice(String symbol, Float assetPrice) {
        binanceMockServer.stubFor(get("/api/v3/ticker/price?symbol=" + symbol)
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("{\"symbol\": \"BTC\", \"price\": " +  assetPrice + "}")));
    }

}
