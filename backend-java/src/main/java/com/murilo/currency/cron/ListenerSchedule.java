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
public class ListenerSchedule {
	
	@Autowired
	private RateService rateService;

	@Scheduled(cron = "0 0/15 * * * *")  //=> each 15 minutes
	public void updateCurrencyExchangePool() {
		try {
			List<String> exchangeList = rateService.findMap(Useful.EXCHANGE_POOL);
			List<String> exchangeJobList = rateService.findMap(Useful.EXCHANGE_POOL_JOB);
			if(exchangeList != null && !exchangeList.isEmpty()) {
				Map<String,String> exchangeToRefresh = new HashMap<>();
				Integer total = 0;
				for (String exchange : exchangeList) {
					if(exchangeJobList != null) {
						if(total == 0) {
							total = exchangeJobList.size();
						}
						if(exchangeJobList.isEmpty() || !exchangeJobList.contains(exchange)) {
							exchangeToRefresh.put(exchange, exchange);
							total++;
						}
					}else {
						exchangeToRefresh.put(exchange, exchange);
						total++;
					}
				}
				if(total > 0 && (exchangeJobList == null || total > exchangeJobList.size())) {
					rateService.addItensMap(exchangeToRefresh, Useful.EXCHANGE_POOL_JOB);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
