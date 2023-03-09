/*
 * Copyright (c) 2019-2023 Ronald Brill.
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
package org.htmlunit.css.parser.media;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.htmlunit.css.dom.CSSValueImpl;
import org.htmlunit.css.dom.Property;
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
}
