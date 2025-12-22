/*
 * Copyright (c) 2019-2024 Ronald Brill.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.cssparser.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CssCharStream}.
 *
 * @author Ronald Brill
 */
public class CssCharStreamTest {

    /**
     * Test basic character reading.
     * @throws Exception if any error occurs
     */
    @Test
    public void readChar() throws Exception {
        final String input = "abc";
        final CssCharStream stream = new CssCharStream(new StringReader(input), 1, 1);
        
        assertEquals('a', stream.readChar());
        assertEquals('b', stream.readChar());
        assertEquals('c', stream.readChar());
        
        assertThrows(IOException.class, () -> stream.readChar());
    }

    /**
     * Test beginToken functionality.
     * @throws Exception if any error occurs
     */
    @Test
    public void beginToken() throws Exception {
        final String input = "test";
        final CssCharStream stream = new CssCharStream(new StringReader(input), 1, 1);
        
        final char firstChar = stream.beginToken();
        assertEquals('t', firstChar);
        assertEquals('e', stream.readChar());
    }

    /**
     * Test getImage for simple token.
     * @throws Exception if any error occurs
     */
    @Test
    public void getImage() throws Exception {
        final String input = "token";
        final CssCharStream stream = new CssCharStream(new StringReader(input), 1, 1);
        
        stream.beginToken(); // reads 't'
        stream.readChar(); // 'o'
        stream.readChar(); // 'k'
        stream.readChar(); // 'e'
        stream.readChar(); // 'n'
        
        assertEquals("token", stream.getImage());
    }

    /**
     * Test backup functionality.
     * @throws Exception if any error occurs
     */
    @Test
    public void backup() throws Exception {
        final String input = "abcd";
        final CssCharStream stream = new CssCharStream(new StringReader(input), 1, 1);
        
        char c = stream.beginToken(); // reads 'a'
        assertEquals('a', c);
        assertEquals('b', stream.readChar());
        assertEquals('c', stream.readChar());
        
        stream.backup(1);
        assertEquals('c', stream.readChar());
        assertEquals('d', stream.readChar());
    }

    /**
     * Test line and column tracking.
     * @throws Exception if any error occurs
     */
    @Test
    public void lineColumnTracking() throws Exception {
        final String input = "a\nb\nc";
        final CssCharStream stream = new CssCharStream(new StringReader(input), 1, 1);
        
        char c = stream.beginToken(); // reads 'a'
        assertEquals('a', c);
        assertEquals(1, stream.getBeginLine());
        assertEquals(1, stream.getBeginColumn());
        assertEquals(1, stream.getEndLine());
        assertEquals(1, stream.getEndColumn());
        
        stream.readChar(); // '\n'
        assertEquals(1, stream.getEndLine());
        assertEquals(2, stream.getEndColumn());
        
        stream.readChar(); // 'b'
        assertEquals(2, stream.getEndLine());
        assertEquals(1, stream.getEndColumn());
    }

    /**
     * Test carriage return handling.
     * @throws Exception if any error occurs
     */
    @Test
    public void carriageReturnHandling() throws Exception {
        final String input = "a\rb\r\nc";
        final CssCharStream stream = new CssCharStream(new StringReader(input), 1, 1);
        
        stream.beginToken(); // 'a'
        stream.readChar(); // '\r'
        assertEquals(1, stream.getEndLine());
        
        stream.readChar(); // 'b'
        assertEquals(2, stream.getEndLine());
        
        stream.readChar(); // '\r'
        stream.readChar(); // '\n'
        assertEquals(2, stream.getEndLine());
        
        stream.readChar(); // 'c'
        assertEquals(3, stream.getEndLine());
    }

    /**
     * Test getSuffix functionality.
     * @throws Exception if any error occurs
     */
    @Test
    public void getSuffix() throws Exception {
        final String input = "testing";
        final CssCharStream stream = new CssCharStream(new StringReader(input), 1, 1);
        
        stream.beginToken(); // 't'
        for (int i = 0; i < 6; i++) {
            stream.readChar();
        }
        
        final char[] suffix = stream.getSuffix(3);
        assertEquals(3, suffix.length);
        assertEquals('i', suffix[0]);
        assertEquals('n', suffix[1]);
        assertEquals('g', suffix[2]);
    }

    /**
     * Test custom buffer size constructor.
     * @throws Exception if any error occurs
     */
    @Test
    public void customBufferSize() throws Exception {
        final String input = "test";
        final CssCharStream stream = new CssCharStream(new StringReader(input), 1, 1, 10);
        
        char c = stream.beginToken(); // reads 't'
        assertEquals('t', c);
        assertEquals('e', stream.readChar());
        assertEquals('s', stream.readChar());
    }

    /**
     * Test done() method.
     * @throws Exception if any error occurs
     */
    @Test
    public void done() throws Exception {
        final String input = "test";
        final CssCharStream stream = new CssCharStream(new StringReader(input), 1, 1);
        
        stream.beginToken();
        stream.readChar();
        
        stream.done();
        // After done, internal buffers are cleared
    }

    /**
     * Test adjustBeginLineColumn.
     * @throws Exception if any error occurs
     */
    @Test
    public void adjustBeginLineColumn() throws Exception {
        final String input = "test\nline";
        final CssCharStream stream = new CssCharStream(new StringReader(input), 1, 1);
        
        stream.beginToken();
        stream.readChar();
        stream.readChar();
        
        stream.adjustBeginLineColumn(5, 10);
        assertEquals(5, stream.getBeginLine());
        assertEquals(10, stream.getBeginColumn());
    }

    /**
     * Test tab size get/set.
     * @throws Exception if any error occurs
     */
    @Test
    public void tabSize() throws Exception {
        final String input = "test";
        final CssCharStream stream = new CssCharStream(new StringReader(input), 1, 1);
        
        assertEquals(1, stream.getTabSize());
        
        stream.setTabSize(8);
        assertEquals(8, stream.getTabSize());
    }

    /**
     * Test trackLineColumn get/set.
     * @throws Exception if any error occurs
     */
    @Test
    public void trackLineColumn() throws Exception {
        final String input = "test";
        final CssCharStream stream = new CssCharStream(new StringReader(input), 1, 1);
        
        assertTrue(stream.isTrackLineColumn());
        
        stream.setTrackLineColumn(false);
        assertFalse(stream.isTrackLineColumn());
    }

    /**
     * Test buffer expansion with large input.
     * @throws Exception if any error occurs
     */
    @Test
    public void bufferExpansion() throws Exception {
        final StringBuilder largeInput = new StringBuilder();
        for (int i = 0; i < 5000; i++) {
            largeInput.append('x');
        }
        
        final CssCharStream stream = new CssCharStream(new StringReader(largeInput.toString()), 1, 1, 100);
        
        char c = stream.beginToken(); // reads first 'x'
        assertEquals('x', c);
        for (int i = 0; i < 4999; i++) {
            assertEquals('x', stream.readChar());
        }
    }

    /**
     * Test empty input.
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyInput() throws Exception {
        final CssCharStream stream = new CssCharStream(new StringReader(""), 1, 1);
        
        assertThrows(IOException.class, () -> stream.readChar());
    }

    /**
     * Test getImage with wrap around buffer.
     * @throws Exception if any error occurs
     */
    @Test
    public void getImageWrapAround() throws Exception {
        final StringBuilder input = new StringBuilder();
        for (int i = 0; i < 3000; i++) {
            input.append((char) ('a' + (i % 26)));
        }
        
        final CssCharStream stream = new CssCharStream(new StringReader(input.toString()), 1, 1, 100);
        
        stream.beginToken(); // reads first char
        for (int i = 0; i < 49; i++) {
            stream.readChar();
        }
        
        final String image = stream.getImage();
        assertNotNull(image);
        assertEquals(50, image.length());
    }

    /**
     * Test backup with multiple characters.
     * @throws Exception if any error occurs
     */
    @Test
    public void backupMultiple() throws Exception {
        final String input = "abcdefgh";
        final CssCharStream stream = new CssCharStream(new StringReader(input), 1, 1);
        
        stream.beginToken(); // 'a'
        for (int i = 0; i < 4; i++) {
            stream.readChar(); // b, c, d, e
        }
        
        stream.backup(3);
        assertEquals('c', stream.readChar());
        assertEquals('d', stream.readChar());
        assertEquals('e', stream.readChar());
    }

    /**
     * Test multiple tokens.
     * @throws Exception if any error occurs
     */
    @Test
    public void multipleTokens() throws Exception {
        final String input = "first second third";
        final CssCharStream stream = new CssCharStream(new StringReader(input), 1, 1);
        
        // First token
        stream.beginToken(); // 'f'
        for (int i = 0; i < 4; i++) {
            stream.readChar(); // i, r, s, t
        }
        assertEquals("first", stream.getImage());
        
        // Skip space
        stream.readChar();
        
        // Second token
        stream.beginToken(); // 's'
        for (int i = 0; i < 5; i++) {
            stream.readChar(); // e, c, o, n, d
        }
        assertEquals("second", stream.getImage());
    }

    /**
     * Test getSuffix with length greater than buffer position.
     * @throws Exception if any error occurs
     */
    @Test
    public void getSuffixWrapAround() throws Exception {
        final StringBuilder input = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            input.append('a');
        }
        
        final CssCharStream stream = new CssCharStream(new StringReader(input.toString()), 1, 1, 50);
        
        stream.beginToken();
        for (int i = 0; i < 100; i++) {
            stream.readChar();
        }
        
        final char[] suffix = stream.getSuffix(5);
        assertEquals(5, suffix.length);
        for (int i = 0; i < 5; i++) {
            assertEquals('a', suffix[i]);
        }
    }

    /**
     * Test staticFlag constant.
     * @throws Exception if any error occurs
     */
    @Test
    public void staticFlag() throws Exception {
        assertFalse(CssCharStream.staticFlag);
    }
}
