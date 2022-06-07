package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import ar.edu.unq.desapp.grupod.backenddesappapi.service.apiCalls.BCRARequest;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.apiCalls.BinanceRequest;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.BinanceRatesResponse;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.UsdResponse;


@SpringBootTest
public class RateServiceTest extends ServiceTest{
    @InjectMocks
    private RateService rateService;

    @Mock
    private BCRARequest bcraRequest;

    @Mock
    private BinanceRequest binanceRequest;

    private BinanceRatesResponse binanceRatesResponse = new BinanceRatesResponse();
    private List<UsdResponse> usdResponses= new ArrayList<>();;

    @BeforeEach
    void init(){
        UsdResponse usdResponse = new UsdResponse();
        usdResponse.setD("");
        usdResponse.setV(205f);
        usdResponses.add(usdResponse);

        binanceRatesResponse.setSymbol(CRYPTO_ACTIVE_SYMBOL);
        binanceRatesResponse.setPrice(19.66f);

        Mockito.when(bcraRequest.getUsdConvertionToPesos()).thenReturn(usdResponses);
        Mockito.when(binanceRequest.getBinanceRate(CRYPTO_ACTIVE_SYMBOL)).thenReturn(binanceRatesResponse);
    }

    @Test
    void shouldGetValueInPesosAndDollarsAccordingToApiResponse(){
        var currentRate = rateService.getCoinRate(CRYPTO_ACTIVE_SYMBOL);
        assertEquals(currentRate.usdPrice(), 19.66f);
        assertEquals(currentRate.pesosPrice(), 4030.3f);
    }
}
