package com.murilo.currency.enumeration;

public enum OutputSizeEnum {
	COMPACT("compact"),
	FULL("full");

	private final String size;

    private OutputSizeEnum(String size) {
        this.size = size;
    }

	public String getSize() {
		return size;
	}
}
