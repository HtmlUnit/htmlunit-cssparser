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

import org.htmlunit.cssparser.parser.selector.Selector.SelectorType;
import org.junit.jupiter.api.Test;

/**
 * Testcases for {@link DescendantSelector}.
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
        assertEquals(parent, selector.getAncestorSelector());

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
        assertEquals(descendant, selector.getSimpleSelector());

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

        assertEquals("a:after", selector.toString());
    }

    /**
     * Test with null ancestor selector.
     * @throws Exception on failure
     */
    @Test
    public void nullAncestorSelector() {
        final ElementSelector descendant = new ElementSelector("a", null);
        final DescendantSelector selector = new DescendantSelector(null, descendant);
        
        assertEquals(null, selector.getAncestorSelector());
        assertEquals(descendant, selector.getSimpleSelector());
        assertEquals(" a", selector.toString());
    }

    /**
     * Test with null simple selector.
     * @throws Exception on failure
     */
    @Test
    public void nullSimpleSelector() {
        final ElementSelector parent = new ElementSelector("p", null);
        final DescendantSelector selector = new DescendantSelector(parent, null);
        
        assertEquals(parent, selector.getAncestorSelector());
        assertEquals(null, selector.getSimpleSelector());
    }

    /**
     * Test complex descendant chain.
     * @throws Exception on failure
     */
    @Test
    public void complexDescendantChain() {
        final ElementSelector grandParent = new ElementSelector("div", null);
        final ElementSelector parent = new ElementSelector("p", null);
        final DescendantSelector level1 = new DescendantSelector(grandParent, parent);
        final ElementSelector child = new ElementSelector("a", null);
        final DescendantSelector level2 = new DescendantSelector(level1, child);
        
        assertEquals(level1, level2.getAncestorSelector());
        assertEquals(child, level2.getSimpleSelector());
        assertEquals("div p a", level2.toString());
    }

    /**
     * Test descendant with multiple levels and pseudo-element.
     * @throws Exception on failure
     */
    @Test
    public void complexDescendantWithPseudoElement() {
        final ElementSelector grandParent = new ElementSelector("div", null);
        final ElementSelector parent = new ElementSelector("p", null);
        final DescendantSelector level1 = new DescendantSelector(grandParent, parent);
        final PseudoElementSelector pseudo = new PseudoElementSelector("first-line", null, false);
        final DescendantSelector level2 = new DescendantSelector(level1, pseudo);
        
        assertEquals("div p:first-line", level2.toString());
    }
}

