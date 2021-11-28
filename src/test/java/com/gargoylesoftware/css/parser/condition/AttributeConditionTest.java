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
package com.gargoylesoftware.css.parser.condition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.gargoylesoftware.css.parser.condition.Condition.ConditionType;

/**
 * Testcases for {@link AttributeCondition}.
 * @author Ronald Brill
 */
public class AttributeConditionTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutValue() throws Exception {
        final AttributeCondition ac = new AttributeCondition("test", null);
        assertEquals(ConditionType.ATTRIBUTE_CONDITION, ac.getConditionType());
        assertEquals("test", ac.getLocalName());
        assertNull(ac.getValue());

        assertEquals("[test]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyValue() throws Exception {
        final AttributeCondition ac = new AttributeCondition("test", "");
        assertEquals(ConditionType.ATTRIBUTE_CONDITION, ac.getConditionType());
        assertEquals("test", ac.getLocalName());
        assertEquals("", ac.getValue());

        assertEquals("[test=\"\"]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withValue() throws Exception {
        final AttributeCondition ac = new AttributeCondition("test", "value");
        assertEquals(ConditionType.ATTRIBUTE_CONDITION, ac.getConditionType());
        assertEquals("test", ac.getLocalName());
        assertEquals("value", ac.getValue());

        assertEquals("[test=\"value\"]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyValueAndSpecified() throws Exception {
        final AttributeCondition ac = new AttributeCondition("test", "");
        assertEquals(ConditionType.ATTRIBUTE_CONDITION, ac.getConditionType());
        assertEquals("test", ac.getLocalName());
        assertEquals("", ac.getValue());

        assertEquals("[test=\"\"]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutValueAndSpecified() throws Exception {
        final AttributeCondition ac = new AttributeCondition("test", null);
        assertEquals(ConditionType.ATTRIBUTE_CONDITION, ac.getConditionType());
        assertEquals("test", ac.getLocalName());
        assertNull(ac.getValue());

        assertEquals("[test]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withValueAndSpecified() throws Exception {
        final AttributeCondition ac = new AttributeCondition("test", "value");
        assertEquals(ConditionType.ATTRIBUTE_CONDITION, ac.getConditionType());
        assertEquals("test", ac.getLocalName());
        assertEquals("value", ac.getValue());

        assertEquals("[test=\"value\"]", ac.toString());
    }
}
