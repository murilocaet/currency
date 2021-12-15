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
public class Rate implements Serializable {

	private static final long serialVersionUID = -5282549479767080619L;

	private String date;
	private String open;
    private String high;
    private String low;
    private String close;
}