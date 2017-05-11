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
import com.tpa.twitterapi.tools.singleton.logger.LoggerSingleton;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This bean is used for the static Twitter search (cf. com.tpa.twitter.api.call.Search.class) to cast API output
 * 
 * There is also a method to show you how to order messages with Java 8 stream API, cf. getOrderedStatuses()
 * 
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 */
public class TwitterStatuses extends GenericTwitterBean {
    
    public static final Logger LOGGER = LoggerSingleton.getInstance().getLogger(TwitterStatuses.class);

    @Key
    @JsonProperty
    private List<TwitterStatus> statuses;

    public TwitterStatuses(List<TwitterStatus> statuses) {
        this.statuses = statuses;
    }

    public List<TwitterStatus> getStatuses() {
        return statuses;
    }

    /**
     * Orders statuses:
     * 
     * 1. By author createdDate
     * 2. By author ID (to  avoid issues with authors with the same dates)
     * 3. By message createdDate
     * 
     * @return ordered list of statuses
     */
    public List<TwitterStatus> getOrderedStatuses() {
        // We create the comparator
        Comparator<TwitterStatus> comparator = Comparator.comparing(currentStatus -> currentStatus.getAuthor().getCreatedDate());
        comparator = comparator.thenComparing(Comparator.comparing(currentStatus -> currentStatus.getAuthor().getId()));
        comparator = comparator.thenComparing(Comparator.comparing(currentStatus -> currentStatus.getCreatedDate()));

        // We order the stream
        Stream<TwitterStatus> twitterStatusesStream = statuses.stream().sorted(comparator);

        // We recreate a new ordered list and return it...
        return twitterStatusesStream.collect(Collectors.toList());
    }
    
    /**
     * To add a new status in the list statuses
     * 
     * If the property statuses is null, this method automatically creates the ArrayList
     * 
     * @param twitterStatus
     * @return true if status added | false if statuses already contains the input status
     */
    public boolean addStatus(TwitterStatus twitterStatus) {
        if (statuses == null) {
            statuses = new ArrayList<>();
        }
        
        if (!statuses.contains(twitterStatus)) {
            LOGGER.log(Level.INFO, "\t\tNew tweet: {0}", twitterStatus.toString());
            statuses.add(twitterStatus);
            return true;
        }
        
        return false;
    }
}
