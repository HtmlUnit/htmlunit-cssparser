/*
 * Copyright (c) 2019-2021 Ronald Brill.
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
package com.gargoylesoftware.css.parser.selector;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.gargoylesoftware.css.parser.selector.Selector.SelectorType;

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
}
