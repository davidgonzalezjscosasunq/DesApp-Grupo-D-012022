package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TradedVolumeTest extends ServiceWithMockedServersTest {

    @Test
    void shouldCalculateVolumeAccordingToCurrentCoinRate() {
        var symbol = CRYPTO_ACTIVE_SYMBOL;
        var assetPrice = 10.f;
        var dollarToPeso = 202.f;
        var assetPesoPrice = assetPrice * dollarToPeso;

        mockServerToRespondWithSymbolPrice(symbol, assetPrice);
        mockServerToRespondWithDollarToPesoRatioOf(dollarToPeso);

        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();
        var anAdvertisement = publishAdvertisementFor(aPublisher, 20);

        var quantityOfNotConfirmedTransaction = 1;
        var quantityOfConfirmedTransaction = anAdvertisement.quantity() - quantityOfNotConfirmedTransaction;

        tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), quantityOfNotConfirmedTransaction);
        var transactionToConfirm = tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), quantityOfConfirmedTransaction);

        tradingService.confirmTransaction(aPublisher.id(), transactionToConfirm.id());

        var tradedVolume = tradingService.getTradedVolumeBetweenDatesForUser(anInterestedUser.id(), LocalDateTime.parse("2021-12-30T19:34:50.63"), LocalDateTime.now());

        assertEquals(CRYPTO_ACTIVE_SYMBOL, tradedVolume.assets().get(0).symbol());
        assertEquals(quantityOfConfirmedTransaction, tradedVolume.assets().get(0).nominalAmount());
        assertEquals( assetPesoPrice, tradedVolume.assets().get(0).currentPriceInPesos());
        assertEquals(assetPrice, tradedVolume.assets().get(0).currentPriceInUsd());
        assertEquals(assetPesoPrice * quantityOfConfirmedTransaction, tradedVolume.tradedValueInPesos());
        assertEquals(assetPrice * quantityOfConfirmedTransaction, tradedVolume.tradedValueInUsd());
    }
}
