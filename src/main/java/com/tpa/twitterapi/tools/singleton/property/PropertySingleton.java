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

import com.tpa.twitterapi.tools.singleton.AbstractSingleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A singleton to get properties from the file config.properties
 * 
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 */
public class PropertySingleton extends AbstractSingleton {
    
    private static final String FILENAME = "config.properties";
    
    private Properties properties;
    
    public PropertySingleton() {
    }

    public static PropertySingleton getInstance() {
        return PropertySingleton.getGenericInstance(PropertySingleton.class);
    }

    /**
     * Get properties (extends HashTable) from the file defined by the static String FILENAME
     * 
     * @return Properties
     */
    public Properties getProperties() {
        
        if (properties == null) {
        
            properties = new Properties();
            InputStream input = null;

            try {

                input = PropertySingleton.class.getClassLoader().getResourceAsStream(FILENAME);
                properties.load(input);
            } catch (IOException ex) {

            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {

                    }
                }
            }
        }
        
        return properties;
    }
    
    /**
     * Returns the string property value
     * 
     * @param key
     * @return String
     */
    public String getProperty(String key) {
        return getProperties().getProperty(key);
    }
    
    /**
     * Directly converts the property value into a boolean
     * 
     * @param key
     * @return Boolean
     */
    public Boolean getBoolean(String key) {
        return Boolean.parseBoolean(getProperties().getProperty(key));
    }
   
    /**
     * Directly converts the property value into an Integer
     * 
     * @param key
     * @return Integer
     */
    public Integer getInteger(String key) {
        return Integer.parseInt(getProperties().getProperty(key));
    }
    
    /**
     * Returns an object of the class defined in the property value
     * 
     * Ex. :
     * 
     * Handler fh = new FileHandler("myName");
     * fh.setFormatter(PROPERTY_SINGLETON.getObject("java.util.logging.FileHandler.formatter", Formatter.class));
     * 
     * @param <T> the object you want to obtain
     * @param key the property key, must be a class path
     * @param type the super class you want to use to cast
     * @return an object defined by the the generic type T and of a subclass T
     */
    public <T> T getObject(final String key, final Class<T> type){
        try{
            return type.cast(Class.forName(getProperty(key)).newInstance());
        } catch(InstantiationException
              | IllegalAccessException
              | ClassNotFoundException e){
            throw new IllegalStateException(e);
        }
    }
}
