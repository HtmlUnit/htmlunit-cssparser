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
package com.gargoylesoftware.css.parser.condition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Testcases for {@link PseudoClassCondition}.
 * @author Ronald Brill
 */
public class PseudoClassConditionTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutValue() throws Exception {
        final PseudoClassCondition c = new PseudoClassCondition(null, null, false);
        assertNull(c.getLocalName());
        assertNull(c.getValue());

        assertNull(c.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyValue() throws Exception {
        final PseudoClassCondition c = new PseudoClassCondition("", null, false);
        assertNull(c.getLocalName());
        assertEquals("", c.getValue());

        assertEquals(":", c.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withValue() throws Exception {
        final PseudoClassCondition c = new PseudoClassCondition("value", null, false);
        assertNull(c.getLocalName());
        assertEquals("value", c.getValue());

        assertEquals(":value", c.toString());
    }
}
