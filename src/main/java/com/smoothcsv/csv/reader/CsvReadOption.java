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

import lombok.Value;
import lombok.experimental.Wither;

/**
 * Configurations for <code>CsvReader</code>.
 *
 * @author kohii
 */
@Value(staticConstructor = "of")
@Wither
public class CsvReadOption {

  /**
   * Default instance of {@link CsvReadOption}
   */
  public static final CsvReadOption DEFAULT = new CsvReadOption(
      false,
      false,
      false,
      false
  );

  /**
   * if true, characters outside the quotes are ignored.
   */
  private final boolean strictQuotes;

  /**
   * if true, white space in front of a quote in a field is ignored.
   */
  private final boolean ignoreLeadingWhiteSpace;

  /**
   * if true, lines that starts with <code>#</code> are ignored.
   */
  private final boolean skipCommentLines;

  /**
   * if true, empty lines are skipped.
   */
  private final boolean skipEmptyLines;
}
