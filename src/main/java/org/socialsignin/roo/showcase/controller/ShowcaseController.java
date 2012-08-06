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
import java.util.List;

import javax.inject.Inject;

import org.socialsignin.provider.twitter.TwitterProviderService;
import org.springframework.core.env.Environment;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * 
 * Any ProviderServices on your classpath are available for injecting
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
	private Environment environment;
	
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
	
	/**
	 * Display timeline tweets from the locally-authenticated user's Twitter account
	 * 
	 * @param model Holds the tweets retrieved from the current locally authenticated users' timeline
	 * @return The main showcase view if users is authenticated, the twitter connection page otherwise
	 */
	@RequestMapping(value="/myTweets", method=RequestMethod.GET)
	public String myTimeline(Model model) {
		
		/*
		 * Obtain an API to Twitter, authenticated for the current local user
		 */
		Twitter authenticatedUserTwitterAccount = twitterProviderService.getAuthenticatedApi();
		if (authenticatedUserTwitterAccount == null)
		{
			return "connect/twitter";
		}
		List<Tweet> tweets = authenticatedUserTwitterAccount.timelineOperations().getUserTimeline();
		model.addAttribute("tweets", tweets);
		return "showcase";
	}
	
	
	/**
	 * Sends a tweet from this application's Twitter account, announcing the currently authenticated user's profile
	 * to the this application's followers
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/announce", method=RequestMethod.POST)
	public String announce(Model model) {
		Twitter adminUserTwitterAccount = twitterProviderService.getAuthenticatedApi(environment.getProperty("socialsignin.roo.showcase.adminLocalUserId"));
		if (adminUserTwitterAccount != null)
		{
			Twitter localUserTwitterAccount = twitterProviderService.getAuthenticatedApi();
			if (localUserTwitterAccount == null)
			{
				return "connect/twitter";
			}
			else
			{
				String localUserTwitterAccountName = localUserTwitterAccount.userOperations().getScreenName();
				if (!hasMadePreviousRecentAnnouncement(adminUserTwitterAccount,localUserTwitterAccountName))
				{
					adminUserTwitterAccount.timelineOperations().updateStatus(getAnnouncementMessage(localUserTwitterAccountName));
				}
				return "showcase";
			}
		}
		else
		{
			throw new IllegalStateException("No Twitter connection details yet available for local admin user:" + environment.getProperty("socialsignin.roo.showcase.adminLocalUserId"));
		}
	}
	
	/**
	 * Sends a tweet from the authenticated user's twitter account,  promoting SocialSignin Roo Showcase
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/promote", method=RequestMethod.POST)
	public String promote(Model model) {
			Twitter localUserTwitterAccount = twitterProviderService.getAuthenticatedApi();
			if (localUserTwitterAccount == null)
			{
				return "connect/twitter";
			}
			else
			{
				localUserTwitterAccount.timelineOperations().updateStatus(getSocialSigninPromotionMessage());
				return "showcase";
			}
	}
	
	private boolean hasMadePreviousRecentAnnouncement(Twitter adminUserTwitterAccount,String twitterUserName)
	{
		List<Tweet> previousRecentAnnouncements = adminUserTwitterAccount.timelineOperations().getUserTimeline(1, 200);
		for (Tweet previousRecentAnnouncement : previousRecentAnnouncements)
		{
			if (previousRecentAnnouncement.getText().equals(getAnnouncementMessage(twitterUserName)))
			{
				return true;
			}
		}
		return false;
	}
	
	private String getAnnouncementMessage(String twitterUserName)
	{
		return "@" +  twitterUserName + " is trying out SocialSignin Roo Showcase https://github.com/socialsignin/socialsignin-roo-showcase";
	}
	
	private String getSocialSigninPromotionMessage()
	{
		return "Sending a Tweet from SocialSignin Roo Showcase https://github.com/socialsignin/socialsignin-roo-showcase";
	}
	
	
}
