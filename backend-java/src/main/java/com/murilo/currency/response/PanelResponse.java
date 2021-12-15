package com.murilo.currency.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PanelResponse implements Serializable {

	private static final long serialVersionUID = 2498538446681804232L;
	
	@Builder.Default
	private List<String> exchangePoolList = new ArrayList<>();
	
	@Builder.Default
	private List<String> exchangePoolJobList = new ArrayList<>();
	
	@Builder.Default
	private List<String> exchangePoolJobForcedList = new ArrayList<>();
	
	@Builder.Default
	private List<String> historicalPoolList = new ArrayList<>();
	
	@Builder.Default
	private List<String> historicalPoolJobList = new ArrayList<>();

	@Builder.Default
	private String note = "";
}