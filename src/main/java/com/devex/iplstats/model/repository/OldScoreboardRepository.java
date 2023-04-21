package com.devex.iplstats.model.repository;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.devex.iplstats.dto.OldScoreboardDTO;
import com.devex.iplstats.model.OldScoreboard;

@Repository("oldScoreboardRepository")
public interface OldScoreboardRepository extends MongoRepository<OldScoreboard, ObjectId>
{	
	@Aggregation("{ $lookup: { from: \"MatchInfo\", localField: \"matchInfo\", foreignField: \"_id\", as: \"matchDetails\" } } ")
	public List<OldScoreboardDTO> findWithMatchInfo();
	
	@Query("{batsmanScore : {$gte:  100 }}")
	public List<OldScoreboardDTO> findCenturians();
	
	public int countByBatsmanIgnoreCase(String batsman);
	
	public List<OldScoreboardDTO> findByBatsmanContainsIgnoreCase(String batsman);
}
