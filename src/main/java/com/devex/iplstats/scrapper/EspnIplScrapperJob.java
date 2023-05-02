package com.devex.iplstats.scrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
public class EspnIplScrapperJob 
{
	
	@Autowired
	private EspnScrapperController	 espnScrapperController;
	
	//@Scheduled(fixedRate = 1000)
	@Scheduled(cron = "0 0/30 2 * * ?")
	public void updateIplStatsDb()
	{
		log.info("EspnIplScrapperJob started...");
		espnScrapperController.scrapIPL();
	}
}
