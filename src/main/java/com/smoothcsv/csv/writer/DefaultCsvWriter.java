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
package com.smoothcsv.csv.writer;

import java.io.Writer;
import java.util.List;

import com.smoothcsv.csv.CsvProperties;
import com.smoothcsv.csv.NewlineCharacter;

/**
 * Simple CSV Writer. This Writer writes {@link List} into the CSV.
 *
 * @author kohii
 */
public class DefaultCsvWriter extends AbstractCsvWriter<List<Object>> {

  /**
   * Constructs DefaultCsvWriter using {@link CsvProperties#DEFAULT} and
   * {@link CsvWriterOptions#DEFAULT}.
   *
   * @param out A Writer
   */
  public DefaultCsvWriter(Writer out) {
    super(out, CsvProperties.DEFAULT, CsvWriterOptions.DEFAULT);
  }

  /**
   * Constructs DefaultCsvWriter.
   *
   * @param out A Writer
   * @param properties CSV Properties
   */
  public DefaultCsvWriter(Writer out, CsvProperties properties) {
    super(out, properties, CsvWriterOptions.DEFAULT);
  }

  /**
   * Constructs DefaultCsvWriter.
   *
   * @param out A Writer
   * @param properties CSV Properties
   * @param options Options how to write the CSV
   */
  public DefaultCsvWriter(Writer out, CsvProperties properties, CsvWriterOptions options) {
    super(out, properties, options);
  }

  @Override
  protected Object extractLineSeparator(List<Object> row, int rowIndex) {
    return NewlineCharacter.DEFAULT.stringValue();
  }

  @Override
  protected String extractValue(List<Object> row, int rowIndex, int columnIndex) {
    Object val = row.get(columnIndex);
    return val == null ? "" : val.toString();
  }

  @Override
  protected int extractColumnSize(List<Object> row, int rowIndex) {
    return row.size();
  }

}
