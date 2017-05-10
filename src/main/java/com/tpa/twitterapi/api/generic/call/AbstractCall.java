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
package com.tpa.twitterapi.api.generic.call;

import com.tpa.twitterapi.tools.singleton.logger.LoggerSingleton;
import com.tpa.twitterapi.tools.singleton.property.PropertySingleton;
import com.tpa.twitterapi.api.authentificator.TwitterAuthenticator;
import com.tpa.twitterapi.api.generic.bean.GenericTwitterBean;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This is a abstract generic-type class you can use to create new API calls
 *
 * Cf. examples: - com.tpa.twitter.api.call.Search.class -
 * com.tpa.twitter.api.call.StreamingSearch.class
 *
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 * @param <CLASS_TYPE> : the specific call you want to create (for ex.:
 * Search.class)
 * @param <BEAN_TYPE> : the bean manipulated by the call (f. ex.:
 * TwitterStatuses.class), must extend the GenericTwitterBean.class - this is
 * used by Jackson to cast the result into an instance of the class
 */
public abstract class AbstractCall<CLASS_TYPE extends AbstractCall, BEAN_TYPE extends GenericTwitterBean> {

    protected static final PropertySingleton PROPERTY_SINGLETON = PropertySingleton.getInstance();
    protected static Logger LOGGER;

    protected final Class<BEAN_TYPE> GENERIC_BEAN_TYPE;

    protected final TwitterAuthenticator twitterAuthenticator;
    protected final Map<String, Object> parameters;

    protected String url;

    /**
     * 
     * @param GENERIC_BEAN_TYPE : the class of the TwitterBean you want to use to cast Twitter API result
     * @param twitterAuthenticator : containing the authenticated HttpRequestFactory
     * @param url : the default URL of your specific call
     */
    public AbstractCall(Class<BEAN_TYPE> GENERIC_BEAN_TYPE, TwitterAuthenticator twitterAuthenticator, String url) {
        this.GENERIC_BEAN_TYPE = GENERIC_BEAN_TYPE;
        this.twitterAuthenticator = twitterAuthenticator;
        this.url = url;

        this.parameters = new HashMap<>();

        LOGGER = LoggerSingleton.getInstance().getLogger(GENERIC_BEAN_TYPE);
    }

    public final Map<String, String> getParameters() {
        return (Map) this.parameters;
    }

    /**
     * This is a chaining method to add a new parameter
     * 
     * @param key
     * @param value
     * @return an instance of AbstractCall
     */
    public final CLASS_TYPE addParameter(String key, String value) {
        this.parameters.put(key, value);
        return (CLASS_TYPE) this;
    }

    public String getUrl() {
        return url;
    }

    /**
     * This is a chaining method to change the default URL
     * 
     * @param url
     * @return an instance of AbstractCall
     */
    public final CLASS_TYPE setUrl(String url) {
        this.url = url;
        return (CLASS_TYPE) this;
    }
}
