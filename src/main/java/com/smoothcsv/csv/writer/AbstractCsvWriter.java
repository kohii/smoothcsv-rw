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

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import com.smoothcsv.csv.CsvProperties;
import com.smoothcsv.csv.CsvQuoteApplyRule;
import com.smoothcsv.csv.reader.CsvReaderOptions;

/**
 * Abstract CSV writer.
 *
 * @author kohii
 * @param <R> The class that holds values of one row.
 */
public abstract class AbstractCsvWriter<R> implements Closeable, Flushable {

  /**
   * Null character.
   */
  private static final char NULL_CHARACTER = '\0';

  /**
   * A character to separate each fields.
   */
  protected final char separator;

  /**
   * A character to quote a field.
   */
  protected final char quote;

  /**
   * A character to escape {@link #quote}. If this character equals {@link #NULL_CHARACTER},
   * {@link #quote} characters must be represented by a pair of {@link #quote} characters.
   */
  protected final char escape;

  /**
   * Rules how to apply quote to a value.
   */
  protected final CsvQuoteApplyRule quoteRule;

  /**
   * A writer.
   */
  private Writer out;

  /**
   * Index of the current.
   */
  private int rowIndex = 0;

  /**
   * Constructs AbstractCsvWriter using {@link CsvProperties#DEFAULT} and
   * {@link CsvReaderOptions#DEFAULT}.
   *
   * @param out A Writer
   */
  public AbstractCsvWriter(Writer out) {
    this(out, CsvProperties.DEFAULT);
  }

  /**
   * Constructs AbstractCsvWriter.
   *
   * @param out A Writer
   * @param properties CSV Properties
   */
  public AbstractCsvWriter(Writer out, CsvProperties properties) {
    this(out, properties, CsvWriterOptions.DEFAULT);
  }

  /**
   * Constructs AbstractCsvWriter.
   *
   * @param out A Writer
   * @param properties CSV Properties
   * @param options Options how to write the CSV
   */
  public AbstractCsvWriter(Writer out, CsvProperties properties, CsvWriterOptions options) {
    this.out =
        (out instanceof BufferedWriter) || (out instanceof StringWriter) ? out
            : new BufferedWriter(out);

    this.separator = properties.getDelimiter();
    this.quote = properties.getQuote();
    this.escape = properties.getEscape();

    this.quoteRule = options.getQuoteOption();
  }

  /**
   * Writes a row to the file.
   *
   * @param row Row object
   * @throws IOException
   */
  public void writeRow(R row) throws IOException {
    int columnSize = extractColumnSize(row, rowIndex);
    if (columnSize < 0) {
      return;
    }
    for (int i = 0; i < columnSize; i++) {
      if (i != 0) {
        out.write(separator);
      }
      String value = extractValue(row, rowIndex, i);
      boolean doQuote = appliesQuoting(value, rowIndex, columnSize);
      if (doQuote) {
        out.write(quote);
        writeValue(value);
        out.write(quote);
      } else {
        out.write(value);
      }
    }
    Object ls = extractLineSeparator(row, rowIndex);
    if (ls != null) {
      out.write(ls.toString());
    }
    rowIndex++;
  }

  /**
   * Writes all rows to the file.
   *
   * @param rows rows
   * @throws IOException
   */
  public void writeAll(List<R> rows) throws IOException {
    for (R row : rows) {
      writeRow(row);
    }
  }

  /**
   * Returns true if the value should be quoted.
   *
   * @param value The value of single cell.
   * @param rowIndex Index of the current row.
   * @param columnIndex Index of the current column.
   * @return if true, the value should be quoted.
   */
  protected boolean appliesQuoting(String value, int rowIndex, int columnIndex) {
    switch (quoteRule) {
      case QUOTES_ALL:
        return true;
      case QUOTES_IF_NECESSARY:
        for (int i = 0, len = value.length(); i < len; i++) {
          char c = value.charAt(i);
          if (c == separator || c == quote || c == escape || c == '\n' || c == '\r') {
            return true;
          }
        }
        return false;
      case NO_QUOTE:
      default:
        return false;
    }
  }

  /**
   * Extracts the line separator of the row.
   *
   * @param row The row object
   * @param rowIndex Index of the current row.
   * @return line separator
   */
  protected abstract Object extractLineSeparator(R row, int rowIndex);

  /**
   * Extracts the cell value in the row specified by the column index.
   *
   * @param row The row object
   * @param rowIndex Index of the current row.
   * @param columnIndex Column index in the row.
   * @return cell value
   */
  protected abstract String extractValue(R row, int rowIndex, int columnIndex);

  /**
   * Extracts the column size of the row.
   *
   * @param row The row object
   * @param rowIndex Index of the current row.
   * @return column size
   */
  protected abstract int extractColumnSize(R row, int rowIndex);

  /**
   * Writes the value of single cell.
   *
   * @param value value of the cell.
   * @throws IOException
   */
  private void writeValue(String value) throws IOException {
    int len = value.length();
    for (int i = 0; i < len; i++) {
      char c = value.charAt(i);
      if (c == quote) {
        if (escape == NULL_CHARACTER) {
          out.write(quote);
        } else {
          out.write(escape);
        }
      }
      out.write(c);
    }
  }

  @Override
  public void close() throws IOException {
    if (out == null) {
      return;
    }
    try (Writer w = out) {
      flush();
    } finally {
      out = null;
    }
  }

  @Override
  public void flush() throws IOException {
    out.flush();
  }
}
