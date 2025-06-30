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
package org.htmlunit.cssparser.dom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.StringReader;

import org.htmlunit.cssparser.parser.CSSOMParser;
import org.htmlunit.cssparser.parser.InputSource;
import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;

/**
 * Unit tests for {@link CSSMediaRuleImpl}.
 *
 * @author Ronald Brill
 */
public class CSSMediaRuleImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final InputSource source = new InputSource(new StringReader("@media print { body { font-size: 10pt } }"));
        final CSSStyleSheetImpl ss = parser.parseStyleSheet(source, null);
        final CSSMediaRuleImpl mediaRule = (CSSMediaRuleImpl) ss.getCssRules().getRules().get(0);

        assertEquals("@media print {\n  body { font-size: 10pt; }\n}", mediaRule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getParentRule() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final InputSource source = new InputSource(new StringReader("@media print { body { font-size: 10pt } }"));
        final CSSStyleSheetImpl ss = parser.parseStyleSheet(source, null);

        final CSSMediaRuleImpl mediaRule = (CSSMediaRuleImpl) ss.getCssRules().getRules().get(0);
        assertEquals(mediaRule, mediaRule.getCssRules().getRules().get(0).getParentRule());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void insertRule() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final InputSource source = new InputSource(new StringReader("@media print { }"));
        final CSSStyleSheetImpl ss = parser.parseStyleSheet(source, null);
        final CSSMediaRuleImpl mediaRule = (CSSMediaRuleImpl) ss.getCssRules().getRules().get(0);

        mediaRule.insertRule(".testStyle { height: 42px; }", 0);
        assertEquals("*.testStyle { height: 42px; }", mediaRule.getCssRules().getRules().get(0).getCssText());
        assertEquals("@media print {\n  *.testStyle { height: 42px; }\n}", mediaRule.getCssRules().getRules().get(0).getParentRule().getCssText());

        mediaRule.insertRule(".testStyle { height: 43px; }", 0);
        assertEquals("*.testStyle { height: 43px; }", mediaRule.getCssRules().getRules().get(0).getCssText());
        assertEquals("*.testStyle { height: 42px; }", mediaRule.getCssRules().getRules().get(1).getCssText());
        assertEquals("@media print {\n  *.testStyle { height: 43px; }\n  *.testStyle { height: 42px; }\n}",
                mediaRule.getCssRules().getRules().get(0).getParentRule().getCssText());

        mediaRule.insertRule(".testStyle { height: 44px; }", 2);
        assertEquals("*.testStyle { height: 43px; }", mediaRule.getCssRules().getRules().get(0).getCssText());
        assertEquals("*.testStyle { height: 42px; }", mediaRule.getCssRules().getRules().get(1).getCssText());
        assertEquals("*.testStyle { height: 44px; }", mediaRule.getCssRules().getRules().get(2).getCssText());
        assertEquals("@media print {\n  *.testStyle { height: 43px; }"
                + "\n  *.testStyle { height: 42px; }\n  *.testStyle { height: 44px; }\n}",
                mediaRule.getCssRules().getRules().get(0).getParentRule().getCssText());

        mediaRule.insertRule(".testStyle { height: 45px; }", 2);
        assertEquals("*.testStyle { height: 43px; }", mediaRule.getCssRules().getRules().get(0).getCssText());
        assertEquals("*.testStyle { height: 42px; }", mediaRule.getCssRules().getRules().get(1).getCssText());
        assertEquals("*.testStyle { height: 45px; }", mediaRule.getCssRules().getRules().get(2).getCssText());
        assertEquals("*.testStyle { height: 44px; }", mediaRule.getCssRules().getRules().get(3).getCssText());
        assertEquals("@media print {\n  *.testStyle { height: 43px; }\n  *.testStyle { height: 42px; }"
                + "\n  *.testStyle { height: 45px; }\n  *.testStyle { height: 44px; }\n}",
                mediaRule.getCssRules().getRules().get(0).getParentRule().getCssText());
    }

    /**
     * Regression test for bug 2123264.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void insertRuleWithLeadingWhitespace() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final InputSource source = new InputSource(new StringReader("@media print { }"));
        final CSSStyleSheetImpl ss = parser.parseStyleSheet(source, null);
        final CSSMediaRuleImpl mediaRule = (CSSMediaRuleImpl) ss.getCssRules().getRules().get(0);

        mediaRule.insertRule(" .testStyleDef { height: 42px; }", 0);
        assertEquals("*.testStyleDef { height: 42px; }", mediaRule.getCssRules().getRules().get(0).getCssText());

        mediaRule.insertRule("      .testStyleDef { height: 43px;}   ", 0);
        assertEquals("*.testStyleDef { height: 43px; }", mediaRule.getCssRules().getRules().get(0).getCssText());
        assertEquals("*.testStyleDef { height: 42px; }", mediaRule.getCssRules().getRules().get(1).getCssText());

        mediaRule.insertRule("\t.testStyleDef { height: 44px; }\r\n", 0);
        assertEquals("*.testStyleDef { height: 44px; }", mediaRule.getCssRules().getRules().get(0).getCssText());
        assertEquals("*.testStyleDef { height: 43px; }", mediaRule.getCssRules().getRules().get(1).getCssText());
        assertEquals("*.testStyleDef { height: 42px; }", mediaRule.getCssRules().getRules().get(2).getCssText());
    }

    /**
     * Regression test for bug 2123264.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void insertRuleWithoutDeclaration() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final InputSource source = new InputSource(new StringReader("@media print { }"));
        final CSSStyleSheetImpl ss = parser.parseStyleSheet(source, null);
        final CSSMediaRuleImpl mediaRule = (CSSMediaRuleImpl) ss.getCssRules().getRules().get(0);

        try {
            mediaRule.insertRule(".testStyleDef", 0);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertTrue(e.getMessage().startsWith("Syntax error"), e.getMessage());
            assertEquals(0, mediaRule.getCssRules().getLength());
        }
    }

    /**
     * Regression test for bug #56.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void insertRuleNot() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final InputSource source = new InputSource(new StringReader("@media print { }"));
        final CSSStyleSheetImpl ss = parser.parseStyleSheet(source, null);
        final CSSMediaRuleImpl mediaRule = (CSSMediaRuleImpl) ss.getCssRules().getRules().get(0);

        mediaRule.insertRule("li:not(.shiny) { height: 44px }", 0);
        assertEquals("li:not(*.shiny) { height: 44px; }", mediaRule.getCssRules().getRules().get(0).getCssText());

        mediaRule.insertRule("li:not(.cool) { height: 77px }", 0);
        assertEquals(2, mediaRule.getCssRules().getLength());
        assertEquals("li:not(*.cool) { height: 77px; }", mediaRule.getCssRules().getRules().get(0).getCssText());
        assertEquals("li:not(*.shiny) { height: 44px; }", mediaRule.getCssRules().getRules().get(1).getCssText());

        mediaRule.insertRule("li:not(*.fancy) { height: 1px }", 0);
        assertEquals(3, mediaRule.getCssRules().getLength());
        assertEquals("li:not(*.fancy) { height: 1px; }", mediaRule.getCssRules().getRules().get(0).getCssText());
        assertEquals("li:not(*.cool) { height: 77px; }", mediaRule.getCssRules().getRules().get(1).getCssText());
        assertEquals("li:not(*.shiny) { height: 44px; }", mediaRule.getCssRules().getRules().get(2).getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void deleteRule() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final InputSource source = new InputSource(new StringReader("@media print { body { font-size: 10pt } }"));
        final CSSStyleSheetImpl ss = parser.parseStyleSheet(source, null);
        final CSSMediaRuleImpl mediaRule = (CSSMediaRuleImpl) ss.getCssRules().getRules().get(0);

        mediaRule.deleteRule(0);
        assertEquals(0, mediaRule.getCssRules().getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void deleteRuleWrongIndex() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final InputSource source = new InputSource(new StringReader("@media print { }"));
        final CSSStyleSheetImpl ss = parser.parseStyleSheet(source, null);
        final CSSMediaRuleImpl mediaRule = (CSSMediaRuleImpl) ss.getCssRules().getRules().get(0);

        try {
            mediaRule.deleteRule(7);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertTrue(e.getMessage().startsWith("Index out of bounds error"), e.getMessage());
            assertEquals(0, mediaRule.getCssRules().getLength());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void asString() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final InputSource source = new InputSource(new StringReader("@media print { body { font-size: 10pt } }"));
        final CSSStyleSheetImpl ss = parser.parseStyleSheet(source, null);
        final CSSMediaRuleImpl mediaRule = (CSSMediaRuleImpl) ss.getCssRules().getRules().get(0);

        assertEquals("@media print {\n  body { font-size: 10pt; }\n}", mediaRule.toString());
    }
}
