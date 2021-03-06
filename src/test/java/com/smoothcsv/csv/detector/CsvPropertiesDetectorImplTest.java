/*
 * Copyright 2016 kohii
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.smoothcsv.csv.detector;

import static org.junit.Assert.assertEquals;

import com.smoothcsv.csv.prop.CsvProperties;
import com.smoothcsv.csv.prop.QuoteEscapeRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author kohii
 */
public class CsvPropertiesDetectorImplTest {

  public CsvPropertiesDetectorImplTest() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of detectProperties method, of class CsvPropertiesDetectorImpl.
   */
  @Test
  public void testDetectProperties_CharSequence0() {
    CharSequence line = "\"aaa\",\"bbb\",\"ccc\"";
    CsvPropertiesDetectorImpl instance = new CsvPropertiesDetectorImpl();
    CsvProperties result = instance.detectProperties(line);
    assertEquals('"', result.getQuoteChar());
    assertEquals(',', result.getDelimiter());
    assertEquals(QuoteEscapeRule.repeatQuoteChar(), result.getQuoteEscapeRule());
  }

  /**
   * Test of detectProperties method, of class CsvPropertiesDetectorImpl.
   */
  @Test
  public void testDetectProperties_CharSequence1() {
    CharSequence line = "\"aaa\",bbb,ccc";
    CsvPropertiesDetectorImpl instance = new CsvPropertiesDetectorImpl();
    CsvProperties result = instance.detectProperties(line);
    assertEquals('"', result.getQuoteChar());
    assertEquals(',', result.getDelimiter());
    assertEquals(QuoteEscapeRule.repeatQuoteChar(), result.getQuoteEscapeRule());
  }

  /**
   * Test of detectProperties method, of class CsvPropertiesDetectorImpl.
   */
  @Test
  public void testDetectProperties_CharSequence2() {
    CharSequence line = "\"aaa\",\"bb\\\"b\",\"ccc\"";
    CsvPropertiesDetectorImpl instance = new CsvPropertiesDetectorImpl();
    CsvProperties result = instance.detectProperties(line);
    assertEquals('"', result.getQuoteChar());
    assertEquals(',', result.getDelimiter());
    assertEquals(QuoteEscapeRule.escapeWith('\\'), result.getQuoteEscapeRule());
  }

  /**
   * Test of detectProperties method, of class CsvPropertiesDetectorImpl.
   */
  @Test
  public void testDetectProperties_CharSequence3() {
    CharSequence line = "'aaa'\t'bbb'\t'ccc'";
    CsvPropertiesDetectorImpl instance = new CsvPropertiesDetectorImpl();
    CsvProperties result = instance.detectProperties(line);
    assertEquals('\'', result.getQuoteChar());
    assertEquals('\t', result.getDelimiter());
    assertEquals(QuoteEscapeRule.repeatQuoteChar(), result.getQuoteEscapeRule());
  }
}
