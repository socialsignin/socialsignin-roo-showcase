package org.socialsignin.roo.showcase.controller;
/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import javax.inject.Inject;

import org.socialsignin.provider.twitter.TwitterProviderService;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * Showcase Controller - at this stage in our incremental
 * showcase of SocialSignin, we are only showcasing operations
 * performed against a third party api which do not require
 * specific user-level authentication - here searching the 
 * public twitter timeline.
 * 
 * Any ProviderServices on your classpath, are available for injecting
 * here once provider-specific consumer keys/secrets have been added to your
 * environment properties and a component scan has been performed for
 * "org.socialsignin.provider.<providerid> components.
 * 
 * @author Michael Lavelle
 *
 */
@Controller
public class ShowcaseController {
	
	
	@Inject
	private TwitterProviderService twitterProviderService;

	/**
	 * 
	 * Display tweets from the public timeline matching a search query
	 * 
	 * @param model Holds the tweets retrieved for a given search
	 * @param query The query we will use to search for tweets
	 * @return The main showcase view
	 */
	@RequestMapping(value="/searchTweets", method=RequestMethod.GET)
	public String search(Model model,@RequestParam(value="q",required=false,defaultValue="") String query) {
			
		if (!query.trim().isEmpty())
		{
			/*
			 * Obtain an API to Twitter, not authenticated for a specific user
			 */
			Twitter twitter = twitterProviderService.getUnauthenticatedApi();
			SearchResults searchResults = twitter.searchOperations().search(query);
			model.addAttribute("tweets", searchResults.getTweets());
		}
		return "showcase";
	}
}
