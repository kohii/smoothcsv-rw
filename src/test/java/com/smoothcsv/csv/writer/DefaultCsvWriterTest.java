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
package com.smoothcsv.csv.writer;

import com.smoothcsv.csv.NewlineCharacter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author kohii
 */
public class DefaultCsvWriterTest {

  private DefaultCsvWriter csvWriter;

  public DefaultCsvWriterTest() {}

  @Before
  public void setUp() {
    csvWriter = new DefaultCsvWriter(new StringWriter());
  }

  @After
  public void tearDown() throws IOException {
    if (csvWriter != null) {
      csvWriter.close();
    }
  }

  /**
   * Test of extractLineSeparator method, of class DefaultCsvWriter.
   */
  @Test
  public void testExtractLineFeedCode() {
    List<Object> row = Arrays.asList("1", "2", "3");
    Object result = csvWriter.extractLineSeparator(row, 0);
    assertEquals(NewlineCharacter.DEFAULT.stringValue(), result);
  }

  /**
   * Test of extractValue method, of class DefaultCsvWriter.
   */
  @Test
  public void testExtractValue() {
    List<Object> row = Arrays.asList("a", "b", "c");
    int rowIndex = 0;
    String result;
    result = csvWriter.extractValue(row, rowIndex, 0);
    assertEquals("a", result);
    result = csvWriter.extractValue(row, rowIndex, 1);
    assertEquals("b", result);
    result = csvWriter.extractValue(row, rowIndex, 2);
    assertEquals("c", result);
  }

  /**
   * Test of extractColumnSize method, of class DefaultCsvWriter.
   */
  @Test
  public void testExtractColumnSize() {
    List<Object> row = Arrays.asList("1", "2", "3");
    int result = csvWriter.extractColumnSize(row, 0);
    assertEquals(3, result);
  }

}
