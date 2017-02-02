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

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.smoothcsv.csv.CsvProperties;
import com.smoothcsv.csv.NewlineCharacter;

/**
 * Abstract CSV reader.
 *
 * @param <R> The class that holds values of one row.
 * @author kohii
 */
public abstract class AbstractCsvReader<R> implements Closeable {

  /**
   * Default input-buffer size.
   */
  private static final int DEFAULT_CHAR_BUFFER_SIZE = 8192;

  /**
   * Null character.
   */
  private static final char NULL_CHARACTER = '\0';

  /**
   * A character to separate each fields.
   */
  protected final char separator;

  /**
   * A character to quote a field.
   */
  protected final char quote;

  /**
   * A character to escape {@link #quote}. If this character equals {@link #NULL_CHARACTER},
   * {@link #quote} characters must be represented by a pair of {@link #quote} characters.
   */
  protected final char escape;

  /**
   * if true, characters outside the quotes are ignored.
   */
  protected final boolean strictQuotes;

  /**
   * if true, white space in front of a quote in a field is ignored.
   */
  protected final boolean ignoreLeadingWhiteSpace;

  /**
   * if true, lines that starts with <code>#</code> are skipped.
   */
  protected final boolean skipCommentLines;

  /**
   * if true, empty lines are skipped.
   */
  protected final boolean skipEmptyLines;

  /**
   * Maximum count of columns read by this reader.
   */
  private int minColumnCount;

  /**
   * Minimum count of columns read by this reader.
   */
  private int maxColumnCount;

  private Reader in;

  private char[] cb;
  private int nChars, nextChar;
  private int rowIndex;

  /**
   * Constructs AbstractCsvReader.
   *
   * @param in         A Reader
   * @param properties CSV Properties
   * @param options    Options how to read the CSV
   */
  public AbstractCsvReader(Reader in, CsvProperties properties, CsvReaderOptions options) {

    this.separator = properties.getDelimiter();
    this.quote = properties.getQuote();
    this.escape = properties.getEscape();

    this.strictQuotes = options.isStrictQuotes();
    this.ignoreLeadingWhiteSpace = options.isIgnoreLeadingWhiteSpace();
    this.skipCommentLines = options.isSkipCommentLines();
    this.skipEmptyLines = options.isSkipEmptyLines();

    setupReader(in);
  }

  /**
   * @return Current row index.
   */
  public int getRowIndex() {
    return rowIndex;
  }

  /**
   * @return Maximum number of columns that read by this reader
   */
  public int getMaxColumnCount() {
    return maxColumnCount;
  }

  /**
   * @return Minimum number of columns that read by this reader
   */
  public int getMinColumnCount() {
    return minColumnCount;
  }

  /**
   * Reads values in one row.
   *
   * @return Object containing the values of the row, or null if the end of the stream has been reached
   * @throws IOException If an I/O error occurs
   */
  public R readRow() throws IOException {
    ensureOpen();
    if (nextChar >= nChars) {
      readCharactersToBuffer();
    }
    if (nextChar >= nChars) {
      // EOF
      return null;
    }
    if (rowIndex == 0 && cb[0] == 0xFEFF) {
      // if the first character equals UTF-8 BOM, we skip the character.
      nextChar++;
    }
    if (skipCommentLines && cb[nextChar] == '#') {
      // if the first character of the line equals '#', we skip the line.
      skipRow();
      return readRow();
    }
    StringBuilder sb = new StringBuilder(128);
    boolean isEmptyLine = cb[nextChar] == '\n' || cb[nextChar] == '\r';
    R rowData = createNewRow(rowIndex);
    NewlineCharacter lineFeedCode = null;
    boolean inQuotes = false;
    boolean inField = false;
    boolean skipNext = false;
    int columnCount = 0;
    char prev, c = NULL_CHARACTER, next = cb[nextChar];
    int i = nextChar;
    for (; i < nChars; i++) {
      prev = c;
      c = next;
      if (i + 1 < nChars) {
        next = cb[i + 1];
      } else {
        if (readCharactersToBuffer()) {
          next = cb[nextChar];
          i = nextChar - 1;
        } else {
          next = NULL_CHARACTER;
        }
      }
      if (skipNext) {
        skipNext = false;
        continue;
      }
      if (c == this.escape) {
        if (isNextCharacterEscapable(next, inQuotes || inField)) {
          sb.append(next);
          skipNext = true;
        }
      } else if (c == quote) {
        if (isNextCharacterEscapedQuote(next, inQuotes || inField)) {
          sb.append(next);
          skipNext = true;
        } else {

          // the tricky case of an embedded quote in the middle: a,bc"d"ef,g
          if (!inQuotes && !strictQuotes) {
            if (prev != NULL_CHARACTER // not on the beginning of the line
                && prev != this.separator // not at the beginning of an escape sequence
                && (next != '\r' && next != '\n' && next != this.separator) // not at the end of an escape sequence
                ) {
              if (ignoreLeadingWhiteSpace && sb.length() > 0 && isAllWhiteSpace(sb)) {
                sb.setLength(0); // discard white space leading up to quote
              } else {
                sb.append(c);
                // continue;
              }
            }
          }

          inQuotes = !inQuotes;
        }
        inField = !inField;
      } else if (c == separator && !inQuotes) {
        String s = sb.length() == 0 ? "" : sb.toString();
        handleValue(rowData, rowIndex, columnCount++, s);
        sb.setLength(0); // start work on next token
        inField = false;
      } else if (c == '\r' && !inQuotes) {
        if (next == '\n') {
          i++;
          lineFeedCode = NewlineCharacter.CRLF;
        } else {
          lineFeedCode = NewlineCharacter.CR;
        }
        break; // EOL
      } else if (c == '\n' && !inQuotes) {
        lineFeedCode = NewlineCharacter.LF;
        break; // EOL
      } else {
        if (!strictQuotes || inQuotes) {
          sb.append(c);
          inField = true;
        }
      }
    }

    nextChar = i + 1;
    if (isEmptyLine) {
      if (skipEmptyLines) {
        return readRow();
      } else {
        // keep row empty
      }
    } else {
      String s = sb.length() == 0 ? "" : sb.toString();
      handleValue(rowData, rowIndex, columnCount++, s);
    }

    handleLineSeparator(rowData, rowIndex, lineFeedCode);

    minColumnCount = minColumnCount == -1 ? columnCount : Math.min(minColumnCount, columnCount);
    maxColumnCount = maxColumnCount == -1 ? columnCount : Math.max(maxColumnCount, columnCount);
    rowIndex++;

    return rowData;
  }

  /**
   * Skips rows.
   *
   * @param n The number of rows to skip
   * @return The number of rows actually skipped
   * @throws IOException
   */
  public int skipRows(int n) throws IOException {
    if (n < 0) {
      throw new IllegalArgumentException("skipLines value is negative");
    }
    int skiped = 0;
    for (int i = 0; i < n; i++) {
      if (skipRow()) {
        skiped++;
      } else {
        break;
      }
    }
    return skiped;
  }

  /**
   * Skips one row.
   *
   * @return True if a row actually skipped.
   * @throws IOException
   */
  private boolean skipRow() throws IOException {
    ensureOpen();
    if (nextChar >= nChars) {
      readCharactersToBuffer();
    }
    if (nextChar >= nChars) {
      // EOF
      return false;
    }
    do {
      boolean isPrevCR = false;
      for (int i = nextChar; i < nChars; i++) {
        char c = cb[i];
        if (isPrevCR) {
          if (c == '\n') {
            nextChar = i + 1;
            rowIndex++;
            return true;
          } else {
            nextChar = i;
            rowIndex++;
            return true;
          }
        } else {
          if (c == '\r') {
            isPrevCR = true;
          } else if (c == '\n') {
            nextChar = i + 1;
            rowIndex++;
            return true;
          }
        }
      }
    } while (readCharactersToBuffer());
    return false;
  }

  @Override
  public void close() throws IOException {
    if (in == null) {
      return;
    }
    try {
      in.close();
    } finally {
      in = null;
      cb = null;
    }
  }

  /**
   * Reads all CSV text and returns list of rows.
   *
   * @return
   * @throws IOException
   */
  public List<R> readAll() throws IOException {
    List<R> rows = new ArrayList<>();
    R r;
    while ((r = readRow()) != null) {
      rows.add(r);
    }
    return rows;
  }

  /**
   * Returns a {@code Stream}, the elements of which are lines read from this {@code CsvReader}. The
   * {@link Stream} is lazily populated, i.e., read only occurs during the
   * <a href="../util/stream/package-summary.html#StreamOps">terminal stream operation</a>.
   * <p>
   * <p>
   * The reader must not be operated on during the execution of the terminal stream operation.
   * Otherwise, the result of the terminal stream operation is undefined.
   * </p>
   * <p>
   * After execution of the terminal stream operation there are no guarantees that the reader will
   * be at a specific position from which to read the next character or line.
   * </p>
   * <p>
   * If an {@link IOException} is thrown when accessing the underlying {@code BufferedReader}, it is
   * wrapped in an {@link UncheckedIOException} which will be thrown from the {@code Stream} method
   * that caused the read to take place. This method will return a Stream if invoked on a
   * BufferedReader that is closed. Any operation on that stream that requires reading from the
   * BufferedReader after it is closed, will cause an UncheckedIOException to be thrown.
   *
   * @return a {@code Stream<List<String>>} providing the lines of text described by this
   * {@code BufferedReader}
   */
  public Stream<R> rows() {
    Iterator<R> iter = new Iterator<R>() {
      R nextLine = null;

      @Override
      public boolean hasNext() {
        if (nextLine != null) {
          return true;
        } else {
          try {
            nextLine = readRow();
            return (nextLine != null);
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }
        }
      }

      @Override
      public R next() {
        if (nextLine != null || hasNext()) {
          R line = nextLine;
          nextLine = null;
          return line;
        } else {
          throw new NoSuchElementException();
        }
      }
    };
    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(iter, Spliterator.ORDERED | Spliterator.NONNULL),
        false);
  }

  /**
   * @return Input-buffer size
   */
  protected int charBufferSize() {
    return DEFAULT_CHAR_BUFFER_SIZE;
  }

  /**
   * Creates and returns object that holds values of one row.
   *
   * @param rowIndex current row index
   * @return Object that holds values of one row.
   */
  protected abstract R createNewRow(int rowIndex);

  /**
   * Handles a value.
   *
   * @param row
   * @param rowIndex    current row index
   * @param columnIndex
   * @param value
   */
  protected abstract void handleValue(R row, int rowIndex, int columnIndex, String value);

  /**
   * Handles a line feed character.
   *
   * @param row
   * @param rowIndex     current row index
   * @param lineFeedCode
   */
  protected void handleLineSeparator(R row, int rowIndex, NewlineCharacter lineFeedCode) {
    // do nothing
  }

  /**
   * precondition: the current character is an escape
   *
   * @param next     the next character
   * @param inQuotes true if the current context is quoted
   * @return true if the following character is a quote
   */
  private boolean isNextCharacterEscapable(char next, boolean inQuotes) {
    return inQuotes // we are in quotes, therefore there can be escaped quotes in here.
        && next != NULL_CHARACTER // there is indeed another character to check.inQuotes // we are
        // in quotes, therefore there can be escaped quotes in here.
        && next != NULL_CHARACTER // there is indeed another character to check.
        && (next == quote || next == this.escape);
  }

  /**
   * precondition: the current character is a quote or an escape
   *
   * @param next     the next character
   * @param inQuotes true if the current context is quoted
   * @return true if the following character is a quote
   */
  private boolean isNextCharacterEscapedQuote(char next, boolean inQuotes) {
    return inQuotes // we are in quotes, therefore there can be escaped quotes in here.
        && next == quote;
  }

  /**
   * precondition: sb.length() > 0
   *
   * @param sb A sequence of characters to examine
   * @return true if every character in the sequence is whitespace
   */
  private boolean isAllWhiteSpace(CharSequence sb) {
    boolean result = true;
    for (int i = 0; i < sb.length(); i++) {
      char c = sb.charAt(i);

      if (!Character.isWhitespace(c)) {
        return false;
      }
    }
    return result;
  }

  /**
   * Fills the input buffer, taking the mark into account if it is valid.
   *
   * @return true if more than one character read;
   * @throws IOException
   */
  private boolean readCharactersToBuffer() throws IOException {
    nChars = in.read(cb);
    nextChar = 0;
    return nChars >= 0;
  }

  /**
   * Checks to make sure that the stream has not been closed
   */
  private void ensureOpen() throws IOException {
    if (in == null) {
      throw new IOException("Stream closed");
    }
  }

  /**
   * Sets up reader.
   *
   * @param in A Reader
   */
  protected void setupReader(Reader in) {
    this.in = in;
    this.cb = new char[charBufferSize()];
    this.nextChar = this.nChars = 0;
    this.rowIndex = 0;

    this.minColumnCount = -1;
    this.maxColumnCount = -1;
  }
}
