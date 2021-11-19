package com.murilo.currency.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchangeRequest implements Serializable {

	private static final long serialVersionUID = -7166088962475728287L;
	
	@Builder.Default
	private String from = "USD";
	
	@Builder.Default
	private String to = "EUR";
	
	@Builder.Default
	private String apikey = "BTJBTZU72FSKYQNY";
}