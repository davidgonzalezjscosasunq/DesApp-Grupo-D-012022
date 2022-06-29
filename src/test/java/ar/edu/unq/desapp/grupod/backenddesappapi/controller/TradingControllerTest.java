package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.*;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.AssetAdvertisementType;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.TransactionState;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
    public void createAssetAdvertisementFailsWithNotFoundWhenUserIsNotFound() {
        var notRegisteredUserId = -999999L;
        var postAdvertisementCreationDTO = new PostAdvertisementCreationDTO(AssetAdvertisementType.BUY_ADVERTISEMENT, notRegisteredUserId, "BNB", 10, 100.0);

        var responseEntity = restTemplate.postForEntity(assetAdvertisementURL(), new HttpEntity(postAdvertisementCreationDTO), String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("User not found");
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
    public void informTransactionFailsWithNotFoundWhenUserIsNotFound() {
        var pepeGomezDTO = registerPepeGomez();
        var assetAdvertisementDTO = postAssetAdvertisementFor(pepeGomezDTO);

        var notRegisteredUserId = -999L;
        var transactionCreationDTO = new InformTransactionDTO(notRegisteredUserId, assetAdvertisementDTO.id(), 100);

        var responseEntity = restTemplate.postForEntity(transactionURL(), transactionCreationDTO, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("User not found");
    }

    @Test
    public void informTransactionFailsWithNotFoundWhenAssetAdvertisementIsNotFound() {
        var pepeGomezDTO = registerPepeGomez();

        var notPostedAssetAdvertisementId = -999L;
        var transactionCreationDTO = new InformTransactionDTO(pepeGomezDTO.id(), notPostedAssetAdvertisementId, 100);

        var responseEntity = restTemplate.postForEntity(transactionURL(), transactionCreationDTO, String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Advertisement not found");
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
    public void confirmTransactionFailsWithNotFoundWhenUserNotFound() {
        var pepeGomezDTO = registerPepeGomez();
        var juanPerezDTO = registerJuanPerez();
        var assetAdvertisementDTO = postAssetAdvertisementFor(pepeGomezDTO);
        var transactionDTO = informTransaction(juanPerezDTO, assetAdvertisementDTO).getBody();

        var notRegisteredUserId = -999999L;
        var updateTransactionDTO = new UpdateTransactionDTO(notRegisteredUserId, transactionDTO.transactionId(), UpdateTransactionType.CONFIRM);

        var responseEntity = restTemplate.exchange(transactionURL(), HttpMethod.PUT, new HttpEntity<UpdateTransactionDTO>(updateTransactionDTO), String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("User not found");
    }

    @Test
    public void confirmTransactionFailsWithNotFoundWhenTransactionIsNotFound() {
        var pepeGomezDTO = registerPepeGomez();

        var notRegisteredTransactionId = -999L;
        var transactionDTO = new TransactionDTO(notRegisteredTransactionId, "DAI", TransactionState.PENDING);
        var updateTransactionDTO = UpdateTransactionDTO.confirmationFor(pepeGomezDTO, transactionDTO);

        var responseEntity = restTemplate.exchange(transactionURL(), HttpMethod.PUT, new HttpEntity<UpdateTransactionDTO>(updateTransactionDTO), String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Transaction not found");
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
    public void cancelTransactionFailsWithNotFoundWhenTheUserIsNotFound() {
        var pepeGomezDTO = registerPepeGomez();
        var juanPerezDTO = registerJuanPerez();
        var assetAdvertisementDTO = postAssetAdvertisementFor(pepeGomezDTO);
        var transactionDTO = informTransaction(juanPerezDTO, assetAdvertisementDTO).getBody();

        var notRegisteredUserId = -999999L;
        var updateTransactionDTO = new UpdateTransactionDTO(notRegisteredUserId, transactionDTO.transactionId(), UpdateTransactionType.CANCEL);

        var responseEntity = restTemplate.exchange(transactionURL(), HttpMethod.PUT, new HttpEntity<UpdateTransactionDTO>(updateTransactionDTO), String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("User not found");
    }

    @Test
    public void cancelTransactionFailsWithNotFoundWhenTheTransactionIsNotFound() {
        var pepeGomezDTO = registerPepeGomez();
        var notRegisteredTransactionId = -999999L;

        var updateTransactionDTO = new UpdateTransactionDTO(pepeGomezDTO.id(), notRegisteredTransactionId, UpdateTransactionType.CANCEL);

        var responseEntity = restTemplate.exchange(transactionURL(), HttpMethod.PUT, new HttpEntity<UpdateTransactionDTO>(updateTransactionDTO), String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Transaction not found");
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

    @Test
    public void getTradedVolumeSuccessfully() {
        var pepeGomezDTO = registerPepeGomez();
        var juanPerezDTO = registerJuanPerez();
        var assetAdvertisementDTO = postAssetAdvertisementFor(pepeGomezDTO);

        var assetPriceInDollars = 10.f;
        var dollarToPesoConversionRatio = 202.f;
        var assetPesoPrice = assetPriceInDollars * dollarToPesoConversionRatio;

        mockServerToRespondWithSymbolPrice(assetAdvertisementDTO.assetSymbol(), assetPriceInDollars);
        mockServerToRespondWithDollarToPesoRatioOf(dollarToPesoConversionRatio);

        var transactionDTO = informTransaction(juanPerezDTO, assetAdvertisementDTO).getBody();
        var confirmedTransactionDTO = confirmTransaction(pepeGomezDTO, transactionDTO).getBody();

        var responseEntity = getTradedVolumeBy(juanPerezDTO, createTradedVolumeBodyDTO());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getTradedValueInPesos()).isEqualTo(assetPesoPrice * assetAdvertisementDTO.quantity());
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