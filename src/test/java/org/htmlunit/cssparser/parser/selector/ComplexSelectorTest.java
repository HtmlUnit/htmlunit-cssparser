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
package org.htmlunit.cssparser.parser.selector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.StringReader;

import org.htmlunit.cssparser.ErrorHandler;
import org.htmlunit.cssparser.dom.CSSRuleListImpl;
import org.htmlunit.cssparser.dom.CSSStyleRuleImpl;
import org.htmlunit.cssparser.dom.CSSStyleSheetImpl;
import org.htmlunit.cssparser.parser.CSSOMParser;
import org.htmlunit.cssparser.parser.InputSource;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for complex selector combinations.
 *
 * @author Ronald Brill
 */
public class ComplexSelectorTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void complexDescendantChain() throws Exception {
        final String css = "div ul li a { color: blue; }";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        assertNotNull(sheet);
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();
        assertEquals(1, rules.getLength());

        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.getRules().get(0);
        assertEquals("div ul li a", rule.getSelectorText());
        assertEquals("color: blue", rule.getStyle().getCssText());
        assertEquals(1, rule.getStyle().getLength());
        assertEquals("blue", rule.getStyle().getPropertyValue("color"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void childCombinator() throws Exception {
        final String css = "div > p { margin: 10px; }";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        assertNotNull(sheet);
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();
        assertEquals(1, rules.getLength());

        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.getRules().get(0);
        assertEquals("div > p", rule.getSelectorText());
        assertEquals("margin: 10px", rule.getStyle().getCssText());
        assertEquals(1, rule.getStyle().getLength());
        assertEquals("10px", rule.getStyle().getPropertyValue("margin"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void adjacentSiblingCombinator() throws Exception {
        final String css = "h1 + p { font-weight: bold; }";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        assertNotNull(sheet);
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();
        assertEquals(1, rules.getLength());

        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.getRules().get(0);
        assertEquals("h1 + p", rule.getSelectorText());
        assertEquals("font-weight: bold", rule.getStyle().getCssText());
        assertEquals(1, rule.getStyle().getLength());
        assertEquals("bold", rule.getStyle().getPropertyValue("font-weight"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void generalSiblingCombinator() throws Exception {
        final String css = "h1 ~ p { color: gray; }";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        assertNotNull(sheet);
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();
        assertEquals(1, rules.getLength());

        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.getRules().get(0);
        assertEquals("h1 ~ p", rule.getSelectorText());
        assertEquals("color: gray", rule.getStyle().getCssText());
        assertEquals(1, rule.getStyle().getLength());
        assertEquals("gray", rule.getStyle().getPropertyValue("color"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void classesAndIds() throws Exception {
        final String css = "div#main.container.active { display: block; }";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        assertNotNull(sheet);
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();
        assertEquals(1, rules.getLength());

        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.getRules().get(0);
        assertEquals("div#main.container.active", rule.getSelectorText());
        assertEquals("display: block", rule.getStyle().getCssText());
        assertEquals(1, rule.getStyle().getLength());
        assertEquals("block", rule.getStyle().getPropertyValue("display"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void pseudoClassCombinations() throws Exception {
        final String css = "a:link:hover:not(.external) { text-decoration: underline; }";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        assertNotNull(sheet);
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();
        assertEquals(1, rules.getLength());

        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.getRules().get(0);
        assertEquals("a:link:hover:not(*.external)", rule.getSelectorText());
        assertEquals("text-decoration: underline", rule.getStyle().getCssText());
        assertEquals(1, rule.getStyle().getLength());
        assertEquals("underline", rule.getStyle().getPropertyValue("text-decoration"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void attributeSelectorCombinations() throws Exception {
        final String css = "input[type=\"text\"][required]:focus { border-color: blue; }";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        assertNotNull(sheet);
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();
        assertEquals(1, rules.getLength());

        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.getRules().get(0);
        assertEquals("input[type=\"text\"][required]:focus", rule.getSelectorText());
        assertEquals("border-color: blue", rule.getStyle().getCssText());
        assertEquals(1, rule.getStyle().getLength());
        assertEquals("blue", rule.getStyle().getPropertyValue("border-color"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void nthChildVariations() throws Exception {
        final String css = "li:nth-child(2n) { background: gray; }\n"
                         + "li:nth-child(odd) { background: white; }\n"
                         + "li:nth-child(3n+1) { font-weight: bold; }";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        assertNotNull(sheet);
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getWarningCount());
        assertEquals(3, sheet.getCssRules().getLength());

        final CSSStyleRuleImpl rule1 = (CSSStyleRuleImpl) sheet.getCssRules().getRules().get(0);
        assertEquals("li:nth-child(2n)", rule1.getSelectorText());
        assertEquals("background: gray", rule1.getStyle().getCssText());
        assertEquals("gray", rule1.getStyle().getPropertyValue("background"));

        final CSSStyleRuleImpl rule2 = (CSSStyleRuleImpl) sheet.getCssRules().getRules().get(1);
        assertEquals("li:nth-child(odd)", rule2.getSelectorText());
        assertEquals("background: white", rule2.getStyle().getCssText());
        assertEquals("white", rule2.getStyle().getPropertyValue("background"));

        final CSSStyleRuleImpl rule3 = (CSSStyleRuleImpl) sheet.getCssRules().getRules().get(2);
        assertEquals("li:nth-child(3n+1)", rule3.getSelectorText());
        assertEquals("font-weight: bold", rule3.getStyle().getCssText());
        assertEquals("bold", rule3.getStyle().getPropertyValue("font-weight"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void pseudoElementWithPseudoClass() throws Exception {
        final String css = "p:first-child::first-letter { font-size: 2em; }";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        assertNotNull(sheet);
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();
        assertEquals(1, rules.getLength());

        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.getRules().get(0);
        assertEquals("p:first-child::first-letter", rule.getSelectorText());
        assertEquals("font-size: 2em", rule.getStyle().getCssText());
        assertEquals(1, rule.getStyle().getLength());
        assertEquals("2em", rule.getStyle().getPropertyValue("font-size"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void complexSelectorList() throws Exception {
        final String css = "h1, h2, h3, .heading, #title { font-family: Arial; }";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        assertNotNull(sheet);
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();
        assertEquals(1, rules.getLength());

        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.getRules().get(0);
        assertNotNull(rule.getSelectorText());
        assertEquals("h1, h2, h3, *.heading, *#title", rule.getSelectorText());
        assertEquals("font-family: Arial", rule.getStyle().getCssText());
        assertEquals(1, rule.getStyle().getLength());
        assertEquals("Arial", rule.getStyle().getPropertyValue("font-family"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void mixedCombinators() throws Exception {
        final String css = "div > ul li + span ~ a { color: red; }";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        assertNotNull(sheet);
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();
        assertEquals(1, rules.getLength());

        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.getRules().get(0);
        assertEquals("div > ul li + span ~ a", rule.getSelectorText());
        assertEquals("color: red", rule.getStyle().getCssText());
        assertEquals(1, rule.getStyle().getLength());
        assertEquals("red", rule.getStyle().getPropertyValue("color"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void universalSelectorCombinations() throws Exception {
        final String css = "* { box-sizing: border-box; }\n"
                         + "div * { margin: 0; }\n"
                         + "*:hover { cursor: pointer; }";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        assertNotNull(sheet);
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getWarningCount());
        assertEquals(3, sheet.getCssRules().getLength());

        final CSSStyleRuleImpl rule1 = (CSSStyleRuleImpl) sheet.getCssRules().getRules().get(0);
        assertEquals("*", rule1.getSelectorText());
        assertEquals("box-sizing: border-box", rule1.getStyle().getCssText());
        assertEquals("border-box", rule1.getStyle().getPropertyValue("box-sizing"));

        final CSSStyleRuleImpl rule2 = (CSSStyleRuleImpl) sheet.getCssRules().getRules().get(1);
        assertEquals("div *", rule2.getSelectorText());
        assertEquals("margin: 0", rule2.getStyle().getCssText());
        assertEquals("0", rule2.getStyle().getPropertyValue("margin"));

        final CSSStyleRuleImpl rule3 = (CSSStyleRuleImpl) sheet.getCssRules().getRules().get(2);
        assertEquals("*:hover", rule3.getSelectorText());
        assertEquals("cursor: pointer", rule3.getStyle().getCssText());
        assertEquals("pointer", rule3.getStyle().getPropertyValue("cursor"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void namespaceSelectorsCombinations() throws Exception {
        final String css = "svg|rect { fill: blue; }";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        assertNotNull(sheet);
        assertEquals(1, errorHandler.getErrorCount());
        assertEquals("Error in style rule. (Invalid token \"|\". Was expecting one of: <S>, \"{\", \".\", \":\", \"[\", \",\", <HASH>, <S>.)",
                        errorHandler.getErrorMessage());
        assertEquals(1, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();
        assertEquals(0, rules.getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void caseInsensitiveAttribute() throws Exception {
        final String css = "a[href*=\"example\" i] { color: blue; }";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        assertNotNull(sheet);
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();
        assertEquals(1, rules.getLength());

        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.getRules().get(0);
        assertEquals("a[href*=\"example\" i]", rule.getSelectorText());
        assertEquals("color: blue", rule.getStyle().getCssText());
        assertEquals(1, rule.getStyle().getLength());
        assertEquals("blue", rule.getStyle().getPropertyValue("color"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void deeplyNestedSelectors() throws Exception {
        final String css = "html body div.container section article > p:first-of-type { line-height: 1.5; }";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(new InputSource(new StringReader(css)), null);

        assertNotNull(sheet);
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();
        assertEquals(1, rules.getLength());

        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.getRules().get(0);
        assertEquals("html body div.container section article > p:first-of-type", rule.getSelectorText());
        assertEquals("line-height: 1.5", rule.getStyle().getCssText());
        assertEquals(1, rule.getStyle().getLength());
        assertEquals("1.5", rule.getStyle().getPropertyValue("line-height"));
    }
}
