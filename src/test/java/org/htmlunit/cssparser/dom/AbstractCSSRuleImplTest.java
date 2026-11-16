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
package org.htmlunit.cssparser.dom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;

import org.htmlunit.cssparser.parser.CSSOMParser;
import org.htmlunit.cssparser.parser.InputSource;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link AbstractCSSRuleImpl}.
 *
 * @author Ronald Brill
 */
public class AbstractCSSRuleImplTest {

    /**
     * Parent style sheet getter/setter.
     * @throws Exception if any error occurs
     */
    @Test
    public void parentStyleSheet() throws Exception {
        final CSSStyleSheetImpl styleSheet = new CSSStyleSheetImpl();
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(styleSheet, null, null);

        assertSame(styleSheet, rule.getParentStyleSheet());

        final CSSStyleSheetImpl newStyleSheet = new CSSStyleSheetImpl();
        rule.setParentStyleSheet(newStyleSheet);

        assertSame(newStyleSheet, rule.getParentStyleSheet());
    }

    /**
     * Parent rule getter/setter.
     * @throws Exception if any error occurs
     */
    @Test
    public void parentRule() throws Exception {
        final CSSStyleSheetImpl styleSheet = new CSSStyleSheetImpl();
        final CSSPageRuleImpl parentRule = new CSSPageRuleImpl(styleSheet, null, null);
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(styleSheet, parentRule, null);

        assertSame(parentRule, rule.getParentRule());

        final CSSPageRuleImpl newParentRule = new CSSPageRuleImpl(styleSheet, null, null);
        rule.setParentRule(newParentRule);

        assertSame(newParentRule, rule.getParentRule());
    }

    /**
     * Null parent style sheet.
     * @throws Exception if any error occurs
     */
    @Test
    public void nullParentStyleSheet() throws Exception {
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(null, null, null);

        assertNull(rule.getParentStyleSheet());
        assertNull(rule.getParentRule());
    }

    /**
     * Null parent rule.
     * @throws Exception if any error occurs
     */
    @Test
    public void nullParentRule() throws Exception {
        final CSSStyleSheetImpl styleSheet = new CSSStyleSheetImpl();
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(styleSheet, null, null);

        assertSame(styleSheet, rule.getParentStyleSheet());
        assertNull(rule.getParentRule());
    }

    /**
     * Equals with the same object.
     * @throws Exception if any error occurs
     */
    @Test
    public void equalsSameObject() throws Exception {
        final CSSStyleSheetImpl styleSheet = new CSSStyleSheetImpl();
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(styleSheet, null, null);

        assertTrue(rule.equals(rule));
    }

    /**
     * Equals with different types.
     * @throws Exception if any error occurs
     */
    @Test
    public void equalsDifferentType() throws Exception {
        final CSSStyleSheetImpl styleSheet = new CSSStyleSheetImpl();
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(styleSheet, null, null);
        final CSSPageRuleImpl pageRule = new CSSPageRuleImpl(styleSheet, null, null);

        assertFalse(rule.equals("not a rule"));
        assertFalse(rule.equals(null));
        assertFalse(rule.equals(pageRule));
        assertFalse(pageRule.equals(rule));
    }

    /**
     * A parsed rule points to its parent rule and the parent rule contains the rule;
     * equals() must not follow the parent (stack overflow).
     * @throws Exception if any error occurs
     */
    @Test
    public void equalsNoStackOverflow() throws Exception {
        final CSSMediaRuleImpl mediaRule = parseMediaRule();
        final AbstractCSSRuleImpl childRule = mediaRule.getCssRules().getRules().get(0);
        assertSame(mediaRule, childRule.getParentRule());

        assertTrue(mediaRule.equals(mediaRule));
        assertTrue(childRule.equals(childRule));
        assertFalse(mediaRule.equals(childRule));
        assertFalse(childRule.equals(mediaRule));
    }

    /**
     * hashCode() is stable.
     * @throws Exception if any error occurs
     */
    @Test
    public void hashCodeConsistency() throws Exception {
        final CSSStyleSheetImpl styleSheet = new CSSStyleSheetImpl();
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(styleSheet, null, null);

        assertEquals(rule.hashCode(), rule.hashCode());
    }

    /**
     * A parsed rule points to its parent rule and the parent rule contains the rule;
     * hashCode() must not follow the parent (stack overflow).
     * @throws Exception if any error occurs
     */
    @Test
    public void hashCodeNoStackOverflow() throws Exception {
        final CSSMediaRuleImpl mediaRule = parseMediaRule();
        final AbstractCSSRuleImpl childRule = mediaRule.getCssRules().getRules().get(0);
        assertSame(mediaRule, childRule.getParentRule());

        assertEquals(mediaRule.hashCode(), mediaRule.hashCode());
        assertEquals(childRule.hashCode(), childRule.hashCode());
    }

    /**
     * Changing the parent style sheet after construction.
     * @throws Exception if any error occurs
     */
    @Test
    public void changeParentStyleSheet() throws Exception {
        final CSSStyleSheetImpl styleSheet1 = new CSSStyleSheetImpl();
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(styleSheet1, null, null);

        assertSame(styleSheet1, rule.getParentStyleSheet());

        final CSSStyleSheetImpl styleSheet2 = new CSSStyleSheetImpl();
        rule.setParentStyleSheet(styleSheet2);

        assertSame(styleSheet2, rule.getParentStyleSheet());

        rule.setParentStyleSheet(null);
        assertNull(rule.getParentStyleSheet());
    }

    /**
     * Changing the parent rule after construction.
     * @throws Exception if any error occurs
     */
    @Test
    public void changeParentRule() throws Exception {
        final CSSStyleSheetImpl styleSheet = new CSSStyleSheetImpl();
        final CSSPageRuleImpl parent1 = new CSSPageRuleImpl(styleSheet, null, null);
        final CSSStyleRuleImpl rule = new CSSStyleRuleImpl(styleSheet, parent1, null);

        assertSame(parent1, rule.getParentRule());

        final CSSPageRuleImpl parent2 = new CSSPageRuleImpl(styleSheet, null, null);
        rule.setParentRule(parent2);

        assertSame(parent2, rule.getParentRule());

        rule.setParentRule(null);
        assertNull(rule.getParentRule());
    }

    private static CSSMediaRuleImpl parseMediaRule() throws Exception {
        final String css = "@media screen { p { color: red; } }";
        final CSSStyleSheetImpl sheet = new CSSOMParser().parseStyleSheet(new InputSource(new StringReader(css)), null);
        return (CSSMediaRuleImpl) sheet.getCssRules().getRules().get(0);
    }
}
