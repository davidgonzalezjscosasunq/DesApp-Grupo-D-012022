package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.asset_advertisement.AssetAdvertisementCreationDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.asset_advertisement.AssetAdvertisementDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.transaction.TransactionCreationDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.transaction.TransactionDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TradingController {

    @Autowired
    TradingService tradingService;

    @PostMapping(value = "/advertisements")
    public ResponseEntity<AssetAdvertisementDTO> postAdvertisement(@RequestBody AssetAdvertisementCreationDTO assetAdvertisementCreationDTO) {
        var assetAdvertisement = tradingService.postAdvertisement(assetAdvertisementCreationDTO.advertisementType(), assetAdvertisementCreationDTO.publisherId(), assetAdvertisementCreationDTO.assetSymbol(), assetAdvertisementCreationDTO.quantity(), assetAdvertisementCreationDTO.price());

        return new ResponseEntity<AssetAdvertisementDTO>(AssetAdvertisementDTO.form(assetAdvertisement), HttpStatus.CREATED);
    }

    @PostMapping(value = "/transactions")
    public TransactionDTO informTransaction(@RequestBody TransactionCreationDTO transactionCreationDTO) {
        var transaction = tradingService.informTransaction(transactionCreationDTO.interestedUserId(), transactionCreationDTO.advertisementId(), transactionCreationDTO.quantityToTransfer());

        return TransactionDTO.form(transaction);
    }

}
