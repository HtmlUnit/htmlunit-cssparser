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
package org.htmlunit.cssparser.parser.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.cssparser.parser.CSSErrorHandler;
import org.htmlunit.cssparser.parser.CSSException;
import org.htmlunit.cssparser.parser.CSSOMParser;
import org.htmlunit.cssparser.parser.CSSParseException;
import org.htmlunit.cssparser.parser.InputSource;
import org.junit.jupiter.api.Test;

/**
 * Integration test for semantic validation.
 *
 * @author Ronald Brill
 */
public class SemanticValidationIntegrationTest {

    /**
     * Simple error collector for testing.
     */
    private static class ErrorCollector implements CSSErrorHandler {
        private final List<String> errors = new ArrayList<>();
        private final List<String> warnings = new ArrayList<>();

        @Override
        public void warning(final CSSParseException exception) throws CSSException {
            warnings.add(exception.getMessage());
        }

        @Override
        public void error(final CSSParseException exception) throws CSSException {
            errors.add(exception.getMessage());
        }

        @Override
        public void fatalError(final CSSParseException exception) throws CSSException {
            errors.add(exception.getMessage());
        }

        public int getErrorCount() {
            return errors.size();
        }

        public int getWarningCount() {
            return warnings.size();
        }

        public String getFirstError() {
            return errors.isEmpty() ? null : errors.get(0);
        }

        public List<String> getErrors() {
            return errors;
        }

        public List<String> getWarnings() {
            return warnings;
        }
    }

    @Test
    public void testIntegrationWithParserDisabled() throws Exception {
        final String css = ".test { colr: red; widht: 100px; }";

        final CSSOMParser parser = new CSSOMParser();
        parser.setSemanticValidationEnabled(false);
        final ErrorCollector errorHandler = new ErrorCollector();
        parser.setErrorHandler(errorHandler);

        parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        // With validation disabled, no errors should be reported
        assertEquals(0, errorHandler.getErrorCount());
    }

    @Test
    public void testIntegrationWithParserEnabled() throws Exception {
        final String css = ".test { colr: red; widht: 100px; }";

        final CSSOMParser parser = new CSSOMParser();
        parser.setSemanticValidationEnabled(true);
        final ErrorCollector errorHandler = new ErrorCollector();
        parser.setErrorHandler(errorHandler);

        parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        // With validation enabled, errors should be reported
        assertTrue(errorHandler.getErrorCount() >= 2);

        boolean foundColrError = false;
        boolean foundWidhtError = false;
        for (final String error : errorHandler.getErrors()) {
            if (error.contains("colr")) {
                foundColrError = true;
            }
            if (error.contains("widht")) {
                foundWidhtError = true;
            }
        }

        assertTrue(foundColrError, "Should have detected 'colr' typo");
        assertTrue(foundWidhtError, "Should have detected 'widht' typo");
    }

    @Test
    public void testValidCSSProducesNoErrors() throws Exception {
        final String css = ".test { color: red; width: 100px; margin: 10px; }";

        final CSSOMParser parser = new CSSOMParser();
        parser.setSemanticValidationEnabled(true);
        final ErrorCollector errorHandler = new ErrorCollector();
        parser.setErrorHandler(errorHandler);

        parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        // Valid CSS should produce no validation errors
        assertEquals(0, errorHandler.getErrorCount());
    }

    @Test
    public void testCustomPropertiesNoErrors() throws Exception {
        final String css = ".test { --my-color: red; --my-width: 100px; color: var(--my-color); }";

        final CSSOMParser parser = new CSSOMParser();
        parser.setSemanticValidationEnabled(true);
        final ErrorCollector errorHandler = new ErrorCollector();
        parser.setErrorHandler(errorHandler);

        parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        // Custom properties should be valid
        assertEquals(0, errorHandler.getErrorCount());
    }

    @Test
    public void testVendorPrefixesNoErrors() throws Exception {
        final String css = ".test { -webkit-transform: rotate(45deg); -moz-border-radius: 5px; }";

        final CSSOMParser parser = new CSSOMParser();
        parser.setSemanticValidationEnabled(true);
        final ErrorCollector errorHandler = new ErrorCollector();
        parser.setErrorHandler(errorHandler);

        parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        // Vendor prefixes should be valid
        assertEquals(0, errorHandler.getErrorCount());
    }

    @Test
    public void testGlobalKeywordsNoErrors() throws Exception {
        final String css = ".test { color: inherit; width: initial; margin: unset; }";

        final CSSOMParser parser = new CSSOMParser();
        parser.setSemanticValidationEnabled(true);
        final ErrorCollector errorHandler = new ErrorCollector();
        parser.setErrorHandler(errorHandler);

        parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        // Global keywords should be valid for any property
        assertEquals(0, errorHandler.getErrorCount());
    }

    @Test
    public void testTypoSuggestionInErrorMessage() throws Exception {
        final String css = ".test { colr: red; }";

        final CSSOMParser parser = new CSSOMParser();
        parser.setSemanticValidationEnabled(true);
        final ErrorCollector errorHandler = new ErrorCollector();
        parser.setErrorHandler(errorHandler);

        parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        assertTrue(errorHandler.getErrorCount() > 0);
        final String error = errorHandler.getFirstError();
        assertTrue(error.contains("colr"));
        assertTrue(error.contains("color"), "Error should suggest 'color' as correction");
    }

    @Test
    public void testMultipleProperties() throws Exception {
        final String css = ".test { color: red; colr: blue; width: 100px; widht: 50px; }";

        final CSSOMParser parser = new CSSOMParser();
        parser.setSemanticValidationEnabled(true);
        final ErrorCollector errorHandler = new ErrorCollector();
        parser.setErrorHandler(errorHandler);

        parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        // Should catch both typos
        assertTrue(errorHandler.getErrorCount() >= 2);
    }
}
