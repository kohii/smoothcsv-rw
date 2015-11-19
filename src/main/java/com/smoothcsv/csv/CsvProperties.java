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
package com.smoothcsv.csv;

/**
 * CSV properties.
 *
 * @author kohii
 */
public class CsvProperties {

  /**
   * Default value of {@link #delimiter}
   */
  private static final char DEFAULT_DELIMITER = ',';

  /**
   * Default value of {@link #quote}
   */
  private static final char DEFAULT_QUOTE = '"';
  /**
   * Default value of {@link #escape}
   */
  private static final char DEFAULT_ESCAPE = '\0';

  /**
   * Default instance of {@link CsvProperties}. This instance throws
   * {@link UnsupportedOperationException} when called setter methods.
   */
  public static final CsvProperties DEFAULT = new ImmutableCsvProperties();

  /**
   * A character to separate each field.
   */
  private char delimiter;

  /**
   * A character to quote a field.
   */
  private char quote;

  /**
   * A character to escape quote characters. If this character equals '\0', quote characters must be
   * represented by a pair of quote characters.
   */
  private char escape;

  /**
   * Constructs CsvProperties.
   */
  public CsvProperties() {
    this(DEFAULT_DELIMITER, DEFAULT_QUOTE, DEFAULT_ESCAPE);
  }

  /**
   * Constructs CsvProperties.
   *
   * @param delimiter A character to separate each fields.
   * @param quote A character to quote a field.
   */
  public CsvProperties(char delimiter, char quote) {
    this(delimiter, quote, DEFAULT_ESCAPE);
  }

  /**
   * Constructs CsvProperties.
   *
   * @param delimiter A character to separate each fields.
   * @param quote A character to quote a field.
   * @param escape A character to escape {@link #quote}
   */
  public CsvProperties(char delimiter, char quote, char escape) {
    this.delimiter = delimiter;
    this.quote = quote;
    this.escape = escape;
  }

  public char getDelimiter() {
    return delimiter;
  }

  public void setDelimiter(char delimiter) {
    this.delimiter = delimiter;
  }

  public char getQuote() {
    return quote;
  }

  public void setQuote(char quote) {
    this.quote = quote;
  }

  public char getEscape() {
    return escape;
  }

  public void setEscape(char escape) {
    this.escape = escape;
  }

  public static class ImmutableCsvProperties extends CsvProperties {

    public ImmutableCsvProperties() {}

    public ImmutableCsvProperties(char delimiter, char quote, char escape) {
      super(delimiter, quote, escape);
    }

    @Override
    public void setDelimiter(char delimiter) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setQuote(char escape) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setEscape(char escape) {
      throw new UnsupportedOperationException();
    }

  }
}
