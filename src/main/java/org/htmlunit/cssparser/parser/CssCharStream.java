/* Generated By:JavaCC: Do not edit this line. SimpleCharStream.java Version 5.0 */
/* JavaCCOptions:STATIC=false,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.htmlunit.cssparser.parser;

import org.htmlunit.cssparser.parser.javacc.CharStream;

/**
 * An implementation of interface CharStream.
 * There is no processing of escaping in this class because the escaping is
 * part of the parser. CSS has some strange rules about that, so processing
 * unicode escapes in this class is too early.
 */
@SuppressWarnings("all")
public final class CssCharStream implements CharStream
{
  /** Whether parser is static. */
  public static final boolean staticFlag = false;

  private static final int BUFFER_SIZE = 2048;

  int bufsize;
  int available;
  int tokenBegin;
  /** Position in buffer. */
  public int bufpos = -1;
  private int bufline[];
  private int bufcolumn[];

  private int column = 0;
  private int line = 1;

  private boolean prevCharIsCR = false;
  private boolean prevCharIsLF = false;

  private java.io.Reader inputStream;

  private char[] buffer;
  private int maxNextCharInd = 0;
  private int inBuf = 0;

  private int tabSize = 1;
  private boolean trackLineColumn = true;

  private void ExpandBuff(boolean wrapAround)
  {
    char[] newbuffer = new char[bufsize + BUFFER_SIZE];
    int newbufline[] = new int[bufsize + BUFFER_SIZE];
    int newbufcolumn[] = new int[bufsize + BUFFER_SIZE];

    try
    {
      if (wrapAround)
      {
        System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize - tokenBegin);
        System.arraycopy(buffer, 0, newbuffer, bufsize - tokenBegin, bufpos);
        buffer = newbuffer;

        System.arraycopy(bufline, tokenBegin, newbufline, 0, bufsize - tokenBegin);
        System.arraycopy(bufline, 0, newbufline, bufsize - tokenBegin, bufpos);
        bufline = newbufline;

        System.arraycopy(bufcolumn, tokenBegin, newbufcolumn, 0, bufsize - tokenBegin);
        System.arraycopy(bufcolumn, 0, newbufcolumn, bufsize - tokenBegin, bufpos);
        bufcolumn = newbufcolumn;

        maxNextCharInd = (bufpos += (bufsize - tokenBegin));
      }
      else
      {
        System.arraycopy(buffer, tokenBegin, newbuffer, 0, bufsize - tokenBegin);
        buffer = newbuffer;

        System.arraycopy(bufline, tokenBegin, newbufline, 0, bufsize - tokenBegin);
        bufline = newbufline;

        System.arraycopy(bufcolumn, tokenBegin, newbufcolumn, 0, bufsize - tokenBegin);
        bufcolumn = newbufcolumn;

        maxNextCharInd = (bufpos -= tokenBegin);
      }
    }
    catch (Throwable t)
    {
      throw new Error(t.getMessage());
    }

    bufsize += BUFFER_SIZE;
    available = bufsize;
    tokenBegin = 0;
  }

  private final void FillBuff() throws java.io.IOException
  {
    if (maxNextCharInd == available)
    {
      if (available == bufsize)
      {
        if (tokenBegin > BUFFER_SIZE)
        {
          bufpos = maxNextCharInd = 0;
          available = tokenBegin;
        }
        else if (tokenBegin < 0)
          bufpos = maxNextCharInd = 0;
        else
          ExpandBuff(false);
      }
      else if (available > tokenBegin)
        available = bufsize;
      else if ((tokenBegin - available) < BUFFER_SIZE)
        ExpandBuff(true);
      else
        available = tokenBegin;
    }

    int i;
    try {
      if ((i = inputStream.read(buffer, maxNextCharInd, available - maxNextCharInd)) == -1)
      {
        inputStream.close();
        throw new java.io.IOException();
      }

      maxNextCharInd += i;
      return;
    }
    catch(java.io.IOException e) {
      --bufpos;
      backup(0);
      if (tokenBegin == -1)
        tokenBegin = bufpos;
      throw e;
    }
  }

  /** Start. */
  @Override
public final char beginToken() throws java.io.IOException
  {
    tokenBegin = -1;
    char c = readChar();
    tokenBegin = bufpos;

    return c;
  }

  private final void UpdateLineColumn(char c)
  {
    column++;

    if (prevCharIsLF)
    {
      prevCharIsLF = false;
      line += (column = 1);
    }
    else if (prevCharIsCR)
    {
      prevCharIsCR = false;
      if (c == '\n')
      {
        prevCharIsLF = true;
      }
      else
        line += (column = 1);
    }

    switch (c)
    {
      case '\r' :
        prevCharIsCR = true;
        break;
      case '\n' :
        prevCharIsLF = true;
        break;
//        case '\t' :
//            column--;
//            column += (8 - (column & 07));
//            break;
      default :
        break;
    }

    bufline[bufpos] = line;
    bufcolumn[bufpos] = column;
  }

/**
 * {@inheritDoc}
 *
 * Read a character.
 */
  @Override
public final char readChar() throws java.io.IOException
  {
    if (inBuf > 0)
    {
      --inBuf;

      if (++bufpos == bufsize)
        bufpos = 0;

      return buffer[bufpos];
    }

    if (++bufpos >= maxNextCharInd)
      FillBuff();

    char c = buffer[bufpos];

    UpdateLineColumn(c);
    return c;
  }

/**
 * {@inheritDoc}
 *
 * Get token end column number.
 */
  @Override
public final int getEndColumn() {
    return bufcolumn[bufpos];
  }

/**
 * {@inheritDoc}
 *
 * Get token end line number.
 */
  @Override
public final int getEndLine() {
    return bufline[bufpos];
  }

/**
 * {@inheritDoc}
 *
 * Get token beginning column number.
 */
  @Override
public final int getBeginColumn() {
    return bufcolumn[tokenBegin];
  }

/**
 * {@inheritDoc}
 *
 * Get token beginning line number.
 */
  @Override
public final int getBeginLine() {
    return bufline[tokenBegin];
  }

/**
 * {@inheritDoc}
 *
 * Backup a number of characters.
 */
  @Override
public final void backup(int amount) {
    inBuf += amount;
    if ((bufpos -= amount) < 0)
      bufpos += bufsize;
  }

  /**
   * Constructor.
   * @param dstream the stream to read from
   * @param startline startline
   * @param startcolumn startcolumn
   * @param buffersize buffersize
   */
  public CssCharStream(java.io.Reader dstream, int startline, int startcolumn, int buffersize)
  {
    inputStream = dstream;
    line = startline;
    column = startcolumn - 1;

    available = bufsize = buffersize;
    buffer = new char[buffersize];
    bufline = new int[buffersize];
    bufcolumn = new int[buffersize];
  }

  /**
   * Constructor.
   * @param dstream the stream to read from
   * @param startline startline
   * @param startcolumn startcolumn
   */
  public CssCharStream(java.io.Reader dstream, int startline, int startcolumn)
  {
    this(dstream, startline, startcolumn, 4096);
  }

/**
 * {@inheritDoc}
 *
 * Get token literal value.
 */
  @Override
public final String getImage()
  {
    if (bufpos >= tokenBegin)
      return new String(buffer, tokenBegin, bufpos - tokenBegin + 1);
    return new String(buffer, tokenBegin, bufsize - tokenBegin) + new String(buffer, 0, bufpos + 1);
  }

/**
 * {@inheritDoc}
 *
 * Get the suffix.
 */
  @Override
public final char[] getSuffix(int len)
  {
    char[] ret = new char[len];

    if ((bufpos + 1) >= len)
      System.arraycopy(buffer, bufpos - len + 1, ret, 0, len);
    else
    {
      System.arraycopy(buffer, bufsize - (len - bufpos - 1), ret, 0, len - bufpos - 1);
      System.arraycopy(buffer, 0, ret, len - bufpos - 1, bufpos + 1);
    }

    return ret;
  }

/**
 * {@inheritDoc}
 *
 * Reset buffer when finished.
 */
  @Override
public void done()
  {
    buffer = null;
    bufline = null;
    bufcolumn = null;
  }

  /**
   * Method to adjust line and column numbers for the start of a token.
   * @param newLine the new line
   * @param newCol the new column
   */
  public void adjustBeginLineColumn(int newLine, int newCol)
  {
    int start = tokenBegin;
    int len;

    if (bufpos >= tokenBegin)
    {
      len = bufpos - tokenBegin + inBuf + 1;
    }
    else
    {
      len = bufsize - tokenBegin + bufpos + 1 + inBuf;
    }

    int i = 0, j = 0, k = 0;
    int nextColDiff = 0, columnDiff = 0;

    while (i < len && bufline[j = start % bufsize] == bufline[k = ++start % bufsize])
    {
      bufline[j] = newLine;
      nextColDiff = columnDiff + bufcolumn[k] - bufcolumn[j];
      bufcolumn[j] = newCol + columnDiff;
      columnDiff = nextColDiff;
      i++;
    }

    if (i < len)
    {
      bufline[j] = newLine++;
      bufcolumn[j] = newCol + columnDiff;

      while (i++ < len)
      {
        if (bufline[j = start % bufsize] != bufline[++start % bufsize])
          bufline[j] = newLine++;
        else
          bufline[j] = newLine;
      }
    }

    line = bufline[j];
    column = bufcolumn[j];
  }

  /** {@inheritDoc} */
  @Override
public void setTabSize(int i) {
      tabSize = i;
  }

  /** {@inheritDoc} */
  @Override
public int getTabSize() {
      return tabSize;
  }

  /** {@inheritDoc} */
  @Override
public boolean isTrackLineColumn() {
      return trackLineColumn;
  }

  /** {@inheritDoc} */
  @Override
public void setTrackLineColumn(boolean tlc) {
      trackLineColumn = tlc;
  }
}