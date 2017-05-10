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

import com.tpa.twitterapi.tools.singleton.scanner.ScannerSingleton;
import com.tpa.twitterapi.exception.WrongConversionException;
import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 */
public class ScannerSingletonTest {
    
    public ScannerSingletonTest() {
    }
    
    @Test
    public void testGetInstance() {
        ScannerSingleton scannerSingleton1 = ScannerSingleton.getInstance();
        ScannerSingleton scannerSingleton2 = ScannerSingleton.getInstance();
        
        assertNotNull(scannerSingleton1);
        assertNotNull(scannerSingleton2);
        assertEquals(scannerSingleton1.hashCode(), scannerSingleton2.hashCode());
    }

    @Test
    public void testSetScanner() {
        assertNotNull(ScannerSingleton.getInstance().setScanner(new Scanner("coucou!\n")));
    }

    @Test
    public void testStartScanner() {
        assertNotNull(ScannerSingleton.getInstance().startScanner());
    }

    @Test
    public void testStopScanner() {
        assertNotNull(ScannerSingleton.getInstance().stopScanner());
    }

    @Test
    public void testRestartScanner() {
        assertNotNull(ScannerSingleton.getInstance().restartScanner());
    }

    @Test
    public void testScanValue() throws Exception {
        assertEquals(new Integer(10), ScannerSingleton.getInstance().setScanner(new Scanner("10\n")).scanValue(Integer.class, 20, "Value?"));
        assertEquals(new Integer(20), ScannerSingleton.getInstance().setScanner(new Scanner("this is not a Integer, so defautl value is used\n")).scanValue(Integer.class, 20, "Value?"));
        assertEquals("coucou!", ScannerSingleton.getInstance().setScanner(new Scanner("coucou!\n")).scanValue(String.class, "pouet", "Value?"));
        
        try {
            assertEquals(Boolean.TRUE, ScannerSingleton.getInstance().setScanner(new Scanner("coucou!\n")).scanValue(Boolean.class, true, "Value?"));
            fail("Should throw an exception because the Boolean type is not yet defined");
        } catch(WrongConversionException ex){
            assertTrue(true);
        }
    }    
}
