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
package com.tpa.twitterapi.tools.singleton.logger;

import com.tpa.twitterapi.tools.singleton.AbstractSingleton;
import com.tpa.twitterapi.tools.singleton.property.PropertySingleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A singleton to log information
 * 
 * It uses properties from the file config.properties:
 * 
 * - java.util.logging.everyLogInSameFile=true|false => if true: all logs from the entire app will be logged in the same file | if not, one file per class will be created
 * - java.util.logging.FileHandler.dirName=log/ => directory name where you want to store logs
 * - java.util.logging.FileHandler.name=twitter.log => final log file name
 * - java.util.logging.FileHandler.keepSameFile=true|false => false if you want to reset the log file(s) at each restart
 * - java.util.logging.FileHandler.formatter=class_path => the formatter you want to use 
 * - java.util.logging.level=VALUE => minimum level you want to log
 * 
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 */
public class LoggerSingleton extends AbstractSingleton {
    
    private static final PropertySingleton PROPERTY_SINGLETON = PropertySingleton.getInstance();
    
    private final Map<String, Logger> loggers;
    
    public LoggerSingleton() {
        loggers = new HashMap<>();
    }

    public static LoggerSingleton getInstance() {
        return LoggerSingleton.getGenericInstance(LoggerSingleton.class);
    }

    /**
     * Creates and returns the logger for the given class
     * 
     * If it exists already, you will get the former instance
     * 
     * @param loggerClass
     * @return Logger
     */
    public Logger getLogger(Class loggerClass) {
        
        try {
            // If false: it will create one log file per class
            Boolean everyLogInSameFile = PROPERTY_SINGLETON.getBoolean("java.util.logging.everyLogInSameFile");
            
            String loggerName;
            String logFileName;
            
            if (everyLogInSameFile) {
                loggerName = PROPERTY_SINGLETON.getProperty("java.util.logging.loggerName");
                logFileName = PROPERTY_SINGLETON.getProperty("java.util.logging.FileHandler.name");
            }
            else {
                loggerName = loggerClass.getName();
                logFileName = PROPERTY_SINGLETON.getProperty("java.util.logging.FileHandler.name") + "_" + loggerName;
            }
            
            if (!loggers.containsKey(loggerName)) {
                
                Path path = Paths.get(PROPERTY_SINGLETON.getProperty("java.util.logging.FileHandler.dirName"));
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                    Logger.getLogger(LoggerSingleton.class.getName()).log(Level.INFO, "Log directory was created");
                }
                
                Logger logger = Logger.getLogger(loggerName);
                
                Handler fh = new FileHandler(PROPERTY_SINGLETON.getProperty("java.util.logging.FileHandler.dirName")
                        + logFileName, 
                        PROPERTY_SINGLETON.getBoolean("java.util.logging.FileHandler.keepSameFile"));
                
                fh.setFormatter(PROPERTY_SINGLETON.getObject("java.util.logging.FileHandler.formatter", Formatter.class));
                
                logger.addHandler(fh);
                logger.setLevel(Level.parse(PROPERTY_SINGLETON.getProperty("java.util.logging.level")));
                
                loggers.put(loggerName, logger);
            }
            
            return loggers.get(loggerName);
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(LoggerSingleton.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Logger.getLogger(LoggerSingleton.class.getName());
    }
}
