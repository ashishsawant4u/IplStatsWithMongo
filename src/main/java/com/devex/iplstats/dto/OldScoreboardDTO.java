package com.devex.iplstats.dto;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.devex.iplstats.model.MatchInfo;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class OldScoreboardDTO 
{
	private ObjectId _id;

	private String matchId;

	private Integer inning;
	
	private String batsman;

	private Integer batsmanScore;

	private Integer ballsFaced;
	
	private Integer fours;
	
	private Integer sixes;
	
	private String batStatus;
	
	private List<MatchInfo> matchDetails; 
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Kolkata")
	private Date createDateTime;
}
