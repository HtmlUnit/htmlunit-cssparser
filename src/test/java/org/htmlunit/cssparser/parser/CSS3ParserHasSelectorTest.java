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

import org.htmlunit.cssparser.parser.condition.Condition;
import org.htmlunit.cssparser.parser.condition.Condition.ConditionType;
import org.htmlunit.cssparser.parser.condition.HasPseudoClassCondition;
import org.htmlunit.cssparser.parser.selector.ChildSelector;
import org.htmlunit.cssparser.parser.selector.ElementSelector;
import org.htmlunit.cssparser.parser.selector.RelativeSelector;
import org.htmlunit.cssparser.parser.selector.Selector;
import org.htmlunit.cssparser.parser.selector.Selector.SelectorType;
import org.htmlunit.cssparser.parser.selector.SelectorList;
import org.junit.jupiter.api.Test;

/**
 * @author Ronald Brill
 */
public class CSS3ParserHasSelectorTest extends AbstractCSSParserTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void hasPlus() throws Exception {
        // element name
        final SelectorList selectors = createSelectors(":has(+ div#topic > #reference)");
        assertEquals("*:has(+ div#topic > *#reference)", selectors.get(0).toString());

        assertEquals(1, selectors.size());
        final Selector selector = selectors.get(0);

        assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, selector.getSelectorType());

        final ElementSelector elemSel = (ElementSelector) selector;
        assertEquals(1, elemSel.getConditions().size());

        final Condition condition = elemSel.getConditions().get(0);
        assertEquals(ConditionType.HAS_PSEUDO_CLASS_CONDITION, condition.getConditionType());

        final HasPseudoClassCondition pseudo = (HasPseudoClassCondition) condition;
        assertEquals("+ div#topic > *#reference", pseudo.getValue());
        assertEquals(":has(+ div#topic > *#reference)", pseudo.toString());

        final SelectorList conditionSelectors = pseudo.getSelectors();
        assertEquals(1, conditionSelectors.size());
        final Selector conditionSelector = conditionSelectors.get(0);
        final RelativeSelector conditionRelativeSelector = (RelativeSelector) conditionSelector;

        final ChildSelector conditionChildSelector = (ChildSelector) conditionRelativeSelector.getSelector();
        assertEquals("div#topic > *#reference", conditionChildSelector.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void basic() throws Exception {
        parseSelectors("div:has(p)", 0, 0, 0);
        parseSelectors("section:has(h1)", 0, 0, 0);
        parseSelectors("article:has(img)", 0, 0, 0);
        parseSelectors("form:has(input)", 0, 0, 0);
        parseSelectors("ul:has(li)", 0, 0, 0);

        parseSelectors("div:has(.warning)", 0, 0, 0);
        parseSelectors("section:has(#main-title)", 0, 0, 0);
        parseSelectors("article:has(.featured)", 0, 0, 0);
        parseSelectors("container:has(#sidebar)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void attribute() throws Exception {
        parseSelectors("form:has(input[required])", 0, 0, 0);
        parseSelectors("div:has([data-active])", 0, 0, 0);
        parseSelectors("section:has(a[href^=\"https\"])", 0, 0, 0);
        parseSelectors("article:has(img[alt])", 0, 0, 0);
        parseSelectors("fieldset:has(input[type=\"checkbox\"])", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void pseudo() throws Exception {
        parseSelectors("div:has(a:hover)", 0, 0, 0);
        parseSelectors("form:has(input:focus)", 0, 0, 0);
        parseSelectors("ul:has(li:first-child)", 0, 0, 0);
        parseSelectors("table:has(tr:nth-child(odd))", 0, 0, 0);
        parseSelectors("section:has(p:empty)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void combinators() throws Exception {
        parseSelectors("div:has(> p)", 0, 0, 0);
        parseSelectors("section:has(h1 + p)", 0, 0, 0);
        parseSelectors("article:has(h2 ~ p)", 0, 0, 0);
        parseSelectors("container:has(nav a)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void multiple() throws Exception {
        parseSelectors("div:has(p, span)", 0, 0, 0);
        parseSelectors("section:has(h1, h2, h3)", 0, 0, 0);
        parseSelectors("article:has(img, video, iframe)", 0, 0, 0);
        parseSelectors("form:has(input, textarea, select)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void nested() throws Exception {
        parseSelectors("div:has(section:has(p))", 0, 0, 0);
        parseSelectors("article:has(div:has(img))", 0, 0, 0);
        parseSelectors("container:has(nav:has(ul:has(li)))", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void mixedPseudo() throws Exception {
        parseSelectors("div:hover:has(p)", 0, 0, 0);
        parseSelectors("section:focus:has(input)", 0, 0, 0);
        parseSelectors("article:first-child:has(h1)", 0, 0, 0);
        parseSelectors("li:last-child:has(a)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void complexCombinators() throws Exception {
        parseSelectors("main > section:has(aside)", 0, 0, 0);
        parseSelectors("nav + div:has(ul)", 0, 0, 0);
        parseSelectors("header ~ main:has(article)", 0, 0, 0);
        parseSelectors(".sidebar div:has(.widget)", 0, 0, 0);
        parseSelectors("main:has(> section > article)", 0, 0, 0);
        parseSelectors("aside:has(h3 ~ p ~ div)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void mixedNot() throws Exception {
        parseSelectors("div:has(:not(p))", 0, 0, 0);
        parseSelectors("section:has(h1:not(.hidden))", 0, 0, 0);
        parseSelectors("article:has(img:not([alt]))", 0, 0, 0);
        parseSelectors("form:has(input:not(:disabled))", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void mixedIs() throws Exception {
        parseSelectors("div:has(:is(p, span))", 0, 0, 0);
        parseSelectors("section:has(:is(h1, h2):not(.subtitle))", 0, 0, 0);
        parseSelectors("article:has(:is(img, video)[src])", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void whitespace() throws Exception {
        parseSelectors("div:has(p)", 0, 0, 0);
        parseSelectors("div:has( p )", 0, 0, 0);
        parseSelectors("div:has(\n   p\n )", 0, 0, 0);
        parseSelectors("div :has(p)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void empty() throws Exception {
        parseSelectors("section:has(*)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void complex() throws Exception {
        parseSelectors("div:has([data-value*=\"test\"])", 0, 0, 0);
        parseSelectors("section:has([class~=\"active\"])", 0, 0, 0);
        parseSelectors("article:has([id|=\"section\"])", 0, 0, 0);
        parseSelectors("form:has([name$=\"email\"])", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void syntaxErrors() throws Exception {
        parseSelectors("div:has()", 1, 0, 0);
        parseSelectors("section:has( )", 1, 0, 0);
        parseSelectors("article:has(,)", 1, 0, 0);

        parseSelectors(":has(h1", 1, 0, 0);
        parseSelectors(":has h1, h2)", 1, 0, 0);
        parseSelectors("has(h1, h2)", 1, 0, 0);
        parseSelectors(":has[h1, h2]", 1, 0, 0);
        parseSelectors("has((h1, h2)", 1, 0, 0);
        parseSelectors("has(h1, h2))", 1, 0, 0);
        parseSelectors("has((h1, h2))", 1, 0, 0);

        parseSelectors(":has(123, h2)", 1, 0, 0);
        parseSelectors(":has(.class--, h2)", 0, 0, 0); // valid!
        parseSelectors(":has(#, h2)", 1, 0, 0);
        parseSelectors(":has([attr=], h2)", 1, 0, 0);
        parseSelectors(":has([=value], h2)", 1, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void pseudoElementsInside() throws Exception {
        // todo parseSelectors("div:has(p::before)", 1, 0, 0);
        // todo parseSelectors("section:has(::first-line)", 1, 0, 0);
        // todo parseSelectors("article:has(span::after)", 1, 0, 0);
        // todo parseSelectors("div:has(p:has(::before))", 1, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void caseSensitive() throws Exception {
        parseSelectors(":HAS(h2)", 0, 0, 0);
        parseSelectors(":haS(h2)", 0, 0, 0);
        parseSelectors(":Has(h2)", 0, 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void comment() throws Exception {
        parseSelectors(":has(h1 /* comment */, h2)", 0, 0, 0);
        parseSelectors(":has(/* comment */ h1, h2)", 0, 0, 0);
    }
}
