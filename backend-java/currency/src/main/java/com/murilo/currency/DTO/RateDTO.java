package com.murilo.currency.DTO;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateDTO implements Serializable {

	private static final long serialVersionUID = -5434302896986659066L;
	
	private String key;
    private Double value;
}