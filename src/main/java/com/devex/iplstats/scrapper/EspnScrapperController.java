package com.devex.iplstats.scrapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.devex.iplstats.controllers.OldScoreboardController;
import com.devex.iplstats.model.MatchInfo;
import com.devex.iplstats.model.OldScoreboard;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/espn")
public class EspnScrapperController 
{
	@Autowired
	private OldScoreboardController oldScoreboardController;
	
	public String TOURNAMENT_URL = "https://www.espncricinfo.com/series/indian-premier-league-2023-1345038/match-schedule-fixtures-and-results";
	
	public String TOURNAMENT_SEASON_URL_PATTERN = "indian-premier-league-2023-1345038";
	
	@RequestMapping("/scrap")
	@ResponseBody
	public List<String> scrapIPL()
	{
		String iplSeasonPage = getPageContent(TOURNAMENT_URL);
		
		List<String> allMatchUrls = getAllMatchUrls(iplSeasonPage);
		
		getMatchDetails(allMatchUrls);
		
		return allMatchUrls;
	}
	
	private void getMatchDetails(List<String> allMatchUrls)
	{
		for (String link : allMatchUrls) 
		{
			MatchInfo matchInfo = saveMatch(link);
			
			oldScoreboardController.save(matchInfo);
		}
		
	}

	private MatchInfo saveMatch(String matchUrl) 
	{
		String iplMatchPage = getPageContent(matchUrl);
		
		
		Document doc = Jsoup.parse(iplMatchPage);
		Elements matchInfoElement = doc.select("div.ds-text-tight-m.ds-font-regular.ds-text-typo-mid3");
		String matchInfoContent = matchInfoElement.text();
		MatchInfo matchInfo = ESPNUtils.getMatchInfo(TOURNAMENT_URL,matchUrl,matchInfoContent);
		
		
		List<OldScoreboard> scores = new ArrayList<>();
		Elements tablesElement = doc.select("table.ci-scorecard-table");
		
		for (int tableNumber = 0; tableNumber < 2; tableNumber++) 
		{
			Element table1stInn =tablesElement.get(tableNumber);
			Elements rows1stInn = table1stInn.select("tr");
			
			for (int i = 1; i < rows1stInn.size(); i++) 
			{ 
			    Element row = rows1stInn.get(i);
			    Elements cols = row.select("td");
			    
			    if(cols.size()==8)
			    {
			    	String batsman = cols.get(0).text();
				    String batsmanScore =  cols.get(2).text().replace("*","").trim();
				    String ballsFaced =  cols.get(3).text();
				    String batStatus = cols.get(1).text().trim();
				    String fours = cols.get(5).text().trim();
				    String sixes = cols.get(6).text().trim();
				    
				    if(null!= batsman && !batsman.equals("Extras") && !batsman.equals("TOTAL")  && null!=batsmanScore)
					{
						OldScoreboard scoreBoard = OldScoreboard.builder()
														.matchId(matchInfo.getMatchId())
														.inning(tableNumber+1)
														.batsman(batsman)
														.batsmanScore(Integer.parseInt(batsmanScore))
														.ballsFaced(Integer.parseInt(ballsFaced))
														.batStatus(batStatus)
														.fours(Integer.parseInt(fours))
														.sixes(Integer.parseInt(sixes))
														.build();
						
						scores.add(scoreBoard);
					}
			    }
			    
			}
		}
		
		
		
		System.out.println("============================================================");
		System.out.println(matchInfo.getMatchTitle());
		
		matchInfo.setScores(scores);
		return matchInfo;
	}
	

	private List<String> getAllMatchUrls(String iplSeasonPage) 
	{
		Document doc = Jsoup.parse(iplSeasonPage);
		Elements links = doc.select("a[href$=\"full-scorecard\"]");
		
		List<String> allMatchUrls = new ArrayList<>();
		
		for (Element link : links) 
		{
			String href = link.attr("href");
			if(href.contains(TOURNAMENT_SEASON_URL_PATTERN) && !link.parentNode().toString().contains("Match yet to begin") && link.parentNode().toString().contains("RESULT"))
			{
				allMatchUrls.add("https://www.espncricinfo.com"+href);
			}
		}
		
		return allMatchUrls;
	}
	
	public String getPageContent(String Url)
	{
		try 
		{
			URL obj = new URL(Url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			// optional request header
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			int responseCode = con.getResponseCode();
			System.out.println("Response code: " + responseCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
			}
			in.close();
			String html = response.toString();
			return html;
		} 
		catch (Exception e) 
		{
			log.error(e.getMessage());
		}
		
		return "Nothing Found";
	}
}
