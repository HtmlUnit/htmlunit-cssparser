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
 * Unit tests for {@link LCHColorImpl}.
 *
 * @author Ronald Brill
 */
public class LCHColorImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLU() throws Exception {
        final LexicalUnit lchLU = LexicalUnitImpl.createNumber(null, 10);
        final LexicalUnit lu = LexicalUnitImpl.createPercentage(lchLU, 20);
        LexicalUnitImpl.createDegree(lu, 30);

        final LCHColorImpl lch = new LCHColorImpl("lch", lchLU);
        assertEquals("lch(10 20% 30deg)", lch.toString());
        assertEquals("10", lch.getLightness().getCssText());
        assertEquals("20%", lch.getChroma().getCssText());
        assertEquals("30deg", lch.getHue().getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUException() throws Exception {
        try {
            new LCHColorImpl("lch", null);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertEquals("lch requires at least three values.", e.getMessage());
        }

        final LexicalUnit lchLU = LexicalUnitImpl.createNumber(null, 10);

        try {
            final LCHColorImpl color = new LCHColorImpl("lch", lchLU);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("lch requires at least three values.", e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUTooManyValuesException() throws Exception {
        final LexicalUnit lchLU = LexicalUnitImpl.createNumber(null, 100);
        LexicalUnit lu = LexicalUnitImpl.createPercentage(lchLU, 20);
        lu = LexicalUnitImpl.createDegree(lu, 30);
        lu = LexicalUnitImpl.createSlash(lu);
        lu = LexicalUnitImpl.createPercentage(lu, 0.1);
        lu = LexicalUnitImpl.createPercentage(lu, 77);

        try {
            final LCHColorImpl color = new LCHColorImpl("lch", lchLU);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Too many parameters for lch function.", e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final LexicalUnit lchLu = LexicalUnitImpl.createNumber(null, 235);
        final LexicalUnit lu = LexicalUnitImpl.createPercentage(lchLu, 20);
        LexicalUnitImpl.createRadian(lu, 30);

        final LCHColorImpl lch = new LCHColorImpl("lch", lchLu);

        assertEquals("lch(235 20% 30rad)", lch.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFunctionName() throws Exception {
        final LexicalUnit lchLu = LexicalUnitImpl.createNumber(null, 45);

        try {
            final LCHColorImpl color = new LCHColorImpl(null, lchLu);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Color space lch is required.", e.getMessage());
        }

        try {
            final LCHColorImpl color = new LCHColorImpl("", lchLu);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Color space '' not supported.", e.getMessage());
        }

        try {
            final LCHColorImpl color = new LCHColorImpl("xyz", lchLu);
            fail("DOMException expected: " + color);
        }
        catch (final DOMException e) {
            assertEquals("Color space 'xyz' not supported.", e.getMessage());
        }
    }
}
