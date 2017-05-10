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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class GenericTwitterBean {
    
    @Key("id") // NB: @Key : used by Google Http Client
    @JsonProperty("id") // NB: @JsonProperty : used by Jackson
    protected Long id;
    
    @Key("created_at")
    @JsonProperty("created_at")
    protected String createdDate;

    public Long getId() {
        return id;
    }
    
    /**
     * Convert the string createdAt Twitter-API property into a java Date
     * 
     * If you want to get the epoch time, just use the date.getTime() method
     * 
     * @return Date | null if error 
     */
    public Date getCreatedDate() {
        // This is the default date format given from Twitter...
        SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
        sf.setLenient(true);
        
        try {
            return sf.parse(createdDate);
        } catch (ParseException ex) {
            return null;
        }
    }
    
    /**
     * Can be used if you need to check the state of the entity after using Jackson
     * 
     * NB: because there is no validation system in Jackson for required property for ex.
     * 
     * @return Boolean
     */
    public Boolean isValid() {
       return id != null && createdDate != null; 
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((GenericTwitterBean) obj).getId());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }
}
