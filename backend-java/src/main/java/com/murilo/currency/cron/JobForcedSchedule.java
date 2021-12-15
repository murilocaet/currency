package com.murilo.currency.cron;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import com.murilo.currency.service.RateService;
import com.murilo.currency.useful.Useful;


@RestController
public class JobForcedSchedule {
	
	@Autowired
	private RateService rateService;

	@Scheduled(cron = "0 0/1 * * * *")  //=> each 1 minute
	public void updateCurrencyExchangeDataForced() {
		try {
			List<String> exchangeJobForcedList = rateService.findMap(Useful.EXCHANGE_POOL_JOB_FORCED);
			List<String> exchangeJobList = rateService.findMap(Useful.EXCHANGE_POOL_JOB);
			if(exchangeJobForcedList != null && !exchangeJobForcedList.isEmpty()) {
				String key = exchangeJobForcedList.get(0);
				rateService.refreshCurrencyExchangeData(key);
				rateService.removeItemMap(Useful.EXCHANGE_POOL_JOB_FORCED, key);
				
				if(exchangeJobList != null && !exchangeJobList.isEmpty() && exchangeJobList.contains(key)) {
					rateService.removeItemMap(Useful.EXCHANGE_POOL_JOB, key);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
