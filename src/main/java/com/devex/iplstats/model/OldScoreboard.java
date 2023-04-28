package com.devex.iplstats.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Document(collection = "OldScoreboard")
public class OldScoreboard 
{
	@MongoId
	private ObjectId _id;

	private String matchId;
	
	private Integer inning;
	
	@TextIndexed
	private String batsman;

	private Integer batsmanScore;

	private Integer ballsFaced;
	
	private Integer fours;
	
	private Integer sixes;
	
	private String batStatus;
	
	private ObjectId matchInfo; 
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Kolkata")
	@CreatedDate
	private Date createDateTime;
}
