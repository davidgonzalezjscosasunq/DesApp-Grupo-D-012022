package ar.edu.unq.desapp.grupod.backenddesappapi.service;

//import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TradedVolumeTest extends ServiceTest{
    //@Test
//    void recentTransactionInfoWillBeShownIfUserChecksVolumeTradedSoFar() {
//        var anInterestedUser = registerPepe();
//        var aPublisher = registerJuan();
//
//        var anAdvertisement = publishAdvertisementFor(aPublisher, 20);
//        var transactionToConfirm = tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), anAdvertisement.quantity());
//
//        tradingService.confirmTransaction(aPublisher.id(), transactionToConfirm.id());
//
//        var transactions = tradingService.findTransactionsInformedBy(anInterestedUser.id());
//
//        var volume = tradingService.getTradedVolumeBetweenDatesForUser(anInterestedUser.id(), LocalDateTime.parse("2021-12-30T19:34:50.63"), LocalDateTime.now());
//
//        var currentRate = rateService.getCoinRate(CRYPTO_ACTIVE_SYMBOL);
//
//        assertTrue(volume.assets().get(0).symbol().equals(CRYPTO_ACTIVE_SYMBOL));
//        assertEquals(volume.assets().get(0).nominalAmount(), 20);
//        assertEquals(volume.assets().get(0).currentPriceInPesos(), currentRate.pesosPrice());
//        assertEquals(volume.assets().get(0).currentPriceInUsd(), currentRate.usdPrice());
//        assertEquals(volume.tradedValueInPesos(), currentRate.pesosPrice() * 20);
//        assertEquals(volume.tradedValueInUsd(), currentRate.usdPrice() * 20);
//    }
}
