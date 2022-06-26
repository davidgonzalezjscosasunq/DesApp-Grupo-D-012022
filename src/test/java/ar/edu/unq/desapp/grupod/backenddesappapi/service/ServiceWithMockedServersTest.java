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
    private static WireMockServer dollarToPesoConversionRateMockServer;

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
        dynamicPropertyRegistry.add("api_dollar_to_peso_conversion_ratio_url", dollarToPesoConversionRateMockServer::baseUrl);
        dynamicPropertyRegistry.add("api_binance_base_url", binanceMockServer::baseUrl);
    }

    static public void startMockedServers() {
        binanceMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        binanceMockServer.start();

        dollarToPesoConversionRateMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        dollarToPesoConversionRateMockServer.start();
    }

    static public void stopMockedServers() {
        binanceMockServer.stop();
        dollarToPesoConversionRateMockServer.stop();
    }

    public void mockServerToRespondWithDollarToPesoRatioOf(Float dollarToPesoRatioToReturn) {
        var dollarToPesoRatioToReturnAsString = dollarToPesoRatioToReturn.toString().replace('.', ',');

        dollarToPesoConversionRateMockServer.stubFor(get("/")
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("[{\"casa\": {\"nombre\": \"Blue\", \"venta\": \"" + dollarToPesoRatioToReturnAsString + "\"}}]")));
    }

    public void mockServerToRespondWithSymbolPrice(String symbol, Float assetPrice) {
        binanceMockServer.stubFor(get("/api/v3/ticker/price?symbol=" + symbol)
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("{\"symbol\": \"BTC\", \"price\": " +  assetPrice + "}")));
    }

}
