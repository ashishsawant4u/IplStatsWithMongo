package com.devex.iplstats.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.BucketOperation;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.devex.iplstats.dto.BatsmanCommonStatsDTO;
import com.devex.iplstats.dto.BatsmanRecentFormDTO;
import com.devex.iplstats.dto.BatsmanScoreRangeDTO;
import com.devex.iplstats.dto.OldScoreboardDTO;
import com.devex.iplstats.dto.PlayerSearchNameDTO;
import com.devex.iplstats.dto.RequestDTO;
import com.devex.iplstats.model.repository.OldScoreboardRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/bat")
public class BatsmanStatsController 
{
	@Resource(name = "oldScoreboardRepository")
	OldScoreboardRepository oldScoreboardRepository;
	
	 @Autowired
	 private MongoTemplate mongoTemplate;
	 
	 @GetMapping("/home")
	 public String landingPage()
	 {
		 return "batStatsPage";
	 }
	 

	 
	 
	 @GetMapping("/searchPlayerName")
	 @ResponseBody
	 public Set<String> searchPlayerName(@RequestParam("term") String playerName)
	 {
		 MatchOperation match = new MatchOperation(TextCriteria.forDefaultLanguage().matching(playerName));
		 
		 GroupOperation group = Aggregation.group("batsman")
				 								   .addToSet("batsman").as("batsmanMatchingName")
				 								   .count().as("matchCount");
		 
		 
		 ProjectionOperation projection =  Aggregation.project("_id","batsmanMatchingName","matchCount");
		 
		 SortOperation sort = new SortOperation(Sort.by("matchCount").descending());
		 
		 Aggregation recentFormAggr = Aggregation.newAggregation(match,group,projection,sort);
		 
		 List<PlayerSearchNameDTO> recentForm = mongoTemplate.aggregate(recentFormAggr, "OldScoreboard" , PlayerSearchNameDTO.class).getMappedResults();
		 
		 recentForm.forEach(p -> p.set_id(p.get_id().replace("(c)", "").toLowerCase().trim()));
		 
		 return  recentForm.stream().map(p ->  String.join(" ", p.get_id().split("\\W")) ).collect(Collectors.toSet());
		 
	 }
	 
	 
	 @PostMapping("/recentform")
	 @ResponseBody
	 public List<BatsmanRecentFormDTO> batsmanRecentForm(@RequestBody  RequestDTO request)
	 {
		 
		 int PAGE_SIZE  = request.getPageSize();
		 
		 //MatchOperation match = new MatchOperation(TextCriteria.forDefaultLanguage().matching("\""+request.getPlayerName()+"\""));
		 
		 MatchOperation match = new MatchOperation(Criteria.where("batsman").regex("^"+request.getPlayerName(), "i")
				 									.and("ballsFaced").ne(0));
		 
		 LookupOperation scorecardJoinMatchInfo = LookupOperation.newLookup()
					.from("MatchInfo")
					.localField("matchInfo")
					.foreignField("_id")
					.as("matchDetails");
		 
		 SortOperation sort = new SortOperation(Sort.by("matchDetails.season").descending().and(Sort.by("matchId").descending()));
		 
		 
		 SkipOperation skip  = new SkipOperation((request.getPageNumber()-1) * PAGE_SIZE);
		 
		 LimitOperation limit = new LimitOperation(PAGE_SIZE);
		 
		 ProjectionOperation projection =  Aggregation.project("batsman","batsmanScore","ballsFaced")
		 		.andExpression("(batsmanScore / ballsFaced) * 100 ").as("strikeRate")
		 		.and("matchDetails.matchTitle").arrayElementAt(0).as("match")
		 		.and("matchDetails.matchDate").arrayElementAt(0).as("matchDate")
		 		.andExclude("_id");
		 
		
		 
		 Aggregation recentFormAggr = Aggregation.newAggregation(match,scorecardJoinMatchInfo,sort,skip,limit,projection);
		 
		 List<BatsmanRecentFormDTO> recentForm = mongoTemplate.aggregate(recentFormAggr, "OldScoreboard" , BatsmanRecentFormDTO.class).getMappedResults();
		 
		 return recentForm.stream().limit(PAGE_SIZE).collect(Collectors.toList());
	 }
	 
	 @PostMapping("/scoreRange")
	 @ResponseBody
	 public Map<String, List<BatsmanScoreRangeDTO>> batsmanScoreRange(@RequestBody  RequestDTO request)
	 {
		 
		 int totalInnings = totalInnings(request);
		 
		 MatchOperation match = new MatchOperation(Criteria.where("batsman").regex("^"+request.getPlayerName(), "i")
					.and("ballsFaced").ne(0));
		 

		 LookupOperation scorecardJoinMatchInfo = LookupOperation.newLookup()
														.from("MatchInfo")
														.localField("matchInfo")
														.foreignField("_id")
														.as("matchDetails");
		 
		 
		 SortOperation sort = new SortOperation(Sort.by("matchDetails.season").descending().and(Sort.by("matchId").descending()));
		 
		 
		 SkipOperation skip  = new SkipOperation((request.getPageNumber()-1) * request.getPageSize());
		 
		 LimitOperation limit = new LimitOperation(request.getPageSize());
		 
		 List<Integer> range = new ArrayList<>();
		 range.add(0);
		 for (int i = 6; i <= 51; i++) 
		 {
			 range.add(i);
			 
			 i = i + 4;
		 }
		 
		 BucketOperation bucket =  Aggregation.bucket("batsmanScore")
				 							   .withBoundaries(range.toArray())
				 							   .withDefaultBucket("above50")
				 							   .andOutputCount().as("count");
		 
		 ProjectionOperation projection =  Aggregation.project("_id","count")
				 										.andExpression("(count/"+totalInnings+") * 100").as("weight");
		 
		 Aggregation scoreRangeAggr = Aggregation.newAggregation(match,scorecardJoinMatchInfo,sort,skip,limit,bucket,projection);
		 
		 List<BatsmanScoreRangeDTO> scoreRange = mongoTemplate.aggregate(scoreRangeAggr, "OldScoreboard" , BatsmanScoreRangeDTO.class).getMappedResults();
	
		 for(BatsmanScoreRangeDTO b : scoreRange)
		 {
			 if(b.get_id().equals("0"))
			 {
				 b.set_id("0-5");
			 }
			 else if(b.get_id().equals("above50"))
			 {
				 b.set_id("50+");
			 }
			 else 
			 {
				 b.set_id(b.get_id()+"-"+ (Integer.parseInt(b.get_id())+5));	
			 }
			 
		 }
		 
		 Map<String, List<BatsmanScoreRangeDTO>> data = new HashMap<>();
		 data.put("data", scoreRange);
		 
		 return data;
	 }
	 
	 public int totalInnings(RequestDTO request)
	 {
		 MatchOperation match = new MatchOperation(Criteria.where("batsman").regex("^"+request.getPlayerName(), "i"));
		 
		 SortOperation sort = new SortOperation(Sort.by("matchDetails.season").descending().and(Sort.by("matchId").descending()));
		 
		 
		 SkipOperation skip  = new SkipOperation((request.getPageNumber()-1) * request.getPageSize());
		 
		 LimitOperation limit = new LimitOperation(request.getPageSize());
		 
		 CountOperation count = Aggregation.count().as("totalInnings");
		 
		 Aggregation innAggr = Aggregation.newAggregation(match,sort,skip,limit,count);

		 List<BatsmanCommonStatsDTO> result = mongoTemplate.aggregate(innAggr, "OldScoreboard",BatsmanCommonStatsDTO.class).getMappedResults();
		 
		 return result.stream().findFirst().get().getTotalInnings();
	 }
	 
	 public BatsmanCommonStatsDTO runStats(RequestDTO request)
	 {
		 MatchOperation match = new MatchOperation(Criteria.where("batsman").regex("^"+request.getPlayerName(), "i"));
		 
		 SortOperation sort = new SortOperation(Sort.by("matchDetails.season").descending().and(Sort.by("matchId").descending()));
		 
		 
		 SkipOperation skip  = new SkipOperation((request.getPageNumber()-1) * request.getPageSize());
		 
		 LimitOperation limit = new LimitOperation(request.getPageSize());
		 
		 GroupOperation group = Aggregation.group("b")
				 							.sum("fours").as("totalFours")
				 							.sum("sixes").as("totalSixes")
				 							.sum("batsmanScore").as("totalRuns")
				 							.avg("batsmanScore").as("avgRuns");
		 
		 Aggregation innAggr = Aggregation.newAggregation(match,sort,skip,limit,group);

		 List<BatsmanCommonStatsDTO> result = mongoTemplate.aggregate(innAggr, "OldScoreboard",BatsmanCommonStatsDTO.class).getMappedResults();
		 
		 return result.stream().findFirst().get();
	 }
	 
	 public double avgStrikeRate(RequestDTO request)
	 {
		 MatchOperation match = new MatchOperation(Criteria.where("batsman").regex("^"+request.getPlayerName(), "i"));
		 
		 SortOperation sort = new SortOperation(Sort.by("matchDetails.season").descending().and(Sort.by("matchId").descending()));
		 
		 
		 SkipOperation skip  = new SkipOperation((request.getPageNumber()-1) * request.getPageSize());
		 
		 LimitOperation limit = new LimitOperation(request.getPageSize());
		 
		 ProjectionOperation projection =  Aggregation.project("batsman","batsmanScore","ballsFaced")
			 		.andExpression("(batsmanScore / ballsFaced) * 100 ").as("strikeRate")
			 		.andExclude("_id");
		 
		 GroupOperation group = Aggregation.group("batsman")
					.avg("strikeRate").as("avgStrikeRate");
		 
		 
		 Aggregation srAggr = Aggregation.newAggregation(match,sort,skip,limit,projection,group);

		 List<BatsmanCommonStatsDTO> result = mongoTemplate.aggregate(srAggr, "OldScoreboard",BatsmanCommonStatsDTO.class).getMappedResults();
		 
		 return result.stream().findFirst().get().getAvgStrikeRate();
		 
	 }
	 
	 @PostMapping("/commonstats")
	 @ResponseBody
	 public BatsmanCommonStatsDTO commonStats(@RequestBody  RequestDTO request)
	 {
		 BatsmanCommonStatsDTO data = runStats(request);
		 
		 data.setTotalInnings(totalInnings(request));
		 data.setAvgStrikeRate(avgStrikeRate(request));
		 
		 return data;
	 }
}
