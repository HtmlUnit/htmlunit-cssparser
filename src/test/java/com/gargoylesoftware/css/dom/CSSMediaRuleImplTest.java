/*
 * Copyright (c) 2018 Ronald Brill.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.css.dom;

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.DOMException;

import com.gargoylesoftware.css.parser.CSSOMParser;
import com.gargoylesoftware.css.parser.InputSource;

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

        Assert.assertEquals("@media print {body { font-size: 10pt } }", mediaRule.getCssText());
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
        Assert.assertEquals("*.testStyle { height: 42px }", mediaRule.getCssRules().getRules().get(0).getCssText());

        mediaRule.insertRule(".testStyle { height: 43px; }", 0);
        Assert.assertEquals("*.testStyle { height: 43px }", mediaRule.getCssRules().getRules().get(0).getCssText());
        Assert.assertEquals("*.testStyle { height: 42px }", mediaRule.getCssRules().getRules().get(1).getCssText());

        mediaRule.insertRule(".testStyle { height: 44px; }", 2);
        Assert.assertEquals("*.testStyle { height: 43px }", mediaRule.getCssRules().getRules().get(0).getCssText());
        Assert.assertEquals("*.testStyle { height: 42px }", mediaRule.getCssRules().getRules().get(1).getCssText());
        Assert.assertEquals("*.testStyle { height: 44px }", mediaRule.getCssRules().getRules().get(2).getCssText());

        mediaRule.insertRule(".testStyle { height: 45px; }", 2);
        Assert.assertEquals("*.testStyle { height: 43px }", mediaRule.getCssRules().getRules().get(0).getCssText());
        Assert.assertEquals("*.testStyle { height: 42px }", mediaRule.getCssRules().getRules().get(1).getCssText());
        Assert.assertEquals("*.testStyle { height: 45px }", mediaRule.getCssRules().getRules().get(2).getCssText());
        Assert.assertEquals("*.testStyle { height: 44px }", mediaRule.getCssRules().getRules().get(3).getCssText());
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
        Assert.assertEquals("*.testStyleDef { height: 42px }", mediaRule.getCssRules().getRules().get(0).getCssText());

        mediaRule.insertRule("      .testStyleDef { height: 43px;}   ", 0);
        Assert.assertEquals("*.testStyleDef { height: 43px }", mediaRule.getCssRules().getRules().get(0).getCssText());
        Assert.assertEquals("*.testStyleDef { height: 42px }", mediaRule.getCssRules().getRules().get(1).getCssText());

        mediaRule.insertRule("\t.testStyleDef { height: 44px; }\r\n", 0);
        Assert.assertEquals("*.testStyleDef { height: 44px }", mediaRule.getCssRules().getRules().get(0).getCssText());
        Assert.assertEquals("*.testStyleDef { height: 43px }", mediaRule.getCssRules().getRules().get(1).getCssText());
        Assert.assertEquals("*.testStyleDef { height: 42px }", mediaRule.getCssRules().getRules().get(2).getCssText());
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
            Assert.fail("DOMException expected");
        }
        catch (final DOMException e) {
            Assert.assertTrue(e.getMessage(), e.getMessage().startsWith("Syntax error"));
            Assert.assertEquals(0, mediaRule.getCssRules().getLength());
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
        Assert.assertEquals("li:not(.shiny) { height: 44px }", mediaRule.getCssRules().getRules().get(0).getCssText());

        try {
            mediaRule.insertRule("li:not(*.shiny) { height: 44px }", 0);
            Assert.fail("DOMException expected");
        }
        catch (final DOMException e) {
            Assert.assertTrue(e.getMessage(), e.getMessage().startsWith("Syntax error"));
            Assert.assertEquals(1, mediaRule.getCssRules().getLength());
        }
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
        Assert.assertEquals(0, mediaRule.getCssRules().getLength());
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
            Assert.fail("DOMException expected");
        }
        catch (final DOMException e) {
            Assert.assertTrue(e.getMessage(), e.getMessage().startsWith("Index out of bounds error"));
            Assert.assertEquals(0, mediaRule.getCssRules().getLength());
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

        Assert.assertEquals("@media print {body { font-size: 10pt } }", mediaRule.toString());
    }
}
