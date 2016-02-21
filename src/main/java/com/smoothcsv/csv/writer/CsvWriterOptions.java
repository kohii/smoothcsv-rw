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
package com.smoothcsv.csv.writer;

import com.smoothcsv.csv.CsvQuoteApplyRule;

/**
 * Configurations for <code>CsvReader</code>.
 *
 * @author kohii
 */
public class CsvWriterOptions {

  /**
   * Default instance of {@link CsvWriterOptions}. This instance throws
   * {@link UnsupportedOperationException} when called setter methods.
   */
  public static final CsvWriterOptions DEFAULT = new ImmutableCsvWriterOptions();

  private CsvQuoteApplyRule quoteOption = CsvQuoteApplyRule.QUOTES_ALL;

  public CsvQuoteApplyRule getQuoteOption() {
    return quoteOption;
  }

  public void setQuoteOption(CsvQuoteApplyRule quoteOption) {
    this.quoteOption = quoteOption;
  }

  private static class ImmutableCsvWriterOptions extends CsvWriterOptions {

    @Override
    public void setQuoteOption(CsvQuoteApplyRule quoteOption) {
      throw new UnsupportedOperationException();
    }
  }
}
