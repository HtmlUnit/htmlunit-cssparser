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

import org.htmlunit.css.parser.selector.Selector.SelectorType;
import org.junit.jupiter.api.Test;

/**
 * Testcases for {@link ElementSelector}.
 *
 * @author Ronald Brill
 */
public class ElementSelectorTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutValue() throws Exception {
        final ElementSelector s = new ElementSelector(null, null);
        assertNull(s.getLocalName());
        assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, s.getSelectorType());

        assertEquals("*", s.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyValue() throws Exception {
        final ElementSelector s = new ElementSelector("", null);
        assertEquals("", s.getLocalName());
        assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, s.getSelectorType());

        assertEquals("", s.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withValue() throws Exception {
        final ElementSelector s = new ElementSelector("value", null);
        assertEquals("value", s.getLocalName());
        assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, s.getSelectorType());

        assertEquals("value", s.toString());
    }
}
