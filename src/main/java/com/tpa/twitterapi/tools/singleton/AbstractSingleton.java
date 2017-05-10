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
package com.tpa.twitterapi.tools.singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a generic class to create singletons
 * 
 * Your specific singleton needs to:
 * - extend this AbstractSingleton class
 * - have a public default constructor
 * - get the next static method:
 * 
 *      public static YOUR_SINGLETON_CLASS_NAME getInstance() {
 *          return LoggerSingleton.getGenericInstance(YOUR_SINGLETON_CLASS_NAME.class);
 *      }
 * 
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 */
public abstract class AbstractSingleton {
    
    public static Map<String, Object> INSTANCES = null;
    
    protected static <SINGLETON_TYPE> SINGLETON_TYPE getGenericInstance(Class<SINGLETON_TYPE> singletonType) {
        
        try {
            if (INSTANCES == null) {
                INSTANCES = new HashMap<>();
            }
                
            if (!INSTANCES.containsKey(singletonType.getName())) {
                Constructor<SINGLETON_TYPE> constructor = singletonType.getConstructor();
                constructor.setAccessible(true);
                INSTANCES.put(singletonType.getName(), constructor.newInstance());
            }
            
            return singletonType.cast(INSTANCES.get(singletonType.getName()));
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException ex) {
            return null;
        }
    }
}
