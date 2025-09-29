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

import org.htmlunit.cssparser.ErrorHandler;
import org.htmlunit.cssparser.parser.condition.Condition;
import org.htmlunit.cssparser.parser.condition.Condition.ConditionType;
import org.htmlunit.cssparser.parser.condition.WherePseudoClassCondition;
import org.htmlunit.cssparser.parser.selector.ElementSelector;
import org.htmlunit.cssparser.parser.selector.Selector;
import org.htmlunit.cssparser.parser.selector.Selector.SelectorType;
import org.htmlunit.cssparser.parser.selector.SelectorList;
import org.junit.jupiter.api.Test;

/**
 * @author Ronald Brill
 */
public class CSS3ParserWhereSelectorTest extends AbstractCSSParserTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void isElementType() throws Exception {
        // element name
        final SelectorList selectors = parseSelectors(":where(ol, ul)", 0, 0, 0);
        assertEquals("*:where(ol, ul)", selectors.get(0).toString());

        assertEquals(1, selectors.size());
        final Selector selector = selectors.get(0);

        assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, selector.getSelectorType());

        final ElementSelector elemSel = (ElementSelector) selector;
        assertEquals(1, elemSel.getConditions().size());

        final Condition condition = elemSel.getConditions().get(0);
        assertEquals(ConditionType.WHERE_PSEUDO_CLASS_CONDITION, condition.getConditionType());

        final WherePseudoClassCondition pseudo = (WherePseudoClassCondition) condition;
        assertEquals("ol, ul", pseudo.getValue());
        assertEquals(":where(ol, ul)", pseudo.toString());

        final SelectorList conditionSelectors = pseudo.getSelectors();
        assertEquals(2, conditionSelectors.size());
        final Selector conditionSelector = conditionSelectors.get(0);
        final ElementSelector conditionElemSelector = (ElementSelector) conditionSelector;
        assertEquals("ol", conditionElemSelector.getElementName());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void basic() throws Exception {
        parseSelectors(":where(h1, h2, h3)", 0, 0, 0);
        parseSelectors(":where(.class1, .class2)", 0, 0, 0);
        parseSelectors(":where(#id1, #id2, #id3)", 0, 0, 0);
        parseSelectors(":where(div, span, p)", 0, 0, 0);
        parseSelectors(":where([data-attr], [title])", 0, 0, 0);
        parseSelectors(":where(input, textarea, select)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void pseudoClassCombinations() throws Exception {
        parseSelectors(":where(a:hover, a:focus)", 0, 0, 0);
        parseSelectors(":where(button:disabled, input:disabled)", 0, 0, 0);
        parseSelectors(":where(li:first-child, li:last-child)", 0, 0, 0);
        parseSelectors(":where(tr:nth-child(odd), tr:nth-child(even))", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void complex() throws Exception {
        parseSelectors(":where(.nav > li, .menu > li)", 0, 0, 0);
        parseSelectors(":where(article h1, section h1)", 0, 0, 0);
        parseSelectors(":where(.sidebar .widget, .footer .widget)", 0, 0, 0);
        parseSelectors(":where(form input[type=\"text\"], form input[type=\"email\"])", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void nested() throws Exception {
        parseSelectors(":where(:where(h1, h2), :where(h3, h4))", 0, 0, 0);
        parseSelectors(":where(article h1, section h1)", 0, 0, 0);
        parseSelectors(":where(.sidebar .widget, .footer .widget)", 0, 0, 0);
        parseSelectors(":where(.container :where(.item, .element), .wrapper :where(.item, .element))", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void descendant() throws Exception {
        parseSelectors("div :where(p, span)", 0, 0, 0);
        parseSelectors(".container :where(.item, .box)", 0, 0, 0);
        parseSelectors("article :where(h1, h2, h3)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void adjacentGeneralSibling() throws Exception {
        parseSelectors(":where(h1, h2) + p", 0, 0, 0);
        parseSelectors(":where(.alert, .warning) ~ div", 0, 0, 0);
        parseSelectors(":where(img, video) + figcaption", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void multiple() throws Exception {
        parseSelectors(":where(main, aside) > :where(section, article)", 0, 0, 0);
        parseSelectors(":where(.header, .footer) .nav :where(a, button)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void attribute() throws Exception {
        parseSelectors(":where(input[required], textarea[required])", 0, 0, 0);
        parseSelectors(":where([data-theme=\"dark\"], [data-theme=\"night\"])", 0, 0, 0);
        parseSelectors(":where(a[href^=\"http\"], a[href^=\"mailto\"])", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void pseudo() throws Exception {
        parseSelectors(":where(h1, h2, h3)::before", 0, 0, 0);
        parseSelectors(":where(blockquote, q)::after", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void whitespace() throws Exception {
        parseSelectors(":where(h1,h2,h3)", 0, 0, 0);
        parseSelectors(":where( h1 , h2 , h3 )", 0, 0, 0);
        parseSelectors(":where( h1,\n  h2,\n    h3\n )", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void single() throws Exception {
        parseSelectors(":where(div)", 0, 0, 0);
        parseSelectors(":where(.single-class)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyAndWhitespace() throws Exception {
        parseSelectors(":where()", 1, 0, 0);
        parseSelectors(":where( )", 1, 0, 0);
        parseSelectors(":where(,)", 1, 0, 0);
        parseSelectors(":where(h1,)", 1, 0, 0);
        parseSelectors(":where(h1,,h2)", 1, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void syntaxErrors() throws Exception {
        // parseSelectors(":where(h1 h2)", 1, 0, 0);
        // parseSelectors(":is h1, h2", 1, 0, 0);
        parseSelectors("where(h1, h2)", 1, 0, 0);
        parseSelectors(":where((h1, h2))", 1, 0, 0);
        parseSelectors(":where[h1, h2]", 1, 0, 0);

        parseSelectors(":where(h1, h2", 1, 0, 0);
        parseSelectors(":where h1, h2)", 1, 0, 0);

        parseSelectors("::where(h2)", 1, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void syntaxErrorDoubleColon() throws Exception {
        String selector = "::where(h2)";

        final CSSOMParser parser = new CSSOMParser();
        ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        parser.parseSelectors(selector);

        assertEquals(1, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        assertEquals("\"::where(h2)\" is not a valid selector.", errorHandler.getErrorMessage());

        selector = "p::where(h4)";
        errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        parser.parseSelectors(selector);

        assertEquals(1, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        assertEquals("\"::where(h4)\" is not a valid selector.", errorHandler.getErrorMessage());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void pseudoElementsInside() throws Exception {
        parseSelectors(":where(p::before, p::after)", 0, 0, 0);
        parseSelectors(":where(::first-line, ::first-letter)", 0, 0, 0);
        parseSelectors(":where(div::placeholder, input::placeholder)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void invalidSelectorsWithin() throws Exception {
        parseSelectors(":where(123, h2)", 1, 0, 0);
        //parseSelectors(":where(.class--, h2)", 1, 0, 0);
        parseSelectors(":where(#, h2)", 1, 0, 0);
        parseSelectors(":where([attr=], h2)", 1, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void caseSensitive() throws Exception {
        parseSelectors(":where(h2)", 0, 0, 0);
        parseSelectors(":where(h2)", 0, 0, 0);
        parseSelectors(":where(h2)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void comment() throws Exception {
        parseSelectors(":where(h1 /* comment */, h2)", 0, 0, 0);
        parseSelectors(":where(/* comment */ h1, h2)", 0, 0, 0);
    }
}
