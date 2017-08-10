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
package com.smoothcsv.csv.prop;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

/**
 * CSV properties.
 *
 * @author kohii
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CsvProperties {

  private static final char NULL_CHAR = '\0';

  /**
   * Default value of {@link #delimiter}
   */
  private static final char DEFAULT_DELIMITER = ',';

  /**
   * Default instance of {@link CsvProperties}
   */
  public static final CsvProperties DEFAULT = new CsvProperties(
      DEFAULT_DELIMITER,
      '"',
      QuoteEscapeRule.repeatQuoteChar()
  );

  /**
   * A character to separate each field.
   */
  private final char delimiter;

  /**
   * A character to quoteChar a field.
   */
  private final char quoteChar;

  /**
   * The rule to escape quoteChar characters.
   */
  @NonNull
  private final QuoteEscapeRule quoteEscapeRule;

  /**
   * @param delimiter A character to separate each field
   * @return The instance of <code>CsvProperties</code> which corresponds to the specified delimiter
   */
  public static CsvProperties of(char delimiter) {
    return new CsvProperties(delimiter, NULL_CHAR, QuoteEscapeRule.repeatQuoteChar());
  }

  /**
   * @param delimiter       delimiter A character to separate each field
   * @param quoteChar       A character to quoteChar a field
   * @param quoteEscapeRule The rule to escape quoteChar characters
   * @return The instance of <code>CsvProperties</code> which corresponds to the specified parameters
   */
  public static CsvProperties of(char delimiter, char quoteChar, QuoteEscapeRule quoteEscapeRule) {
    return new CsvProperties(delimiter, quoteChar, quoteEscapeRule);
  }

  /**
   * @return True if quotes each cell value
   */
  public boolean quotesCellValues() {
    return quoteChar == NULL_CHAR;
  }
}
