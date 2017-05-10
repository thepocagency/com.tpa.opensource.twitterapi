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
package com.tpa.twitterapi.tools.singleton.scanner;

import com.tpa.twitterapi.tools.singleton.AbstractSingleton;
import com.tpa.twitterapi.tools.singleton.logger.LoggerSingleton;
import com.tpa.twitterapi.exception.WrongConversionException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A singleton to manipulate a Scanner
 * 
 * Default Scanner is based on System.in
 * 
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 */
public final class ScannerSingleton extends AbstractSingleton {
    
    private static final Logger LOGGER = LoggerSingleton.getInstance().getLogger(ScannerSingleton.class);
    
    private Scanner defautlScanner;
    private Scanner scanner;
    
    public ScannerSingleton() {
        setScanner(new Scanner(System.in));
    }
    
    public static ScannerSingleton getInstance() {
        return ScannerSingleton.getGenericInstance(ScannerSingleton.class);
    }
    
    public static ScannerSingleton getInstance(Scanner scanner) {
        return ScannerSingleton.getGenericInstance(ScannerSingleton.class).setScanner(scanner);
    }
    
    public ScannerSingleton setScanner(Scanner scanner) {
        this.defautlScanner = scanner;
        this.scanner = scanner;
        return this;
    }
    
    public ScannerSingleton startScanner() {
        try {
            scanner = defautlScanner;
            LOGGER.info("Scanner is started");
        }
        catch (Exception e) {
            LOGGER.info("Scanner could not be started");
        }
        
        return this;
    }
    
    public ScannerSingleton stopScanner() {
        try {
            scanner.close();
            LOGGER.info("Scanner is closed");
        }
        catch (Exception e) {
            LOGGER.info("Scanner could not be closed");
        }
        
        return this;
    }
    
    public ScannerSingleton restartScanner() {
        return this.stopScanner().startScanner();
    }
    
    /**
     * Scan a value and cast it into the selected type
     * 
     * @param <T> can only be a String or an Integer according to the outputType
     * @param outputType can only be a String.class or an Integer.class
     * @param defaultValue the default value if exception is thrown
     * @param preText the text you want to show to the user before listening his value
     * @return a String or an Integer according to the outputType
     * @throws WrongConversionException if you try to cast something which is not a String or Integer
     */
    public <T> T scanValue(Class<T> outputType, T defaultValue, String preText) throws WrongConversionException {
        
        String scannedValue = null;
        try {
            LOGGER.info(preText);
            LOGGER.info("Please enter your text and validate with <ENTER>:");
            LOGGER.log(Level.INFO, "NB: if empty, default value will be {0}", defaultValue);
            LOGGER.info(">>");
            
            scannedValue = scanner.nextLine();
        } catch(Exception e) {
            LOGGER.info("No value given");
        }
        
        LOGGER.log(Level.INFO, "The obtained value is {0} and should be converted in a {1}", new Object[]{scannedValue, outputType.getName()});

        try {
            if (outputType == Integer.class) {
                return (T) (Integer) Integer.parseInt(scannedValue);
            }

            if (outputType == String.class) {
                return (T) scannedValue;
            }
        }
        catch (Exception e) { // If there was a conversion exception
            LOGGER.log(Level.WARNING, "User gave this value: {0} but this could not be converted into a {1} so the default value will be returned: {2}", new Object[]{scannedValue, outputType.getName(), defaultValue});
            
            if (defaultValue != null) {
                return defaultValue;
            }
        }
        
        // If at this step of the method, we do not have any return yet: that means that you asked for a wrong type...
        throw new WrongConversionException("The scanner output could not be converted into the possible type offered by this method...");
    }
}
