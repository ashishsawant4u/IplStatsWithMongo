package com.devex.iplstats.configurations;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@EnableMongoAuditing()
@Configuration
public class MongoDbConfigurations 
{
	@Value("${spring.data.mongodb.uri}")
	public String mongodbConnectionUrl;
	
	@Value("${spring.data.mongodb.database}")
	public String database;
	
	@Bean
	public MongoClient mongoClient()
	{	
		ConnectionString connString = new ConnectionString(mongodbConnectionUrl);
		
		ServerApi serverApi = ServerApi.builder().version(ServerApiVersion.V1).build();
		
		MongoClientSettings  settings = MongoClientSettings.builder()
												.applyConnectionString(connString)
												.serverApi(serverApi)
												.build();
		
		MongoClient mongoClient  = MongoClients.create(settings);
		
		return mongoClient;
	}
	
	@Bean
	public MongoDatabaseFactory mongoDatabaseFactory() 
	{
	    return new SimpleMongoClientDatabaseFactory(mongoClient(), database);
	}
	
	
	@Bean
	public MongoTemplate mongoTemplate() throws Exception 
	{
		
		//remove _class
		MappingMongoConverter converter = new MappingMongoConverter(mongoDatabaseFactory(), new MongoMappingContext());
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
			
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDatabaseFactory(), converter);
					
		return mongoTemplate;
			
	}
	
}
