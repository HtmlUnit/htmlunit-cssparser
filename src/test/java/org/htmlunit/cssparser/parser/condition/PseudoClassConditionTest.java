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
package org.htmlunit.cssparser.parser.condition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Testcases for {@link PseudoClassCondition}.
 *
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

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void doubleColonPrefix() throws Exception {
        final PseudoClassCondition c = new PseudoClassCondition("hover", null, true);
        assertEquals("hover", c.getValue());
        assertEquals("::hover", c.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void singleColonPrefix() throws Exception {
        final PseudoClassCondition c = new PseudoClassCondition("hover", null, false);
        assertEquals("hover", c.getValue());
        assertEquals(":hover", c.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void doubleColonWithNullValue() throws Exception {
        final PseudoClassCondition c = new PseudoClassCondition(null, null, true);
        assertNull(c.getValue());
        assertNull(c.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void conditionType() throws Exception {
        final PseudoClassCondition c = new PseudoClassCondition("active", null, false);
        assertEquals(Condition.ConditionType.PSEUDO_CLASS_CONDITION, c.getConditionType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void variousPseudoClassesDoubleColon() throws Exception {
        final PseudoClassCondition c1 = new PseudoClassCondition("first-child", null, true);
        assertEquals("::first-child", c1.toString());

        final PseudoClassCondition c2 = new PseudoClassCondition("last-child", null, true);
        assertEquals("::last-child", c2.toString());

        final PseudoClassCondition c3 = new PseudoClassCondition("nth-child(2n)", null, true);
        assertEquals("::nth-child(2n)", c3.toString());
    }
}
