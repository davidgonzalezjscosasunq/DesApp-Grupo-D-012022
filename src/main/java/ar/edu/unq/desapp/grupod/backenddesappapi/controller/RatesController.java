package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unq.desapp.grupod.backenddesappapi.service.RateService;
import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.CoinRateDTO;


@RestController
public class RatesController {

    @Autowired
    private RateService rateService;

    @GetMapping(value = "/rates/{symbol}")
    public ResponseEntity<CoinRateDTO> getCoinRate(@PathVariable("symbol") String symbol) {
        var coinRate = rateService.getCoinRate(symbol);
        return new ResponseEntity<CoinRateDTO>(CoinRateDTO.form(coinRate), HttpStatus.OK);
    }

}
