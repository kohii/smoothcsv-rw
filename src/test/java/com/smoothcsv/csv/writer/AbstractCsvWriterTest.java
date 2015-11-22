/*
 * Copyright 2015 kohii
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

import com.smoothcsv.csv.CsvProperties;
import com.smoothcsv.csv.CsvQuoteApplyRule;
import com.smoothcsv.csv.NewlineCharacter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author kohii
 */
public class AbstractCsvWriterTest {

  public AbstractCsvWriterTest() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of writeRow method, of class AbstractCsvWriter.
   */
  @Test
  public void testWriteRow() throws Exception {
    AbstractCsvWriter<List<String>> instance = createWriter();
    instance.writeRow(Arrays.asList("a", "b", "c"));
    instance.writeRow(Arrays.asList("aaa", "bbb", "ccc"));
    instance.writeRow(Arrays.asList("ddd", "eee", "fff"));
    instance.writeRow(Arrays.asList("a\na\na\n", "\nb\nb\nb\n", "\nc\n\nc\n\nc"));
    instance.writeRow(Arrays.asList("\",", "", ""));
    instance.writeRow(Arrays.asList("1", "2", "3", "4"));
    instance.writeRow(Arrays.asList());
    String csv = getResultString(instance);
    assertEquals(readFile("test_0.csv"), csv);
  }

  /**
   * Test of writeAll method, of class AbstractCsvWriter.
   */
  @Test
  public void testWriteAll() throws Exception {
    AbstractCsvWriter<List<String>> instance = createWriter();
    List<List<String>> lines = new ArrayList<>();
    lines.add(Arrays.asList("a", "b", "c"));
    lines.add(Arrays.asList("aaa", "bbb", "ccc"));
    lines.add(Arrays.asList("ddd", "eee", "fff"));
    lines.add(Arrays.asList("a\na\na\n", "\nb\nb\nb\n", "\nc\n\nc\n\nc"));
    lines.add(Arrays.asList("\",", "", ""));
    lines.add(Arrays.asList("1", "2", "3", "4"));
    lines.add(Arrays.asList());
    instance.writeAll(lines);
    String csv = getResultString(instance);
    assertEquals(readFile("test_0.csv"), csv);
  }

  /**
   * Test of appliesQuoting method, of class AbstractCsvWriter.
   */
  @Test
  public void testAppliesQuoting_quoteWhenNeeded() {
    CsvWriterOptions options = new CsvWriterOptions();
    options.setQuoteOption(CsvQuoteApplyRule.QUOTES_IF_NECESSARY);
    AbstractCsvWriter<List<String>> instance = createWriter(CsvProperties.DEFAULT, options);
    assertFalse(instance.appliesQuoting("abc", 0, 0));
    assertTrue(instance.appliesQuoting("a\"bc", 0, 0));
    assertTrue(instance.appliesQuoting("a,bc", 0, 0));
    assertTrue(instance.appliesQuoting("a\nbc", 0, 0));
    assertTrue(instance.appliesQuoting("a\rbc", 0, 0));
  }

  /**
   * Test of appliesQuoting method, of class AbstractCsvWriter.
   */
  @Test
  public void testAppliesQuoting_noQuote() {
    CsvWriterOptions options = new CsvWriterOptions();
    options.setQuoteOption(CsvQuoteApplyRule.NO_QUOTE);
    AbstractCsvWriter<List<String>> instance = createWriter(CsvProperties.DEFAULT, options);
    assertFalse(instance.appliesQuoting("abc", 0, 0));
    assertFalse(instance.appliesQuoting("a\"bc", 0, 0));
    assertFalse(instance.appliesQuoting("a,bc", 0, 0));
    assertFalse(instance.appliesQuoting("a\nbc", 0, 0));
    assertFalse(instance.appliesQuoting("a\rbc", 0, 0));
  }

  /**
   * Test of appliesQuoting method, of class AbstractCsvWriter.
   */
  @Test
  public void testAppliesQuoting_quoteAll() {
    CsvWriterOptions options = new CsvWriterOptions();
    options.setQuoteOption(CsvQuoteApplyRule.QUOTES_ALL);
    AbstractCsvWriter<List<String>> instance = createWriter(CsvProperties.DEFAULT, options);
    assertTrue(instance.appliesQuoting("abc", 0, 0));
    assertTrue(instance.appliesQuoting("a\"bc", 0, 0));
    assertTrue(instance.appliesQuoting("a,bc", 0, 0));
    assertTrue(instance.appliesQuoting("a\nbc", 0, 0));
    assertTrue(instance.appliesQuoting("a\rbc", 0, 0));
  }

  public static class AbstractCsvWriterImpl extends AbstractCsvWriter<List<String>> {

    private StringWriter out;

    public AbstractCsvWriterImpl(StringWriter out) {
      super(out);
      this.out = out;
    }

    public AbstractCsvWriterImpl(StringWriter out, CsvProperties properties,
        CsvWriterOptions options) {
      super(out, properties, options);
      this.out = out;
    }

    public Object extractLineSeparator(List<String> row, int rowIndex) {
      return NewlineCharacter.DEFAULT.stringValue();
    }

    public String extractValue(List<String> row, int rowIndex, int columnIndex) {
      return row.get(columnIndex);
    }

    public int extractColumnSize(List<String> row, int rowIndex) {
      return row.size();
    }

    public String getResult() {
      return out.toString();
    }
  }

  private static String getResultString(AbstractCsvWriter<List<String>> writer) throws IOException {
    writer.close();
    return ((AbstractCsvWriterImpl) writer).getResult();
  }

  private static AbstractCsvWriter<List<String>> createWriter() {
    return new AbstractCsvWriterImpl(new StringWriter());
  }

  private static AbstractCsvWriter<List<String>> createWriter(CsvProperties prop,
      CsvWriterOptions options) {
    return new AbstractCsvWriterImpl(new StringWriter(), prop, options);
  }

  private static String readFile(String name) throws IOException {
    InputStream is = AbstractCsvWriterTest.class.getResourceAsStream("/" + name);
    try (InputStreamReader reader = new InputStreamReader(is)) {
      StringBuilder sb = new StringBuilder();
      int c;
      while ((c = reader.read()) >= 0) {
        sb.append((char) c);
      }
      return sb.toString();
    }
  }
}
