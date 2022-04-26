package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unq.desapp.grupod.backenddesappapi.service.RateService;


@RestController
public class RatesController {

    @Autowired
    private RateService rateService;

    @GetMapping(value = "/rates/{symbol}")
    public Object getCoinRates(@PathVariable("symbol") String symbol) {
        return rateService.getCoinRate(symbol);
    }

}
