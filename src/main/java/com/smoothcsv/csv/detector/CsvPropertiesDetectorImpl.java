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
package com.smoothcsv.csv.detector;

import com.smoothcsv.csv.CsvProperties;

/**
 *
 * @author kohii
 */
public class CsvPropertiesDetectorImpl implements CsvPropertiesDetector {

  /**
   * Null character.
   */
  private static final char NULL_CHARACTER = '\0';

  @Override
  public CsvProperties detectProperties(CharSequence line) {

    if (line.length() == 0) {
      // could not detect
      return null;
    }

    char delimiter = detectDelimiter(line);

    if (delimiter == NULL_CHARACTER) {
      // could not detect
      return null;
    }

    return detectProperties(line, delimiter);
  }

  /**
   * Detects a delimiter.
   *
   * @param line CharSequence
   * @return a delimiter char, or '\0' if could not detect
   */
  protected char detectDelimiter(CharSequence line) {
    int tabCount = 0, commaCount = 0;
    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);
      if (c == '\t') {
        tabCount++;
      } else if (c == ',') {
        commaCount++;
      }
    }
    if (tabCount > 0) {
      return '\t';
    } else if (commaCount > 0) {
      return ',';
    }
    return NULL_CHARACTER;
  }

  /**
   * Detests properties of the CSV.
   *
   * @param line CharSequence
   * @param delimiter delimiter
   * @return a quote char
   */
  public CsvProperties detectProperties(CharSequence line, char delimiter) {
    int length = line.length(), i = 0;
    if (length > 0 && line.charAt(0) == 0xFEFF) {
      // if the first character equals UTF-8 BOM, we skip the character.
      i++;
    }

    char escape = NULL_CHARACTER;
    char quote = '"';
    // CsvQuoteApplyRule quoteApplyRule = CsvQuoteApplyRule.QUOTES_ALL;

    @SuppressWarnings("unused")
    int fieldCount = 0;
    int doubleQuoteCount = 0;
    int singleQuoteCount = 0;
    int noQuoteCount = 0;
    char fieldStartingChar = NULL_CHARACTER;

    char prev, c = NULL_CHARACTER, next = line.charAt(i);
    for (; i < length; i++) {
      prev = c;
      c = next;
      next = i + 1 < length ? line.charAt(i + 1) : NULL_CHARACTER;

      if (prev == NULL_CHARACTER && c == '"' || c == '\'') {
        fieldStartingChar = c;
        continue;
      }

      if (c == delimiter) {
        if (fieldStartingChar == NULL_CHARACTER) {
          fieldCount++;
          noQuoteCount++;
        } else if (fieldStartingChar == prev) {
          fieldCount++;
          if (prev == '"') {
            doubleQuoteCount++;
          } else if (prev == '\'') {
            singleQuoteCount++;
          }
          i++;
        }

        if (next == '"' || next == '\'') {
          fieldStartingChar = next;
          i++;
        } else {
          fieldStartingChar = NULL_CHARACTER;
        }
      } else if (c == '"' || c == '\'') {
        if (fieldStartingChar == c) {
          if (prev == '\\') {
            escape = '\\';
          } else if (next == c) {
            // escaped.
            i++;
          }
        }
      }
    }

    if (doubleQuoteCount > 0) {
      quote = '"';
    } else if (singleQuoteCount > 0) {
      quote = '\'';
    } else if (noQuoteCount > 0) {
      quote = NULL_CHARACTER;
    }

    CsvProperties properties = new CsvProperties();
    properties.setDelimiter(delimiter);
    properties.setQuote(quote);
    properties.setEscape(escape);
    return properties;
  }
}
