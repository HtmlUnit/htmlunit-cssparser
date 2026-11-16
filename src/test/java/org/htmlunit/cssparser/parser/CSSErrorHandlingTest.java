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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;

import org.htmlunit.cssparser.ErrorHandler;
import org.htmlunit.cssparser.dom.CSSMediaRuleImpl;
import org.htmlunit.cssparser.dom.CSSRuleListImpl;
import org.htmlunit.cssparser.dom.CSSStyleRuleImpl;
import org.htmlunit.cssparser.dom.CSSStyleSheetImpl;
import org.htmlunit.cssparser.dom.CSSUnknownRuleImpl;
import org.junit.jupiter.api.Test;

/**
 * Tests for the error handling of the parser; the parser has to report the errors
 * and to recover like the browsers do.
 *
 * @author Ronald Brill
 */
public class CSSErrorHandlingTest {

    /**
     * A malformed selector drops the whole rule, the other rules are kept.
     * @throws Exception if any error occurs
     */
    @Test
    public void malformedSelector() throws Exception {
        final String css = "p { color: red; }\n"
                         + "div# { font-size: 12px; }\n"
                         + "span { margin: 10px; }";

        final ErrorHandler errorHandler = new ErrorHandler();
        final CSSRuleListImpl rules = parse(css, errorHandler);

        assertEquals(1, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(1, errorHandler.getWarningCount());
        assertTrue(errorHandler.getErrorMessage().startsWith("Error in style rule. (Invalid token \"#\"."),
                errorHandler.getErrorMessage());
        assertEquals("2", errorHandler.getErrorLines());
        assertEquals("4", errorHandler.getErrorColumns());
        assertEquals("Ignoring the whole rule.", errorHandler.getWarningMessage());

        assertEquals(2, rules.getLength());
        assertEquals("p { color: red; }", rules.getRules().get(0).getCssText());
        assertEquals("span { margin: 10px; }", rules.getRules().get(1).getCssText());
    }

    /**
     * An invalid property value drops only this declaration.
     * @throws Exception if any error occurs
     */
    @Test
    public void malformedPropertyValue() throws Exception {
        final String css = "p { color: #gg; font-size: 12px; }";

        final ErrorHandler errorHandler = new ErrorHandler();
        final CSSRuleListImpl rules = parse(css, errorHandler);

        assertEquals(1, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(0, errorHandler.getWarningCount());
        assertEquals("Invalid color \"#gg\".", errorHandler.getErrorMessage());
        assertEquals("1", errorHandler.getErrorLines());
        assertEquals("12", errorHandler.getErrorColumns());

        assertEquals(1, rules.getLength());
        assertEquals("p { font-size: 12px; }", rules.getRules().get(0).getCssText());
    }

    /**
     * A missing closing brace; the following rule is parsed as (invalid) declaration
     * of the unclosed rule and the end of the input closes the rule.
     * @throws Exception if any error occurs
     */
    @Test
    public void unclosedBrace() throws Exception {
        final String css = "p { color: red;\n"
                         + "div { margin: 10px; }";

        final ErrorHandler errorHandler = new ErrorHandler();
        final CSSRuleListImpl rules = parse(css, errorHandler);

        assertEquals(1, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(0, errorHandler.getWarningCount());
        assertTrue(errorHandler.getErrorMessage().startsWith("Error in declaration. (Invalid token \"{\"."),
                errorHandler.getErrorMessage());
        assertEquals("2", errorHandler.getErrorLines());
        assertEquals("5", errorHandler.getErrorColumns());

        assertEquals(1, rules.getLength());
        assertEquals("p { color: red; }", rules.getRules().get(0).getCssText());
    }

    /**
     * Empty input.
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyInput() throws Exception {
        final ErrorHandler errorHandler = new ErrorHandler();
        final CSSRuleListImpl rules = parse("", errorHandler);

        assertNoErrors(errorHandler);
        assertEquals(0, rules.getLength());
    }

    /**
     * Input with white space only.
     * @throws Exception if any error occurs
     */
    @Test
    public void whitespaceOnlyInput() throws Exception {
        final ErrorHandler errorHandler = new ErrorHandler();
        final CSSRuleListImpl rules = parse("   \n\t  \r\n  ", errorHandler);

        assertNoErrors(errorHandler);
        assertEquals(0, rules.getLength());
    }

    /**
     * An unknown at-rule is kept as unknown rule. The ';' after the block
     * starts the prelude of the next rule, therefore the next rule is dropped
     * (same as in the browsers).
     * @throws Exception if any error occurs
     */
    @Test
    public void invalidAtRule() throws Exception {
        final String css = "@unknown-rule { content };\n"
                         + "p { color: blue; }";

        final ErrorHandler errorHandler = new ErrorHandler();
        final CSSRuleListImpl rules = parse(css, errorHandler);

        assertEquals(1, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(1, errorHandler.getWarningCount());
        assertTrue(errorHandler.getErrorMessage().startsWith("Error in rule. (Invalid token \";\"."),
                errorHandler.getErrorMessage());
        assertEquals("1", errorHandler.getErrorLines());
        assertEquals("26", errorHandler.getErrorColumns());
        assertEquals("Ignoring the whole rule.", errorHandler.getWarningMessage());

        assertEquals(1, rules.getLength());
        assertTrue(rules.getRules().get(0) instanceof CSSUnknownRuleImpl);
        assertEquals("@unknown-rule { content }", rules.getRules().get(0).getCssText());
    }

    /**
     * An escape that is not followed by hex digits is just the escaped char.
     * @throws Exception if any error occurs
     */
    @Test
    public void invalidUnicodeEscape() throws Exception {
        final String css = "p { content: '\\GGGG'; }";

        final ErrorHandler errorHandler = new ErrorHandler();
        final CSSRuleListImpl rules = parse(css, errorHandler);

        assertNoErrors(errorHandler);

        assertEquals(1, rules.getLength());
        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.getRules().get(0);
        assertEquals("GGGG", rule.getStyle().getPropertyCSSValue("content").getStringValue());
    }

    /**
     * Every malformed rule is reported and dropped, the parser recovers for the last rule.
     * @throws Exception if any error occurs
     */
    @Test
    public void multipleConsecutiveErrors() throws Exception {
        final String css = "p# { }\n"
                         + "div## { }\n"
                         + "span### { }\n"
                         + "a { color: red; }";

        final ErrorHandler errorHandler = new ErrorHandler();
        final CSSRuleListImpl rules = parse(css, errorHandler);

        assertEquals(3, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(3, errorHandler.getWarningCount());
        assertEquals("1 2 3", errorHandler.getErrorLines());
        assertEquals("2 4 5", errorHandler.getErrorColumns());
        assertEquals("Ignoring the whole rule. Ignoring the whole rule. Ignoring the whole rule.",
                errorHandler.getWarningMessage());

        assertEquals(1, rules.getLength());
        assertEquals("a { color: red; }", rules.getRules().get(0).getCssText());
    }

    /**
     * An at-keyword inside a selector drops the rule.
     * @throws Exception if any error occurs
     */
    @Test
    public void invalidCharacterInSelector() throws Exception {
        final String css = "p@invalid { color: red; }";

        final ErrorHandler errorHandler = new ErrorHandler();
        final CSSRuleListImpl rules = parse(css, errorHandler);

        assertEquals(1, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(1, errorHandler.getWarningCount());
        assertTrue(errorHandler.getErrorMessage().startsWith("Error in style rule. (Invalid token \"@invalid\"."),
                errorHandler.getErrorMessage());
        assertEquals("1", errorHandler.getErrorLines());
        assertEquals("2", errorHandler.getErrorColumns());

        assertEquals(0, rules.getLength());
    }

    /**
     * A missing semicolon makes the declaration invalid; both properties are dropped.
     * @throws Exception if any error occurs
     */
    @Test
    public void missingSemicolon() throws Exception {
        final String css = "p { color: red margin: 10px; }";

        final ErrorHandler errorHandler = new ErrorHandler();
        final CSSRuleListImpl rules = parse(css, errorHandler);

        assertEquals(1, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(0, errorHandler.getWarningCount());
        assertEquals("Error in expression; ':' found after identifier \"margin\".", errorHandler.getErrorMessage());
        assertEquals("1", errorHandler.getErrorLines());
        assertEquals("16", errorHandler.getErrorColumns());

        assertEquals(1, rules.getLength());
        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.getRules().get(0);
        assertEquals("p { }", rule.getCssText());
        assertEquals(0, rule.getStyle().getLength());
    }

    /**
     * Many rules.
     * @throws Exception if any error occurs
     */
    @Test
    public void veryLongInput() throws Exception {
        final StringBuilder css = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            css.append(".class").append(i).append(" { color: red; margin: ").append(i).append("px; }\n");
        }

        final ErrorHandler errorHandler = new ErrorHandler();
        final CSSRuleListImpl rules = parse(css.toString(), errorHandler);

        assertNoErrors(errorHandler);

        assertEquals(1000, rules.getLength());
        assertEquals("*.class0 { color: red; margin: 0px; }", rules.getRules().get(0).getCssText());
        assertEquals("*.class999 { color: red; margin: 999px; }", rules.getRules().get(999).getCssText());
    }

    /**
     * A supports rule nested inside a media rule.
     * @throws Exception if any error occurs
     */
    @Test
    public void deeplyNestedAtRules() throws Exception {
        final String css = "@media screen { "
                         + "@supports (display: flex) { "
                         + "div { display: flex; } "
                         + "} "
                         + "}";

        final ErrorHandler errorHandler = new ErrorHandler();
        final CSSRuleListImpl rules = parse(css, errorHandler);

        assertNoErrors(errorHandler);

        assertEquals(1, rules.getLength());
        final CSSMediaRuleImpl mediaRule = (CSSMediaRuleImpl) rules.getRules().get(0);
        assertEquals(1, mediaRule.getCssRules().getLength());
        assertEquals("@supports (display: flex) { div { display: flex; } }",
                mediaRule.getCssRules().getRules().get(0).getCssText());
    }

    /**
     * Escapes of chars that are not hex digits are the escaped char itself.
     * @throws Exception if any error occurs
     */
    @Test
    public void specialCharactersInStrings() throws Exception {
        final String css = "p::before { content: '\\n\\r\\t\\\\'; }";

        final ErrorHandler errorHandler = new ErrorHandler();
        final CSSRuleListImpl rules = parse(css, errorHandler);

        assertNoErrors(errorHandler);

        assertEquals(1, rules.getLength());
        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.getRules().get(0);
        assertEquals("p::before", rule.getSelectorText());
        assertEquals("nrt\\", rule.getStyle().getPropertyCSSValue("content").getStringValue());
    }

    /**
     * Comments are not nested; the first '*&#47;' ends the comment and the
     * rest makes the selector invalid.
     * @throws Exception if any error occurs
     */
    @Test
    public void commentsWithSpecialContent() throws Exception {
        final String css = "/* comment with // and /* nested */ */ p { color: red; }";

        final ErrorHandler errorHandler = new ErrorHandler();
        final CSSRuleListImpl rules = parse(css, errorHandler);

        assertEquals(1, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(1, errorHandler.getWarningCount());
        assertTrue(errorHandler.getErrorMessage().startsWith("Error in style rule. (Invalid token \"/\"."),
                errorHandler.getErrorMessage());
        assertEquals("1", errorHandler.getErrorLines());
        assertEquals("38", errorHandler.getErrorColumns());

        assertEquals(0, rules.getLength());
    }

    private static CSSRuleListImpl parse(final String css, final ErrorHandler errorHandler) throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);
        return sheet.getCssRules();
    }

    private static void assertNoErrors(final ErrorHandler errorHandler) {
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(0, errorHandler.getWarningCount());
    }
}
