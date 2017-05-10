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

import com.tpa.twitterapi.api.generic.bean.GenericTwitterBean;
import com.tpa.twitterapi.api.generic.call.AbstractCall;
import com.tpa.twitterapi.exception.TwitterAuthenticationException;
import com.tpa.twitterapi.exception.TwitterRequestException;

/**
 * This interface must be used for the static calls
 * 
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 * @param <CLASS_TYPE>
 * @param <BEAN_TYPE>
 */
public interface InterfaceStaticCall<CLASS_TYPE extends AbstractCall, BEAN_TYPE extends GenericTwitterBean> {

    /**
     * Execute the request defined by the default URL
     * 
     * @return an unique element
     * @throws TwitterAuthenticationException
     * @throws TwitterRequestException 
     */
    public abstract BEAN_TYPE executeRequest() throws TwitterAuthenticationException, TwitterRequestException;
}
