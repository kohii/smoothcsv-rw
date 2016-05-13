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

import com.smoothcsv.csv.CsvProperties;
import com.smoothcsv.csv.NewlineCharacter;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple CSV Reader. This Reader reads CSV lines and parses into {@link List}.
 *
 * @author kohii
 */
public class DefaultCsvReader extends AbstractCsvReader<List<String>> {

  /**
   * Constructs DefaultCsvReader using {@link CsvProperties#DEFAULT} and
   * {@link CsvReaderOptions#DEFAULT}.
   *
   * @param in A Reader
   */
  public DefaultCsvReader(Reader in) {
    super(in, CsvProperties.DEFAULT, CsvReaderOptions.DEFAULT);
  }

  /**
   * Constructs DefaultCsvReader.
   *
   * @param in         A Reader
   * @param properties CSV Properties
   */
  public DefaultCsvReader(Reader in, CsvProperties properties) {
    super(in, properties, CsvReaderOptions.DEFAULT);
  }

  /**
   * Constructs DefaultCsvReader.
   *
   * @param in         A Reader
   * @param properties CSV Properties
   * @param options    Options how to read the CSV
   */
  public DefaultCsvReader(Reader in, CsvProperties properties, CsvReaderOptions options) {
    super(in, properties, options);
  }

  @Override
  protected List<String> createNewRow(int rowIndex) {
    return new ArrayList<>(Math.max(0, getMaxColumnCount()));
  }

  @Override
  protected void handleValue(List<String> row, int rowIndex, int columnIndex, String data) {
    row.add(data);
  }

  @Override
  protected void handleLineSeparator(List<String> row, int rowIndex, NewlineCharacter lineFeedCode) {
    // do nothing
  }
}
