package com.murilo.currency.enumeration;

public enum RefRateEnum {
	FIVE_MINUTES("5min"),
	SIXTY_MINUTES("60min"),
	DAILY("Daily"),
	WEEKLY("Weekly"),
	MONTHLY("Monthly"),
	YEARLY("Yearly");

	private final String ref;

    private RefRateEnum(String ref) {
        this.ref = ref;
    }

	public String getRef() {
		return ref;
	}
}
