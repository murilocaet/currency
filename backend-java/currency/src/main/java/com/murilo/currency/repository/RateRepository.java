package com.murilo.currency.repository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

import com.murilo.currency.entity.Currency;
import com.murilo.currency.entity.CurrencyExchange;
import com.murilo.currency.entity.Rate;

@Repository
public class RateRepository {

	@Autowired
	private RedisTemplate<String, Rate> redisTemplate;

    private HashOperations hashOperations;

    public RateRepository(RedisTemplate<String, Rate> redisTemplate) {
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
    }

    public void save(CurrencyExchange currencyExchange, String key) {
   		hashOperations.put(key, "", currencyExchange);
    }
    
    public void saveAllRate(Map<String, Rate> map, String key) {
   		hashOperations.putAll(key, map);
    }
    
    public void saveAllCurrency(Map<String, Currency> map, String key) {
   		hashOperations.putAll(key, map);
    }

    public Map<String, Rate> findAll() {
        return hashOperations.entries("*");
    }

    public List<String> findMap(String key) {
    	List<String> mapList = (List<String>)hashOperations.values(key);
		return mapList;
    }

    public CurrencyExchange findCurrencyExchangeData(String key) {
    	CurrencyExchange currencyExchange = (CurrencyExchange)hashOperations.get(key, "");
		return currencyExchange;
    }
    
    public List<Currency> findCurrencyData(String key) {
    	List<Currency> currency = (List<Currency>)hashOperations.values(key);
		return currency;
    }
    
    public List<Rate> findHistoricalData(String key) {
    	List<Rate> rates = (List<Rate>)hashOperations.values(key);
		return rates;
    }
    
    public Rate findById(String key, String date) {
        return (Rate)hashOperations.get(key, date);
    }

    public void updateRate(Map<String, Rate> map, String key) {
    	saveAllRate(map, key);
    }

    public void updateCurrency(Map<String, Currency> map, String key) {
    	saveAllCurrency(map, key);
    }

    public void delete(String key) {
        hashOperations.delete(key);
    }
    
    public void delete(String key, String date) {
        hashOperations.delete(key, key);
    }

    public void deleteAll(String key) {
        hashOperations.delete(key, key);
    }
    
    public void addItensMap(Map<String,String> map, String key) {
   		hashOperations.putAll(key, map);
    }

    public void removeItemMap(String key, String hash) {
        hashOperations.delete(key, hash);
    }
}
