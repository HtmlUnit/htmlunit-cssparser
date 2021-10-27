/*
 * Copyright (c) 2019-2020 Ronald Brill.
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
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.gargoylesoftware.css.parser.selector.Selector.SelectorType;

/**
 * Testcases for {@link DirectAdjacentSelector}.
 * @author Ronald Brill
 */
public class DirectAdjacentSelectorTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutParentSimple() throws Exception {
        final DirectAdjacentSelector s = new DirectAdjacentSelector(null, null);
        assertEquals(SelectorType.DIRECT_ADJACENT_SELECTOR, s.getSelectorType());
        assertNull(s.getSelector());
        assertNull(s.getSimpleSelector());

        assertEquals(" + ", s.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void selectorOnly() throws Exception {
        final ElementSelector selector = new ElementSelector("p", null);
        final DirectAdjacentSelector s = new DirectAdjacentSelector(selector, null);
        assertEquals(SelectorType.DIRECT_ADJACENT_SELECTOR, s.getSelectorType());
        assertEquals(selector, s.getSelector());
        assertNull(s.getSimpleSelector());

        assertEquals("p + ", s.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void simpleOnly() throws Exception {
        final ElementSelector simple = new ElementSelector("c", null);
        final DirectAdjacentSelector s = new DirectAdjacentSelector(null, simple);
        assertEquals(SelectorType.DIRECT_ADJACENT_SELECTOR, s.getSelectorType());
        assertNull(s.getSelector());
        assertEquals(simple, s.getSimpleSelector());

        assertEquals(" + c", s.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void both() throws Exception {
        final ElementSelector selector = new ElementSelector("p", null);
        final ElementSelector simple = new ElementSelector("c", null);
        final DirectAdjacentSelector s = new DirectAdjacentSelector(selector, simple);
        assertEquals(SelectorType.DIRECT_ADJACENT_SELECTOR, s.getSelectorType());
        assertEquals(selector, s.getSelector());
        assertEquals(simple, s.getSimpleSelector());

        assertEquals("p + c", s.toString());
    }
}
