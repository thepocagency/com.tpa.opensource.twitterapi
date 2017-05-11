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
package com.tpa.twitterapi.api.generic.call.streamingcall;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.tpa.twitterapi.api.authentificator.TwitterAuthenticator;
import com.tpa.twitterapi.api.generic.call.AbstractCall;
import com.tpa.twitterapi.api.generic.bean.GenericTwitterBean;
import com.tpa.twitterapi.exception.TwitterAuthenticationException;
import com.tpa.twitterapi.exception.TwitterRequestException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * This is a first version a generic Streaming-calls class
 * 
 * You have to implement the InterfaceStreamingCall to implement the executeListRequest() method
 * 
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 * @param <CLASS_TYPE>
 * @param <BEAN_TYPE>
 */
public abstract class AbstractStreamingCall<CLASS_TYPE extends AbstractCall, BEAN_TYPE extends GenericTwitterBean> extends AbstractCall<CLASS_TYPE, BEAN_TYPE> implements InterfaceStreamingCall<CLASS_TYPE, BEAN_TYPE> {
    
    private Long endTime;
    private Integer delayInSeconds;
    private Integer sizeLimit;

    /**
     * 
     * @param genericTwitterBeanType : the TwitterBean you want to use to cast result of the Twitter API
     * @param twitterAuthenticator : containing the authenticated HttpRequestFactory
     * @param url : the default URL
     * @param delayInSeconds : delay of waiting, if null will set to the default value (cf. config.properties)
     * @param sizeLimit : max number of elements, if null will set to the default value (cf. config.properties)
     */
    public AbstractStreamingCall(Class<BEAN_TYPE> genericTwitterBeanType, TwitterAuthenticator twitterAuthenticator, String url, Integer delayInSeconds, Integer sizeLimit) {
        super(genericTwitterBeanType, twitterAuthenticator, url);
        
        if (delayInSeconds == null) {
            delayInSeconds = PROPERTY_SINGLETON.getInteger("twitter.default.delayInSeconds");
            LOGGER.log(Level.INFO, "Call will be executed during the default duration: {0} seconds", delayInSeconds);
        }
        else {
            this.delayInSeconds = delayInSeconds;
            LOGGER.log(Level.INFO, "Call will be executed during {0} seconds", delayInSeconds);
        }
        
        if (sizeLimit == null) {
            sizeLimit = PROPERTY_SINGLETON.getInteger("twitter.default.sizeLimit");
            LOGGER.log(Level.INFO, "Or call will be executed until the default number of {0} elements are reached", sizeLimit);
        }
        else {
            this.sizeLimit = sizeLimit;
            LOGGER.log(Level.INFO, "Or call will be executed until {0} elements are reached", sizeLimit);
        }
        
        // Will be defined just before the request call
        this.endTime = null;
    }
    
    /**
     * Checks if the delay is reached or not
     * 
     * At the first call, this method will set the endTime
     * 
     * During the other calls, this method will check the current time and check if it's still smaller than the endTime
     * 
     * @return true if delay is reached
     */
    private Boolean isDelayReached() {
        if (endTime == null) {
            endTime = System.currentTimeMillis() + delayInSeconds * 1000;
            LOGGER.log(Level.INFO, "End time will be:{0}", endTime);
        }
        
        long currentTime = System.currentTimeMillis();

        if (currentTime > endTime) {
            LOGGER.info("Delay was reached");
            return Boolean.TRUE;
        }
        
        return Boolean.FALSE;
    }
    
    /**
     * Defined in the InterfaceStreamingCall
     * 
     * Call the default URL until the delay is reached or until the max number of elements is obtained
     * 
     * @return List<BEAN_TYPE> : the list of elements converted from the generic BEAN_TYPE
     * @throws TwitterAuthenticationException
     * @throws TwitterRequestException 
     */
    @Override
    public List<BEAN_TYPE> executeListRequest() throws TwitterAuthenticationException, TwitterRequestException {
        
        HttpResponse httpResponse = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        
        ObjectMapper mapper = new ObjectMapper();
        List<BEAN_TYPE> genericApiResults = new ArrayList<>();
        
        try {
            
            GenericUrl genericUrl = new GenericUrl(url);
            genericUrl.setUnknownKeys(parameters);
            
            HttpRequest request = twitterAuthenticator.getHttpRequestFactory().buildGetRequest(genericUrl);
            httpResponse = request.execute();
            
            LOGGER.log(Level.INFO, "Request: {0} is streaming with status code: {1}", new Object[]{request.getUrl().toString(), httpResponse.getStatusCode()});
            inputStream = httpResponse.getContent();
            
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            
            String line;
            
            // We iterate on the stream lines until the time is reached or until we get the max number of elements
            while ((line = bufferedReader.readLine()) != null
                    && !isDelayReached()
                    && genericApiResults.size() < sizeLimit) {
                
                try {
                    BEAN_TYPE currentApiResult = mapper.readValue(line, GENERIC_BEAN_TYPE);
                    
                    if (currentApiResult.isValid()) {
                        genericApiResults.add(currentApiResult);
                        LOGGER.log(Level.INFO, "A line was parsed: {0}", line);
                    }
                    else {
                        LOGGER.log(Level.CONFIG, "A line was parsed but did not contain all the required properties: {0}", line);
                    }
                }
                catch(Exception e) {
                    LOGGER.log(Level.INFO, "A line appeared but could not be parsed:" + line, e);
                }
            }

            LOGGER.log(Level.INFO, "Result retrieved and list created with {0} elements", genericApiResults.size());
        } catch (IOException e) {
            throw new TwitterRequestException("Request could not be executed:", e);
        }
        finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return genericApiResults;
    }
}
