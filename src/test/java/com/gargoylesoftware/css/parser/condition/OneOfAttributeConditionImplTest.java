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
package com.gargoylesoftware.css.parser.condition;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.css.parser.condition.Condition.ConditionType;

/**
 * Testcases for {@link OneOfAttributeConditionImpl}.
 */
public class OneOfAttributeConditionImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutValue() throws Exception {
        final OneOfAttributeConditionImpl ac = new OneOfAttributeConditionImpl("test", null);
        Assert.assertEquals(ConditionType.ONE_OF_ATTRIBUTE_CONDITION, ac.getConditionType());
        Assert.assertEquals("test", ac.getLocalName());
        Assert.assertNull(ac.getValue());

        Assert.assertEquals("[test]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyValue() throws Exception {
        final OneOfAttributeConditionImpl ac = new OneOfAttributeConditionImpl("test", "");
        Assert.assertEquals(ConditionType.ONE_OF_ATTRIBUTE_CONDITION, ac.getConditionType());
        Assert.assertEquals("test", ac.getLocalName());
        Assert.assertEquals("", ac.getValue());

        Assert.assertEquals("[test~=\"\"]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withValue() throws Exception {
        final OneOfAttributeConditionImpl ac = new OneOfAttributeConditionImpl("test", "value");
        Assert.assertEquals(ConditionType.ONE_OF_ATTRIBUTE_CONDITION, ac.getConditionType());
        Assert.assertEquals("test", ac.getLocalName());
        Assert.assertEquals("value", ac.getValue());

        Assert.assertEquals("[test~=\"value\"]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutValueAndSpecified() throws Exception {
        final OneOfAttributeConditionImpl ac = new OneOfAttributeConditionImpl("test", null);
        Assert.assertEquals(ConditionType.ONE_OF_ATTRIBUTE_CONDITION, ac.getConditionType());
        Assert.assertEquals("test", ac.getLocalName());
        Assert.assertNull(ac.getValue());

        Assert.assertEquals("[test]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyValueAndSpecified() throws Exception {
        final OneOfAttributeConditionImpl ac = new OneOfAttributeConditionImpl("test", "");
        Assert.assertEquals(ConditionType.ONE_OF_ATTRIBUTE_CONDITION, ac.getConditionType());
        Assert.assertEquals("test", ac.getLocalName());
        Assert.assertEquals("", ac.getValue());

        Assert.assertEquals("[test~=\"\"]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withValueAndSpecified() throws Exception {
        final OneOfAttributeConditionImpl ac = new OneOfAttributeConditionImpl("test", "value");
        Assert.assertEquals(ConditionType.ONE_OF_ATTRIBUTE_CONDITION, ac.getConditionType());
        Assert.assertEquals("test", ac.getLocalName());
        Assert.assertEquals("value", ac.getValue());

        Assert.assertEquals("[test~=\"value\"]", ac.toString());
    }
}
