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
package org.htmlunit.css.dom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.htmlunit.css.parser.LexicalUnit;
import org.htmlunit.css.parser.LexicalUnitImpl;
import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;

/**
 * Unit tests for {@link RGBColorImpl}.
 *
 * @author Ronald Brill
 * @author Paul Selormey
 */
public class HSLColorImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLU() throws Exception {
        final LexicalUnit hslLU = LexicalUnitImpl.createDegree(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createComma(hslLU);
        lu = LexicalUnitImpl.createPercentage(lu, 20);
        lu = LexicalUnitImpl.createComma(lu);
        LexicalUnitImpl.createPercentage(lu, 30);

        final HSLColorImpl hsl = new HSLColorImpl("hsl", hslLU);
        assertEquals("hsl(10deg, 20%, 30%)", hsl.toString());
        assertEquals("10deg", hsl.getHue().getCssText());
        assertEquals("20%", hsl.getSaturation().getCssText());
        assertEquals("30%", hsl.getLightness().getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUException() throws Exception {
        LexicalUnit hslLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createComma(hslLU);
        LexicalUnitImpl.createDivide(lu);

        try {
            final HSLColorImpl color = new HSLColorImpl("hsl", hslLU);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Saturation part has to be percentage.", e.getMessage());
        }

        hslLU = LexicalUnitImpl.createDegree(null, 10);
        lu = LexicalUnitImpl.createComma(hslLU);
        lu = LexicalUnitImpl.createPercentage(lu, 20);
        LexicalUnitImpl.createPercentage(lu, 30);

        try {
            final HSLColorImpl color = new HSLColorImpl("hsl", hslLU);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("hsl parameters must be separated by ','.", e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUTooManyValuesException() throws Exception {
        final LexicalUnit hslLU = LexicalUnitImpl.createNumber(null, 100);
        LexicalUnit lu = LexicalUnitImpl.createComma(hslLU);
        lu = LexicalUnitImpl.createPercentage(lu, 20);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createPercentage(lu, 30);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createPercentage(lu, 0.1);
        LexicalUnitImpl.createComma(lu);

        try {
            final HSLColorImpl color = new HSLColorImpl("hsl", hslLU);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Too many parameters for hsl function.", e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final LexicalUnit hslLu = LexicalUnitImpl.createNumber(null, 235);
        LexicalUnit lu = LexicalUnitImpl.createComma(hslLu);
        lu = LexicalUnitImpl.createPercentage(lu, 20);
        lu = LexicalUnitImpl.createComma(lu);
        LexicalUnitImpl.createPercentage(lu, 30);

        final HSLColorImpl hsl = new HSLColorImpl("hsl", hslLu);

        assertEquals("hsl(235, 20%, 30%)", hsl.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFunctionName() throws Exception {
        final LexicalUnit hslLu = LexicalUnitImpl.createNumber(null, 45);

        try {
            final HSLColorImpl color = new HSLColorImpl(null, hslLu);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Color space hsl or hsla is required.", e.getMessage());
        }

        try {
            final HSLColorImpl color = new HSLColorImpl("", hslLu);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Color space '' not supported.", e.getMessage());
        }

        try {
            final HSLColorImpl color = new HSLColorImpl("xyz", hslLu);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Color space 'xyz' not supported.", e.getMessage());
        }
    }
}
