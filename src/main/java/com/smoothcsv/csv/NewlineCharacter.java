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
package com.smoothcsv.csv;

/**
 * Line feed code.
 *
 * @author kohii
 */
public enum NewlineCharacter {

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
   * Default stringValue of {@link NewlineCharacter}.
   */
  public static final NewlineCharacter DEFAULT = of(System.getProperty("line.separator"));

  private final String stringValue;

  private NewlineCharacter(String value) {
    this.stringValue = value;
  }

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
   * Returns the stringValue represented by this {@link NewlineCharacter}.
   *
   * @return a value
   */
  public String stringValue() {
    return this.stringValue;

  }

  /**
   * Returns the {@link NewlineCharacter} constant of the specified line separator string.
   *
   * @param lineSeparator line separator
   * @return {@link NewlineCharacter}
   */
  public static NewlineCharacter of(String lineSeparator) {
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
