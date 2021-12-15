package com.murilo.currency.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.murilo.currency.enumeration.FunctionRateEnum;
import com.murilo.currency.enumeration.RefRateEnum;
import com.murilo.currency.request.CurrencyExchangeRequest;
import com.murilo.currency.request.RateRequest;
import com.murilo.currency.response.CurrencyExchangeResponse;
import com.murilo.currency.response.CurrencyResponse;
import com.murilo.currency.response.PanelResponse;
import com.murilo.currency.response.RateResponse;
import com.murilo.currency.service.RateService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/rates")
@AllArgsConstructor
@NoArgsConstructor
public class RateController {

	@Autowired
	private RateService rateService;
	
	@GetMapping(value = "/findCurrencyData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrencyResponse> findCurrencyData() {
        try{
        	CurrencyResponse response = rateService.findCurrencyData();
        	if(!response.getCurrencies().isEmpty()) {
            	return ResponseEntity.ok().body(response);
            }else {
                return ResponseEntity.noContent().build();
            }
        } catch(Exception ex){
        	ex.printStackTrace();
            log.error(ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
	
	@GetMapping(value = "/findCurrencyExchangeRate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrencyExchangeResponse> findCurrencyExchangeData(
    		@RequestParam(name = "from", defaultValue = "USD") String from,
    		@RequestParam(name = "to", defaultValue = "EUR") String to) {
        try{
        	CurrencyExchangeRequest request = CurrencyExchangeRequest.builder()
        			.from(from)
        			.to(to)
        			.build();
        	
        	CurrencyExchangeResponse response = rateService.findCurrencyExchangeData(request);
        	if(response.getCurrencyExchange() != null) {
            	return ResponseEntity.ok().body(response);
            }else {
                return ResponseEntity.ok().body(response);
            }
        } catch(Exception ex){
        	ex.printStackTrace();
            log.error(ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
	
	@GetMapping(value = "/findHistoricalData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RateResponse> findHistoricalData(
    		@RequestParam(name = "functionRate") String functionRate,
    		@RequestParam(name = "refRate") String refRate,
    		@RequestParam(name = "from", defaultValue = "USD") String from,
    		@RequestParam(name = "to", defaultValue = "EUR") String to) {
        try{
        	RateRequest rateRequest = RateRequest.builder()
        			.functionRate(FunctionRateEnum.valueOf(functionRate))
        			.refRate(RefRateEnum.valueOf(refRate))
        			.fromSymbol(from)
        			.toSymbol(to)
        			.build();
        			
            RateResponse response = rateService.findHistoricalData(rateRequest);
            if(!response.getRates().isEmpty()) {
            	return ResponseEntity.ok().body(response);
            }else {
                return ResponseEntity.noContent().build();
            }
        } catch(Exception ex){
        	ex.printStackTrace();
            log.error(ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
	
	@PutMapping(value = "/updateExchange", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrencyExchangeResponse> updateCurrencyExchangeData(@RequestBody CurrencyExchangeRequest request) {
        try{
        	CurrencyExchangeResponse response = rateService.updateCurrencyExchangeData(request);
        	if(!response.getSuccess().isEmpty()) {
            	return ResponseEntity.accepted().body(response);//202
            }else {
                return ResponseEntity.notFound().build();//404
            }
        } catch(Exception ex){
        	ex.printStackTrace();
            log.error(ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
	
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrencyExchangeResponse> updateRates(@RequestParam String from,  @RequestParam String to) {
        try{
        	CurrencyExchangeRequest request= CurrencyExchangeRequest.builder()
        			.from(from)
        			.to(to)
        			.build();
        	CurrencyExchangeResponse response = rateService.updateCurrencyExchangeData(request);
        	if(!response.getSuccess().isEmpty()) {
            	return ResponseEntity.accepted().body(response);//202
            }else {
                return ResponseEntity.notFound().build();//404
            }
        } catch(Exception ex){
        	ex.printStackTrace();
            log.error(ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
	
	@GetMapping(value = "/panelData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PanelResponse> getPanelData() {
        try{
        	PanelResponse response = rateService.getPanelData();
        	if(response.getNote().isEmpty()) {
            	return ResponseEntity.ok().body(response);//202
            }else {
                return ResponseEntity.notFound().build();//404
            }
        } catch(Exception ex){
        	ex.printStackTrace();
            log.error(ex.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

}
