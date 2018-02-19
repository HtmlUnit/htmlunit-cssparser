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

/**
 * Testcases for {@link ClassCondition}.
 */
public class ClassConditionTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutValue() throws Exception {
        final ClassCondition c = new ClassCondition(null, null);
        Assert.assertNull(c.getLocalName());
        Assert.assertNull(c.getValue());

        Assert.assertEquals(".", c.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyValue() throws Exception {
        final ClassCondition c = new ClassCondition("", null);
        Assert.assertNull(c.getLocalName());
        Assert.assertEquals("", c.getValue());

        Assert.assertEquals(".", c.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withValue() throws Exception {
        final ClassCondition c = new ClassCondition("value", null);
        Assert.assertNull(c.getLocalName());
        Assert.assertEquals("value", c.getValue());

        Assert.assertEquals(".value", c.toString());
    }
}
