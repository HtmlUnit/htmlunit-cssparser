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

import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertEquals(SelectorType.CHILD_SELECTOR, s.getSelectorType());
        Assert.assertNull(s.getAncestorSelector());
        Assert.assertNull(s.getSimpleSelector());

        Assert.assertEquals(" > ", s.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parentOnly() throws Exception {
        final ElementSelector parent = new ElementSelector("p", null);
        final ChildSelector s = new ChildSelector(parent, null);
        Assert.assertEquals(SelectorType.CHILD_SELECTOR, s.getSelectorType());
        Assert.assertEquals(parent, s.getAncestorSelector());
        Assert.assertNull(s.getSimpleSelector());

        Assert.assertEquals("p > ", s.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void simpleOnly() throws Exception {
        final ElementSelector simple = new ElementSelector("c", null);
        final ChildSelector s = new ChildSelector(null, simple);
        Assert.assertEquals(SelectorType.CHILD_SELECTOR, s.getSelectorType());
        Assert.assertNull(s.getAncestorSelector());
        Assert.assertEquals(simple, s.getSimpleSelector());

        Assert.assertEquals(" > c", s.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void both() throws Exception {
        final ElementSelector parent = new ElementSelector("p", null);
        final ElementSelector simple = new ElementSelector("c", null);
        final ChildSelector s = new ChildSelector(parent, simple);
        Assert.assertEquals(SelectorType.CHILD_SELECTOR, s.getSelectorType());
        Assert.assertEquals(parent, s.getAncestorSelector());
        Assert.assertEquals(simple, s.getSimpleSelector());

        Assert.assertEquals("p > c", s.toString());
    }
}
