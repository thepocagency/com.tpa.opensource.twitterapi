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
package com.tpa.twitterapi.tools.singleton.property;

import com.tpa.twitterapi.tools.singleton.logger.LoggerSingleton;
import java.util.logging.Logger;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 */
public class PropertySingletonTest {
    
    public PropertySingletonTest() {
    }
    
    @Test
    public void testGetInstance() {
        PropertySingleton propertySingleton1 = PropertySingleton.getInstance();
        PropertySingleton propertySingleton2 = PropertySingleton.getInstance();
        
        assertNotNull(propertySingleton1);
        assertNotNull(propertySingleton2);
        assertEquals(propertySingleton1.hashCode(), propertySingleton2.hashCode());
    }

    @Test
    public void testGetProperties() {
        PropertySingleton propertySingleton = PropertySingleton.getInstance();
        
        assertNotNull(propertySingleton.getProperties());
    }

    @Test
    public void testProperty_everyLogInSameFile() {
        String stringValue = PropertySingleton.getInstance().getProperty("java.util.logging.everyLogInSameFile");
        
        assertNotNull(stringValue);
        assertEquals(PropertySingleton.getInstance().getBoolean("java.util.logging.everyLogInSameFile"), Boolean.parseBoolean(stringValue));
    }

    @Test
    public void testProperty_loggerName() {
        assertNotNull(PropertySingleton.getInstance().getProperty("java.util.logging.loggerName"));
    }

    @Test
    public void testProperty_dirName() {
        assertNotNull(PropertySingleton.getInstance().getProperty("java.util.logging.FileHandler.dirName"));
    }

    @Test
    public void testProperty_name() {
        assertNotNull(PropertySingleton.getInstance().getProperty("java.util.logging.FileHandler.name"));
    }
    
    @Test
    public void testProperty_keepSameFile() {
        String stringValue = PropertySingleton.getInstance().getProperty("java.util.logging.FileHandler.keepSameFile");
        
        assertNotNull(stringValue);
        assertEquals(PropertySingleton.getInstance().getBoolean("java.util.logging.FileHandler.keepSameFile"), Boolean.parseBoolean(stringValue));
    }
    
    @Test
    public void testProperty_formatter() {
        Logger logger = LoggerSingleton.getInstance().getLogger(PropertySingletonTest.class);
        String expectedType = PropertySingleton.getInstance().getProperty("java.util.logging.FileHandler.formatter");
        
        try {
            assertNotNull(expectedType);
            assertThat(logger.getHandlers()[0].getFormatter(), CoreMatchers.instanceOf(Class.forName(expectedType)));
        } catch (ClassNotFoundException ex) {
            fail("The test is not valid, please check the type of the formater in the config file!");
        }
    }
        
    @Test
    public void testProperty_level() {
        assertNotNull(PropertySingleton.getInstance().getProperty("java.util.logging.level"));
    }
    
    @Test
    public void testProperty_consumerKey() {
        assertNotNull(PropertySingleton.getInstance().getProperty("twitter.consumerKey"));
    }
    
    @Test
    public void testProperty_consumerSecret() {
        assertNotNull(PropertySingleton.getInstance().getProperty("twitter.consumerSecret"));
    }
    
    @Test
    public void testProperty_delayInSeconds() {
        assertThat(PropertySingleton.getInstance().getInteger("twitter.default.delayInSeconds"), CoreMatchers.instanceOf(Integer.class));
        assertTrue(PropertySingleton.getInstance().getInteger("twitter.default.delayInSeconds") > 0);
    }
    
    @Test
    public void testProperty_sizeLimit() {
        assertThat(PropertySingleton.getInstance().getInteger("twitter.default.sizeLimit"), CoreMatchers.instanceOf(Integer.class));
        assertTrue(PropertySingleton.getInstance().getInteger("twitter.default.sizeLimit") > 0);
    }
    
    @Test
    public void testProperty_searchText() {
        assertNotNull(PropertySingleton.getInstance().getProperty("twitter.default.searchText"));
    }
}
