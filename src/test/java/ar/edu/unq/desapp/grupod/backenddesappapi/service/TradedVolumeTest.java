package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.AssetAdvertisement;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.Transaction;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.User;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.clock.Clock;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.clock.SimulatedClock;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.TransactionsRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.UserRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.CoinRate;


@SpringBootTest
public class TradedVolumeTest extends ServiceTest{

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void beforeAll() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8080);
        stubFor(get(urlEqualTo("http://localhost:8080/rate233332s/" + CRYPTO_ACTIVE_SYMBOL))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "appication/json")
                        .withBody("{\"usdPrice\":20.66,\"pesosPrice\":4235.3}")));
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @Test
    void shouldCalculateVolumeAccordingToCurrentCoinRate() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishAdvertisementFor(aPublisher, 20);
        var transactionToConfirm = tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), anAdvertisement.quantity());

        tradingService.confirmTransaction(aPublisher.id(), transactionToConfirm.id());

        var transactions = tradingService.findTransactionsInformedBy(anInterestedUser.id());

        var volume = tradingService.getTradedVolumeBetweenDatesForUser(anInterestedUser.id(), LocalDateTime.parse("2021-12-30T19:34:50.63"), LocalDateTime.now());

        var currentRate = rateService.getCoinRate(CRYPTO_ACTIVE_SYMBOL);

        assertEquals(CRYPTO_ACTIVE_SYMBOL, volume.assets().get(0).symbol());
        assertEquals(20, volume.assets().get(0).nominalAmount());
        assertEquals( currentRate.pesosPrice(), volume.assets().get(0).currentPriceInPesos());
        assertEquals(currentRate.usdPrice(), volume.assets().get(0).currentPriceInUsd());
        assertEquals(currentRate.pesosPrice() * 20, volume.tradedValueInPesos());
        assertEquals(currentRate.usdPrice() * 20, volume.tradedValueInUsd());
    }
}
