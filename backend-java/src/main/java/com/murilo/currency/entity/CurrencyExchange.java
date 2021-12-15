package com.murilo.currency.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchange implements Serializable {

	private static final long serialVersionUID = 8211096348535024115L;
	
	private String fromCurrency;
	private String fromCurrencyName;
	private String toCurrency;
	private String toCurrencyName;
	private Double exchangeRate;
	private String lastRefreshed;
	private String timeZone;
	private Double bidPrice;
	private Double askPrice;
}