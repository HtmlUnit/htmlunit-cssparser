/*
 * Copyright (c) 2019 Ronald Brill.
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
        Assert.assertNull(selector.getSelector());
        Assert.assertNull(selector.getSimpleSelector());

        Assert.assertEquals(" ~ ", selector.toString());
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void withoutParent() {
        final ElementSelector descendant = new ElementSelector("a", null);
        final GeneralAdjacentSelector selector = new GeneralAdjacentSelector(null, descendant);
        Assert.assertNull(selector.getSelector());
        Assert.assertEquals(descendant, selector.getSimpleSelector());

        Assert.assertEquals(" ~ a", selector.toString());
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void withoutDescendant() {
        final ElementSelector parent = new ElementSelector("p", null);
        final GeneralAdjacentSelector selector = new GeneralAdjacentSelector(parent, null);
        Assert.assertEquals(parent, selector.getSelector());
        Assert.assertNull(null, selector.getSimpleSelector());

        Assert.assertEquals("p ~ ", selector.toString());
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void both() {
        final ElementSelector parent = new ElementSelector("p", null);
        final ElementSelector descendant = new ElementSelector("a", null);
        final GeneralAdjacentSelector selector = new GeneralAdjacentSelector(parent, descendant);
        Assert.assertEquals(parent, selector.getSelector());
        Assert.assertEquals(descendant, selector.getSimpleSelector());

        Assert.assertEquals("p ~ a", selector.toString());
    }
}
