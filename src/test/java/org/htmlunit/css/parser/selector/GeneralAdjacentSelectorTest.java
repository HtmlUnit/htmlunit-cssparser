/*
 * Copyright (c) 2019-2023 Ronald Brill.
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
package org.htmlunit.css.parser.selector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Test cases for {@link GeneralAdjacentSelector}.
 * @author Ronald Brill
 */
public class GeneralAdjacentSelectorTest {

    /**
     * @throws Exception on failure
     */
    @Test
    public void withoutParentDescendant() {
        final GeneralAdjacentSelector selector = new GeneralAdjacentSelector(null, null);
        assertNull(selector.getSelector());
        assertNull(selector.getSimpleSelector());

        assertEquals(" ~ ", selector.toString());
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void withoutParent() {
        final ElementSelector descendant = new ElementSelector("a", null);
        final GeneralAdjacentSelector selector = new GeneralAdjacentSelector(null, descendant);
        assertNull(selector.getSelector());
        assertEquals(descendant, selector.getSimpleSelector());

        assertEquals(" ~ a", selector.toString());
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void withoutDescendant() {
        final ElementSelector parent = new ElementSelector("p", null);
        final GeneralAdjacentSelector selector = new GeneralAdjacentSelector(parent, null);
        assertEquals(parent, selector.getSelector());
        assertNull(selector.getSimpleSelector());

        assertEquals("p ~ ", selector.toString());
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void both() {
        final ElementSelector parent = new ElementSelector("p", null);
        final ElementSelector descendant = new ElementSelector("a", null);
        final GeneralAdjacentSelector selector = new GeneralAdjacentSelector(parent, descendant);
        assertEquals(parent, selector.getSelector());
        assertEquals(descendant, selector.getSimpleSelector());

        assertEquals("p ~ a", selector.toString());
    }
}
