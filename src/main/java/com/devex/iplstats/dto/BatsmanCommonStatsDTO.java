package com.devex.iplstats.dto;

import lombok.Data;

@Data
public class BatsmanCommonStatsDTO 
{
	private int totalInnings;
	
	private int totalRuns;

	private int totalFours;
	
	private int totalSixes;
	
	private double avgRuns;
	
	private double avgStrikeRate;
}
