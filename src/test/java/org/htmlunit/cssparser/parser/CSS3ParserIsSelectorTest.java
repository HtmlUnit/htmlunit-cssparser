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
import org.htmlunit.cssparser.parser.condition.IsPseudoClassCondition;
import org.htmlunit.cssparser.parser.selector.ElementSelector;
import org.htmlunit.cssparser.parser.selector.Selector;
import org.htmlunit.cssparser.parser.selector.Selector.SelectorType;
import org.htmlunit.cssparser.parser.selector.SelectorList;
import org.junit.jupiter.api.Test;

/**
 * @author Ronald Brill
 */
public class CSS3ParserIsSelectorTest extends AbstractCSSParserTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void isElementType() throws Exception {
        // element name
        final SelectorList selectors = parseSelectors(":is(ol, ul)", 0, 0, 0);
        assertEquals("*:is(ol, ul)", selectors.get(0).toString());

        assertEquals(1, selectors.size());
        final Selector selector = selectors.get(0);

        assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, selector.getSelectorType());

        final ElementSelector elemSel = (ElementSelector) selector;
        assertEquals(1, elemSel.getConditions().size());

        final Condition condition = elemSel.getConditions().get(0);
        assertEquals(ConditionType.IS_PSEUDO_CLASS_CONDITION, condition.getConditionType());

        final IsPseudoClassCondition pseudo = (IsPseudoClassCondition) condition;
        assertEquals("ol, ul", pseudo.getValue());
        assertEquals(":is(ol, ul)", pseudo.toString());

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
        parseSelectors(":is(h1, h2, h3)", 0, 0, 0);
        parseSelectors(":is(.class1, .class2)", 0, 0, 0);
        parseSelectors(":is(#id1, #id2, #id3)", 0, 0, 0);
        parseSelectors(":is(div, span, p)", 0, 0, 0);
        parseSelectors(":is([data-attr], [title])", 0, 0, 0);
        parseSelectors(":is(input, textarea, select)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void pseudoClassCombinations() throws Exception {
        parseSelectors(":is(a:hover, a:focus)", 0, 0, 0);
        parseSelectors(":is(button:disabled, input:disabled)", 0, 0, 0);
        parseSelectors(":is(li:first-child, li:last-child)", 0, 0, 0);
        parseSelectors(":is(tr:nth-child(odd), tr:nth-child(even))", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void complex() throws Exception {
        parseSelectors(":is(.nav > li, .menu > li)", 0, 0, 0);
        parseSelectors(":is(article h1, section h1)", 0, 0, 0);
        parseSelectors(":is(.sidebar .widget, .footer .widget)", 0, 0, 0);
        parseSelectors(":is(form input[type=\"text\"], form input[type=\"email\"])", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void nested() throws Exception {
        parseSelectors(":is(:is(h1, h2), :is(h3, h4))", 0, 0, 0);
        parseSelectors(":is(article h1, section h1)", 0, 0, 0);
        parseSelectors(":is(.sidebar .widget, .footer .widget)", 0, 0, 0);
        parseSelectors(":is(.container :is(.item, .element), .wrapper :is(.item, .element))", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void descendant() throws Exception {
        parseSelectors("div :is(p, span)", 0, 0, 0);
        parseSelectors(".container :is(.item, .box)", 0, 0, 0);
        parseSelectors("article :is(h1, h2, h3)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void adjacentGeneralSibling() throws Exception {
        parseSelectors(":is(h1, h2) + p", 0, 0, 0);
        parseSelectors(":is(.alert, .warning) ~ div", 0, 0, 0);
        parseSelectors(":is(img, video) + figcaption", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void multiple() throws Exception {
        parseSelectors(":is(main, aside) > :is(section, article)", 0, 0, 0);
        parseSelectors(":is(.header, .footer) .nav :is(a, button)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void attribute() throws Exception {
        parseSelectors(":is(input[required], textarea[required])", 0, 0, 0);
        parseSelectors(":is([data-theme=\"dark\"], [data-theme=\"night\"])", 0, 0, 0);
        parseSelectors(":is(a[href^=\"http\"], a[href^=\"mailto\"])", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void pseudo() throws Exception {
        parseSelectors(":is(h1, h2, h3)::before", 0, 0, 0);
        parseSelectors(":is(blockquote, q)::after", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void whitespace() throws Exception {
        parseSelectors(":is(h1,h2,h3)", 0, 0, 0);
        parseSelectors(":is( h1 , h2 , h3 )", 0, 0, 0);
        parseSelectors(":is( h1,\n  h2,\n    h3\n )", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void single() throws Exception {
        parseSelectors(":is(div)", 0, 0, 0);
        parseSelectors(":is(.single-class)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyAndWhitespace() throws Exception {
        parseSelectors(":is()", 1, 0, 0);
        parseSelectors(":is( )", 1, 0, 0);
        parseSelectors(":is(,)", 1, 0, 0);
        parseSelectors(":is(h1,)", 1, 0, 0);
        parseSelectors(":is(h1,,h2)", 1, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void syntaxErrors() throws Exception {
        // valid!
        parseSelectors(":is(h1 h2)", 0, 0, 0);

        // parseSelectors(":is h1, h2", 1, 0, 0);
        parseSelectors("is(h1, h2)", 1, 0, 0);
        parseSelectors(":is((h1, h2))", 1, 0, 0);
        parseSelectors(":is[h1, h2]", 1, 0, 0);

        parseSelectors(":is(h1, h2", 1, 0, 0);
        parseSelectors(":is h1, h2)", 1, 0, 0);

        parseSelectors("::is(h2)", 1, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void syntaxErrorDoubleColon() throws Exception {
        String selector = "::is(h2)";

        final CSSOMParser parser = new CSSOMParser();
        ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        parser.parseSelectors(selector);

        assertEquals(1, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        assertEquals("\"::is(h2)\" is not a valid selector.", errorHandler.getErrorMessage());

        selector = "p::is(h4)";
        errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);
        parser.parseSelectors(selector);

        assertEquals(1, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        assertEquals("\"::is(h4)\" is not a valid selector.", errorHandler.getErrorMessage());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void pseudoElementsInside() throws Exception {
        parseSelectors(":is(p::before, p::after)", 0, 0, 0);
        parseSelectors(":is(::first-line, ::first-letter)", 0, 0, 0);
        parseSelectors(":is(div::placeholder, input::placeholder)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void invalidSelectorsWithin() throws Exception {
        parseSelectors(":is(123, h2)", 1, 0, 0);
        parseSelectors(":is(.class--, h2)", 0, 0, 0); // valid!
        parseSelectors(":is(#, h2)", 1, 0, 0);
        parseSelectors(":is([attr=], h2)", 1, 0, 0);
        parseSelectors(":is([=value], h2)", 1, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void caseSensitive() throws Exception {
        parseSelectors(":IS(h2)", 0, 0, 0);
        parseSelectors(":iS(h2)", 0, 0, 0);
        parseSelectors(":Is(h2)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void comment() throws Exception {
        parseSelectors(":is(h1 /* comment */, h2)", 0, 0, 0);
        parseSelectors(":is(/* comment */ h1, h2)", 0, 0, 0);
    }
}
