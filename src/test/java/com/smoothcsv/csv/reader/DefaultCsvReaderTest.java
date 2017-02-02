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
package com.smoothcsv.csv.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author kohii
 */
public class DefaultCsvReaderTest {

  DefaultCsvReader csvReader;

  public DefaultCsvReaderTest() {}

  @Before
  public void setUp() {
    csvReader = createReader();
  }

  @After
  public void tearDown() throws IOException {
    if (csvReader != null) {
      csvReader.close();
    }
  }

  /**
   * Test of createNewRow method, of class DefaultCsvReader.
   */
  @Test
  public void testCreateNewRow() {
    List<String> result = csvReader.createNewRow(0);
    assertNotNull(result);
    assertEquals(result.size(), 0);
  }

  /**
   * Test of handleValue method, of class DefaultCsvReader.
   */
  @Test
  public void testHandleValue() {
    List<String> row = new ArrayList<>();
    int rowIndex = 0;
    String data = "abc";
    int columnIndex = 0;
    csvReader.handleValue(row, rowIndex, columnIndex, data);
    assertEquals(row.size(), 1);
    assertEquals(data, row.get(0));

  }

  private static DefaultCsvReader createReader() {
    InputStreamReader isr =
        new InputStreamReader(DefaultCsvReaderTest.class.getResourceAsStream("/test_0.csv"));
    return new DefaultCsvReader(isr);
  }
}
