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
 * Unit tests for {@link LABColorImpl}.
 *
 * @author Ronald Brill
 */
public class LABColorImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLU() throws Exception {
        final LexicalUnit labLU = LexicalUnitImpl.createNumber(null, 10);
        final LexicalUnit lu = LexicalUnitImpl.createNumber(labLU, 20);
        LexicalUnitImpl.createNumber(lu, 30);

        final LABColorImpl lab = new LABColorImpl("lab", labLU);
        assertEquals("lab(10 20 30)", lab.toString());
        assertEquals("10", lab.getLightness().getCssText());
        assertEquals("20", lab.getA().getCssText());
        assertEquals("30", lab.getB().getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUException() throws Exception {
        try {
            new LABColorImpl("lab", null);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertEquals("'lab' requires at least three values.", e.getMessage());
        }

        final LexicalUnit labLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnitImpl.createDivide(labLU);

        try {
            new LABColorImpl("lab", labLU);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertEquals("Color part has to be numeric or percentage.", e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUTooManyValuesException() throws Exception {
        final LexicalUnit labLU = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createNumber(labLU, 20);
        lu = LexicalUnitImpl.createNumber(lu, 30);
        lu = LexicalUnitImpl.createSlash(lu);
        lu = LexicalUnitImpl.createPercentage(lu, 0.1);
        lu = LexicalUnitImpl.createPercentage(lu, 77);

        try {
            final LABColorImpl color = new LABColorImpl("lab", labLU);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Too many parameters for 'lab' function.", e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final LexicalUnit labLu = LexicalUnitImpl.createNumber(null, 10);
        final LexicalUnit lu = LexicalUnitImpl.createNumber(labLu, 20);
        LexicalUnitImpl.createNumber(lu, 30);

        final LABColorImpl lab = new LABColorImpl("lab", labLu);

        assertEquals("lab(10 20 30)", lab.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFunctionName() throws Exception {
        final LexicalUnit labLu = LexicalUnitImpl.createNumber(null, 10);

        try {
            final LABColorImpl color = new LABColorImpl(null, labLu);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Color space 'lab' or 'oklab' is required.", e.getMessage());
        }

        try {
            final LABColorImpl color = new LABColorImpl("", labLu);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Color space '' not supported.", e.getMessage());
        }

        try {
            final LABColorImpl color = new LABColorImpl("xyz", labLu);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Color space 'xyz' not supported.", e.getMessage());
        }
    }
}
