package com.murilo.currency.cron;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import com.murilo.currency.service.RateService;
import com.murilo.currency.useful.Useful;

@RestController
public class ListenerHistoricalSchedule {
	
	@Autowired
	private RateService rateService;

	@Scheduled(cron = "0 0 0/1 * * *")  //=> each 1 hour
	public void updateHistoricalPool() {
		try {
			List<String> historicalList = rateService.findMap(Useful.HISTORICAL_DATA_POOL);
			List<String> historicalJobList = rateService.findMap(Useful.HISTORICAL_DATA_POOL_JOB);
			if(historicalList != null && !historicalList.isEmpty()) {
				Map<String,String> historicalToRefresh = new HashMap<>();
				Integer total = 0;
				for (String historical : historicalList) {
					if(historicalJobList != null) {
						if(total == 0) {
							total = historicalJobList.size();
						}
						if(historicalJobList.isEmpty() || !historicalJobList.contains(historical)) {
							historicalToRefresh.put(historical, historical);
							total++;
						}
					}else {
						historicalToRefresh.put(historical, historical);
						total++;
					}
				}
				if(total > 0 && (historicalJobList == null || total > historicalJobList.size())) {
					rateService.addItensMap(historicalToRefresh, Useful.HISTORICAL_DATA_POOL_JOB);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
