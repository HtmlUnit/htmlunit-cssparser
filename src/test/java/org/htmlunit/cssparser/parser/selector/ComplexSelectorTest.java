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
     * Test complex descendant selector chain.
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
        
        final CSSRuleListImpl rules = sheet.getCssRules();
        assertEquals(1, rules.getLength());
    }

    /**
     * Test child combinator selector.
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
    }

    /**
     * Test adjacent sibling combinator.
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
    }

    /**
     * Test general sibling combinator.
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
    }

    /**
     * Test complex selector with classes and IDs.
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
    }

    /**
     * Test pseudo-class combinations.
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
    }

    /**
     * Test attribute selector combinations.
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
    }

    /**
     * Test nth-child pseudo-class variations.
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
        assertEquals(3, sheet.getCssRules().getLength());
    }

    /**
     * Test pseudo-element with pseudo-class.
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
    }

    /**
     * Test complex selector list.
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
        
        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) sheet.getCssRules().getRules().get(0);
        assertNotNull(rule.getSelectorText());
    }

    /**
     * Test mixed combinators.
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
    }

    /**
     * Test universal selector combinations.
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
        assertEquals(3, sheet.getCssRules().getLength());
    }

    /**
     * Test namespace selectors.
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
    }

    /**
     * Test case-insensitive attribute selector.
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
    }

    /**
     * Test deeply nested selectors.
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
    }
}
