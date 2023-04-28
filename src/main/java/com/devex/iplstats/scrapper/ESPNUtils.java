package com.devex.iplstats.scrapper;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import com.devex.iplstats.model.MatchInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ESPNUtils 
{
	public static String getTournamentId(String TOURNAMENT_URL)
	{
		String tournamenturl[] = Arrays.asList(TOURNAMENT_URL.split("/")).get(4).split("-");
		String tournamentId = tournamenturl[tournamenturl.length-1];
		
		return tournamentId.trim();
	}
	
	public static String getTournamentTitle(String TOURNAMENT_URL,String matchUrl)
	{
		String tempArr[] =  matchUrl.split("/");
		String title =  tempArr[4];
		String tournamentTitle = title.replace(getTournamentId(TOURNAMENT_URL),"").replaceAll("-", " ");
		return tournamentTitle.trim();
	}
	
	public static String getMatchId(String matchUrl)
	{	
		String temp = matchUrl.split("/")[5];
		String matchArr[] = temp.split("-");
		String matchId = matchArr[matchArr.length-1];
		return matchId.trim();
	}
	
	public static String getMatchTitle(String matchUrl)
	{	
		String tempArr[] =  matchUrl.split("/");
		String matchTitle = tempArr[5];
		return matchTitle.trim();
	}
	
	public static MatchInfo getMatchInfo(String TOURNAMENT_URL,String matchUrl,String matchInfo)
	{	
		try 
		{
			String tournamentId = ESPNUtils.getTournamentId(TOURNAMENT_URL);
			String tournamenttitle = ESPNUtils.getTournamentTitle(TOURNAMENT_URL, matchUrl);
			String matchId = ESPNUtils.getMatchId(matchUrl);
			String matchTitle = ESPNUtils.getMatchTitle(matchUrl);
			
			
			String tempArr[] =  matchInfo.split(",");
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
			String matchDate = tempArr[2].trim()+tempArr[3];
			
			MatchInfo dto = MatchInfo.builder()
								.matchId(matchId)
								.matchNumber(tempArr[0].trim())
								.matchTitle(matchTitle)
								.tournamentId(tournamentId)
								.tournamentTitle(tournamenttitle)
								.venue(tempArr[1].trim())
								.matchDate(dateFormat.parse(matchDate))
								.league(tempArr[4].trim())
								.season(Integer.parseInt(tempArr[3].trim()))
								.build();
			
			System.out.println(dto);
			
			return dto;
		} 
		catch (Exception e) 
		{
			log.error(e.getMessage());
		}

		return null;
		
	}
}
