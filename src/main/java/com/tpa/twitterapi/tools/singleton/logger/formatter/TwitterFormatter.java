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
package com.tpa.twitterapi.tools.singleton.logger.formatter;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * This is a simple formatter to print TwitterMessage in log file, without any "log information"
 * 
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 */
public class TwitterFormatter extends Formatter {

    /**
     * Format a given LogRecord.
     *
     * @param record the log record to be formatted.
     * @return a formatted log record
     */
    @Override
    public synchronized String format(LogRecord record) {

        StringBuilder sb = new StringBuilder();

        // The message
        sb.append(formatMessage(record));
        
        // Line break after every message
        sb.append("\n");
        
        return sb.toString();
    }
}
