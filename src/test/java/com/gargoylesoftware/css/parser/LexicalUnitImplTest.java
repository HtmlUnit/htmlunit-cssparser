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
package com.gargoylesoftware.css.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Assert;
import org.junit.Test;

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

        Assert.assertEquals(LexicalUnitType.OPERATOR_GT, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertNull(unit.getDimension());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals(">", unit.toString());
        Assert.assertEquals("OPERATOR_GT", unit.toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setDoubleValue() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.KILOHERTZ);
        unit.setDoubleValue(7.1234f);

        Assert.assertEquals(LexicalUnitType.KILOHERTZ, unit.getLexicalUnitType());
        Assert.assertEquals(7.1234f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(7, unit.getIntegerValue());
        Assert.assertNull(unit.getDimension());
        Assert.assertEquals("kHz", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("7.1234kHz", unit.toString());
        Assert.assertEquals("KILOHERTZ(7.1234kHz)", unit.toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setDimension() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.DIMENSION);
        unit.setDimension("Watt");

        Assert.assertEquals(LexicalUnitType.DIMENSION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("Watt", unit.getDimension());
        Assert.assertEquals("Watt", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("0Watt", unit.toString());
        Assert.assertEquals("DIMENSION(0Watt)", unit.toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setFunctionName() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.FUNCTION);
        unit.setFunctionName("sqrt");

        Assert.assertEquals(LexicalUnitType.FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertNull(unit.getDimension());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("sqrt", unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("sqrt()", unit.toString());
        Assert.assertEquals("FUNCTION(sqrt())", unit.toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setParameters() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.FUNCTION);
        unit.setFunctionName("sqrt");
        unit.setParameters(LexicalUnitImpl.createCentimeter(null, 14f));

        Assert.assertEquals(LexicalUnitType.FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertNull(unit.getDimension());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("sqrt", unit.getFunctionName());
        Assert.assertEquals("14cm", unit.getParameters().toString());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("sqrt(14cm)", unit.toString());
        Assert.assertEquals("FUNCTION(sqrt(14cm))", unit.toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setStringValue() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.UNICODERANGE);
        unit.setStringValue("testValue");

        Assert.assertEquals(LexicalUnitType.UNICODERANGE, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertNull(unit.getDimension());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertEquals("testValue", unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("testValue", unit.toString());
        Assert.assertEquals("UNICODERANGE(testValue)", unit.toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setStringValueNull() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.UNICODERANGE);
        unit.setStringValue(null);

        Assert.assertEquals(LexicalUnitType.UNICODERANGE, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertNull(unit.getDimension());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("", unit.toString());
        Assert.assertEquals("UNICODERANGE(null)", unit.toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void nextLexicalUnit() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, null);
        Assert.assertNull(unit.getNextLexicalUnit());

        final LexicalUnitImpl next = new LexicalUnitImpl(null, null);
        unit.setNextLexicalUnit(next);
        Assert.assertEquals(next, unit.getNextLexicalUnit());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void previousLexicalUnit() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, null);
        Assert.assertNull(unit.getPreviousLexicalUnit());

        final LexicalUnitImpl prev = new LexicalUnitImpl(null, null);
        unit.setPreviousLexicalUnit(prev);
        Assert.assertEquals(prev, unit.getPreviousLexicalUnit());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void refreshToStringCache() throws Exception {
        LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.OPERATOR_COMMA);
        Assert.assertEquals(",", unit.toString());

        unit = new LexicalUnitImpl(null, LexicalUnitType.KILOHERTZ);
        Assert.assertEquals("0kHz", unit.toString());

        unit.setDoubleValue(7.1234f);
        Assert.assertEquals("7.1234kHz", unit.toString());

        unit = new LexicalUnitImpl(null, LexicalUnitType.DIMENSION);
        Assert.assertEquals("0", unit.toString());

        unit.setDoubleValue(7.1234f);
        Assert.assertEquals("7.1234", unit.toString());
        unit.setDimension("Ohm");
        Assert.assertEquals("7.1234Ohm", unit.toString());

        unit = new LexicalUnitImpl(null, LexicalUnitType.FUNCTION);
        Assert.assertEquals("()", unit.toString());
        unit.setFunctionName("pow");
        Assert.assertEquals("pow()", unit.toString());

        unit.setParameters(LexicalUnitImpl.createPixel(null, 4f));
        Assert.assertEquals("pow(4px)", unit.toString());

        unit = new LexicalUnitImpl(null, LexicalUnitType.SUB_EXPRESSION);
        Assert.assertEquals("", unit.toString());
        unit.setStringValue("value");

        Assert.assertEquals("value", unit.toString());
        Assert.assertEquals("SUB_EXPRESSION(value)", unit.toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getDimensionUnitText() throws Exception {
        LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.EM);
        Assert.assertEquals("em", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.REM);
        Assert.assertEquals("rem", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.EX);
        Assert.assertEquals("ex", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.PIXEL);
        Assert.assertEquals("px", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.INCH);
        Assert.assertEquals("in", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.CENTIMETER);
        Assert.assertEquals("cm", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.MILLIMETER);
        Assert.assertEquals("mm", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.POINT);
        Assert.assertEquals("pt", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.PICA);
        Assert.assertEquals("pc", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.PERCENTAGE);
        Assert.assertEquals("%", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.DEGREE);
        Assert.assertEquals("deg", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.GRADIAN);
        Assert.assertEquals("grad", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.RADIAN);
        Assert.assertEquals("rad", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.MILLISECOND);
        Assert.assertEquals("ms", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.SECOND);
        Assert.assertEquals("s", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.HERTZ);
        Assert.assertEquals("Hz", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.KILOHERTZ);
        Assert.assertEquals("kHz", unit.getDimensionUnitText());

        unit = new LexicalUnitImpl(null, LexicalUnitType.DIMENSION);
        unit.setDimension("test");
        Assert.assertEquals("test", unit.getDimensionUnitText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createNumberFromInt() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createNumber(null, 17);

        Assert.assertEquals(LexicalUnitType.INTEGER, unit.getLexicalUnitType());
        Assert.assertEquals(17f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(17, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("17", unit.toString());
        Assert.assertEquals("INTEGER(17)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createNumberFromDouble() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createNumber(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.REAL, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7", unit.toString());
        Assert.assertEquals("REAL(1.7)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createNumberFromDoublePecision() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createNumber(null, 1.234567654321f);

        Assert.assertEquals(LexicalUnitType.REAL, unit.getLexicalUnitType());
        Assert.assertEquals(1.2345676f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.2346", unit.toString());
        Assert.assertEquals("REAL(1.2346)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createNumberFromDoublePecisionWithoutExponent() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createNumber(null, 0.0001f);

        Assert.assertEquals(LexicalUnitType.REAL, unit.getLexicalUnitType());
        Assert.assertEquals(0.0001f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("0.0001", unit.toString());
        Assert.assertEquals("REAL(0.0001)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createPercentage() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createPercentage(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.PERCENTAGE, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("%", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7%", unit.toString());
        Assert.assertEquals("PERCENTAGE(1.7%)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createPixel() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createPixel(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.PIXEL, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("px", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7px", unit.toString());
        Assert.assertEquals("PIXEL(1.7px)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createCentimeter() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createCentimeter(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.CENTIMETER, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("cm", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7cm", unit.toString());
        Assert.assertEquals("CENTIMETER(1.7cm)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createMillimeter() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createMillimeter(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.MILLIMETER, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("mm", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7mm", unit.toString());
        Assert.assertEquals("MILLIMETER(1.7mm)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createInch() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createInch(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.INCH, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("in", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7in", unit.toString());
        Assert.assertEquals("INCH(1.7in)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createPoint() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createPoint(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.POINT, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("pt", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7pt", unit.toString());
        Assert.assertEquals("POINT(1.7pt)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createPica() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createPica(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.PICA, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("pc", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7pc", unit.toString());
        Assert.assertEquals("PICA(1.7pc)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createEm() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createEm(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.EM, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("em", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7em", unit.toString());
        Assert.assertEquals("EM(1.7em)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createRem() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createRem(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.REM, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("rem", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7rem", unit.toString());
        Assert.assertEquals("REM(1.7rem)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createEx() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createEx(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.EX, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("ex", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7ex", unit.toString());
        Assert.assertEquals("EX(1.7ex)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createDegree() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createDegree(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.DEGREE, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("deg", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7deg", unit.toString());
        Assert.assertEquals("DEGREE(1.7deg)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createRadian() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createRadian(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.RADIAN, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("rad", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7rad", unit.toString());
        Assert.assertEquals("RADIAN(1.7rad)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createGradian() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createGradian(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.GRADIAN, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("grad", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7grad", unit.toString());
        Assert.assertEquals("GRADIAN(1.7grad)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createMillisecond() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createMillisecond(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.MILLISECOND, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("ms", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7ms", unit.toString());
        Assert.assertEquals("MILLISECOND(1.7ms)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createSecond() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createSecond(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.SECOND, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("s", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7s", unit.toString());
        Assert.assertEquals("SECOND(1.7s)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createHertz() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createHertz(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.HERTZ, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("Hz", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7Hz", unit.toString());
        Assert.assertEquals("HERTZ(1.7Hz)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createDimension() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createDimension(null, 1.7f, "Ohm");

        Assert.assertEquals(LexicalUnitType.DIMENSION, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("Ohm", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7Ohm", unit.toString());
        Assert.assertEquals("DIMENSION(1.7Ohm)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createKiloHertz() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createKiloHertz(null, 1.7f);

        Assert.assertEquals(LexicalUnitType.KILOHERTZ, unit.getLexicalUnitType());
        Assert.assertEquals(1.7f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(1, unit.getIntegerValue());
        Assert.assertEquals("kHz", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("1.7kHz", unit.toString());
        Assert.assertEquals("KILOHERTZ(1.7kHz)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createCounter() throws Exception {
        final LexicalUnit value = LexicalUnitImpl.createIdent(null, "CounterValue");
        final LexicalUnit unit = LexicalUnitImpl.createCounter(null, value);

        Assert.assertEquals(LexicalUnitType.COUNTER_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("counter", unit.getFunctionName());
        Assert.assertEquals("CounterValue", unit.getParameters().toString());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("counter(CounterValue)", unit.toString());
        Assert.assertEquals("COUNTER_FUNCTION(counter(CounterValue))", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createCounterNoValue() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createCounter(null, null);

        Assert.assertEquals(LexicalUnitType.COUNTER_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("counter", unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("counter()", unit.toString());
        Assert.assertEquals("COUNTER_FUNCTION(counter())", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createCounters() throws Exception {
        final LexicalUnit value1 = LexicalUnitImpl.createIdent(null, "CounterValue");
        LexicalUnitImpl.createIdent(LexicalUnitImpl.createComma(value1), "Second");
        final LexicalUnit unit = LexicalUnitImpl.createCounters(null, value1);

        Assert.assertEquals(LexicalUnitType.COUNTERS_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("counters", unit.getFunctionName());
        Assert.assertEquals("CounterValue", unit.getParameters().toString());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("counters(CounterValue, Second)", unit.toString());
        Assert.assertEquals("COUNTERS_FUNCTION(counters(CounterValue, Second))",
                ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createCountersNoValue() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createCounters(null, null);

        Assert.assertEquals(LexicalUnitType.COUNTERS_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("counters", unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("counters()", unit.toString());
        Assert.assertEquals("COUNTERS_FUNCTION(counters())", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createAttr() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createAttr(null, "attrValue");

        Assert.assertEquals(LexicalUnitType.ATTR, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("name", unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertEquals("attrValue", unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("attr(attrValue)", unit.toString());
        Assert.assertEquals("ATTR(attr(attrValue))", ((LexicalUnitImpl) unit).toDebugString());
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

        Assert.assertEquals(LexicalUnitType.RECT_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("rect", unit.getFunctionName());
        Assert.assertEquals("1", unit.getParameters().toString());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("rect(1, 2, 3, 4)", unit.toString());
        Assert.assertEquals("RECT_FUNCTION(rect(1, 2, 3, 4))", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createRgbColor() throws Exception {
        final LexicalUnit r = LexicalUnitImpl.createNumber(null, 255);
        final LexicalUnit g = LexicalUnitImpl.createNumber(LexicalUnitImpl.createComma(r), 128);
        LexicalUnitImpl.createNumber(LexicalUnitImpl.createComma(g), 0);
        final LexicalUnit unit = LexicalUnitImpl.createRgbColor(null, r);

        Assert.assertEquals(LexicalUnitType.RGBCOLOR, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("rgb", unit.getFunctionName());
        Assert.assertEquals("255", unit.getParameters().toString());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("rgb(255, 128, 0)", unit.toString());
        Assert.assertEquals("RGBCOLOR(rgb(255, 128, 0))", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createFunction() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createFunction(null, "foo",
                LexicalUnitImpl.createString(null, "param"));

        Assert.assertEquals(LexicalUnitType.FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("foo", unit.getFunctionName());
        Assert.assertEquals("\"param\"", unit.getParameters().toString());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("foo(\"param\")", unit.toString());
        Assert.assertEquals("FUNCTION(foo(\"param\"))", ((LexicalUnitImpl) unit).toDebugString());
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

        Assert.assertEquals(LexicalUnitType.FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertEquals("foo", unit.getFunctionName());
        Assert.assertEquals("10", unit.getParameters().toString());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("foo(10, 11)", unit.toString());
        Assert.assertEquals("FUNCTION(foo(10,11))", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createString() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createString(null, "RBRi");

        Assert.assertEquals(LexicalUnitType.STRING_VALUE, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertEquals("RBRi", unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("\"RBRi\"", unit.toString());
        Assert.assertEquals("STRING_VALUE(\"RBRi\")", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createStringContainingNewLine() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createString(null, "RBRi\ntest");

        Assert.assertEquals(LexicalUnitType.STRING_VALUE, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertEquals("RBRi\ntest", unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("\"RBRi\\A test\"", unit.toString());
        Assert.assertEquals("STRING_VALUE(\"RBRi\ntest\")", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createIdent() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createIdent(null, "css");

        Assert.assertEquals(LexicalUnitType.IDENT, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertEquals("css", unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("css", unit.toString());
        Assert.assertEquals("IDENT(css)", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createURI() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createURI(null, "www.wetator.org");

        Assert.assertEquals(LexicalUnitType.URI, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertEquals("www.wetator.org", unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals("url(www.wetator.org)", unit.toString());
        Assert.assertEquals("URI(url(www.wetator.org))", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void createComma() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createComma(null);

        Assert.assertEquals(LexicalUnitType.OPERATOR_COMMA, unit.getLexicalUnitType());
        Assert.assertEquals(0f, unit.getDoubleValue(), 0.0001f);
        Assert.assertEquals(0, unit.getIntegerValue());
        Assert.assertEquals("", unit.getDimensionUnitText());
        Assert.assertNull(unit.getFunctionName());
        Assert.assertNull(unit.getParameters());
        Assert.assertNull(unit.getStringValue());

        Assert.assertNull(unit.getNextLexicalUnit());
        Assert.assertNull(unit.getPreviousLexicalUnit());

        Assert.assertEquals(",", unit.toString());
        Assert.assertEquals("OPERATOR_COMMA", ((LexicalUnitImpl) unit).toDebugString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void serializeTest() throws Exception {
        final LexicalUnit unit = LexicalUnitImpl.createSecond(null, 10f);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(unit);
        oos.flush();
        oos.close();
        final byte[] bytes = baos.toByteArray();
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        final Object o = ois.readObject();

        Assert.assertEquals(unit.toString(), o.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void newlineInsideString() throws Exception {
        final LexicalUnitImpl unit = new LexicalUnitImpl(null, LexicalUnitType.STRING_VALUE);
        unit.setStringValue("abc \n def");

        Assert.assertEquals("abc \n def", unit.getStringValue());
        // CSS unicode escaping eats up the space char after the escape sequence
        // because of this we have to add this on reconstruction
        Assert.assertEquals("\"abc \\A  def\"", unit.getCssText());
    }
}
