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
 * Testcases for {@link AndCondition}.
 */
public class AndConditionTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutFirstSecond() throws Exception {
        final AndCondition ac = new AndCondition(null, null);
        Assert.assertEquals(ConditionType.AND_CONDITION, ac.getConditionType());
        Assert.assertNull(ac.getFirstCondition());
        Assert.assertNull(ac.getSecondCondition());

        Assert.assertEquals("", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void firstOnly() throws Exception {
        final AttributeCondition first = new AttributeCondition("test", null);
        final AndCondition ac = new AndCondition(first, null);
        Assert.assertEquals(ConditionType.AND_CONDITION, ac.getConditionType());
        Assert.assertEquals(first, ac.getFirstCondition());
        Assert.assertNull(ac.getSecondCondition());

        Assert.assertEquals("[test]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void secondOnly() throws Exception {
        final AttributeCondition second = new AttributeCondition("test", null);
        final AndCondition ac = new AndCondition(null, second);
        Assert.assertEquals(ConditionType.AND_CONDITION, ac.getConditionType());
        Assert.assertNull(ac.getFirstCondition());
        Assert.assertEquals(second, ac.getSecondCondition());

        Assert.assertEquals("[test]", ac.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void both() throws Exception {
        final IdCondition first = new IdCondition("value", null);
        final AttributeCondition second = new AttributeCondition("test", null);
        final AndCondition ac = new AndCondition(first, second);
        Assert.assertEquals(ConditionType.AND_CONDITION, ac.getConditionType());
        Assert.assertEquals(first, ac.getFirstCondition());
        Assert.assertEquals(second, ac.getSecondCondition());

        Assert.assertEquals("#value[test]", ac.toString());
    }
}
