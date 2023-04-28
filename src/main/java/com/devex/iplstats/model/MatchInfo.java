package com.devex.iplstats.model;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Document(collection = "MatchInfo")
public class MatchInfo 
{
	@MongoId
	private ObjectId _id;
	
	@Indexed(unique = true)
	private String matchId;
	
	private String matchTitle;
	
	private String matchNumber;
	
	private String tournamentId;
	
	private String tournamentTitle;
	
	private String venue;
	
	@JsonFormat(pattern = "MMM dd yyyy")
	private Date matchDate;
	
	private String league;
	
	private int season;
	
	@Transient
	List<OldScoreboard> scores;
	
	@CreatedDate
	private Date createDateTime;
}
