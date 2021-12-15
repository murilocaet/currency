package com.murilo.currency.response;

import java.io.Serializable;

import com.murilo.currency.entity.CurrencyExchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchangeResponse implements Serializable {

	private static final long serialVersionUID = 2949404617508707464L;

	private CurrencyExchange currencyExchange;

	@Builder.Default
	private String note = "";

	@Builder.Default
	private String success = "";

	@Builder.Default
	private String error = "";
}
