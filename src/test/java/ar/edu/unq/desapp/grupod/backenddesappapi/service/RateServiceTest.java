package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class RateServiceTest extends ServiceTest {
    private static WireMockServer binanceMockServer;
    private static WireMockServer estadisticasBCRAMockServer;

    @Value("${security.bcra_token}")
    public String bcraToken;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("api_estadisticasbcra_base_url", estadisticasBCRAMockServer::baseUrl);
        dynamicPropertyRegistry.add("api_binance_base_url", binanceMockServer::baseUrl);
    }

    @Autowired
    RateService rateService;

    @BeforeAll
    static void beforeAll() {
        binanceMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        binanceMockServer.start();

        estadisticasBCRAMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        estadisticasBCRAMockServer.start();
    }

    @AfterAll
    static void afterAll() {
        binanceMockServer.stop();
        estadisticasBCRAMockServer.stop();
    }

    @Test
    public void dollarToPesoConversionRateSuccessfully() {
        var dollarToPesoRatio = 999.f;
        mockServerToRespondWithDollarToPesoRatioOf(dollarToPesoRatio);

        assertThat(rateService.dollarToPesoConversionRate()).isEqualTo(dollarToPesoRatio);
    }

    @Test
    public void getCoinRateSuccessfully() {
        var symbol = "BTC";
        var assetPrice = 10.f;
        var dollarToPesoRatio = 999.f;

        mockServerToRespondWithSymbolPrice(symbol, assetPrice);
        mockServerToRespondWithDollarToPesoRatioOf(dollarToPesoRatio);

        var coinRate = rateService.getCoinRate(symbol);

        assertThat(coinRate.usdPrice()).isEqualTo(assetPrice);
        assertThat(coinRate.pesosPrice()).isEqualTo(assetPrice * dollarToPesoRatio);
    }

    private void mockServerToRespondWithDollarToPesoRatioOf(Float dollarToPesoRatioToReturn) {
        estadisticasBCRAMockServer.stubFor(get("/usd")
                .withHeader("Authorization", equalTo("Bearer " + bcraToken))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("[{\"d\": \"2003-01-03\", \"v\": " + dollarToPesoRatioToReturn + "}]")));
    }

    private void mockServerToRespondWithSymbolPrice(String symbol, Float assetPrice) {
        binanceMockServer.stubFor(get("/api/v3/ticker/price?symbol=" + symbol)
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("{\"symbol\": \"BTC\", \"price\": " +  assetPrice + "}")));
    }
}
