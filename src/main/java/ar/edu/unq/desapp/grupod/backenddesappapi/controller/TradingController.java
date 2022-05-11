package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.AssetAdvertisementDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.UserDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.UserRegistrationDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.AssetAdvertisement;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.AssetAdvertisementType;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.ModelException;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.Transaction;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.TradingService;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TradingController {

    @Autowired
    TradingService tradingService;

    @PostMapping(value = "/advertisements")
    public ResponseEntity<AssetAdvertisementDTO> postAdvertisement(@RequestBody PostAdvertisementCreationDTO postAdvertisementCreationDTO) {
        var assetAdvertisement = tradingService.postAdvertisement(postAdvertisementCreationDTO.advertisementType(), postAdvertisementCreationDTO.sellerId(), postAdvertisementCreationDTO.assetSymbol(), postAdvertisementCreationDTO.quantityToSell(), postAdvertisementCreationDTO.sellingPrice());

        return new ResponseEntity(AssetAdvertisementDTO.form(assetAdvertisement), HttpStatus.CREATED);
    }

}
