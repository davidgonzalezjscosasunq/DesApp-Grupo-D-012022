package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
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

    @InjectMocks
    private TradingService tradingService;

    @Mock
    private RateService rateService;

    @Mock
    UserRepository userRepository;

    @Mock
    TransactionsRepository transactionsRepository;

    @Mock
    Clock clock;

    private CoinRate coinRate;

    private User anInterestedUser;
    private User aPublisher;
    private AssetAdvertisement anAdvertisement;
    private List<Transaction> transactions = new ArrayList<>();

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        coinRate = new CoinRate((float) 20, (float) 60);
        anInterestedUser = registerPepe();
        aPublisher = registerJuan();
        anAdvertisement = publishAdvertisementFor(aPublisher, 20);
        clock = new SimulatedClock();

        var transaction = new Transaction(anInterestedUser, anAdvertisement, anAdvertisement.quantity(), LocalDateTime.now());
        transaction.confirmBy(aPublisher);
        transactions.add(transaction);

        Mockito.when(rateService.getCoinRate(CRYPTO_ACTIVE_SYMBOL)).thenReturn(coinRate);
        Mockito.when(userRepository.findById(anInterestedUser.id())).thenReturn(java.util.Optional.ofNullable(anInterestedUser));
        Mockito.when(transactionsRepository.findAllByUserIdBetweenDates(anInterestedUser.id(), LocalDateTime.parse("2021-12-30T19:34:50.63"), LocalDateTime.parse("2022-03-30T19:34:50.63"))).thenReturn(transactions);
    }

    @Test
    void shouldCalculateVolumeAccordingToCurrentCoinRate() {
       var volume = tradingService.getTradedVolumeBetweenDatesForUser(anInterestedUser.id(), LocalDateTime.parse("2021-12-30T19:34:50.63"), LocalDateTime.parse("2022-03-30T19:34:50.63"));

       var currentRate = rateService.getCoinRate(CRYPTO_ACTIVE_SYMBOL);

       assertEquals(CRYPTO_ACTIVE_SYMBOL, volume.assets().get(0).symbol());
       assertEquals(20, volume.assets().get(0).nominalAmount());
       assertEquals( currentRate.pesosPrice(), volume.assets().get(0).currentPriceInPesos());
       assertEquals(currentRate.usdPrice(), volume.assets().get(0).currentPriceInUsd());
       assertEquals(currentRate.pesosPrice() * 20, volume.tradedValueInPesos());
       assertEquals(currentRate.usdPrice() * 20, volume.tradedValueInUsd());
    }
}
