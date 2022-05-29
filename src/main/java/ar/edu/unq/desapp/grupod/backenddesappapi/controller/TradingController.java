package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.*;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.TradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "advertisements")
public class TradingController {

    @Autowired
    TradingService tradingService;

    @PostMapping
    public ResponseEntity<AssetAdvertisementDTO> postAdvertisement(@RequestBody PostAdvertisementCreationDTO postAdvertisementCreationDTO) {
        var assetAdvertisement = tradingService.postAdvertisement(postAdvertisementCreationDTO.assetAdvertisementType(), postAdvertisementCreationDTO.publisherId(), postAdvertisementCreationDTO.assetSymbol(), postAdvertisementCreationDTO.quantity(), postAdvertisementCreationDTO.price());

        return ResponseEntity.status(HttpStatus.CREATED).body(AssetAdvertisementDTO.form(assetAdvertisement));
    }

    @GetMapping(value = "/{assetSymbol}")
    public ResponseEntity<List<AssetAdvertisementDTO>> findAssetAdvertisementsWithSymbol(@PathVariable String assetSymbol) {
        var assetAdvertisements = tradingService.findAdvertisementsWithSymbol(assetSymbol);

        return ResponseEntity.ok().body(assetAdvertisements.stream().map(AssetAdvertisementDTO::form).collect(Collectors.toList()));
    }

    @GetMapping(value = "/sell/{assetSymbol}")
    public ResponseEntity<List<AssetAdvertisementDTO>> findSellAdvertisementsWithSymbol(@PathVariable String assetSymbol) {
        var assetAdvertisements = tradingService.findSellAdvertisementsWithSymbol(assetSymbol);

        return ResponseEntity.ok().body(assetAdvertisements.stream().map(AssetAdvertisementDTO::form).collect(Collectors.toList()));
    }

    @GetMapping(value = "/buy/{assetSymbol}")
    public ResponseEntity<List<AssetAdvertisementDTO>> findBuyAdvertisementsWithSymbol(@PathVariable String assetSymbol) {
        var assetAdvertisements = tradingService.findBuyAdvertisementsWithSymbol(assetSymbol);

        return ResponseEntity.ok().body(assetAdvertisements.stream().map(AssetAdvertisementDTO::form).collect(Collectors.toList()));
    }

    @PostMapping(value = "/transactions")
    public ResponseEntity<TransactionDTO> informTransaction(@RequestBody InformTransactionDTO informTransactionDTO) {
        var transaction = tradingService.informTransaction(informTransactionDTO.interestedUserId(), informTransactionDTO.advertisementId(), informTransactionDTO.quantityToTransfer());

        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionDTO.from(transaction));
    }

    @PutMapping(value = "/transactions")
    public ResponseEntity<TransactionDTO> updateTransaction(@RequestBody UpdateTransactionDTO updateTransactionDTO) {
        var updatedTransaction = updateTransactionDTO.applyTo(tradingService);

        return ResponseEntity.status(HttpStatus.OK).body(TransactionDTO.from(updatedTransaction));
    }

    @GetMapping(value = "/transactions/users/{userId}")
    public ResponseEntity<List<TransactionDTO>> findTransactionsInformedBy(@PathVariable Long userId) {
        var transactions = tradingService.findTransactionsInformedBy(userId);

        return ResponseEntity.status(HttpStatus.OK).body(transactions.stream().map(TransactionDTO::from).collect(Collectors.toList()));
    }

}
