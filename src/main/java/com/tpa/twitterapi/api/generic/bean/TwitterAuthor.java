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
package com.tpa.twitterapi.api.generic.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.Key;
import java.util.ArrayList;

/**
 * 
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 */
public class TwitterAuthor extends GenericTwitterBean {

    @Key
    @JsonProperty
    private String name;
    
    @Key("screen_name")
    @JsonProperty("screen_name")
    private String screenName;
    
    private TwitterStatuses statuses;

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public TwitterStatuses getStatuses() {
        if (statuses == null) {
            statuses = new TwitterStatuses(new ArrayList<>());
        }
        return statuses;
    }
    
    /**
     * Can be used if you need to check the state of the entity after using Jackson
     * 
     * NB: because there is no validation system in Jackson for required property for ex.
     * 
     * @return Boolean
     */
    @Override
    public Boolean isValid() {
        return super.isValid() && name != null && screenName != null;
    }

    @Override
    public String toString() {
        return "Author name: " + name + "(creating date: " + createdDate + ")";
    }
}
