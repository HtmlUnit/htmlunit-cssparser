/*
 * Copyright (c) 2019-2021 Ronald Brill.
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
package com.gargoylesoftware.css.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Test;

import com.gargoylesoftware.css.parser.LexicalUnit.LexicalUnitType;

/**
 * Unit tests for {@link LexicalUnitImpl}.
 *
 * @author Ronald Brill
 */
public class LexicalUnitImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setLexicalUnitType() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.OPERATOR_GT);

        assertEquals(LexicalUnitType.OPERATOR_GT, unit.getLexicalUnitType());
        assertEquals(0d, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertNull(unit.getDimension());
        assertEquals("", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals(">", unit.toString());
        assertEquals("OPERATOR_GT", unit.toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setDoubleValue() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.KILOHERTZ);
        unit.setDoubleValue(7.1234);

        assertEquals(LexicalUnitType.KILOHERTZ, unit.getLexicalUnitType());
        assertEquals(7.1234, unit.getDoubleValue(), 0.0001);
        assertEquals(7, unit.getIntegerValue());
        assertNull(unit.getDimension());
        assertEquals("kHz", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("7.1234kHz", unit.toString());
        assertEquals("KILOHERTZ(7.1234kHz)", unit.toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setDimension() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.DIMENSION);
        unit.setDimension("Watt");

        assertEquals(LexicalUnitType.DIMENSION, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertEquals("Watt", unit.getDimension());
        assertEquals("Watt", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("0Watt", unit.toString());
        assertEquals("DIMENSION(0Watt)", unit.toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setFunctionName() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.FUNCTION);
        unit.setFunctionName("sqrt");

        assertEquals(LexicalUnitType.FUNCTION, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertNull(unit.getDimension());
        assertEquals("", unit.getDimensionUnitText());
        assertEquals("sqrt", unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("sqrt()", unit.toString());
        assertEquals("FUNCTION(sqrt())", unit.toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setParameters() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.FUNCTION);
        unit.setFunctionName("sqrt");
        unit.setParameters(LexicalUnitImpl.createCentimeter(null, 14));

        assertEquals(LexicalUnitType.FUNCTION, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertNull(unit.getDimension());
        assertEquals("", unit.getDimensionUnitText());
        assertEquals("sqrt", unit.getFunctionName());
        assertEquals("14cm", unit.getParameters().toString());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("sqrt(14cm)", unit.toString());
        assertEquals("FUNCTION(sqrt(14cm))", unit.toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setStringValue() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.UNICODERANGE);
        unit.setStringValue("testValue");

        assertEquals(LexicalUnitType.UNICODERANGE, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertNull(unit.getDimension());
        assertEquals("", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertEquals("testValue", unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("testValue", unit.toString());
        assertEquals("UNICODERANGE(testValue)", unit.toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setStringValueNull() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.UNICODERANGE);
        unit.setStringValue(null);

        assertEquals(LexicalUnitType.UNICODERANGE, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertNull(unit.getDimension());
        assertEquals("", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("", unit.toString());
        assertEquals("UNICODERANGE(null)", unit.toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void nextLexicalUnit() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, null);
        assertNull(unit.getNextLexicalUnit());

        final LexicalUnitImpl next = new LexicalUnitImpl(null, null);
        unit.setNextLexicalUnit(next);
        assertEquals(next, unit.getNextLexicalUnit());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void previousLexicalUnit() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, null);
        assertNull(unit.getPreviousLexicalUnit());

        final LexicalUnitImpl prev = new LexicalUnitImpl(null, null);
        unit.setPreviousLexicalUnit(prev);
        assertEquals(prev, unit.getPreviousLexicalUnit());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void refreshToStringCache() throws Exception {
        LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.OPERATOR_COMMA);
        assertEquals(",", unit.toString());

        unit = new LexicalUnitImpl(null, LexicalUnitType.KILOHERTZ);
        assertEquals("0kHz", unit.toString());

        unit.setDoubleValue(7.1234);
        assertEquals("7.1234kHz", unit.toString());

        unit = new LexicalUnitImpl(null, LexicalUnitType.DIMENSION);
        assertEquals("0", unit.toString());

        unit.setDoubleValue(7.1234);
        assertEquals("7.1234", unit.toString());
        unit.setDimension("Ohm");
        assertEquals("7.1234Ohm", unit.toString());

        unit = new LexicalUnitImpl(null, LexicalUnitType.FUNCTION);
        assertEquals("()", unit.toString());
        unit.setFunctionName("pow");
        assertEquals("pow()", unit.toString());

        unit.setParameters(LexicalUnitImpl.createPixel(null, 4));
        assertEquals("pow(4px)", unit.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getDimensionUnitText() throws Exception {
        LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.EM);
        assertEquals("em", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.REM);
        assertEquals("rem", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.EX);
        assertEquals("ex", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.CH);
        assertEquals("ch", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.VW);
        assertEquals("vw", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.VH);
        assertEquals("vh", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.VMIN);
        assertEquals("vmin", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.VMAX);
        assertEquals("vmax", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.PIXEL);
        assertEquals("px", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.INCH);
        assertEquals("in", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.CENTIMETER);
        assertEquals("cm", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.MILLIMETER);
        assertEquals("mm", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.POINT);
        assertEquals("pt", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.PICA);
        assertEquals("pc", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.QUATER);
        assertEquals("Q", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.PERCENTAGE);
        assertEquals("%", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.DEGREE);
        assertEquals("deg", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.GRADIAN);
        assertEquals("grad", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.RADIAN);
        assertEquals("rad", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.TURN);
        assertEquals("turn", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.MILLISECOND);
        assertEquals("ms", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.SECOND);
        assertEquals("s", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.HERTZ);
        assertEquals("Hz", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.KILOHERTZ);
        assertEquals("kHz", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.DIMENSION);
        unit.setDimension("test");
        assertEquals("test", unit.getDimensionUnitText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createNumberFromInt() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createNumber(null, 17);

        assertEquals(LexicalUnitType.INTEGER, unit.getLexicalUnitType());
        assertEquals(17, unit.getDoubleValue(), 0.0001);
        assertEquals(17, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("17", unit.toString());
        assertEquals("INTEGER(17)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createNumberFromDouble() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createNumber(null, 1.7);

        assertEquals(LexicalUnitType.REAL, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7", unit.toString());
        assertEquals("REAL(1.7)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createNumberFromDoublePecision() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createNumber(null, 1.234567654321);

        assertEquals(LexicalUnitType.REAL, unit.getLexicalUnitType());
        assertEquals(1.2345676, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.234567654321", unit.toString());
        assertEquals("REAL(1.234567654321)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createNumberFromDoublePecisionWithoutExponent() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createNumber(null, 0.0001);

        assertEquals(LexicalUnitType.REAL, unit.getLexicalUnitType());
        assertEquals(0.0001, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("0.0001", unit.toString());
        assertEquals("REAL(0.0001)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createPercentage() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createPercentage(null, 1.7);

        assertEquals(LexicalUnitType.PERCENTAGE, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("%", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7%", unit.toString());
        assertEquals("PERCENTAGE(1.7%)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createPixel() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createPixel(null, 1.7);

        assertEquals(LexicalUnitType.PIXEL, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("px", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7px", unit.toString());
        assertEquals("PIXEL(1.7px)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createCentimeter() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createCentimeter(null, 1.7);

        assertEquals(LexicalUnitType.CENTIMETER, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("cm", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7cm", unit.toString());
        assertEquals("CENTIMETER(1.7cm)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createMillimeter() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createMillimeter(null, 1.7);

        assertEquals(LexicalUnitType.MILLIMETER, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("mm", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7mm", unit.toString());
        assertEquals("MILLIMETER(1.7mm)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createInch() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createInch(null, 1.7);

        assertEquals(LexicalUnitType.INCH, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("in", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7in", unit.toString());
        assertEquals("INCH(1.7in)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createPoint() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createPoint(null, 1.7);

        assertEquals(LexicalUnitType.POINT, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("pt", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7pt", unit.toString());
        assertEquals("POINT(1.7pt)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createPica() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createPica(null, 1.7);

        assertEquals(LexicalUnitType.PICA, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("pc", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7pc", unit.toString());
        assertEquals("PICA(1.7pc)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createQuater() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createQuater(null, 1.7);

        assertEquals(LexicalUnitType.QUATER, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("Q", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7Q", unit.toString());
        assertEquals("QUATER(1.7Q)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createEm() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createEm(null, 1.7);

        assertEquals(LexicalUnitType.EM, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("em", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7em", unit.toString());
        assertEquals("EM(1.7em)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createRem() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createRem(null, 1.7);

        assertEquals(LexicalUnitType.REM, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("rem", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7rem", unit.toString());
        assertEquals("REM(1.7rem)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createCh() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createCh(null, 1.7);

        assertEquals(LexicalUnitType.CH, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("ch", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7ch", unit.toString());
        assertEquals("CH(1.7ch)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createVw() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createVw(null, 1.7);

        assertEquals(LexicalUnitType.VW, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("vw", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7vw", unit.toString());
        assertEquals("VW(1.7vw)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createVh() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createVh(null, 1.7);

        assertEquals(LexicalUnitType.VH, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("vh", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7vh", unit.toString());
        assertEquals("VH(1.7vh)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createVMin() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createVMin(null, 1.7);

        assertEquals(LexicalUnitType.VMIN, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("vmin", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7vmin", unit.toString());
        assertEquals("VMIN(1.7vmin)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createVMax() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createVMax(null, 1.7);

        assertEquals(LexicalUnitType.VMAX, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("vmax", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7vmax", unit.toString());
        assertEquals("VMAX(1.7vmax)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createEx() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createEx(null, 1.7);

        assertEquals(LexicalUnitType.EX, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("ex", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7ex", unit.toString());
        assertEquals("EX(1.7ex)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createDegree() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createDegree(null, 1.7);

        assertEquals(LexicalUnitType.DEGREE, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("deg", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7deg", unit.toString());
        assertEquals("DEGREE(1.7deg)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createRadian() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createRadian(null, 1.7);

        assertEquals(LexicalUnitType.RADIAN, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("rad", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7rad", unit.toString());
        assertEquals("RADIAN(1.7rad)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createGradian() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createGradian(null, 1.7);

        assertEquals(LexicalUnitType.GRADIAN, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("grad", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7grad", unit.toString());
        assertEquals("GRADIAN(1.7grad)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createTurn() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createTurn(null, 1.7);

        assertEquals(LexicalUnitType.TURN, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("turn", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7turn", unit.toString());
        assertEquals("TURN(1.7turn)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createMillisecond() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createMillisecond(null, 1.7);

        assertEquals(LexicalUnitType.MILLISECOND, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("ms", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7ms", unit.toString());
        assertEquals("MILLISECOND(1.7ms)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createSecond() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createSecond(null, 1.7);

        assertEquals(LexicalUnitType.SECOND, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("s", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7s", unit.toString());
        assertEquals("SECOND(1.7s)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createHertz() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createHertz(null, 1.7);

        assertEquals(LexicalUnitType.HERTZ, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("Hz", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7Hz", unit.toString());
        assertEquals("HERTZ(1.7Hz)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createDimension() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createDimension(null, 1.7, "Ohm");

        assertEquals(LexicalUnitType.DIMENSION, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("Ohm", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7Ohm", unit.toString());
        assertEquals("DIMENSION(1.7Ohm)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createKiloHertz() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createKiloHertz(null, 1.7);

        assertEquals(LexicalUnitType.KILOHERTZ, unit.getLexicalUnitType());
        assertEquals(1.7, unit.getDoubleValue(), 0.0001);
        assertEquals(1, unit.getIntegerValue());
        assertEquals("kHz", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("1.7kHz", unit.toString());
        assertEquals("KILOHERTZ(1.7kHz)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createCounter() throws Exception {
        final LexicalUnit value = LexicalUnitImpl.createIdent(null, "CounterValue");
        final LexicalUnit unit = LexicalUnitImpl.createCounter(null, value);

        assertEquals(LexicalUnitType.COUNTER_FUNCTION, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertEquals("counter", unit.getFunctionName());
        assertEquals("CounterValue", unit.getParameters().toString());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("counter(CounterValue)", unit.toString());
        assertEquals("COUNTER_FUNCTION(counter(CounterValue))", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createCounterNoValue() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createCounter(null, null);

        assertEquals(LexicalUnitType.COUNTER_FUNCTION, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertEquals("counter", unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("counter()", unit.toString());
        assertEquals("COUNTER_FUNCTION(counter())", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createCounters() throws Exception {
        final LexicalUnit value1 = LexicalUnitImpl.createIdent(null, "CounterValue");
        LexicalUnitImpl.createIdent(LexicalUnitImpl.createComma(value1), "Second");
        final LexicalUnit unit = LexicalUnitImpl.createCounters(null, value1);

        assertEquals(LexicalUnitType.COUNTERS_FUNCTION, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertEquals("counters", unit.getFunctionName());
        assertEquals("CounterValue", unit.getParameters().toString());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("counters(CounterValue, Second)", unit.toString());
        assertEquals("COUNTERS_FUNCTION(counters(CounterValue, Second))",
                ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createCountersNoValue() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createCounters(null, null);

        assertEquals(LexicalUnitType.COUNTERS_FUNCTION, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertEquals("counters", unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("counters()", unit.toString());
        assertEquals("COUNTERS_FUNCTION(counters())", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createAttr() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createAttr(null, "attrValue");

        assertEquals(LexicalUnitType.ATTR, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertEquals("name", unit.getFunctionName());
        assertNull(unit.getParameters());
        assertEquals("attrValue", unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("attr(attrValue)", unit.toString());
        assertEquals("ATTR(attr(attrValue))", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createRect() throws Exception {
        // rect() function impl assumes the commas are part of the declaration
        final LexicalUnit x1 = LexicalUnitImpl.createNumber(null, 1);
        final LexicalUnit y1 = LexicalUnitImpl.createNumber(LexicalUnitImpl.createComma(x1), 2);
        final LexicalUnit x2 = LexicalUnitImpl.createNumber(LexicalUnitImpl.createComma(y1), 3);
        LexicalUnitImpl.createNumber(LexicalUnitImpl.createComma(x2), 4);

        final LexicalUnit unit = LexicalUnitImpl.createRect(null, x1);

        assertEquals(LexicalUnitType.RECT_FUNCTION, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertEquals("rect", unit.getFunctionName());
        assertEquals("1", unit.getParameters().toString());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("rect(1, 2, 3, 4)", unit.toString());
        assertEquals("RECT_FUNCTION(rect(1, 2, 3, 4))", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createRgbColor() throws Exception {
        final LexicalUnit r = LexicalUnitImpl.createNumber(null, 255);
        final LexicalUnit g = LexicalUnitImpl.createNumber(LexicalUnitImpl.createComma(r), 128);
        LexicalUnitImpl.createNumber(LexicalUnitImpl.createComma(g), 0);
        final LexicalUnit unit = LexicalUnitImpl.createRgbColor(null, "rgb", r);

        assertEquals(LexicalUnitType.RGBCOLOR, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertEquals("rgb", unit.getFunctionName());
        assertEquals("255", unit.getParameters().toString());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("rgb(255, 128, 0)", unit.toString());
        assertEquals("RGBCOLOR(rgb(255, 128, 0))", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createHslColor() throws Exception {
        final LexicalUnit r = LexicalUnitImpl.createNumber(null, 255);
        final LexicalUnit g = LexicalUnitImpl.createPercentage(LexicalUnitImpl.createComma(r), 128);
        LexicalUnitImpl.createPercentage(LexicalUnitImpl.createComma(g), 0);
        final LexicalUnit unit = LexicalUnitImpl.createHslColor(null, "hsl", r);

        assertEquals(LexicalUnitType.HSLCOLOR, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertEquals("hsl", unit.getFunctionName());
        assertEquals("255", unit.getParameters().toString());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("hsl(255, 128%, 0%)", unit.toString());
        assertEquals("HSLCOLOR(hsl(255, 128%, 0%))", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createFunction() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createFunction(null, "foo",
                LexicalUnitImpl.createString(null, "param"));

        assertEquals(LexicalUnitType.FUNCTION, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertEquals("foo", unit.getFunctionName());
        assertEquals("\"param\"", unit.getParameters().toString());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("foo(\"param\")", unit.toString());
        assertEquals("FUNCTION(foo(\"param\"))", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createFunctionTwoParams() throws Exception {
        final LexicalUnit x = LexicalUnitImpl.createNumber(null, 10);
        final LexicalUnit c = LexicalUnitImpl.createComma(x);
        LexicalUnitImpl.createNumber(c, 11);

        final LexicalUnit unit = LexicalUnitImpl.createFunction(null, "foo", x);

        assertEquals(LexicalUnitType.FUNCTION, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertEquals("foo", unit.getFunctionName());
        assertEquals("10", unit.getParameters().toString());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("foo(10, 11)", unit.toString());
        assertEquals("FUNCTION(foo(10,11))", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createString() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createString(null, "RBRi");

        assertEquals(LexicalUnitType.STRING_VALUE, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertEquals("RBRi", unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("\"RBRi\"", unit.toString());
        assertEquals("STRING_VALUE(\"RBRi\")", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createStringContainingNewLine() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createString(null, "RBRi\ntest");

        assertEquals(LexicalUnitType.STRING_VALUE, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertEquals("RBRi\ntest", unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("\"RBRi\\A test\"", unit.toString());
        assertEquals("STRING_VALUE(\"RBRi\ntest\")", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createIdent() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createIdent(null, "css");

        assertEquals(LexicalUnitType.IDENT, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertEquals("css", unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("css", unit.toString());
        assertEquals("IDENT(css)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createURI() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createURI(null, "www.wetator.org");

        assertEquals(LexicalUnitType.URI, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertEquals("www.wetator.org", unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals("url(\"www.wetator.org\")", unit.toString());
        assertEquals("URI(url(www.wetator.org))", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createComma() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createComma(null);

        assertEquals(LexicalUnitType.OPERATOR_COMMA, unit.getLexicalUnitType());
        assertEquals(0, unit.getDoubleValue(), 0.0001);
        assertEquals(0, unit.getIntegerValue());
        assertEquals("", unit.getDimensionUnitText());
        assertNull(unit.getFunctionName());
        assertNull(unit.getParameters());
        assertNull(unit.getStringValue());

        assertNull(unit.getNextLexicalUnit());
        assertNull(unit.getPreviousLexicalUnit());

        assertEquals(",", unit.toString());
        assertEquals("OPERATOR_COMMA", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void serializeTest() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createSecond(null, 10);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(unit);
        oos.flush();
        oos.close();
        final byte[] bytes = baos.toByteArray();
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        final Object o = ois.readObject();

        assertEquals(unit.toString(), o.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void newlineInsideString() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.STRING_VALUE);
        unit.setStringValue("abc \n def");

        assertEquals("abc \n def", unit.getStringValue());
        // CSS unicode escaping eats up the space char after the escape sequence
        // because of this we have to add this on reconstruction
        assertEquals("\"abc \\A  def\"", unit.getCssText());
    }
}
