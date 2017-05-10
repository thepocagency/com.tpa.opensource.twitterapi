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
import com.tpa.twitterapi.api.generic.bean.TwitterStatus;
import com.tpa.twitterapi.api.generic.call.streamingcall.AbstractStreamingCall;
import com.tpa.twitterapi.api.generic.call.streamingcall.InterfaceStreamingCall;

/**
 * This is an example to call the https://stream.twitter.com/1.1/statuses/filter.json
 * 
 * There is one default parameter:
 * 
 * track = default text search, value defined in the config.properties
 * 
 * You can more HTTP parameters by using the generic addParameter() method
 * 
 * Cf. Twitter developer documentation: https://dev.twitter.com/streaming/reference/post/statuses/filter for more information
 * 
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 */
public class StreamingSearch extends AbstractStreamingCall<StreamingSearch, TwitterStatus> 
        implements InterfaceStreamingCall<StreamingSearch, TwitterStatus> {

    private static final String STREAMING_SEARCH_URL = "https://stream.twitter.com/1.1/statuses/filter.json";
    
    /**
     * 
     * @param twitterAuthenticator : containing the authenticated HttpRequestFactory
     * @param delayInSeconds : the number of second you want to wait for the twitter call 
     * @param sizeLimit : or the max number of statuses you want to retrieve
     */
    public StreamingSearch(TwitterAuthenticator twitterAuthenticator, Integer delayInSeconds, Integer sizeLimit) {
        super(TwitterStatus.class, twitterAuthenticator, STREAMING_SEARCH_URL, delayInSeconds, sizeLimit);
        
        this.addParameter("track", PROPERTY_SINGLETON.getProperty("twitter.searchText"));
    }
}
