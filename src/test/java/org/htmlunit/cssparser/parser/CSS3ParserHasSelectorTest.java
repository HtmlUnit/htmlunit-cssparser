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

// /* VALID CASES - Attribute selectors within :has() */
// form:has(input[required]) { border: 2px solid red; }
// div:has([data-active]) { opacity: 1; }
// section:has(a[href^="https"]) { padding-left: 20px; }
// article:has(img[alt]) { margin: 10px; }
// fieldset:has(input[type="checkbox"]) { background: #e8f4fd; }
//
// /* VALID CASES - Pseudo-class selectors within :has() */
// div:has(a:hover) { background: lightblue; }
// form:has(input:focus) { box-shadow: 0 0 5px blue; }
// ul:has(li:first-child) { margin-top: 0; }
// table:has(tr:nth-child(odd)) { border: 1px solid; }
// section:has(p:empty) { min-height: 100px; }
//
// /* VALID CASES - Combinators within :has() */
// div:has(> p) { padding: 10px; } /* Direct child */
// section:has(h1 + p) { margin: 20px; } /* Adjacent sibling */
// article:has(h2 ~ p) { line-height: 1.5; } /* General sibling */
// container:has(nav a) { position: relative; } /* Descendant */
//
// /* VALID CASES - Multiple selectors in :has() */
// div:has(p, span) { color: blue; }
// section:has(h1, h2, h3) { font-weight: bold; }
// article:has(img, video, iframe) { max-width: 100%; }
// form:has(input, textarea, select) { padding: 15px; }
//
// /* VALID CASES - Nested :has() selectors */
// div:has(section:has(p)) { border: 2px dashed green; }
// article:has(div:has(img)) { background: lightgray; }
// container:has(nav:has(ul:has(li))) { position: sticky; }
//
// /* VALID CASES - :has() with other pseudo-classes */
// div:hover:has(p) { transform: scale(1.02); }
// section:focus:has(input) { outline: 2px solid blue; }
// article:first-child:has(h1) { margin-top: 0; }
// li:last-child:has(a) { border-bottom: none; }
//
// /* VALID CASES - Complex combinators with :has() */
// main > section:has(aside) { display: grid; }
// nav + div:has(ul) { margin-top: 10px; }
// header ~ main:has(article) { padding-top: 20px; }
// .sidebar div:has(.widget) { background: white; }
//
// /* VALID CASES - :has() with :not() */
// div:has(:not(p)) { color: red; }
// section:has(h1:not(.hidden)) { display: block; }
// article:has(img:not([alt])) { border: 1px solid red; }
// form:has(input:not(:disabled)) { opacity: 1; }
//
// /* VALID CASES - :has() with :is() */
// div:has(:is(p, span)) { margin: 10px; }
// section:has(:is(h1, h2):not(.subtitle)) { padding: 15px; }
// article:has(:is(img, video)[src]) { position: relative; }
//
// /* VALID CASES - Whitespace variations */
// div:has(p) { color: red; }
// div:has( p ) { color: blue; }
// div:has(
//   p
// ) { color: green; }
// div :has(p) { color: purple; } /* Space before :has() - different meaning */
//
// /* VALID CASES - Single and empty selectors */
// div:has(span) { display: block; }
// section:has(*) { border: 1px solid; } /* Has any child */
//
// /* EDGE CASES - Complex attribute selectors */
// div:has([data-value*="test"]) { background: yellow; }
// section:has([class~="active"]) { color: green; }
// article:has([id|="section"]) { margin: 20px; }
// form:has([name$="email"]) { border: 1px solid blue; }
//
// /* EDGE CASES - Escaped characters */
// div:has(.class\:name) { color: red; }
// section:has(#id\.special) { background: blue; }
// article:has([data-test\=value]) { padding: 10px; }
//
// /* EDGE CASES - Unicode selectors */
// div:has(.class-über) { font-family: serif; }
// section:has([data-测试]) { color: red; }
// article:has(.🎉) { animation: bounce 1s; }
//
// /* INVALID CASES - Empty :has() */
// div:has() { color: red; } /* Invalid - empty selector */
// section:has( ) { color: blue; } /* Invalid - whitespace only */
// article:has(,) { color: green; } /* Invalid - empty selectors */
//
// /* INVALID CASES - Pseudo-elements inside :has() */
// div:has(p::before) { color: red; } /* Invalid - pseudo-elements not allowed */
// section:has(::first-line) { background: blue; } /* Invalid */
// article:has(span::after) { margin: 10px; } /* Invalid */
//
// /* INVALID CASES - Nested :has() with pseudo-elements */
// div:has(p:has(::before)) { color: red; } /* Invalid - pseudo-elements in nested :has() */
//
// /* INVALID CASES - :has() inside :has() with invalid selectors */
// div:has(:has()) { color: red; } /* Invalid - empty nested :has() */
// section:has(p:has(::after)) { background: blue; } /* Invalid - pseudo-element in nested :has() */
//
// /* INVALID CASES - Syntax errors */
// div:has(p { color: red; } /* Invalid - missing closing parenthesis */
// section:has p) { color: blue; } /* Invalid - missing opening parenthesis */
// article has(p) { color: green; } /* Invalid - missing colon */
// div:has[p] { color: yellow; } /* Invalid - wrong brackets */
// section:has((p)) { color: purple; } /* Invalid - double parentheses */
//
// /* INVALID CASES - Malformed selectors within :has() */
// div:has(123) { color: red; } /* Invalid - selector starting with number */
// section:has(.class--) { color: blue; } /* Invalid - malformed class name */
// article:has(#) { color: green; } /* Invalid - empty ID */
// form:has([=value]) { color: yellow; } /* Invalid - missing attribute name */
//
// /* COMPLEX VALID CASES */
// .container:has(.sidebar):has(.main-content) { display: grid; grid-template-columns: 1fr 3fr; }
// article:has(h1):has(p):not(:has(img)) { font-family: serif; }
// section:has(> div:first-child:has(h2)) { margin-top: 2rem; }
// form:has(fieldset:has(legend):has(input[required])) { border: 2px solid red; }
//
// /* PERFORMANCE TEST CASES - Deeply nested */
// div:has(div:has(div:has(div:has(p)))) { color: red; }
// section:has(article:has(header:has(h1:has(span)))) { background: lightblue; }
//
// /* EDGE CASES - :has() with various combinators */
// main:has(> section > article) { padding: 20px; }
// nav:has(ul + div) { position: relative; }
// aside:has(h3 ~ p ~ div) { border-left: 3px solid; }
//
// /* EDGE CASES - Comments within :has() */
// div:has(p /* comment */) { color: red; }
// section:has(/* comment */ h1) { background: blue; }
//
// /* EDGE CASES - Very long selector lists in :has() */
// div:has(h1, h2, h3, h4, h5, h6, p, span, div, section, article, aside, header, footer, nav, main) { font-size: 14px; }
//
// /* EDGE CASES - Case sensitivity */
// DIV:has(P) { color: red; } /* Valid - CSS is case-insensitive for HTML elements */
// div:HAS(p) { color: blue; } /* Invalid - pseudo-classes are case-sensitive */
//
// /* EDGE CASES - Specificity testing */
// div:has(p) { color: red; } /* Specificity: 0,1,1 */
// div.container:has(p.content) { color: blue; } /* Specificity: 0,2,2 */
// #main div:has(p) { color: green; } /* Specificity: 1,1,1 */
//
// /* EDGE CASES - Multiple :has() selectors */
// div:has(p):has(span) { background: yellow; }
// section:has(h1):has(img):has(a) { border: 1px solid; }
// article:has(.title):has(.content):has(.footer) { margin: 20px; }
//
// /* EDGE CASES - :has() with :where() (low specificity) */
// :where(div):has(p) { color: red; }
// div:has(:where(p, span)) { background: blue; }
//
// /* BROWSER COMPATIBILITY - Older syntax (for comparison) */
// /* Note: :has() is relatively new, no legacy equivalents exist */
//
// /* STRESS TESTS - Complex realistic scenarios */
// .card:has(.card-header):has(.card-body):not(:has(.card-footer)) {
//     border-bottom: 2px solid #ccc;
// }
//
// .form-group:has(input[required]):has(label):not(:has(.error)) {
//     border-left: 3px solid green;
// }
//
// .navigation:has(ul:has(li:has(a[href^="#"]))):has(.dropdown) {
//     position: sticky;
//     top: 0;
// }
//
// /* EDGE CASES - :has() at different positions */
// :has(p) div { color: red; } /* Invalid - :has() must be preceded by a selector */
// *:has(p) { color: blue; } /* Valid - universal selector with :has() */
// :root:has(body) { font-size: 16px; } /* Valid but unusual */

}
