package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class RateServiceTest extends ServiceWithMockedServersTest {

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

}
