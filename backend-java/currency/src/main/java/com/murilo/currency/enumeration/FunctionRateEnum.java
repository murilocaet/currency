package com.murilo.currency.enumeration;

public enum FunctionRateEnum {
	FX_INTRADAY("FX_INTRADAY"),
	FX_DAILY("FX_DAILY"),
	FX_WEEKLY("FX_WEEKLY"),
	FX_MONTHLY("FX_MONTHLY");

	private final String function;

    private FunctionRateEnum(String function) {
        this.function = function;
    }

	public String getFunction() {
		return function;
	}
}
