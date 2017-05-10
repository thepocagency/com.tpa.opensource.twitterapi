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

import com.tpa.twitterapi.tools.singleton.logger.LoggerSingleton;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 */
public class LoggerSingletonTest {
    
    public LoggerSingletonTest() {
    }
    
    @Test
    public void testGetInstance() {
        LoggerSingleton loggerSingleton1 = LoggerSingleton.getInstance();
        LoggerSingleton loggerSingleton2 = LoggerSingleton.getInstance();
        
        assertNotNull(loggerSingleton1);
        assertNotNull(loggerSingleton2);
        assertEquals(loggerSingleton1.hashCode(), loggerSingleton2.hashCode());
    }

    @Test
    public void testGetLogger() {        
        Logger logger = LoggerSingleton.getInstance().getLogger(LoggerSingletonTest.class);
        
        assertNotNull(logger);
        assertNotNull(logger.getHandlers());
    }
    
    @Test
    public void testGetLoggerInfo() {
        try {
            String expectedValue = "This a test insert!";
            
            LoggerSingleton.getInstance().getLogger(LoggerSingletonTest.class).info(expectedValue);
            String logFileContent = new String(Files.readAllBytes(Paths.get("log/twitter.log")), StandardCharsets.UTF_8);
            
            Assert.assertThat(logFileContent, CoreMatchers.containsString(expectedValue));
        } catch (IOException ex) {
            fail("The test is not valid, please check the file and/or the writing rights!");
        }
    }
}
