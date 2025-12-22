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
package org.htmlunit.cssparser.parser.media;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.htmlunit.cssparser.dom.CSSValueImpl;
import org.htmlunit.cssparser.dom.Property;
import org.junit.jupiter.api.Test;

/**
 * Testcases for {@link MediaQuery}.
 * @author Ronald Brill
 */
public class MediaQueryTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void testToString() throws Exception {
        MediaQuery mq = new MediaQuery("test");
        assertEquals("test", mq.toString());

        mq = new MediaQuery("test", false, false);
        assertEquals("test", mq.toString());

        mq = new MediaQuery("test", true, false);
        assertEquals("only test", mq.toString());

        mq = new MediaQuery("test", false, true);
        assertEquals("not test", mq.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void properties() throws Exception {
        Property prop = new Property("prop", new CSSValueImpl(null), false);
        MediaQuery mq = new MediaQuery("test");
        mq.addMediaProperty(prop);
        assertEquals("test and (prop: )", mq.toString());

        final CSSValueImpl value = new CSSValueImpl(null);
        value.setCssText("10dpi");
        prop = new Property("prop", value, false);
        mq = new MediaQuery("test", true, false);
        mq.addMediaProperty(prop);
        assertEquals("only test and (prop: 10dpi)", mq.toString());

        assertEquals(1, mq.getProperties().size());

        prop = new Property("min-foo", value, false);
        mq.addMediaProperty(prop);
        assertEquals("only test and (prop: 10dpi) and (min-foo: 10dpi)", mq.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void media() throws Exception {
        final MediaQuery mq = new MediaQuery("test");
        assertEquals("test", mq.getMedia());
    }

    /**
     * Test isOnly method.
     * @throws Exception if any error occurs
     */
    @Test
    public void isOnly() throws Exception {
        MediaQuery mq = new MediaQuery("screen", false, false);
        assertEquals(false, mq.isOnly());
        
        mq = new MediaQuery("screen", true, false);
        assertEquals(true, mq.isOnly());
    }

    /**
     * Test isNot method.
     * @throws Exception if any error occurs
     */
    @Test
    public void isNot() throws Exception {
        MediaQuery mq = new MediaQuery("screen", false, false);
        assertEquals(false, mq.isNot());
        
        mq = new MediaQuery("screen", false, true);
        assertEquals(true, mq.isNot());
    }

    /**
     * Test with both only and not (edge case).
     * @throws Exception if any error occurs
     */
    @Test
    public void onlyAndNot() throws Exception {
        // If both only and not are true, only takes precedence
        final MediaQuery mq = new MediaQuery("screen", true, true);
        assertEquals("only screen", mq.toString());
    }

    /**
     * Test with null media type.
     * @throws Exception if any error occurs
     */
    @Test
    public void nullMediaType() throws Exception {
        final MediaQuery mq = new MediaQuery(null);
        // null media is converted to "all" but implicitAll is set
        // so toString doesn't show it unless there are properties
        assertEquals("", mq.toString());
        assertEquals("all", mq.getMedia());
    }

    /**
     * Test with empty media type.
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyMediaType() throws Exception {
        final MediaQuery mq = new MediaQuery("");
        assertEquals("", mq.toString());
    }

    /**
     * Test multiple properties.
     * @throws Exception if any error occurs
     */
    @Test
    public void multipleProperties() throws Exception {
        final MediaQuery mq = new MediaQuery("screen");
        
        final CSSValueImpl value1 = new CSSValueImpl(null);
        value1.setCssText("800px");
        final Property prop1 = new Property("min-width", value1, false);
        mq.addMediaProperty(prop1);
        
        final CSSValueImpl value2 = new CSSValueImpl(null);
        value2.setCssText("1200px");
        final Property prop2 = new Property("max-width", value2, false);
        mq.addMediaProperty(prop2);
        
        final CSSValueImpl value3 = new CSSValueImpl(null);
        value3.setCssText("landscape");
        final Property prop3 = new Property("orientation", value3, false);
        mq.addMediaProperty(prop3);
        
        assertEquals("screen and (min-width: 800px) and (max-width: 1200px) and (orientation: landscape)", 
                     mq.toString());
        assertEquals(3, mq.getProperties().size());
    }

    /**
     * Test common media types.
     * @throws Exception if any error occurs
     */
    @Test
    public void commonMediaTypes() throws Exception {
        MediaQuery mq = new MediaQuery("all");
        assertEquals("all", mq.toString());
        
        mq = new MediaQuery("screen");
        assertEquals("screen", mq.toString());
        
        mq = new MediaQuery("print");
        assertEquals("print", mq.toString());
        
        mq = new MediaQuery("speech");
        assertEquals("speech", mq.toString());
    }

    /**
     * Test with only and properties.
     * @throws Exception if any error occurs
     */
    @Test
    public void onlyWithProperties() throws Exception {
        final MediaQuery mq = new MediaQuery("print", true, false);
        
        final CSSValueImpl value = new CSSValueImpl(null);
        value.setCssText("300dpi");
        final Property prop = new Property("resolution", value, false);
        mq.addMediaProperty(prop);
        
        assertEquals("only print and (resolution: 300dpi)", mq.toString());
    }

    /**
     * Test with not and properties.
     * @throws Exception if any error occurs
     */
    @Test
    public void notWithProperties() throws Exception {
        final MediaQuery mq = new MediaQuery("screen", false, true);
        
        final CSSValueImpl value = new CSSValueImpl(null);
        value.setCssText("600px");
        final Property prop = new Property("max-width", value, false);
        mq.addMediaProperty(prop);
        
        assertEquals("not screen and (max-width: 600px)", mq.toString());
    }
}

