package com.devex.iplstats.model.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.devex.iplstats.model.MatchInfo;


@Repository("matchInfoRepository")
public interface MatchInfoRepository extends MongoRepository<MatchInfo, ObjectId> 
{

}