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

/**
 * Indicates how to apply quote character to each cell value.
 *
 * @author kohii
 */
public enum QuoteApplyRule {

  /**
   * Suppresses all quoting.
   */
  NO_QUOTE,
  /**
   * Quotes all values.
   */
  QUOTES_ALL,
  /**
   * Quotes only when the value contains {@link CsvProperties#delimiter},
   * {@link CsvProperties#quoteChar}, or {@link QuoteEscapeRule#escapeChar}.
   */
  QUOTES_IF_NECESSARY
}
