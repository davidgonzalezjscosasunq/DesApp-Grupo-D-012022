package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.unq.desapp.grupod.backenddesappapi.service.RateService;


@RestController
public class RatesController {

    RateService rateService = new RateService();

    @GetMapping(value = "/rates/{symbol}")
    public Object getCoinRates(@PathVariable("symbol") String symbol) {

        Object rates = rateService.getCoinRate(symbol);

        return rates;
    }

}
