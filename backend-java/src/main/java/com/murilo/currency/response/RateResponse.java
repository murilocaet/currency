package com.murilo.currency.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.murilo.currency.DTO.RateDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateResponse implements Serializable {

	private static final long serialVersionUID = 4640321273922734786L;

	@Builder.Default
	private Double min = 0d;
	
	@Builder.Default
	private Double max = 0d;
	
	@Builder.Default
	private List<RateDTO> rates = new ArrayList<>();

	@Builder.Default
	private String note = "";
}
