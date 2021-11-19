package com.murilo.currency.cron;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import com.murilo.currency.service.RateService;
import com.murilo.currency.useful.Useful;

@RestController
public class JobSchedule {
	
	@Autowired
	private RateService rateService;

	@Scheduled(cron = "0 0/5 * * * *")  //=> each 5 minute
	public void updateCurrencyExchangeData() {
		try {
			List<String> exchangeJobList = rateService.findMap(Useful.EXCHANGE_POOL_JOB);
			if(exchangeJobList != null && !exchangeJobList.isEmpty()) {
				String key = exchangeJobList.get(0);
				rateService.refreshCurrencyExchangeData(key);
				rateService.removeItemMap(Useful.EXCHANGE_POOL_JOB, key);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
