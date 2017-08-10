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
 * Defines the strategy to escape quote characters inside cell values
 *
 * @author kohii
 */
public enum QuoteEscapeStrategy {
  /**
   * Repeat the quote character to represent single quote character
   */
  REPEAT_QUOTE_CHAR,
  /**
   * Use a special character to escape quote characters
   */
  USE_ESCAPE_CHAR
}
