package com.murilo.currency.cron;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import com.murilo.currency.service.RateService;
import com.murilo.currency.useful.Useful;


@RestController
public class JobHistoricalSchedule {
	
	@Autowired
	private RateService rateService;

	@Scheduled(cron = "0 0/20 * * * *")  //=> each 20 minute
	public void updateHistoricalData() {
		try {
			List<String> historicalJobList = rateService.findMap(Useful.HISTORICAL_DATA_POOL_JOB);
			if(historicalJobList != null && !historicalJobList.isEmpty()) {
				String key = historicalJobList.get(0);
				rateService.refreshHistoricalData(key);
				rateService.removeItemMap(Useful.HISTORICAL_DATA_POOL_JOB, key);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
