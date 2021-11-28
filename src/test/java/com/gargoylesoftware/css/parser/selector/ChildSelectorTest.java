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
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.gargoylesoftware.css.parser.selector.Selector.SelectorType;

/**
 * Testcases for {@link ChildSelector}.
 * @author Ronald Brill
 */
public class ChildSelectorTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutParentSimple() throws Exception {
        final ChildSelector s = new ChildSelector(null, null);
        assertEquals(SelectorType.CHILD_SELECTOR, s.getSelectorType());
        assertNull(s.getAncestorSelector());
        assertNull(s.getSimpleSelector());

        assertEquals(" > ", s.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parentOnly() throws Exception {
        final ElementSelector parent = new ElementSelector("p", null);
        final ChildSelector s = new ChildSelector(parent, null);
        assertEquals(SelectorType.CHILD_SELECTOR, s.getSelectorType());
        assertEquals(parent, s.getAncestorSelector());
        assertNull(s.getSimpleSelector());

        assertEquals("p > ", s.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void simpleOnly() throws Exception {
        final ElementSelector simple = new ElementSelector("c", null);
        final ChildSelector s = new ChildSelector(null, simple);
        assertEquals(SelectorType.CHILD_SELECTOR, s.getSelectorType());
        assertNull(s.getAncestorSelector());
        assertEquals(simple, s.getSimpleSelector());

        assertEquals(" > c", s.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void both() throws Exception {
        final ElementSelector parent = new ElementSelector("p", null);
        final ElementSelector simple = new ElementSelector("c", null);
        final ChildSelector s = new ChildSelector(parent, simple);
        assertEquals(SelectorType.CHILD_SELECTOR, s.getSelectorType());
        assertEquals(parent, s.getAncestorSelector());
        assertEquals(simple, s.getSimpleSelector());

        assertEquals("p > c", s.toString());
    }
}
