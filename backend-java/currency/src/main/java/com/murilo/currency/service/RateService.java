package com.murilo.currency.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.murilo.currency.DTO.RateDTO;
import com.murilo.currency.entity.Currency;
import com.murilo.currency.entity.CurrencyExchange;
import com.murilo.currency.entity.Rate;
import com.murilo.currency.enumeration.FunctionRateEnum;
import com.murilo.currency.enumeration.OutputSizeEnum;
import com.murilo.currency.enumeration.RefRateEnum;
import com.murilo.currency.repository.RateRepository;
import com.murilo.currency.request.CurrencyExchangeRequest;
import com.murilo.currency.request.RateRequest;
import com.murilo.currency.response.CurrencyExchangeResponse;
import com.murilo.currency.response.CurrencyResponse;
import com.murilo.currency.response.PanelResponse;
import com.murilo.currency.response.RateResponse;
import com.murilo.currency.useful.Useful;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RateService {
	
	@Autowired
	private RateRepository rateRepository;
	
    public void save(CurrencyExchange currencyExchange, String key) {
    	rateRepository.save(currencyExchange, key);
    }
	
    public void saveAllRate(Map<String, Rate> map, String key) {
    	rateRepository.saveAllRate(map, key);
    }
	
    public void saveAllCurrency(Map<String, Currency> map, String key) {
    	rateRepository.saveAllCurrency(map, key);
    }

    public Map<String, Rate> findAll() {
    	return rateRepository.findAll();
    }
    
    public PanelResponse getPanelData() {
    	PanelResponse response = new PanelResponse();
    	
    	List<String> aux = findMap(Useful.EXCHANGE_POOL);
    	for (String s : aux) {
    		response.getExchangePoolList().add(s.replace(Useful.EXCHANGE_RATE, ""));
		}

    	aux = findMap(Useful.EXCHANGE_POOL_JOB);
    	for (String s : aux) {
    		response.getExchangePoolJobList().add(s.replace(Useful.EXCHANGE_RATE, ""));
		}
		
    	aux = findMap(Useful.HISTORICAL_DATA_POOL);
    	for (String s : aux) {
    		response.getHistoricalPoolList().add(s.replace(Useful.HISTORICAL_DATA, ""));
		}
    	
    	aux = findMap(Useful.HISTORICAL_DATA_POOL_JOB);
    	for (String s : aux) {
    		response.getHistoricalPoolJobList().add(s.replace(Useful.HISTORICAL_DATA, ""));
		}

    	aux = findMap(Useful.EXCHANGE_POOL_JOB_FORCED);
    	for (String s : aux) {
    		response.getExchangePoolJobForcedList().add(s.replace(Useful.EXCHANGE_RATE, ""));
		}
		
    	return response;
    }
    
    public boolean validateExchange(String from, String to) {
    	CurrencyResponse responseCurrency = findCurrencyData();
		boolean existsFrom = false;
		boolean existsTo = false;
		for (Currency currency : responseCurrency.getCurrencies()) {
			if(currency.getKey().equals(from)){
				existsFrom = true;
				if(existsTo) {break;}
			}
			if(currency.getKey().equals(to)){
				existsTo = true;
				if(existsFrom) {break;}
			}
		}
		return existsFrom && existsTo;
    }
    
    public CurrencyExchangeResponse updateCurrencyExchangeData(CurrencyExchangeRequest request) {
    	CurrencyExchangeResponse response = new CurrencyExchangeResponse();
    	
    	if(request.getFrom() != null && !request.getFrom().isEmpty() &&
    			request.getTo() != null && !request.getTo().isEmpty()) {
    		
    		if(validateExchange(request.getFrom(), request.getTo())) {
    			String key = Useful.EXCHANGE_RATE.concat(request.getFrom())
    					.concat(Useful.TO)
    					.concat(request.getTo());
        		
        		List<String> exchangeList = findMap(Useful.EXCHANGE_POOL);
    			if(exchangeList == null) {
    				exchangeList = new ArrayList<>();
    			}
    			Map<String,String> exchangeMap = new HashMap<>();
    			for (String exchange : exchangeList) {
    				exchangeMap.put(exchange, exchange);
    			}
    			exchangeMap.put(key,key);
    			addItensMap(exchangeMap, Useful.EXCHANGE_POOL);
        		
        		List<String> exchangeJobList = findMap(Useful.EXCHANGE_POOL_JOB_FORCED);
    			if(exchangeJobList == null) {
    				exchangeJobList = new ArrayList<>();
    			}
    			Map<String,String> exchangeJobMap = new HashMap<>();
    			for (String exchange : exchangeJobList) {
    				exchangeJobMap.put(exchange, exchange);
    			}
    			exchangeJobMap.put(key,key);
    			addItensMap(exchangeJobMap, Useful.EXCHANGE_POOL_JOB_FORCED);
    			
    			response.setSuccess("Currency Exchange added successfully!");
    		}else {
    			response.setError("Currency Exchange not found!");
    		}
    	}else {
    		response.setNote("Required field!");
    	}
    	return response;
    }
    
    public CurrencyExchangeResponse findCurrencyExchangeData(CurrencyExchangeRequest request) {
    	CurrencyExchangeResponse response = new CurrencyExchangeResponse();
    	String key = null;
    	
    	if(request.getFrom() != null && !request.getFrom().isEmpty() &&
    			request.getTo() != null && !request.getTo().isEmpty()) {
				
			key = Useful.EXCHANGE_RATE.concat(request.getFrom())
					.concat(Useful.TO)
					.concat(request.getTo());
				
			List<String> exchangeList = findMap(Useful.EXCHANGE_POOL);
			if(exchangeList == null) {
				exchangeList = new ArrayList<>();
			}

	    	CurrencyExchange currencyExchange = rateRepository.findCurrencyExchangeData(key);
	    	if(currencyExchange == null) {
	    		try {
	    			currencyExchange = getCurrencyExchangeDataApi(request, key);
	    			Map<String,String> exchangeMap = new HashMap<>();
	    			for (String exchange : exchangeList) {
	    				exchangeMap.put(exchange, exchange);
					}
    				exchangeMap.put(key,key);
    				addItensMap(exchangeMap, Useful.EXCHANGE_POOL);
				} catch (Exception e) {
					response.setNote(e.getMessage());
				}
	    	}
	    	if(currencyExchange != null) {
	    		try {
		    		response.setCurrencyExchange(currencyExchange);
	    		}catch(Exception e) {
	    		}
	    	}
    	}
    	return response;
    }
    
    public void refreshCurrencyExchangeData(String key) {
    	if(key != null && !key.isEmpty()) {
    		String parts[] = key.split(Useful.SEPARATOR);
    		RateRequest request = RateRequest.builder()
				.fromSymbol(parts[2].substring(0, 3))
    			.toSymbol(parts[2].substring(5, parts[2].length()))
				.build();
	    	refreshCurrencyExchangeDataApi(request, key);
    	}
    }
    
    public CurrencyResponse findCurrencyData() {
    	CurrencyResponse response = new CurrencyResponse();
    	String key = Useful.CURRENCIES;

    	List<Currency> currencyList = rateRepository.findCurrencyData(key);
    	if(currencyList == null || currencyList.isEmpty()) {
    		try {
    			currencyList = getCurrencyDataApi(key);
			} catch (Exception e) {
				response.setNote(e.getMessage());
			}
    	}
    	if(!currencyList.isEmpty()) {
    		try {
	    		Comparator<Currency> comparator = Comparator.comparing(Currency::getCurrencyName);
	        	Collections.sort(currencyList, comparator);

	    		response.setCurrencies(currencyList);
    		}catch(Exception e) {
    		}
    	}
    	return response;
    }
    
    private void setFirstDate(Calendar lastDate, Calendar firstDate, DateFormat sdf, List<Rate> rateList) throws ParseException {
    	lastDate.setTime(sdf.parse(rateList.get(rateList.size()-1).getDate()));
		firstDate.setTime(lastDate.getTime());
    }

    public RateResponse findHistoricalData(RateRequest rateRequest) {
    	RateResponse response = new RateResponse();
    	String key = null;
    	
    	if(rateRequest.getFromSymbol() != null && !rateRequest.getFromSymbol().isEmpty() &&
				rateRequest.getToSymbol() != null && !rateRequest.getToSymbol().isEmpty() &&
				rateRequest.getRefRate() != null) {
    		
				key = Useful.HISTORICAL_DATA.concat(rateRequest.getFromSymbol())
						.concat(Useful.TO)
						.concat(rateRequest.getToSymbol())
						.concat(Useful.SEPARATOR)
						.concat(rateRequest.getFunctionRate().name())
						.concat(Useful.SEPARATOR)
						.concat(rateRequest.getRefRate().name());
				
				List<String> historicalList = findMap(Useful.HISTORICAL_DATA_POOL);
				if(historicalList == null) {
					historicalList = new ArrayList<>();
				}
				
		    	RateDTO rateDTO;
		    	Double min;
		    	Double max;
		    	List<RateDTO> rateDtoList;
		    	rateRepository.deleteAll(key);
		    	List<Rate> rateList = rateRepository.findHistoricalData(key);
		    	if(rateList == null || rateList.isEmpty()) {
		    		try {
						rateList = getHistoricalDataApi(rateRequest, key);
						Map<String,String> historicalMap = new HashMap<>();
		    			for (String historical : historicalList) {
		    				historicalMap.put(historical, historical);
						}
		    			historicalMap.put(key,key);
	    				addItensMap(historicalMap, Useful.HISTORICAL_DATA_POOL);
					} catch (Exception e) {
						response.setNote(e.getMessage());
					}
		    	}
		    	if(!rateList.isEmpty()) {
		    		try {
			    		Comparator<Rate> comparator = Comparator.comparing(Rate::getDate);
			        	Collections.sort(rateList, comparator);
			    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    		
			    		Calendar firstDate = Calendar.getInstance();
			    		Calendar lastDate = Calendar.getInstance();
			    		
			    		switch(rateRequest.getRefRate()) {
			    			case FIVE_MINUTES:
			    				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    				setFirstDate(lastDate,firstDate,sdf,rateList);
			    				firstDate.add(Calendar.DAY_OF_MONTH, -1);
			    			break;
			    			case SIXTY_MINUTES:
			    				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    				setFirstDate(lastDate,firstDate,sdf,rateList);
			    				firstDate.add(Calendar.DAY_OF_MONTH, -5);
				    		break;
			    			case DAILY:
			    				sdf = new SimpleDateFormat("yyyy-MM-dd");
			    				setFirstDate(lastDate,firstDate,sdf,rateList);firstDate.getTime();lastDate.getTime();
			    				firstDate.add(Calendar.DAY_OF_MONTH, -30);
				    		break;
			    			case WEEKLY:
			    				sdf = new SimpleDateFormat("yyyy-MM-dd");
			    				setFirstDate(lastDate,firstDate,sdf,rateList);
			    				firstDate.add(Calendar.MONTH, -12);
				    		break;
			    			case MONTHLY:
			    				sdf = new SimpleDateFormat("yyyy-MM-dd");
			    				setFirstDate(lastDate,firstDate,sdf,rateList);
			    				firstDate.add(Calendar.YEAR, -5);
				    		break;
				    		default:
			    				sdf = new SimpleDateFormat("yyyy-MM-dd");
			    				lastDate.setTime(sdf.parse(rateList.get(rateList.size()-1).getDate()));
			    				firstDate.setTime(sdf.parse(rateList.get(0).getDate()));
			    		}
	    				firstDate.add(Calendar.DAY_OF_MONTH, -1);
			    		lastDate.add(Calendar.DAY_OF_MONTH, 1);
			    		
			    		rateDtoList = new ArrayList<>();
			    		max = min = Double.valueOf(rateList.get(0).getClose());
			    		for (Rate rate : rateList) {
			    			
			    			if(sdf.parse(rate.getDate()).after(firstDate.getTime()) && 
			    				sdf.parse(rate.getDate()).before(lastDate.getTime())) {
			    				
				    			rateDTO = RateDTO.builder()
				    					.key(rate.getDate())
				    					.value(Double.valueOf(rate.getClose()))
				    					.build();
				    			
				    			min = Double.min(rateDTO.getValue(), min);
				    			max = Double.max(rateDTO.getValue(), max);
				    			
				    			rateDtoList.add(rateDTO);
			    			}
						}
			    		
			    		response.setMin(min);
			    		response.setMax(max);
			    		response.setRates(rateDtoList);
		    		}catch(Exception e) {
		    		}
		    	}
    	}
    	
    	
    	return response;
    }
    
    public RateResponse refreshHistoricalData(String key) {
    	RateResponse response = new RateResponse();
    	
    	if(key != null && !key.isEmpty()) {
	    	RateDTO rateDTO;
	    	Double min;
	    	Double max;
	    	List<RateDTO> rateDtoList;
	    	List<Rate> rateList = null;
	    	
	    	String parts[] = key.split(Useful.SEPARATOR);
	    	RateRequest rateRequest = RateRequest.builder()
	    			.fromSymbol(parts[2].substring(0, 3))
	    			.toSymbol(parts[2].substring(5, parts[2].length()))
	    			.functionRate(FunctionRateEnum.valueOf(parts[3]))
	    			.refRate(RefRateEnum.valueOf(parts[4])).build();
    		try {
				rateList = getHistoricalDataApi(rateRequest, key);
			} catch (Exception e) {
				response.setNote(e.getMessage());
			}
	    	if(!rateList.isEmpty()) {
	    		try {
		    		Comparator<Rate> comparator = Comparator.comparing(Rate::getDate);
		        	Collections.sort(rateList, comparator);
		    		SimpleDateFormat sdf = null;
		    		
		    		Calendar firstDate = Calendar.getInstance();
		    		Calendar lastDate = Calendar.getInstance();
		    		
		    		switch(rateRequest.getRefRate()) {
		    			case FIVE_MINUTES:
		    				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    				setFirstDate(lastDate,firstDate,sdf,rateList);
		    				firstDate.add(Calendar.DAY_OF_MONTH, -1);
		    			break;
		    			case SIXTY_MINUTES:
		    				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    				setFirstDate(lastDate,firstDate,sdf,rateList);
		    				firstDate.add(Calendar.DAY_OF_MONTH, -5);
			    		break;
		    			case DAILY:
		    				sdf = new SimpleDateFormat("yyyy-MM-dd");
		    				setFirstDate(lastDate,firstDate,sdf,rateList);firstDate.getTime();lastDate.getTime();
		    				firstDate.add(Calendar.DAY_OF_MONTH, -30);
			    		break;
		    			case WEEKLY:
		    				sdf = new SimpleDateFormat("yyyy-MM-dd");
		    				setFirstDate(lastDate,firstDate,sdf,rateList);
		    				firstDate.add(Calendar.MONTH, -12);
			    		break;
		    			case MONTHLY:
		    				sdf = new SimpleDateFormat("yyyy-MM-dd");
		    				setFirstDate(lastDate,firstDate,sdf,rateList);
		    				firstDate.add(Calendar.YEAR, -5);
			    		break;
			    		default:
		    				sdf = new SimpleDateFormat("yyyy-MM-dd");
		    				lastDate.setTime(sdf.parse(rateList.get(rateList.size()-1).getDate()));
		    				firstDate.setTime(sdf.parse(rateList.get(0).getDate()));
		    		}
    				firstDate.add(Calendar.DAY_OF_MONTH, -1);
		    		lastDate.add(Calendar.DAY_OF_MONTH, 1);
		    		
		    		rateDtoList = new ArrayList<>();
		    		max = min = Double.valueOf(rateList.get(0).getClose());
		    		for (Rate rate : rateList) {
		    			
		    			if(sdf.parse(rate.getDate()).after(firstDate.getTime()) && 
		    				sdf.parse(rate.getDate()).before(lastDate.getTime())) {
		    				
			    			rateDTO = RateDTO.builder()
			    					.key(rate.getDate())
			    					.value(Double.valueOf(rate.getClose()))
			    					.build();
			    			
			    			min = Double.min(rateDTO.getValue(), min);
			    			max = Double.max(rateDTO.getValue(), max);
			    			
			    			rateDtoList.add(rateDTO);
		    			}
					}
		    				    		
		    		response.setMin(min);
		    		response.setMax(max);
		    		response.setRates(rateDtoList);
	    		}catch(Exception e) {
	    		}
	    	}
    	}
    	
    	
    	return response;
    }

    public void updateRate(Map<String, Rate> map, String key) {
    	rateRepository.updateRate(map, key);
    }

    public void updateCurrency(Map<String, Currency> map, String key) {
    	rateRepository.updateCurrency(map, key);
    }

    public void delete(String id) {
    	rateRepository.delete(id);
    }
    
    public CurrencyExchange getCurrencyExchangeDataApi(CurrencyExchangeRequest request, String key) throws Exception {
    	String returnedData = null;
    	CurrencyExchange currencyExchange = null;
		try {			
			RestTemplate restTemplate = new RestTemplate();
			StringBuilder uri = new StringBuilder("https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE");
			uri.append("&from_currency=");
			uri.append(request.getFrom());
			uri.append("&to_currency=");
			uri.append(request.getTo());
			uri.append("&apikey=");
			uri.append(Useful.API_KEY);
			
			returnedData = restTemplate.getForObject(uri.toString(), String.class);
		
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error to retrieve data!");
			return null;
		}
		
		try {
			if(returnedData != null) {
				JSONObject jsonObject = new JSONObject(returnedData);
				String index = "Realtime Currency Exchange Rate";
		
				if(jsonObject.has(index)) {
					JSONObject serieData = jsonObject.getJSONObject(index);
						
					currencyExchange = new CurrencyExchange();
					currencyExchange.setFromCurrency(serieData.get("1. From_Currency Code").toString());
					currencyExchange.setFromCurrencyName(serieData.get("2. From_Currency Name").toString());
					currencyExchange.setToCurrency(serieData.get("3. To_Currency Code").toString());
					currencyExchange.setToCurrencyName(serieData.get("4. To_Currency Name").toString());
					currencyExchange.setExchangeRate(Double.valueOf(serieData.get("5. Exchange Rate").toString()));
					currencyExchange.setLastRefreshed(serieData.get("6. Last Refreshed").toString());
					currencyExchange.setTimeZone(serieData.get("7. Time Zone").toString());
					currencyExchange.setBidPrice(Double.valueOf(serieData.get("8. Bid Price").toString()));
					currencyExchange.setAskPrice(Double.valueOf(serieData.get("9. Ask Price").toString()));

					if(currencyExchange.getFromCurrency() != null && currencyExchange.getToCurrency() != null && 
						currencyExchange.getExchangeRate() != null ) {
		    			save(currencyExchange,key);
		    			return currencyExchange;
		    		}
				}else {
					System.out.println(returnedData);
					throw new Exception("No records found!");
				}
			}
		}catch(JSONException e) {
			e.printStackTrace();
			log.error("Error to retrieve data!");
			throw new Exception(e.getMessage());
		}
		return null;
    }
    
    public void refreshCurrencyExchangeDataApi(RateRequest request, String key) {
    	String returnedData = null;
    	CurrencyExchange currencyExchange = null;
		try {			
			RestTemplate restTemplate = new RestTemplate();
			StringBuilder uri = new StringBuilder("https://www.alphavantage.co/query?function=CURRENCY_EXCHANGE_RATE");
			uri.append("&from_currency=");
			uri.append(request.getFromSymbol());
			uri.append("&to_currency=");
			uri.append(request.getToSymbol());
			uri.append("&apikey=");
			uri.append(Useful.API_KEY);
			
			returnedData = restTemplate.getForObject(uri.toString(), String.class);
		
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error to retrieve data!");
		}
		
		try {
			if(returnedData != null) {
				JSONObject jsonObject = new JSONObject(returnedData);
				String index = "Realtime Currency Exchange Rate";
		
				if(jsonObject.has(index)) {
					JSONObject serieData = jsonObject.getJSONObject(index);
						
					currencyExchange = new CurrencyExchange();
					currencyExchange.setFromCurrency(serieData.get("1. From_Currency Code").toString());
					currencyExchange.setFromCurrencyName(serieData.get("2. From_Currency Name").toString());
					currencyExchange.setToCurrency(serieData.get("3. To_Currency Code").toString());
					currencyExchange.setToCurrencyName(serieData.get("4. To_Currency Name").toString());
					currencyExchange.setExchangeRate(Double.valueOf(serieData.get("5. Exchange Rate").toString()));
					currencyExchange.setLastRefreshed(serieData.get("6. Last Refreshed").toString());
					currencyExchange.setTimeZone(serieData.get("7. Time Zone").toString());
					currencyExchange.setBidPrice(Double.valueOf(serieData.get("8. Bid Price").toString()));
					currencyExchange.setAskPrice(Double.valueOf(serieData.get("9. Ask Price").toString()));

					if(currencyExchange.getFromCurrency() != null && currencyExchange.getToCurrency() != null && 
						currencyExchange.getExchangeRate() != null ) {
		    			save(currencyExchange,key);
		    		}
				}else {
					System.out.println(returnedData);
					log.error("No records found!");
				}
			}
		}catch(JSONException e) {
			e.printStackTrace();
			log.error("Error to retrieve data!");
		}
    }
    
    public List<Currency> getCurrencyDataApi(String key) throws Exception {
    	String returnedData = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			returnedData = restTemplate.getForObject("https://free.currconv.com/api/v7/currencies?apiKey=746d31a422eaefeac810", String.class);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error to retrieve data!");
			return null;
		}
		
		try {
			if(returnedData != null) {
				JSONObject jsonObject = new JSONObject(returnedData);
				String index = "results";
		
				if(jsonObject.has(index)) {
					JSONObject serieData = jsonObject.getJSONObject(index);
					JSONObject rowData = null;
						
					List<Currency> currencies = new ArrayList<>();
					Map<String, Currency> currencyMap = new HashMap<>();
					Currency currency;
					for(int i = 0; i < serieData.names().length(); i++) {
						index = serieData.names().get(i).toString();
						rowData = serieData.getJSONObject(index);
						currency = new Currency();
						currency.setKey(index);
						currency.setCurrencyName(rowData.get("currencyName").toString());
						
						currencyMap.put(index, currency);
						currencies.add(currency);
					}
					if(!currencies.isEmpty()) {
		    			saveAllCurrency(currencyMap,key);
		    			return currencies;
		    		}
				}else {
					System.out.println(returnedData);
					throw new Exception("No records found!");
				}
			}
		}catch(JSONException e) {
			throw new Exception(e.getMessage());
		}
		return null;
    }
    
    public List<Rate> getHistoricalDataApi(RateRequest rateRequest, String key) throws Exception {
    	String returnedData = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			StringBuilder uri = new StringBuilder("https://www.alphavantage.co/query?function=");
			uri.append(rateRequest.getFunctionRate());
			uri.append("&from_symbol=");
			uri.append(rateRequest.getFromSymbol());
			uri.append("&to_symbol=");
			uri.append(rateRequest.getToSymbol());
			if(rateRequest.getRefRate().equals(RefRateEnum.FIVE_MINUTES)){
				uri.append("&interval=");
				uri.append(RefRateEnum.FIVE_MINUTES.getRef());
			} else if(rateRequest.getRefRate().equals(RefRateEnum.SIXTY_MINUTES)){
				uri.append("&interval=");
				uri.append(RefRateEnum.SIXTY_MINUTES.getRef());
				uri.append("&outputsize=");
				uri.append(rateRequest.getOutputSize().getSize());
			} else if(rateRequest.getRefRate().equals(RefRateEnum.YEARLY)){
				uri.append("&outputsize=");
				uri.append(OutputSizeEnum.FULL.getSize());
			}
			uri.append("&apikey=");
			uri.append(Useful.API_KEY);
			
			returnedData = restTemplate.getForObject(uri.toString(), String.class);
		
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error to retrieve data!");
			return null;
		}
		
		try {
			if(returnedData != null) {
				JSONObject jsonObject = new JSONObject(returnedData);
				String index = "Time Series FX (".concat(rateRequest.getRefRate().getRef()).concat(")");
				if(rateRequest.getRefRate().equals(RefRateEnum.YEARLY)){
					index = "Time Series FX (Monthly)";
				}
		
				if(jsonObject.has("Meta Data") && jsonObject.has(index)) {
//					JSONObject metaData = jsonObject.getJSONObject("Meta Data");
					JSONObject serieData = jsonObject.getJSONObject(index);
					JSONObject rowData = null;
						
					List<Rate> rates = new ArrayList<>();
					Map<String, Rate> rateMap = new HashMap<>();
					Rate rate;
					for(int i = 0; i < serieData.names().length(); i++) {
						index = serieData.names().get(i).toString();
						rowData = serieData.getJSONObject(index);
						rate = new Rate();
						rate.setDate(index);
						for(int j = 0; j < rowData.names().length(); j++) {
							if(rowData.names().get(j).toString().contains("1")) {
								rate.setOpen(rowData.get(rowData.names().get(j).toString()).toString());
							}
							if(rowData.names().get(j).toString().contains("2")) {
								rate.setHigh(rowData.get(rowData.names().get(j).toString()).toString());
							}
							if(rowData.names().get(j).toString().contains("3")) {
								rate.setLow(rowData.get(rowData.names().get(j).toString()).toString());
							}
							if(rowData.names().get(j).toString().contains("4")) {
								rate.setClose(rowData.get(rowData.names().get(j).toString()).toString());
							}
						}
						rateMap.put(index, rate);
						rates.add(rate);
					}
					if(!rateMap.isEmpty()) {
		    			saveAllRate(rateMap,key);
		    			return rates;
		    		}
				}else {
					System.out.println(returnedData);
					if(jsonObject.has("Note")) {
						throw new Exception(jsonObject.getString("Note"));
//						{
//						    "Note": "Thank you for using Alpha Vantage! Our standard API call frequency is 5 calls per 
//									minute and 500 calls per day. Please visit https://www.alphavantage.co/premium/ if 
//									you would like to target a higher API call frequency."
//						}
					}else {
						throw new Exception("No records found!");
					}
				}
			}
		}catch(JSONException e) {
			throw new Exception(e.getMessage());
		}
		return null;
    }
    
    public List<String> findMap(String key) {
    	return rateRepository.findMap(key);
    }
    
    public void addItensMap(Map<String,String> map, String key) {
    	rateRepository.addItensMap(map, key);
    }
    
    public void removeItemMap(String key, String hash) {
    	rateRepository.removeItemMap(key, hash);
    }
}