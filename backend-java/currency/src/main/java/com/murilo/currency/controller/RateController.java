package com.murilo.currency.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murilo.currency.request.CurrencyExchangeRequest;
import com.murilo.currency.request.RateRequest;
import com.murilo.currency.response.CurrencyExchangeResponse;
import com.murilo.currency.response.CurrencyResponse;
import com.murilo.currency.response.PanelResponse;
import com.murilo.currency.response.RateResponse;
import com.murilo.currency.service.RateService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/rate")
public class RateController {

	@Autowired
	private RateService rateService;
	
	@GetMapping(value = "/findCurrencyData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrencyResponse> findCurrencyData() {
        try{
        	CurrencyResponse response = rateService.findCurrencyData();
            
            return ResponseEntity.ok().body(response);
        } catch(Exception ex){
        	ex.printStackTrace();
            log.error(ex.getMessage());
            return ResponseEntity.ok().body(null);
        }
    }
	
	@PostMapping(value = "/findCurrencyExchangeRate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrencyExchangeResponse> findCurrencyExchangeData(@RequestBody CurrencyExchangeRequest request) {
        try{
        	CurrencyExchangeResponse response = rateService.findCurrencyExchangeData(request);
            
            return ResponseEntity.ok().body(response);
        } catch(Exception ex){
        	ex.printStackTrace();
            log.error(ex.getMessage());
            return ResponseEntity.ok().body(null);
        }
    }
	
	@PostMapping(value = "/findHistoricalData", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RateResponse> findHistoricalData(@RequestBody RateRequest rateRequest) {
        try{
            RateResponse response = rateService.findHistoricalData(rateRequest);
            
            return ResponseEntity.ok().body(response);
        } catch(Exception ex){
        	ex.printStackTrace();
            log.error(ex.getMessage());
            return ResponseEntity.ok().body(null);
        }
    }
	
	@PostMapping(value = "/updateExchange", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrencyExchangeResponse> updateCurrencyExchangeData(@RequestBody CurrencyExchangeRequest request) {
        try{
        	CurrencyExchangeResponse response = rateService.updateCurrencyExchangeData(request);
            
            return ResponseEntity.ok().body(response);
        } catch(Exception ex){
        	ex.printStackTrace();
            log.error(ex.getMessage());
            return ResponseEntity.ok().body(null);
        }
    }
	
	@PutMapping(value = "/rates", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrencyExchangeResponse> updateCurrencyExchangeData(@RequestParam String from,  @RequestParam String to) {
        try{
        	CurrencyExchangeRequest request= new CurrencyExchangeRequest();
        	request.setFrom(from);
	        request.setTo(to);
        	CurrencyExchangeResponse response = rateService.updateCurrencyExchangeData(request);
            
            return ResponseEntity.ok().body(response);
        } catch(Exception ex){
        	ex.printStackTrace();
            log.error(ex.getMessage());
            return ResponseEntity.ok().body(null);
        }
    }
	
	@GetMapping(value = "/getPanelData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PanelResponse> getPanelData() {
        try{
        	PanelResponse response = rateService.getPanelData();
            
            return ResponseEntity.ok().body(response);
        } catch(Exception ex){
        	ex.printStackTrace();
            log.error(ex.getMessage());
            return ResponseEntity.ok().body(null);
        }
    }

}
