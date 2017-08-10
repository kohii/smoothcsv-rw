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

import lombok.RequiredArgsConstructor;

/**
 * Line feed code.
 *
 * @author kohii
 */
@RequiredArgsConstructor
public enum LineSeparator {

  /**
   * line feeds (CR).
   */
  CR("\r"),
  /**
   * line feeds (LF).
   */
  LF("\n"),
  /**
   * line feeds (CR+LF).
   */
  CRLF("\r\n");

  /**
   * Default stringValue of {@link LineSeparator}.
   */
  public static final LineSeparator DEFAULT = of(System.getProperty("line.separator"));

  private final String stringValue;

  @Override
  public String toString() {
    switch (this) {
      case CRLF:
        return "CR+LF";
      case LF:
        return "LF";
      case CR:
        return "CR";
      default:
        throw new IllegalStateException();
    }
  }

  /**
   * Returns the stringValue represented by this {@link LineSeparator}.
   *
   * @return a value
   */
  public String stringValue() {
    return this.stringValue;

  }

  /**
   * Returns the {@link LineSeparator} constant of the specified line separator string.
   *
   * @param lineSeparator line separator
   * @return {@link LineSeparator}
   */
  public static LineSeparator of(String lineSeparator) {
    switch (lineSeparator) {
      case "\r\n":
        return CRLF;
      case "\r":
        return CR;
      case "\n":
        return LF;
      default:
        throw new IllegalArgumentException(lineSeparator);
    }
  }
}
