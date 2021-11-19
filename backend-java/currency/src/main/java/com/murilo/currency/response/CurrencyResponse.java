package com.murilo.currency.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.murilo.currency.entity.Currency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyResponse implements Serializable {

	private static final long serialVersionUID = 2498538446681804232L;
	
	@Builder.Default
	private List<Currency> currencies = new ArrayList<>();

	@Builder.Default
	private String note = "";
}
