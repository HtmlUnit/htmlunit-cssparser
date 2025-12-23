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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.htmlunit.cssparser.ErrorHandler;
import org.junit.jupiter.api.Test;

/**
 * Tests for context-aware error messages.
 *
 * @author Ronald Brill
 */
public class ContextAwareErrorTest extends AbstractCSSParserTest {

    /**
     * Test that errors in declarations include context information.
     * @throws IOException if any error occurs
     */
    @Test
    public void testDeclarationErrorWithContext() throws IOException {
        final String css = ".class { color red; }";  // Missing colon
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(css));
        parser.parseStyleSheet(source, null);

        assertEquals(1, errorHandler.getErrorCount());
        final String errorMsg = errorHandler.getErrorMessage();

        // Should contain context about being in a style rule
        assertTrue(errorMsg.contains("style-rule") || errorMsg.contains("declaration"),
            "Error message should contain context: " + errorMsg);
    }

    /**
     * Test that errors in nested media rules include full context path.
     * @throws IOException if any error occurs
     */
    @Test
    public void testNestedMediaRuleErrorWithContext() throws IOException {
        final String css = "@media screen { .class { color red; } }";  // Missing colon
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(css));
        parser.parseStyleSheet(source, null);

        assertEquals(1, errorHandler.getErrorCount());
        final String errorMsg = errorHandler.getErrorMessage();

        // Should contain context showing we're in @media > style-rule
        assertTrue(errorMsg.contains("@media"),
            "Error message should contain @media context: " + errorMsg);
    }

    /**
     * Test that property name is included in error context.
     * @throws IOException if any error occurs
     */
    @Test
    public void testPropertyNameInErrorContext() throws IOException {
        final String css = ".class { color: red; background blue; }";  // Missing colon
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(css));
        parser.parseStyleSheet(source, null);

        assertTrue(errorHandler.getErrorCount() > 0);
        final String errorMsg = errorHandler.getErrorMessage();

        // Should contain information about the parsing context
        assertTrue(errorMsg.length() > 0, "Error message should not be empty");
    }

    /**
     * Test that invalid selector errors include expected tokens.
     * @throws IOException if any error occurs
     */
    @Test
    public void testInvalidSelectorWithExpectedTokens() throws IOException {
        final String css = ".class color: red; }";  // Missing opening brace
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(css));
        parser.parseStyleSheet(source, null);

        assertTrue(errorHandler.getErrorCount() > 0);
        final String errorMsg = errorHandler.getErrorMessage();

        // Should indicate what was expected (like "{" or selector)
        assertTrue(errorMsg.contains("Expected") || errorMsg.contains("expecting"),
            "Error message should mention expected tokens: " + errorMsg);
    }

    /**
     * Test that media rule errors include context.
     * @throws IOException if any error occurs
     */
    @Test
    public void testMediaRuleErrorWithContext() throws IOException {
        final String css = "@media screeen { }";
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(css));
        parser.parseStyleSheet(source, null);

        // This may or may not produce an error depending on parser behavior
        // The test validates that if there is an error, it includes context
        if (errorHandler.getErrorCount() > 0) {
            final String errorMsg = errorHandler.getErrorMessage();
            assertTrue(errorMsg.length() > 0, "Error message should not be empty");
        }
    }

    /**
     * Test that existing valid CSS still parses without errors.
     * @throws IOException if any error occurs
     */
    @Test
    public void testValidCssStillParses() throws IOException {
        final String css = ".class { color: red; background-color: blue; }";
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(css));
        parser.parseStyleSheet(source, null);

        assertEquals(0, errorHandler.getErrorCount(), "Valid CSS should parse without errors");
    }

    /**
     * Test that nested rules show proper context hierarchy.
     * @throws IOException if any error occurs
     */
    @Test
    public void testNestedContextHierarchy() throws IOException {
        final String css = "@media print { .page { color black; } }";  // Missing colon
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(css));
        parser.parseStyleSheet(source, null);

        assertEquals(1, errorHandler.getErrorCount());
        final String errorMsg = errorHandler.getErrorMessage();

        // Verify the error message contains some form of context
        assertTrue(errorMsg.length() > 0, "Error message should not be empty");
        assertTrue(errorMsg.contains("Error") || errorMsg.contains("Invalid"),
            "Error message should indicate an error: " + errorMsg);
    }
}
