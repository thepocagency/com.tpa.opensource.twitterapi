/*
 * Copyright 2017 the original author or authors.
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
package com.tpa.twitterapi.api.call;

import com.tpa.twitterapi.api.authentificator.TwitterAuthenticator;
import com.tpa.twitterapi.api.generic.bean.TwitterStatuses;
import com.tpa.twitterapi.api.generic.call.staticcall.AbstractStaticCall;
import com.tpa.twitterapi.api.generic.call.staticcall.InterfaceStaticCall;

/**
 * This is an example to call the https://api.twitter.com/1.1/search/tweets.json
 * 
 * Default parameters are:
 * 
 * q = default text search, value defined in the config.properties
 * count = the max number of retrieved statuses, value defined in the config.properties
 * 
 * You can more HTTP parameters by using the generic addParameter() method
 * 
 * Cf. Twitter developer documentation: https://dev.twitter.com/rest/reference/get/search/tweets for more information
 * 
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 */
public class Search extends AbstractStaticCall<Search, TwitterStatuses> 
        implements InterfaceStaticCall<Search, TwitterStatuses> {

    private static final String SEARCH_URL = "https://api.twitter.com/1.1/search/tweets.json";
    
    /**
     * 
     * @param twitterAuthenticator : containing the authenticated HttpRequestFactory
     */
    public Search(TwitterAuthenticator twitterAuthenticator) {
        super(TwitterStatuses.class, twitterAuthenticator, SEARCH_URL);
        
        this.addParameter("q", PROPERTY_SINGLETON.getProperty("twitter.searchText"));
        this.addParameter("count", PROPERTY_SINGLETON.getProperty("twitter.maxMessage"));
    }
}
