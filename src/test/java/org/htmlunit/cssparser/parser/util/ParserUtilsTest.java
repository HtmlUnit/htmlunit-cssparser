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
package org.htmlunit.cssparser.parser.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.htmlunit.cssparser.util.ParserUtils;
import org.junit.jupiter.api.Test;

/**
 * @author Ronald Brill
 */
public class ParserUtilsTest {

    /**
     * @throws Exception on failure
     */
    @Test
    public void trimBy() {
        assertEquals("test", ParserUtils.trimBy(new StringBuilder("test"), 0, 0));

        assertEquals("est", ParserUtils.trimBy(new StringBuilder("test"), 1, 0));
        assertEquals("st", ParserUtils.trimBy(new StringBuilder("test"), 2, 0));

        assertEquals("tes", ParserUtils.trimBy(new StringBuilder("test"), 0, 1));
        assertEquals("te", ParserUtils.trimBy(new StringBuilder("test"), 0, 2));

        assertEquals("e", ParserUtils.trimBy(new StringBuilder("test"), 1, 2));
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void trimByString() {
        assertEquals("test", ParserUtils.trimBy("test", 0, 0));

        assertEquals("est", ParserUtils.trimBy("test", 1, 0));
        assertEquals("st", ParserUtils.trimBy("test", 2, 0));

        assertEquals("tes", ParserUtils.trimBy("test", 0, 1));
        assertEquals("te", ParserUtils.trimBy("test", 0, 2));

        assertEquals("e", ParserUtils.trimBy("test", 1, 2));

        // Test edge cases with explicit String type
        final String nullString = null;
        assertNull(ParserUtils.trimBy(nullString, 1, 1));
        final String testString = "test";
        assertEquals("test", ParserUtils.trimBy(testString, -1, 0));
        assertEquals("test", ParserUtils.trimBy(testString, 0, -1));
        assertEquals("test", ParserUtils.trimBy(testString, 3, 2));
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void trimUrl() {
        assertEquals("test", ParserUtils.trimUrl(new StringBuilder("url(test)")));
        assertEquals("", ParserUtils.trimUrl(new StringBuilder("url()")));

        assertEquals("test", ParserUtils.trimUrl(new StringBuilder("url('test')")));
        assertEquals("test", ParserUtils.trimUrl(new StringBuilder("url(\"test\")")));

        assertEquals("test", ParserUtils.trimUrl(new StringBuilder("url(   test \t )")));
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void equalsIgnoreCase() {
        assertTrue(ParserUtils.equalsIgnoreCase("test", "TEST"));
        assertTrue(ParserUtils.equalsIgnoreCase("Test", "test"));
        assertTrue(ParserUtils.equalsIgnoreCase("test", "test"));
        assertTrue(ParserUtils.equalsIgnoreCase("", ""));

        assertFalse(ParserUtils.equalsIgnoreCase("test", "other"));
        assertFalse(ParserUtils.equalsIgnoreCase("test", "TEST123"));

        assertTrue(ParserUtils.equalsIgnoreCase(null, null));
        assertFalse(ParserUtils.equalsIgnoreCase("test", null));
        assertFalse(ParserUtils.equalsIgnoreCase(null, "test"));

        // Same reference
        final String s = "test";
        assertTrue(ParserUtils.equalsIgnoreCase(s, s));
    }
}
