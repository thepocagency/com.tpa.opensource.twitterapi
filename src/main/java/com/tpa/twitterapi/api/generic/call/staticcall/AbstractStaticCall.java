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
package com.tpa.twitterapi.api.generic.call.staticcall;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.tpa.twitterapi.api.authentificator.TwitterAuthenticator;
import com.tpa.twitterapi.api.generic.bean.GenericTwitterBean;
import com.tpa.twitterapi.api.generic.call.AbstractCall;
import com.tpa.twitterapi.exception.TwitterAuthenticationException;
import com.tpa.twitterapi.exception.TwitterRequestException;
import java.io.IOException;
import java.util.logging.Level;

/**
 * This is a first version a generic static call class
 * 
 * You have to implement the InterfaceStaticCall to implement the executeRequest() method
 * 
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 * @param <CLASS_TYPE>
 * @param <BEAN_TYPE>
 */
public abstract class AbstractStaticCall<CLASS_TYPE extends AbstractCall, BEAN_TYPE extends GenericTwitterBean> extends AbstractCall<CLASS_TYPE, BEAN_TYPE> 
        implements InterfaceStaticCall<CLASS_TYPE, BEAN_TYPE> {
    
    public AbstractStaticCall(Class<BEAN_TYPE> genericTwitterBeanType, TwitterAuthenticator twitterAuthenticator, String url) {
        super(genericTwitterBeanType, twitterAuthenticator, url);
    }
    
    /**
     * Execute the request
     * 
     * It uses "Google Http Client" to parse the result
     * 
     * @return the <BEAN_TYPE> object result
     * @throws TwitterAuthenticationException
     * @throws TwitterRequestException 
     */
    @Override
    public BEAN_TYPE executeRequest() throws TwitterAuthenticationException, TwitterRequestException {
        HttpResponse httpResponse = null;
        BEAN_TYPE genericApiResult = null;
        
        try {
            
            GenericUrl genericUrl = new GenericUrl(url);
            genericUrl.setUnknownKeys(parameters);
            
            HttpRequest request = twitterAuthenticator.getHttpRequestFactory().buildGetRequest(genericUrl);
            request.setParser(new JacksonFactory().createJsonObjectParser());
            
            httpResponse = request.execute();
            
            LOGGER.log(Level.INFO, "Request: {0} executed with status code: {1}", new Object[]{request.getUrl().toString(), httpResponse.getStatusCode()});
            genericApiResult = httpResponse.parseAs(GENERIC_BEAN_TYPE); // This is using @Key annotations of the beans
            LOGGER.info("Result retrieved and parsed");
        } catch (IOException e) {
            throw new TwitterRequestException("Request could not be executed:", e);
        }
        
        return genericApiResult;
    }
}
