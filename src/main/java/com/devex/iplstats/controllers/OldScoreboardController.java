package com.devex.iplstats.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devex.iplstats.dto.OldScoreboardDTO;
import com.devex.iplstats.model.MatchInfo;
import com.devex.iplstats.model.OldScoreboard;
import com.devex.iplstats.model.repository.MatchInfoRepository;
import com.devex.iplstats.model.repository.OldScoreboardRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/oldscore")
public class OldScoreboardController {
	
	@Resource(name = "matchInfoRepository")
	MatchInfoRepository matchInfoRepository;
	
	@Resource(name = "oldScoreboardRepository")
	OldScoreboardRepository oldScoreboardRepository;
	
	 @Autowired
	 private MongoTemplate mongoTemplate;
	
	@GetMapping("/")
	public String scoreboard()
	{
		return "ipl stats with mongo";
	}
	
	
	@GetMapping("/find")
	public List<OldScoreboardDTO> find()
	{
		LookupOperation lookupOperation = LookupOperation.newLookup()
											.from("MatchInfo")
											.localField("matchInfo")
											.foreignField("_id")
											.as("matchDetails");	
		
		Aggregation scoreboardJoinMatchinfo = Aggregation.newAggregation(lookupOperation);
		
		//System.out.println( mongoTemplate.aggregate(scoreboardJoinMatchinfo, "OldScoreboard",Document.class).getRawResults());
		
		List<OldScoreboardDTO> results = mongoTemplate.aggregate(scoreboardJoinMatchinfo, "OldScoreboard" , OldScoreboardDTO.class).getMappedResults();
		
		return results.stream().limit(5).collect(Collectors.toList());
	}
	
	@GetMapping("/find2")
	public List<OldScoreboardDTO> find2()
	{
		return oldScoreboardRepository.findWithMatchInfo().stream().limit(5).collect(Collectors.toList());
	}
	
	@GetMapping("/find3")
	public List<OldScoreboardDTO> find3()
	{
		return oldScoreboardRepository.findCenturians().stream().limit(5).collect(Collectors.toList());
	}
	
	@PostMapping("/save")
	public ResponseEntity<String> save(@RequestBody MatchInfo matchInfo)
	{
		
		if(!isMatchInfoExists(matchInfo) && !CollectionUtils.isEmpty(matchInfo.getScores()))
		{
			log.info("Saving ==> "+matchInfo.getMatchId()+" records ==> "+matchInfo.getScores().size());
			
			MatchInfo info = matchInfoRepository.save(matchInfo);
			
			matchInfo.getScores().forEach(r->r.setMatchInfo(info.get_id()));
			
			List<OldScoreboard> savedScoreboard = oldScoreboardRepository.saveAll(matchInfo.getScores());
			
			return new ResponseEntity<String>("SAVED DCOUMENTS "+savedScoreboard.size(),HttpStatus.ACCEPTED);
		}
		
		return new ResponseEntity<String>("Match Exists ",HttpStatus.OK);
		
	}
	
	
	public boolean isMatchInfoExists(MatchInfo matchInfo)
	{
		ExampleMatcher modelMatcher = ExampleMatcher.matching()
				  .withIgnorePaths("_id") 
				  .withMatcher("matchId", ExampleMatcher.GenericPropertyMatchers.exact());
		
		Example<MatchInfo> example = Example.of(matchInfo, modelMatcher);
		boolean exists = matchInfoRepository.exists(example);
		
		log.info("isMatchInfoExists ==> "+exists);
		
		return exists;
	}
}
