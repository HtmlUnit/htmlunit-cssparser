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
import static org.junit.jupiter.api.Assertions.assertNull;

import org.htmlunit.cssparser.parser.selector.Selector.SelectorType;
import org.junit.jupiter.api.Test;

/**
 * Testcases for {@link DescendantSelector}.
 *
 * @author Ronald Brill
 */
public class DescendantSelectorImplTest {

    /**
     * @throws Exception on failure
     */
    @Test
    public void ancestorSelector() {
        final ElementSelector parent = new ElementSelector("p", null);
        final ElementSelector descendant = new ElementSelector("a", null);
        final DescendantSelector selector = new DescendantSelector(parent, descendant);

        assertNotNull(selector.getAncestorSelector());
        assertEquals(parent, selector.getAncestorSelector());
        assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, selector.getAncestorSelector().getSelectorType());
        assertEquals("p", ((ElementSelector) selector.getAncestorSelector()).getLocalName());

        assertNotNull(selector.getSimpleSelector());
        assertEquals(descendant, selector.getSimpleSelector());
        assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, selector.getSimpleSelector().getSelectorType());

        assertEquals(SelectorType.DESCENDANT_SELECTOR, selector.getSelectorType());
        assertEquals("p a", selector.toString());
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void simpleSelector() {
        final ElementSelector parent = new ElementSelector("p", null);
        final ElementSelector descendant = new ElementSelector("a", null);
        final DescendantSelector selector = new DescendantSelector(parent, descendant);

        assertNotNull(selector.getSimpleSelector());
        assertEquals(descendant, selector.getSimpleSelector());
        assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, selector.getSimpleSelector().getSelectorType());
        assertEquals("a", ((ElementSelector) selector.getSimpleSelector()).getLocalName());

        assertEquals(SelectorType.DESCENDANT_SELECTOR, selector.getSelectorType());
        assertEquals("p a", selector.toString());
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void selectorType() {
        final ElementSelector parent = new ElementSelector("p", null);
        final ElementSelector descendant = new ElementSelector("a", null);
        final DescendantSelector selector = new DescendantSelector(parent, descendant);

        assertEquals(SelectorType.DESCENDANT_SELECTOR, selector.getSelectorType());
        assertNotNull(selector.getAncestorSelector());
        assertNotNull(selector.getSimpleSelector());
        assertEquals("p a", selector.toString());
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void elementDescendant() {
        final ElementSelector parent = new ElementSelector("p", null);
        final ElementSelector descendant = new ElementSelector("a", null);
        final DescendantSelector selector = new DescendantSelector(parent, descendant);

        assertNotNull(selector.getAncestorSelector());
        assertEquals("p", ((ElementSelector) selector.getAncestorSelector()).getLocalName());
        assertEquals("p", ((ElementSelector) selector.getAncestorSelector()).getLocalNameLowerCase());

        assertNotNull(selector.getSimpleSelector());
        assertEquals("a", ((ElementSelector) selector.getSimpleSelector()).getLocalName());
        assertEquals("a", ((ElementSelector) selector.getSimpleSelector()).getLocalNameLowerCase());

        assertEquals(SelectorType.DESCENDANT_SELECTOR, selector.getSelectorType());
        assertEquals("p a", selector.toString());
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void pseudoElementDescendant() {
        final ElementSelector parent = new ElementSelector("a", null);
        final PseudoElementSelector descendant = new PseudoElementSelector("after", null, false);
        final DescendantSelector selector = new DescendantSelector(parent, descendant);

        assertNotNull(selector.getAncestorSelector());
        assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, selector.getAncestorSelector().getSelectorType());
        assertEquals("a", ((ElementSelector) selector.getAncestorSelector()).getLocalName());

        assertNotNull(selector.getSimpleSelector());
        assertEquals(SelectorType.PSEUDO_ELEMENT_SELECTOR, selector.getSimpleSelector().getSelectorType());
        assertEquals("after", ((PseudoElementSelector) selector.getSimpleSelector()).getLocalName());

        assertEquals(SelectorType.DESCENDANT_SELECTOR, selector.getSelectorType());
        assertEquals("a:after", selector.toString());
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void nullAncestorSelector() {
        final ElementSelector descendant = new ElementSelector("a", null);
        final DescendantSelector selector = new DescendantSelector(null, descendant);

        assertNull(selector.getAncestorSelector());

        assertNotNull(selector.getSimpleSelector());
        assertEquals(descendant, selector.getSimpleSelector());
        assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, selector.getSimpleSelector().getSelectorType());
        assertEquals("a", ((ElementSelector) selector.getSimpleSelector()).getLocalName());

        assertEquals(SelectorType.DESCENDANT_SELECTOR, selector.getSelectorType());
        assertEquals(" a", selector.toString());
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void nullSimpleSelector() {
        final ElementSelector parent = new ElementSelector("p", null);
        final DescendantSelector selector = new DescendantSelector(parent, null);

        assertNotNull(selector.getAncestorSelector());
        assertEquals(parent, selector.getAncestorSelector());
        assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, selector.getAncestorSelector().getSelectorType());
        assertEquals("p", ((ElementSelector) selector.getAncestorSelector()).getLocalName());

        assertNull(selector.getSimpleSelector());

        assertEquals(SelectorType.DESCENDANT_SELECTOR, selector.getSelectorType());
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void complexDescendantChain() {
        final ElementSelector grandParent = new ElementSelector("div", null);
        final ElementSelector parent = new ElementSelector("p", null);
        final DescendantSelector level1 = new DescendantSelector(grandParent, parent);
        final ElementSelector child = new ElementSelector("a", null);
        final DescendantSelector level2 = new DescendantSelector(level1, child);

        assertNotNull(level2.getAncestorSelector());
        assertEquals(level1, level2.getAncestorSelector());
        assertEquals(SelectorType.DESCENDANT_SELECTOR, level2.getAncestorSelector().getSelectorType());

        assertNotNull(level2.getSimpleSelector());
        assertEquals(child, level2.getSimpleSelector());
        assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, level2.getSimpleSelector().getSelectorType());
        assertEquals("a", ((ElementSelector) level2.getSimpleSelector()).getLocalName());

        // Verify level1 structure
        assertNotNull(level1.getAncestorSelector());
        assertEquals(grandParent, level1.getAncestorSelector());
        assertEquals("div", ((ElementSelector) level1.getAncestorSelector()).getLocalName());

        assertNotNull(level1.getSimpleSelector());
        assertEquals(parent, level1.getSimpleSelector());
        assertEquals("p", ((ElementSelector) level1.getSimpleSelector()).getLocalName());

        assertEquals(SelectorType.DESCENDANT_SELECTOR, level2.getSelectorType());
        assertEquals("div p a", level2.toString());
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void complexDescendantWithPseudoElement() {
        final ElementSelector grandParent = new ElementSelector("div", null);
        final ElementSelector parent = new ElementSelector("p", null);
        final DescendantSelector level1 = new DescendantSelector(grandParent, parent);
        final PseudoElementSelector pseudo = new PseudoElementSelector("first-line", null, false);
        final DescendantSelector level2 = new DescendantSelector(level1, pseudo);

        assertNotNull(level2.getAncestorSelector());
        assertEquals(level1, level2.getAncestorSelector());
        assertEquals(SelectorType.DESCENDANT_SELECTOR, level2.getAncestorSelector().getSelectorType());

        assertNotNull(level2.getSimpleSelector());
        assertEquals(pseudo, level2.getSimpleSelector());
        assertEquals(SelectorType.PSEUDO_ELEMENT_SELECTOR, level2.getSimpleSelector().getSelectorType());
        assertEquals("first-line", ((PseudoElementSelector) level2.getSimpleSelector()).getLocalName());

        assertEquals(SelectorType.DESCENDANT_SELECTOR, level2.getSelectorType());
        assertEquals("div p:first-line", level2.toString());
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void bothSelectorsNull() {
        final DescendantSelector selector = new DescendantSelector(null, null);

        assertNull(selector.getAncestorSelector());
        assertNull(selector.getSimpleSelector());
        assertEquals(SelectorType.DESCENDANT_SELECTOR, selector.getSelectorType());
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void tripleNestedDescendant() {
        final ElementSelector level0 = new ElementSelector("html", null);
        final ElementSelector level1 = new ElementSelector("body", null);
        final ElementSelector level2 = new ElementSelector("div", null);
        final ElementSelector level3 = new ElementSelector("span", null);

        final DescendantSelector desc1 = new DescendantSelector(level0, level1);
        final DescendantSelector desc2 = new DescendantSelector(desc1, level2);
        final DescendantSelector desc3 = new DescendantSelector(desc2, level3);

        assertNotNull(desc3.getAncestorSelector());
        assertEquals(desc2, desc3.getAncestorSelector());
        assertEquals(SelectorType.DESCENDANT_SELECTOR, desc3.getAncestorSelector().getSelectorType());

        assertNotNull(desc3.getSimpleSelector());
        assertEquals(level3, desc3.getSimpleSelector());
        assertEquals("span", ((ElementSelector) desc3.getSimpleSelector()).getLocalName());

        assertEquals("html body div span", desc3.toString());
    }
}
