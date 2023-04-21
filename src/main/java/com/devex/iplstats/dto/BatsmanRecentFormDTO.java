package com.devex.iplstats.dto;

import lombok.Data;

@Data
public class BatsmanRecentFormDTO 
{
	private String batsman;

	private Integer batsmanScore;

	private Integer ballsFaced;
	
	private double strikeRate;
	
	private String match;
	
	private String matchDate;
}
