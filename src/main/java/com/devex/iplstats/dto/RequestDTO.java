package com.devex.iplstats.dto;

import lombok.Data;

@Data
public class RequestDTO 
{
	private String playerName;
	
	private int pageNumber;
	
	private int pageSize = 5;
	
	private int inning;
}
