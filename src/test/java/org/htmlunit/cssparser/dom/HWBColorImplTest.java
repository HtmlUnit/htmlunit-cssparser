/*
 * Copyright (c) 2019-2024 Ronald Brill.
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
package org.htmlunit.cssparser.dom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.htmlunit.cssparser.parser.LexicalUnit;
import org.htmlunit.cssparser.parser.LexicalUnitImpl;
import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;

/**
 * Unit tests for {@link HWBColorImpl}.
 *
 * @author Ronald Brill
 */
public class HWBColorImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLU() throws Exception {
        final LexicalUnit hwbLU = LexicalUnitImpl.createDegree(null, 10);
        final LexicalUnit lu = LexicalUnitImpl.createPercentage(hwbLU, 20);
        LexicalUnitImpl.createPercentage(lu, 30);

        final HWBColorImpl hwb = new HWBColorImpl("hwb", hwbLU);
        assertEquals("hwb(10deg 20% 30%)", hwb.toString());
        assertEquals("10deg", hwb.getHue().getCssText());
        assertEquals("20%", hwb.getWhiteness().getCssText());
        assertEquals("30%", hwb.getBlackness().getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUException() throws Exception {
        try {
            new HWBColorImpl("hwb", null);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertEquals("'hwb' requires at least three values.", e.getMessage());
        }

        final LexicalUnit hwbLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnitImpl.createDivide(hwbLU);

        try {
            final HWBColorImpl color = new HWBColorImpl("hwb", hwbLU);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Whiteness part has to be percentage.", e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUTooManyValuesException() throws Exception {
        final LexicalUnit hwbLU = LexicalUnitImpl.createNumber(null, 100);
        LexicalUnit lu = LexicalUnitImpl.createPercentage(hwbLU, 20);
        lu = LexicalUnitImpl.createPercentage(lu, 30);
        lu = LexicalUnitImpl.createSlash(lu);
        lu = LexicalUnitImpl.createPercentage(lu, 0.1);
        lu = LexicalUnitImpl.createPercentage(lu, 77);

        try {
            final HWBColorImpl color = new HWBColorImpl("hwb", hwbLU);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Too many parameters for 'hwb' function.", e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final LexicalUnit hwbLu = LexicalUnitImpl.createNumber(null, 235);
        final LexicalUnit lu = LexicalUnitImpl.createPercentage(hwbLu, 20);
        LexicalUnitImpl.createPercentage(lu, 30);

        final HWBColorImpl hwb = new HWBColorImpl("hwb", hwbLu);

        assertEquals("hwb(235 20% 30%)", hwb.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFunctionName() throws Exception {
        final LexicalUnit hwbLu = LexicalUnitImpl.createNumber(null, 45);

        try {
            final HWBColorImpl color = new HWBColorImpl(null, hwbLu);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Color space 'hwb' is required.", e.getMessage());
        }

        try {
            final HWBColorImpl color = new HWBColorImpl("", hwbLu);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Color space '' not supported.", e.getMessage());
        }

        try {
            final HWBColorImpl color = new HWBColorImpl("xyz", hwbLu);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Color space 'xyz' not supported.", e.getMessage());
        }
    }
}
