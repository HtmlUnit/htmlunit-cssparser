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
package org.htmlunit.css.parser.condition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.htmlunit.css.parser.condition.Condition.ConditionType;
import org.junit.jupiter.api.Test;

/**
 * Test cases for {@link PrefixAttributeCondition}.
 * @author Ronald Brill
 */
public class PrefixAttributeConditionTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutValue() throws Exception {
        final PrefixAttributeCondition ac = new PrefixAttributeCondition("test", null, null);
        assertEquals(ConditionType.PREFIX_ATTRIBUTE_CONDITION, ac.getConditionType());
        assertEquals("test", ac.getLocalName());
        assertNull(ac.getValue());
        assertFalse(ac.isCaseInSensitive());

        assertEquals("[test]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyValue() throws Exception {
        final PrefixAttributeCondition ac = new PrefixAttributeCondition("test", "", null);
        assertEquals(ConditionType.PREFIX_ATTRIBUTE_CONDITION, ac.getConditionType());
        assertEquals("test", ac.getLocalName());
        assertEquals("", ac.getValue());
        assertFalse(ac.isCaseInSensitive());

        assertEquals("[test^=\"\"]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withValue() throws Exception {
        final PrefixAttributeCondition ac = new PrefixAttributeCondition("test", "value", null);
        assertEquals(ConditionType.PREFIX_ATTRIBUTE_CONDITION, ac.getConditionType());
        assertEquals("test", ac.getLocalName());
        assertEquals("value", ac.getValue());
        assertFalse(ac.isCaseInSensitive());

        assertEquals("[test^=\"value\"]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutValueAndSpecified() throws Exception {
        final PrefixAttributeCondition ac = new PrefixAttributeCondition("test", null, null);
        assertEquals(ConditionType.PREFIX_ATTRIBUTE_CONDITION, ac.getConditionType());
        assertEquals("test", ac.getLocalName());
        assertNull(ac.getValue());
        assertFalse(ac.isCaseInSensitive());

        assertEquals("[test]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyValueAndSpecified() throws Exception {
        final PrefixAttributeCondition ac = new PrefixAttributeCondition("test", "", null);
        assertEquals(ConditionType.PREFIX_ATTRIBUTE_CONDITION, ac.getConditionType());
        assertEquals("test", ac.getLocalName());
        assertEquals("", ac.getValue());
        assertFalse(ac.isCaseInSensitive());

        assertEquals("[test^=\"\"]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withValueAndSpecified() throws Exception {
        final PrefixAttributeCondition ac = new PrefixAttributeCondition("test", "value", null);
        assertEquals(ConditionType.PREFIX_ATTRIBUTE_CONDITION, ac.getConditionType());
        assertEquals("test", ac.getLocalName());
        assertEquals("value", ac.getValue());
        assertFalse(ac.isCaseInSensitive());

        assertEquals("[test^=\"value\"]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withValueAndSpecifiedCaseInsensitive() throws Exception {
        final PrefixAttributeCondition ac = new PrefixAttributeCondition("test", "value", true);
        assertEquals(ConditionType.PREFIX_ATTRIBUTE_CONDITION, ac.getConditionType());
        assertEquals("test", ac.getLocalName());
        assertEquals("value", ac.getValue());
        assertTrue(ac.isCaseInSensitive());

        assertEquals("[test^=\"value\" i]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withValueAndSpecifiedCaseSensitive() throws Exception {
        final PrefixAttributeCondition ac = new PrefixAttributeCondition("test", "value", false);
        assertEquals(ConditionType.PREFIX_ATTRIBUTE_CONDITION, ac.getConditionType());
        assertEquals("test", ac.getLocalName());
        assertEquals("value", ac.getValue());
        assertFalse(ac.isCaseInSensitive());

        assertEquals("[test^=\"value\" s]", ac.toString());
    }
}
