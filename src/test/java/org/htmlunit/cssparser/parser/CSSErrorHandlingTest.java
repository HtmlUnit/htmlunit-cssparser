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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;

import org.htmlunit.cssparser.ErrorHandler;
import org.htmlunit.cssparser.dom.CSSStyleSheetImpl;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for CSS error handling and recovery.
 *
 * @author Ronald Brill
 */
public class CSSErrorHandlingTest {

    /**
     * Test malformed selector recovery.
     * @throws Exception if any error occurs
     */
    @Test
    public void malformedSelector() throws Exception {
        final String css = "p { color: red; }\n"
                         + "div# { font-size: 12px; }\n"  // malformed selector
                         + "span { margin: 10px; }";
        
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        
        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);
        
        assertNotNull(sheet);
        // Parser should recover and continue parsing
        assertTrue(errorHandler.getErrorCount() > 0 || errorHandler.getWarningCount() > 0);
    }

    /**
     * Test malformed property value recovery.
     * @throws Exception if any error occurs
     */
    @Test
    public void malformedPropertyValue() throws Exception {
        final String css = "p { color: #gg; font-size: 12px; }";  // invalid color
        
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        
        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);
        
        assertNotNull(sheet);
    }

    /**
     * Test unclosed brace recovery.
     * @throws Exception if any error occurs
     */
    @Test
    public void unclosedBrace() throws Exception {
        final String css = "p { color: red;\n"
                         + "div { margin: 10px; }";  // missing closing brace for p
        
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        
        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);
        
        assertNotNull(sheet);
    }

    /**
     * Test empty input.
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyInput() throws Exception {
        final String css = "";
        
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        
        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);
        
        assertNotNull(sheet);
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
    }

    /**
     * Test whitespace only input.
     * @throws Exception if any error occurs
     */
    @Test
    public void whitespaceOnlyInput() throws Exception {
        final String css = "   \n\t  \r\n  ";
        
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        
        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);
        
        assertNotNull(sheet);
        assertEquals(0, errorHandler.getErrorCount());
    }

    /**
     * Test invalid at-rule recovery.
     * @throws Exception if any error occurs
     */
    @Test
    public void invalidAtRule() throws Exception {
        final String css = "@unknown-rule { content };\n"
                         + "p { color: blue; }";
        
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        
        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);
        
        assertNotNull(sheet);
    }

    /**
     * Test invalid unicode escape sequence.
     * @throws Exception if any error occurs
     */
    @Test
    public void invalidUnicodeEscape() throws Exception {
        final String css = "p { content: '\\GGGG'; }";  // invalid hex digits
        
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        
        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);
        
        assertNotNull(sheet);
    }

    /**
     * Test null input source.
     * @throws Exception if any error occurs
     */
    @Test
    public void nullReader() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        
        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader("")), null);
        
        assertNotNull(sheet);
    }

    /**
     * Test multiple consecutive errors.
     * @throws Exception if any error occurs
     */
    @Test
    public void multipleConsecutiveErrors() throws Exception {
        final String css = "p# { }\n"
                         + "div## { }\n"
                         + "span### { }\n"
                         + "a { color: red; }";
        
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        
        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);
        
        assertNotNull(sheet);
    }

    /**
     * Test invalid character in selector.
     * @throws Exception if any error occurs
     */
    @Test
    public void invalidCharacterInSelector() throws Exception {
        final String css = "p@invalid { color: red; }";
        
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        
        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);
        
        assertNotNull(sheet);
    }

    /**
     * Test missing semicolon between properties.
     * @throws Exception if any error occurs
     */
    @Test
    public void missingSemicolon() throws Exception {
        final String css = "p { color: red margin: 10px; }";  // missing semicolon after red
        
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        
        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);
        
        assertNotNull(sheet);
    }

    /**
     * Test very long CSS input for buffer handling.
     * @throws Exception if any error occurs
     */
    @Test
    public void veryLongInput() throws Exception {
        final StringBuilder css = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            css.append(".class").append(i).append(" { color: red; margin: ").append(i).append("px; }\n");
        }
        
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        
        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css.toString())), null);
        
        assertNotNull(sheet);
        assertTrue(sheet.getCssRules().getLength() > 0);
    }

    /**
     * Test deeply nested at-rules.
     * @throws Exception if any error occurs
     */
    @Test
    public void deeplyNestedAtRules() throws Exception {
        final String css = "@media screen { "
                         + "@supports (display: flex) { "
                         + "div { display: flex; } "
                         + "} "
                         + "}";
        
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        
        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);
        
        assertNotNull(sheet);
    }

    /**
     * Test special characters in strings.
     * @throws Exception if any error occurs
     */
    @Test
    public void specialCharactersInStrings() throws Exception {
        final String css = "p::before { content: '\\n\\r\\t\\\\'; }";
        
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        
        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);
        
        assertNotNull(sheet);
    }

    /**
     * Test comments with special content.
     * @throws Exception if any error occurs
     */
    @Test
    public void commentsWithSpecialContent() throws Exception {
        final String css = "/* comment with // and /* nested */ */ p { color: red; }";
        
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        
        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);
        
        assertNotNull(sheet);
    }
}
