/*
 * Copyright 2014 kohii.
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

import com.smoothcsv.csv.CsvProperties;
import com.smoothcsv.csv.NewlineCharacter;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author kohii
 */
public class AbstractCsvReaderTest {

  public AbstractCsvReaderTest() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of getRowIndex method, of class AbstractCsvReader.
   */
  @Test
  public void testGetRowIndex() throws Exception {
    try (AbstractCsvReader<List<String>> instance = createReader()) {
      assertEquals(0, instance.getRowIndex());
      instance.readRow();
      assertEquals(1, instance.getRowIndex());
      instance.skipRows(1);
      assertEquals(2, instance.getRowIndex());
    }
  }

  /**
   * Test of readRow method, of class AbstractCsvReader.
   */
  @Test
  public void testReadRow() throws Exception {
    try (AbstractCsvReader<List<String>> instance = createReader()) {
      List<String> row = instance.readRow();
      assertArrayEquals(new String[] {"a", "b", "c"}, row.toArray());
      row = instance.readRow();
      assertArrayEquals(new String[] {"aaa", "bbb", "ccc"}, row.toArray());
      row = instance.readRow();
      assertArrayEquals(new String[] {"ddd", "eee", "fff"}, row.toArray());
      row = instance.readRow();
      assertArrayEquals(new String[] {"a\na\na\n", "\nb\nb\nb\n", "\nc\n\nc\n\nc"}, row.toArray());
      row = instance.readRow();
      assertArrayEquals(new String[] {"\",", "", ""}, row.toArray());
      row = instance.readRow();
      assertArrayEquals(new String[] {"1", "2", "3", "4"}, row.toArray());
      row = instance.readRow();
      assertArrayEquals(new String[] {}, row.toArray());
      row = instance.readRow();
      assertNull(row);
    }
  }

  @Test
  public void testReadRow2() throws Exception {
    CsvProperties prop = new CsvProperties('\t', '"');
    try (AbstractCsvReader<List<String>> instance =
        createReader(prop, CsvReaderOptions.DEFAULT, "test_0.tsv")) {
      List<String> row = instance.readRow();
      assertArrayEquals(new String[] {"a", "b", "c"}, row.toArray());
      row = instance.readRow();
      assertArrayEquals(new String[] {"aaa", "bbb", "ccc"}, row.toArray());
      row = instance.readRow();
      assertArrayEquals(new String[] {"ddd", "eee", "fff"}, row.toArray());
      row = instance.readRow();
      assertArrayEquals(new String[] {"a\na\na\n", "\nb\nb\nb\n", "\nc\n\nc\n\nc"}, row.toArray());
      row = instance.readRow();
      assertArrayEquals(new String[] {"\",", "", ""}, row.toArray());
      row = instance.readRow();
      assertArrayEquals(new String[] {"1", "2", "3", "4"}, row.toArray());
      row = instance.readRow();
      assertArrayEquals(new String[] {}, row.toArray());
      row = instance.readRow();
      assertNull(row);
    }
  }

  /**
   * Test of skipRows method, of class AbstractCsvReader.
   */
  @Test
  public void testSkipRows() throws Exception {
    try (AbstractCsvReader<List<String>> instance = createReader()) {
      int result = instance.skipRows(2);
      assertEquals(result, 2);
      assertEquals(instance.getRowIndex(), 2);
      List<String> row = instance.readRow();
      assertEquals(row.size(), 3);
      assertEquals(row.get(0), "ddd");
      assertEquals(row.get(1), "eee");
      assertEquals(row.get(2), "fff");
    }
  }

  /**
   * Test of close method, of class AbstractCsvReader.
   */
  @Test
  public void testClose() throws Exception {
    AbstractCsvReader<List<String>> instance = new DefaultCsvReader(new StringReader(""));
    instance.close();

    // normal end
  }

  /**
   * Test of readAll method, of class AbstractCsvReader.
   */
  @Test
  public void testReadAll() throws Exception {
    try (AbstractCsvReader<List<String>> instance = createReader()) {

    }
  }

  /**
   * Test of rows method, of class AbstractCsvReader.
   */
  @Test
  public void testRows() throws Exception {
    try (AbstractCsvReader<List<String>> instance = createReader()) {
      String[] array =
          instance.rows().filter(r -> r.size() == 4).map(r -> r.toArray(new String[4])).findFirst()
              .get();
      assertEquals(array.length, 4);
      assertEquals(array[0], "1");
      assertEquals(array[1], "2");
      assertEquals(array[2], "3");
      assertEquals(array[3], "4");
    }
  }

  public static class AbstractCsvReaderImpl extends AbstractCsvReader<List<String>> {

    public AbstractCsvReaderImpl(Reader in) {
      super(in, CsvProperties.DEFAULT, CsvReaderOptions.DEFAULT);
    }

    public AbstractCsvReaderImpl(Reader in, CsvProperties prop, CsvReaderOptions options) {
      super(in, prop, options);
    }

    public List<String> createNewRow(int rowIndex) {
      return new ArrayList<>();
    }

    public void handleValue(List<String> row, int rowIndex, int columnIndex, String value) {
      row.add(value);
    }

    public void handleLineSeparator(List<String> row, int rowIndex, NewlineCharacter lineFeedCode) {
      // do nothing
    }
  }

  private static AbstractCsvReader<List<String>> createReader() {
    InputStreamReader isr =
        new InputStreamReader(AbstractCsvReaderTest.class.getResourceAsStream("/test_0.csv"));
    return new AbstractCsvReaderImpl(isr);
  }

  private static AbstractCsvReader<List<String>> createReader(CsvProperties prop,
      CsvReaderOptions options, String file) {
    InputStreamReader isr =
        new InputStreamReader(AbstractCsvReaderTest.class.getResourceAsStream("/" + file));
    return new AbstractCsvReaderImpl(isr, prop, options);
  }
}
