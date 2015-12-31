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
package com.smoothcsv.csv.reader;

/**
 * Configurations for <code>CsvReader</code>.
 *
 * @author kohii
 */
public class CsvReaderOptions {

  /**
   * Default instance of {@link CsvReaderOptions}. This instance throws
   * {@link UnsupportedOperationException} when called setter methods.
   */
  public static final CsvReaderOptions DEFAULT = new ImmutableCsvReaderOptions();

  /**
   * if true, characters outside the quotes are ignored.
   */
  private boolean strictQuotes = false;

  /**
   * if true, white space in front of a quote in a field is ignored.
   */
  private boolean ignoreLeadingWhiteSpace = false;

  /**
   * if true, lines that starts with <code>#</code> are ignored.
   */
  private boolean skipCommentLines = false;

  /**
   * if true, empty lines are skipped.
   */
  private boolean skipEmptyLines = false;

  public boolean isStrictQuotes() {
    return strictQuotes;
  }

  public void setStrictQuotes(boolean strictQuotes) {
    this.strictQuotes = strictQuotes;
  }

  public boolean isIgnoreLeadingWhiteSpace() {
    return ignoreLeadingWhiteSpace;
  }

  public void setIgnoreLeadingWhiteSpace(boolean ignoreLeadingWhiteSpace) {
    this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
  }

  public boolean isSkipCommentLines() {
    return skipCommentLines;
  }

  public void setSkipCommentLines(boolean skipCommentLines) {
    this.skipCommentLines = skipCommentLines;
  }

  public boolean isSkipEmptyLines() {
    return skipEmptyLines;
  }

  public void setSkipEmptyLines(boolean skipEmptyLines) {
    this.skipEmptyLines = skipEmptyLines;
  }

  private static class ImmutableCsvReaderOptions extends CsvReaderOptions {

    @Override
    public void setStrictQuotes(boolean strictQuotes) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setIgnoreLeadingWhiteSpace(boolean ignoreLeadingWhiteSpace) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setSkipCommentLines(boolean skipCommentLines) {
      throw new UnsupportedOperationException();
    }
  }
}
