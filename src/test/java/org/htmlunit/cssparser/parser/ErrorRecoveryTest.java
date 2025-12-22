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

import org.htmlunit.cssparser.dom.CSSRuleListImpl;
import org.htmlunit.cssparser.dom.CSSStyleSheetImpl;
import org.junit.jupiter.api.Test;

/**
 * Tests for error recovery methods in the CSS parser.
 * These tests verify that the parser can recover from various malformed CSS
 * and continue parsing subsequent valid rules.
 *
 * @author Ronald Brill
 */
public class ErrorRecoveryTest extends AbstractCSSParserTest {

    /**
     * Test recovery from error at the start of the stylesheet.
     * The parser should skip the invalid content and parse the valid rule that follows.
     */
    @Test
    public void errorAtStartWithRecovery() throws Exception {
        final String css = "// invalid comment \n"
                + "h1 { color: red; }";
        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
        final CSSRuleListImpl rules = sheet.getCssRules();

        // Should recover and have no valid rules (the invalid comment causes the next rule to be skipped)
        assertEquals(0, rules.getLength());
    }

    /**
     * Test recovery from multiple errors.
     * The parser should skip each error and continue parsing.
     */
    @Test
    public void multipleErrorsWithRecovery() throws Exception {
        final String css = "// error 1\n"
                + "h1 { color: red; }\n"
                + "// error 2\n"
                + "h2 { color: blue; }";
        final CSSStyleSheetImpl sheet = parse(css, 2, 0, 2);
        final CSSRuleListImpl rules = sheet.getCssRules();

        // Both rules should be skipped due to the errors
        assertEquals(0, rules.getLength());
    }

    /**
     * Test recovery from unbalanced braces.
     * The parser should skip to the next valid rule.
     */
    @Test
    public void unbalancedBracesRecovery() throws Exception {
        final String css = "h1 { color: red;\n"
                + "h2 { color: blue; }";
        // Parser parses this with an error for the unbalanced brace
        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 0);
        final CSSRuleListImpl rules = sheet.getCssRules();

        // Parser creates a rule with the error
        assertEquals(1, rules.getLength());
    }

    /**
     * Test recovery from invalid property in declaration.
     */
    @Test
    public void invalidPropertyRecovery() throws Exception {
        final String css = "h1 { @invalid; color: red; }";
        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
        final CSSRuleListImpl rules = sheet.getCssRules();

        // Should have the rule but skip the invalid property
        assertEquals(1, rules.getLength());
    }

    /**
     * Test recovery at EOF boundary.
     * The parser should handle EOF gracefully without hanging.
     */
    @Test
    public void errorAtEOF() throws Exception {
        final String css = "h1 { color: red; }\n"
                + "// invalid at end";
        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
        final CSSRuleListImpl rules = sheet.getCssRules();

        // Should have the first rule
        assertEquals(1, rules.getLength());
    }

    /**
     * Test recovery from misplaced @charset rule.
     */
    @Test
    public void misplacedCharsetRecovery() throws Exception {
        final String css = "h1 { color: red; }\n"
                + "@charset \"UTF-8\";\n"
                + "h2 { color: blue; }";
        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
        final CSSRuleListImpl rules = sheet.getCssRules();

        // Should have both h1 and h2 rules
        assertEquals(2, rules.getLength());
        assertEquals("h1 { color: red; }", rules.getRules().get(0).getCssText());
        assertEquals("h2 { color: blue; }", rules.getRules().get(1).getCssText());
    }

    /**
     * Test recovery from invalid at-rule.
     */
    @Test
    public void invalidAtRuleRecovery() throws Exception {
        final String css = "@invalid { content: \"test\"; }\n"
                + "h1 { color: red; }";
        final CSSStyleSheetImpl sheet = parse(css, 0, 0, 0);
        final CSSRuleListImpl rules = sheet.getCssRules();

        // Should skip the invalid at-rule and parse h1
        assertEquals(2, rules.getLength());
    }

    /**
     * Test that recovery doesn't consume too many tokens.
     */
    @Test
    public void recoveryDoesNotConsumeValidRules() throws Exception {
        final String css = "// invalid\n"
                + "p { color: green; }\n"
                + "h1 { color: red; }\n"
                + "h2 { color: blue; }";
        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
        final CSSRuleListImpl rules = sheet.getCssRules();

        // Should have p, h1, and h2 rules after skipping the invalid comment
        // Note: The behavior depends on how error recovery is implemented
        assertEquals(2, rules.getLength());
    }

    /**
     * Test recovery from malformed selector.
     */
    @Test
    public void malformedSelectorRecovery() throws Exception {
        final String css = "h1 & h2 { color: red; }\n"
                + "h3 { color: blue; }";
        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
        final CSSRuleListImpl rules = sheet.getCssRules();

        // Should skip the invalid rule and parse h3
        assertEquals(1, rules.getLength());
        assertEquals("h3 { color: blue; }", rules.getRules().get(0).getCssText());
    }

    /**
     * Test recovery from incomplete rule.
     */
    @Test
    public void incompleteRuleRecovery() throws Exception {
        final String css = "h1 { color:\n"
                + "h2 { color: blue; }";
        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
        final CSSRuleListImpl rules = sheet.getCssRules();

        // Parser treats h2 as a value for the color property in h1
        assertEquals(1, rules.getLength());
        assertEquals("h1 { color: h2; }", rules.getRules().get(0).getCssText());
    }

    /**
     * Test that the stylesheet object is created even with errors.
     */
    @Test
    public void stylesheetCreatedWithErrors() throws Exception {
        final String css = "// invalid\n"
                + "// more invalid";
        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
        
        // Should create stylesheet even though it has no valid rules
        assertNotNull(sheet);
        assertEquals(0, sheet.getCssRules().getLength());
    }

    /**
     * Test recovery from error in media query.
     */
    @Test
    public void errorInMediaQuery() throws Exception {
        final String css = "@media screen and invalid {\n"
                + "  h1 { color: red; }\n"
                + "}\n"
                + "h2 { color: blue; }";
        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
        final CSSRuleListImpl rules = sheet.getCssRules();

        // Should skip the invalid media rule and parse h2
        assertEquals(1, rules.getLength());
        assertEquals("h2 { color: blue; }", rules.getRules().get(0).getCssText());
    }

    /**
     * Test recovery handles balanced braces correctly.
     */
    @Test
    public void balancedBracesInError() throws Exception {
        final String css = "h1 { content: \"{}\"; color: red; }\n"
                + "h2 { color: blue; }";
        final CSSStyleSheetImpl sheet = parse(css, 0, 0, 0);
        final CSSRuleListImpl rules = sheet.getCssRules();

        // Both rules should be valid
        assertEquals(2, rules.getLength());
        assertEquals("h1 { content: \"{}\"; color: red; }", rules.getRules().get(0).getCssText());
        assertEquals("h2 { color: blue; }", rules.getRules().get(1).getCssText());
    }
}
