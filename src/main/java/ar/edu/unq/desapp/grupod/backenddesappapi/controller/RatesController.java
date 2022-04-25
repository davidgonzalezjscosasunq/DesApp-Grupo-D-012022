package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import ar.edu.unq.desapp.grupod.backenddesappapi.service.RateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RatesController {

    RateService rateService = new RateService();

    @GetMapping(value = "/rates/{symbol}")
    public Object getCoinRates(@PathVariable("symbol") String symbol) throws JsonProcessingException {

        Object rates = rateService.getCoinRates(symbol);

        return rates;
    }

}
