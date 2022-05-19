package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.AssetAdvertisementDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TradingControllerTest extends ControllerTest {

    @Test
    public void createAssetAdvertisementSuccessfully() {
        var pepeGomezDTO = registerPepeGomez();
        var assetAdvertisementCreationDTO = createAssetAdvertisementCreationDTOFor(pepeGomezDTO);

        var responseEntity = postAssetAdvertisement(assetAdvertisementCreationDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody().id()).isNotNull();
        assertThat(responseEntity.getBody().type()).isEqualTo(assetAdvertisementCreationDTO.assetAdvertisementType());
        assertThat(responseEntity.getBody().assetSymbol()).isEqualTo(assetAdvertisementCreationDTO.assetSymbol());
        assertThat(responseEntity.getBody().quantity()).isEqualTo(assetAdvertisementCreationDTO.quantity());
        assertThat(responseEntity.getBody().price()).isEqualTo(assetAdvertisementCreationDTO.price());
        assertThat(responseEntity.getBody().publisherId()).isEqualTo(assetAdvertisementCreationDTO.publisherId());
    }

    @Test
    public void findBuyAssetAdvertisementsWithSymbolSuccessfully() {
        var pepeGomezDTO = registerPepeGomez();
        postAssetAdvertisement(createSellAssetAdvertisementCreationDTOFor(pepeGomezDTO)).getBody();
        var buyAssetAdvertisementDTO = postAssetAdvertisement(createBuyAssetAdvertisementCreationDTOFor(pepeGomezDTO)).getBody();

        var responseEntity = findBuyAdvertisementsWithSymbol(buyAssetAdvertisementDTO.assetSymbol());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().length).isEqualTo(1);
        assertEqualAssetAdvertisementDTO(buyAssetAdvertisementDTO, responseEntity.getBody()[0]);
    }

    @Test
    public void findSellAssetAdvertisementsWithSymbolSuccessfully() {
        var pepeGomezDTO = registerPepeGomez();
        var sellAssetAdvertisementDTO = postAssetAdvertisement(createSellAssetAdvertisementCreationDTOFor(pepeGomezDTO)).getBody();
        postAssetAdvertisement(createBuyAssetAdvertisementCreationDTOFor(pepeGomezDTO)).getBody();

        var responseEntity = findSellAdvertisementsWithSymbol(sellAssetAdvertisementDTO.assetSymbol());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().length).isEqualTo(1);
        assertEqualAssetAdvertisementDTO(sellAssetAdvertisementDTO, responseEntity.getBody()[0]);
    }

    @Test
    public void findAssetAdvertisementsWithSymbolSuccessfully() {
        var pepeGomezDTO = registerPepeGomez();
        var assetAdvertisementDTO = postAssetAdvertisement(createAssetAdvertisementCreationDTOFor(pepeGomezDTO)).getBody();

        var responseEntity = findAdvertisementsWithSymbol(assetAdvertisementDTO.assetSymbol());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().length).isEqualTo(1);
        assertEqualAssetAdvertisementDTO(assetAdvertisementDTO, responseEntity.getBody()[0]);
    }

    @Test
    public void informTransactionSuccessfully() {
        var pepeGomezDTO = registerPepeGomez();
        var juanPerezDTO = registerJuanPerez();
        var assetAdvertisementDTO = postAssetAdvertisementFor(pepeGomezDTO);

        var responseEntity = informTransaction(juanPerezDTO, assetAdvertisementDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody().transactionId()).isNotNull();
        assertThat(responseEntity.getBody().state()).isEqualTo("PENDING");
        assertThat(responseEntity.getBody().assetSymbol()).isEqualTo(assetAdvertisementDTO.assetSymbol());
    }

    @Test
    public void confirmTransactionSuccessfully() {
        var pepeGomezDTO = registerPepeGomez();
        var juanPerezDTO = registerJuanPerez();
        var assetAdvertisementDTO = postAssetAdvertisementFor(pepeGomezDTO);
        var transactionDTO = informTransaction(juanPerezDTO, assetAdvertisementDTO).getBody();

        var responseEntity = confirmTransaction(pepeGomezDTO, transactionDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().transactionId()).isNotNull();
        assertThat(responseEntity.getBody().state()).isEqualTo("CONFIRMED");
        assertThat(responseEntity.getBody().assetSymbol()).isEqualTo(assetAdvertisementDTO.assetSymbol());
    }

    @Test
    public void cancelTransactionSuccessfully() {
        var pepeGomezDTO = registerPepeGomez();
        var juanPerezDTO = registerJuanPerez();
        var assetAdvertisementDTO = postAssetAdvertisementFor(pepeGomezDTO);
        var transactionDTO = informTransaction(juanPerezDTO, assetAdvertisementDTO).getBody();

        var responseEntity = cancelTransaction(pepeGomezDTO, transactionDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().transactionId()).isNotNull();
        assertThat(responseEntity.getBody().state()).isEqualTo("CANCELLED");
        assertThat(responseEntity.getBody().assetSymbol()).isEqualTo(assetAdvertisementDTO.assetSymbol());
    }

    @Test
    public void findTransactionsInformedByUserSuccessfully() {
        var pepeGomezDTO = registerPepeGomez();
        var juanPerezDTO = registerJuanPerez();
        var assetAdvertisementDTO = postAssetAdvertisementFor(pepeGomezDTO);
        var transactionDTO = informTransaction(juanPerezDTO, assetAdvertisementDTO, assetAdvertisementDTO.quantity() - 1).getBody();

        var responseEntity = findTransactionsInformedBy(juanPerezDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()[0].transactionId()).isEqualTo(transactionDTO.transactionId());
    }

    private void assertEqualAssetAdvertisementDTO(AssetAdvertisementDTO expectedAssetAdvertisement, AssetAdvertisementDTO actualAssetAdvertisement) {
        assertThat(actualAssetAdvertisement.id()).isNotNull();
        assertThat(actualAssetAdvertisement.id()).isEqualTo(expectedAssetAdvertisement.id());
        assertThat(actualAssetAdvertisement.type()).isEqualTo(expectedAssetAdvertisement.type());
        assertThat(actualAssetAdvertisement.assetSymbol()).isEqualTo(expectedAssetAdvertisement.assetSymbol());
        assertThat(actualAssetAdvertisement.quantity()).isEqualTo(expectedAssetAdvertisement.quantity());
        assertThat(actualAssetAdvertisement.price()).isEqualTo(expectedAssetAdvertisement.price());
        assertThat(actualAssetAdvertisement.publisherId()).isEqualTo(expectedAssetAdvertisement.publisherId());
    }
}