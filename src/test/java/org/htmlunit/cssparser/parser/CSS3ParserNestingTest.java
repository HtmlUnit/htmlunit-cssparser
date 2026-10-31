/*
 * Copyright (c) 2019-2026 Ronald Brill.
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

import static org.htmlunit.cssparser.parser.AbstractCSSParser.MAX_NESTING_DEPTH;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.htmlunit.cssparser.ErrorHandler;
import org.htmlunit.cssparser.dom.CSSStyleDeclarationImpl;
import org.htmlunit.cssparser.dom.CSSStyleSheetImpl;
import org.junit.jupiter.api.Test;

/**
 * Tests for the limit of the nesting depth; deeply nested input must not
 * result in a {@link StackOverflowError}.
 *
 * @author Ronald Brill
 */
public class CSS3ParserNestingTest extends AbstractCSSParserTest {

    private static final String NESTING_ERROR = "Nesting too deep; the maximum nesting depth is "
            + MAX_NESTING_DEPTH + ".";

    /** Without the limit this results in a StackOverflowError (default stack size). */
    private static final int VERY_DEEP = 20_000;

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void functionMaxDepth() throws Exception {
        // the innermost value is a term too
        final String css = "f(".repeat(MAX_NESTING_DEPTH - 1) + "1" + ")".repeat(MAX_NESTING_DEPTH - 1);
        final CSSStyleDeclarationImpl decl = parseStyleDeclaration("width: " + css, 0, "");
        assertEquals("width: " + css, decl.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void functionTooDeep() throws Exception {
        final String css = "f(".repeat(MAX_NESTING_DEPTH) + "1" + ")".repeat(MAX_NESTING_DEPTH);
        final CSSStyleDeclarationImpl decl = parseStyleDeclaration("width: " + css + "; color: red", 1, NESTING_ERROR);
        assertEquals("color: red", decl.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void functionVeryDeep() throws Exception {
        final String css = "f(".repeat(VERY_DEEP) + "1" + ")".repeat(VERY_DEEP);
        final CSSStyleDeclarationImpl decl = parseStyleDeclaration("width: " + css + "; color: red", 1, NESTING_ERROR);
        assertEquals("color: red", decl.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void functionVeryDeepNotClosed() throws Exception {
        final CSSStyleDeclarationImpl decl = parseStyleDeclaration("width: " + "f(".repeat(VERY_DEEP), 1, NESTING_ERROR);
        assertEquals("", decl.getCssText());
    }

    /**
     * Not an url token; every 'url(' is a function.
     * The error recovery skips to the next semicolon (also inside of the open functions).
     * @throws Exception if any error occurs
     */
    @Test
    public void urlVeryDeep() throws Exception {
        final CSSStyleDeclarationImpl decl = parseStyleDeclaration(
                "background: " + "url(".repeat(VERY_DEEP) + "img.png); color: red", 1, NESTING_ERROR);
        assertEquals("color: red", decl.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void varVeryDeep() throws Exception {
        final String css = "var(--a, ".repeat(VERY_DEEP) + "1px" + ")".repeat(VERY_DEEP);
        final CSSStyleDeclarationImpl decl = parseStyleDeclaration("width: " + css + "; color: red", 1, NESTING_ERROR);
        assertEquals("color: red", decl.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void calc() throws Exception {
        final String css = "calc(" + "(".repeat(10) + "1px + 2px" + ")".repeat(10) + ")";
        final CSSStyleDeclarationImpl decl = parseStyleDeclaration("width: " + css, 0, "");
        assertEquals("width: " + css, decl.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void calcVeryDeep() throws Exception {
        final String css = "calc(" + "(".repeat(VERY_DEEP) + "1px" + ")".repeat(VERY_DEEP) + ")";
        final CSSStyleDeclarationImpl decl = parseStyleDeclaration("width: " + css + "; color: red", 1, NESTING_ERROR);
        assertEquals("color: red", decl.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void calcInColorVeryDeep() throws Exception {
        final String css = "rgb(calc(".repeat(VERY_DEEP) + "1" + "), 2, 3)".repeat(VERY_DEEP);
        final CSSStyleDeclarationImpl decl = parseStyleDeclaration("color: " + css + "; width: 1px", 1, null);
        assertEquals("width: 1px", decl.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void notSelectorMaxDepth() throws Exception {
        final String selector = "a" + ":not(".repeat(MAX_NESTING_DEPTH) + "b" + ")".repeat(MAX_NESTING_DEPTH);
        final CSSStyleSheetImpl sheet = parse(selector + " { color: red }", 0, 0, 0);
        assertEquals(1, sheet.getCssRules().getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void notSelectorVeryDeep() throws Exception {
        final String selector = "a" + ":not(".repeat(VERY_DEEP) + "b" + ")".repeat(VERY_DEEP);
        final CSSStyleSheetImpl sheet = parse(selector + " { color: red } p { color: blue }", 1, 0, 1);
        assertEquals(1, sheet.getCssRules().getLength());
        assertEquals("p { color: blue; }", sheet.getCssRules().getRules().get(0).getCssText());
    }

    /**
     * The nesting depth is reset after an error; the same parser can be used again.
     * @throws Exception if any error occurs
     */
    @Test
    public void parserReused() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        parser.parseStyleDeclaration("width: " + "f(".repeat(VERY_DEEP));
        assertEquals(1, errorHandler.getErrorCount());

        final String css = "f(".repeat(MAX_NESTING_DEPTH - 1) + "1" + ")".repeat(MAX_NESTING_DEPTH - 1);
        final CSSStyleDeclarationImpl decl = parser.parseStyleDeclaration("width: " + css);
        assertEquals(1, errorHandler.getErrorCount());
        assertEquals("width: " + css, decl.getCssText());
    }

    private static CSSStyleDeclarationImpl parseStyleDeclaration(final String css,
            final int errors, final String errorMessage) throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleDeclarationImpl decl = parser.parseStyleDeclaration(css);

        assertEquals(errors, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        if (errorMessage != null) {
            assertEquals(errorMessage, errorHandler.getErrorMessage());
        }
        return decl;
    }
}
