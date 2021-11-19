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
public class Currency implements Serializable {

	private static final long serialVersionUID = -2039045199602036409L;
	
	private String key;
	private String currencyName;
}