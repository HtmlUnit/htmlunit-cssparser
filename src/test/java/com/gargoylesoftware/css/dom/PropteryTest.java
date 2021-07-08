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
package com.gargoylesoftware.css.dom;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.css.parser.LexicalUnit;
import com.gargoylesoftware.css.parser.LexicalUnitImpl;

/**
 * Unit tests for {@link Property}.
 *
 * @author Ronald Brill
 */
public class PropteryTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defaultConstructor() throws Exception {
        final Property prop = new Property(null, null, false);
        Assert.assertEquals("null", prop.toString());
        Assert.assertNull(prop.getName());
        Assert.assertNull(prop.getValue());
        Assert.assertFalse(prop.isImportant());

        prop.setName("MyName");
        Assert.assertEquals("MyName", prop.toString());
        Assert.assertEquals("MyName", prop.getName());
        Assert.assertNull(prop.getValue());
        Assert.assertFalse(prop.isImportant());

        LexicalUnit lu = LexicalUnitImpl.createString(null, "MyValue");
        prop.setValue(new CSSValueImpl(lu, true));
        Assert.assertEquals("MyName: \"MyValue\"", prop.toString());
        Assert.assertEquals("MyName", prop.getName());
        Assert.assertEquals("\"MyValue\"", prop.getValue().toString());
        Assert.assertFalse(prop.isImportant());

        lu = LexicalUnitImpl.createPixel(null, 11);
        prop.setValue(new CSSValueImpl(lu, true));
        Assert.assertEquals("MyName: 11px", prop.toString());
        Assert.assertEquals("MyName", prop.getName());
        Assert.assertEquals("11px", prop.getValue().toString());
        Assert.assertFalse(prop.isImportant());

        prop.setImportant(true);
        Assert.assertEquals("MyName: 11px !important", prop.toString());
        Assert.assertEquals("MyName", prop.getName());
        Assert.assertEquals("11px", prop.getValue().toString());
        Assert.assertTrue(prop.isImportant());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defaultConstructorNoValueImportant() throws Exception {
        final Property prop = new Property(null, null, false);
        prop.setName("MyName");
        prop.setImportant(true);
        Assert.assertEquals("MyName !important", prop.toString());
        Assert.assertEquals("MyName", prop.getName());
        Assert.assertNull(prop.getValue());
        Assert.assertTrue(prop.isImportant());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructWithParams() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createCentimeter(null, 13.2);
        final Property prop = new Property("MyName", new CSSValueImpl(lu, true), false);
        Assert.assertEquals("MyName: 13.2cm", prop.toString());
        Assert.assertEquals("MyName", prop.getName());
        Assert.assertEquals("13.2cm", prop.getValue().toString());
        Assert.assertFalse(prop.isImportant());
    }
}
