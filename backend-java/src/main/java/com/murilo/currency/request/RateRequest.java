package com.murilo.currency.request;

import java.io.Serializable;

import com.murilo.currency.enumeration.FunctionRateEnum;
import com.murilo.currency.enumeration.OutputSizeEnum;
import com.murilo.currency.enumeration.RefRateEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateRequest implements Serializable {

	private static final long serialVersionUID = 2678388741867226259L;

	@Builder.Default
	private FunctionRateEnum functionRate = FunctionRateEnum.FX_INTRADAY;
	
	@Builder.Default
	private RefRateEnum refRate = RefRateEnum.FIVE_MINUTES;
	
	@Builder.Default
	private OutputSizeEnum outputSize = OutputSizeEnum.COMPACT;
	
	@Builder.Default
	private String fromSymbol = "USD";
	
	@Builder.Default
	private String toSymbol = "EUR";
}