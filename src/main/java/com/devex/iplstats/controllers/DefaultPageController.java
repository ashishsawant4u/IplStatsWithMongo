package com.devex.iplstats.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/")
public class DefaultPageController 
{
	@RequestMapping("/")
	public String defaultPage()
	{
		//return "batStatsPage";
		return "redirect:/bat/home";
	}
}
