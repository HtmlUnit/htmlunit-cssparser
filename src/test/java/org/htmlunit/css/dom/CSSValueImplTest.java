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

import org.htmlunit.css.dom.CSSValueImpl.CSSPrimitiveValueType;
import org.htmlunit.css.parser.LexicalUnit;
import org.htmlunit.css.parser.LexicalUnitImpl;
import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;

/**
 * Unit tests for {@link CSSValueImpl}.
 *
 * @author Ronald Brill
 */
public class CSSValueImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void attr() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createAttr(null, "attrValue");
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("attr(attrValue)", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_ATTR, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.ATTR, value.getLexicalUnitType());
        assertEquals(0.0, value.getDoubleValue(), 0.00001);
        assertEquals("attrValue", value.getStringValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void cm() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createCentimeter(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2cm", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_CM, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.CENTIMETER, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void q() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createQuater(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2Q", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_Q, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.QUATER, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void counter() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createCounter(null, null);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("counter()", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_COUNTER, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.COUNTER_FUNCTION, value.getLexicalUnitType());
        assertEquals(0.0, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void deg() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createDegree(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2deg", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_DEG, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.DEGREE, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void turn() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createTurn(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2turn", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_TURN, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.TURN, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimension() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createDimension(null, 1.2, "lumen");
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2lumen", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_DIMENSION, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.DIMENSION, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void ems() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createEm(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2em", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_EMS, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.EM, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emx() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createEx(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2ex", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_EXS, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.EX, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void function() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createFunction(null, "foo", LexicalUnitImpl.createString(null, "param"));
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("foo(\"param\")", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_STRING, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.FUNCTION, value.getLexicalUnitType());
        assertEquals("foo(\"param\")", value.getStringValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void functionTwoParams() throws Exception {
        final LexicalUnit x = LexicalUnitImpl.createNumber(null, 10);
        final LexicalUnit c = LexicalUnitImpl.createComma(x);
        LexicalUnitImpl.createNumber(c, 11);

        final LexicalUnit lu = LexicalUnitImpl.createFunction(null, "foo", x);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("foo(10, 11)", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_STRING, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.FUNCTION, value.getLexicalUnitType());
        assertEquals("foo(10, 11)", value.getStringValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void gradian() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createGradian(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2grad", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_GRAD, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.GRADIAN, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void hertz() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createHertz(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2Hz", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_HZ, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.HERTZ, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void ident() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createIdent(null, "id");
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("id", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_IDENT, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.IDENT, value.getLexicalUnitType());
        assertEquals(0, value.getDoubleValue(), 0.00001);
        assertEquals("id", value.getStringValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void inch() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createInch(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2in", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_IN, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.INCH, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void kiloHertz() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createKiloHertz(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2kHz", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_KHZ, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.KILOHERTZ, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void millimeter() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createMillimeter(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2mm", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_MM, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.MILLIMETER, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void millisecond() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createMillisecond(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2ms", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_MS, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.MILLISECOND, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void numberDouble() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createNumber(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_NUMBER, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.REAL, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void numberInt() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createNumber(null, 12);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("12", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_NUMBER, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.INTEGER, value.getLexicalUnitType());
        assertEquals(12, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void pica() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createPica(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2pc", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_PC, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.PICA, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void percentage() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createPercentage(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2%", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_PERCENTAGE, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.PERCENTAGE, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void point() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createPoint(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2pt", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_PT, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.POINT, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void pixel() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createPixel(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2px", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_PX, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.PIXEL, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void radian() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createRadian(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2rad", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_RAD, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.RADIAN, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void rect() throws Exception {
        final LexicalUnit rect = LexicalUnitImpl.createNumber(null, 1);
        LexicalUnit param = LexicalUnitImpl.createComma(rect);
        param = LexicalUnitImpl.createNumber(param, 2);
        param = LexicalUnitImpl.createComma(param);
        param = LexicalUnitImpl.createNumber(param, 3);
        param = LexicalUnitImpl.createComma(param);
        param = LexicalUnitImpl.createNumber(param, 4);

        final LexicalUnit lu = LexicalUnitImpl.createRect(null, rect);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("rect(1, 2, 3, 4)", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_RECT, value.getPrimitiveType());
        assertEquals(null, value.getLexicalUnitType());
        try {
            value.getDoubleValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void rem() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createRem(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2rem", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_REM, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.REM, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void ch() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createCh(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2ch", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_CH, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.CH, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void vw() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createVw(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2vw", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_VW, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.VW, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void vh() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createVh(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2vh", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_VH, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.VH, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void vmin() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createVMin(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2vmin", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_VMIN, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.VMIN, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void vmax() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createVMax(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2vmax", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_VMAX, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.VMAX, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void rgbColor() throws Exception {
        final LexicalUnit color = LexicalUnitImpl.createNumber(null, 255);
        LexicalUnit param = LexicalUnitImpl.createComma(color);
        param = LexicalUnitImpl.createNumber(param, 128);
        param = LexicalUnitImpl.createComma(param);
        param = LexicalUnitImpl.createNumber(param, 0);

        final LexicalUnit lu = LexicalUnitImpl.createRgbColor(null, "rgb", color);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("rgb(255, 128, 0)", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_RGBCOLOR, value.getPrimitiveType());
        assertEquals(null, value.getLexicalUnitType());
        try {
            value.getDoubleValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void second() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createSecond(null, 1.2);
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("1.2s", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_S, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.SECOND, value.getLexicalUnitType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
        try {
            value.getStringValue();
            fail("DomException expected");
        }
        catch (final DOMException e) {
            // expected
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void string() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createString(null, "value");
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("\"value\"", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_STRING, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.STRING_VALUE, value.getLexicalUnitType());
        assertEquals(0.0, value.getDoubleValue(), 0.00001); // TODO is this correct?
        assertEquals("value", value.getStringValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void uri() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createURI(null, "cssparser");
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("url(\"cssparser\")", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_URI, value.getPrimitiveType());
        assertEquals(LexicalUnit.LexicalUnitType.URI, value.getLexicalUnitType());
        assertEquals(0.0, value.getDoubleValue(), 0.00001);
        assertEquals("cssparser", value.getStringValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setCssText() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createURI(null, "cssparser");
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        value.setCssText("1.2s");
        assertEquals("1.2s", value.getCssText());
        assertEquals(CSSPrimitiveValueType.CSS_S, value.getPrimitiveType());
        assertEquals(1.2, value.getDoubleValue(), 0.00001);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFormated() throws Exception {
        final LexicalUnit lu = LexicalUnitImpl.createString(null, "value");
        final CSSValueImpl value = new CSSValueImpl(lu, false);

        assertEquals("\"value\"", value.toString());
        assertEquals("\"value\"", value.getCssText());
    }
}
