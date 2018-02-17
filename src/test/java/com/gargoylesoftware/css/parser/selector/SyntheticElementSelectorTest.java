/*
 * Copyright (c) 2018 Ronald Brill.
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
 * Testcases for {@link SyntheticElementSelector}.
 */
public class SyntheticElementSelectorTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void ctor() throws Exception {
        final SyntheticElementSelector s = new SyntheticElementSelector();
        Assert.assertNull(s.getLocalName());
        Assert.assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, s.getSelectorType());

        Assert.assertEquals("", s.toString());
    }
}
