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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;

import com.gargoylesoftware.css.parser.LexicalUnit;
import com.gargoylesoftware.css.parser.LexicalUnitImpl;

/**
 * Unit tests for {@link RGBColorImpl}.
 *
 * @author Ronald Brill
 */
public class RGBColorImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLU() throws Exception {
        final LexicalUnit rgbLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createComma(rgbLU);
        lu = LexicalUnitImpl.createNumber(lu, 20);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 30);

        final RGBColorImpl rgb = new RGBColorImpl("rgb", rgbLU);
        assertEquals("rgb(10, 20, 30)", rgb.toString());
        assertEquals("10", rgb.getRed().getCssText());
        assertEquals("20", rgb.getGreen().getCssText());
        assertEquals("30", rgb.getBlue().getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUException() throws Exception {
        LexicalUnit rgbLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createComma(rgbLU);
        lu = LexicalUnitImpl.createDivide(lu);

        try {
            new RGBColorImpl("rgb", rgbLU);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertEquals("Color part has to be numeric or percentage.", e.getMessage());
        }

        rgbLU = LexicalUnitImpl.createNumber(null, 10);
        lu = LexicalUnitImpl.createComma(rgbLU);
        lu = LexicalUnitImpl.createNumber(lu, 20);
        lu = LexicalUnitImpl.createNumber(lu, 30);

        try {
            new RGBColorImpl("rgb", rgbLU);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertEquals("rgb parameters must be separated by ','.", e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUTooManyValuesException() throws Exception {
        final LexicalUnit rgbLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createComma(rgbLU);
        lu = LexicalUnitImpl.createNumber(lu, 20);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 30);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 0.1);
        lu = LexicalUnitImpl.createComma(lu);

        try {
            new RGBColorImpl("rgb", rgbLU);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertEquals("Too many parameters for rgb function.", e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final LexicalUnit rgbLu = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createComma(rgbLu);
        lu = LexicalUnitImpl.createNumber(lu, 20);
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createNumber(lu, 30);

        final RGBColorImpl rgb = new RGBColorImpl("rgb", rgbLu);

        assertEquals("rgb(10, 20, 30)", rgb.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFunctionName() throws Exception {
        final LexicalUnit rgbLu = LexicalUnitImpl.createNumber(null, 10);

        try {
            new RGBColorImpl(null, rgbLu);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertEquals("Color space rgb or rgba is required.", e.getMessage());
        }

        try {
            new RGBColorImpl("", rgbLu);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertEquals("Color space '' not supported.", e.getMessage());
        }

        try {
            new RGBColorImpl("xyz", rgbLu);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertEquals("Color space 'xyz' not supported.", e.getMessage());
        }
    }}
