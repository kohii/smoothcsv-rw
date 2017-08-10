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
 * The rule to escape quote characters
 *
 * @author kohii
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QuoteEscapeRule {

  private static final QuoteEscapeRule REPEAT_QUOTE_CHAR = new QuoteEscapeRule(
      QuoteEscapeStrategy.REPEAT_QUOTE_CHAR,
      '\0'
  );

  @NonNull
  private final QuoteEscapeStrategy strategy;

  /**
   * A character to escape quote characters
   */
  private final char escapeChar;

  /**
   * Use a special character to escape quote characters
   *
   * @param escapeChar A character to escape quote characters
   * @return The instance of <code>QuoteEscapeRule</code> which corresponds to the specified escape char
   */
  public static QuoteEscapeRule escapeWith(char escapeChar) {
    if (escapeChar == '\0') {
      throw new IllegalArgumentException();
    }
    return new QuoteEscapeRule(QuoteEscapeStrategy.USE_ESCAPE_CHAR, escapeChar);
  }

  /**
   * Repeat the quote character to represent single quote character
   */
  public static QuoteEscapeRule repeatQuoteChar() {
    return REPEAT_QUOTE_CHAR;
  }
}
