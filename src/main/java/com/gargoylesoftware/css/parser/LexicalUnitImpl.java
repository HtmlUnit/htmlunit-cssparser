/*
 * Copyright (c) 2018 Ronald Brill.
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

import java.io.Serializable;

/**
 * Implementation of {@link LexicalUnit}.
 *
 * @author Ronald Brill
 */
public class LexicalUnitImpl extends AbstractLocatable implements LexicalUnit, Serializable {

    private LexicalUnitType lexicalUnitType_;
    private LexicalUnit nextLexicalUnit_;
    private LexicalUnit previousLexicalUnit_;
    private float floatValue_;
    private String dimension_;
    private String functionName_;
    private LexicalUnit parameters_;
    private String stringValue_;
    private String sourceStringValue_;

    /** cache */
    private transient String toString_;

    public void setNextLexicalUnit(final LexicalUnit next) {
        nextLexicalUnit_ = next;
    }

    public void setPreviousLexicalUnit(final LexicalUnit prev) {
        previousLexicalUnit_ = prev;
    }

    public void setFloatValue(final float floatVal) {
        floatValue_ = floatVal;
        toString_ = null;
    }

    public String getDimension() {
        return dimension_;
    }

    public void setDimension(final String dimension) {
        dimension_ = dimension;
        toString_ = null;
    }

    public void setFunctionName(final String function) {
        functionName_ = function;
        toString_ = null;
    }

    public void setParameters(final LexicalUnit params) {
        parameters_ = params;
        toString_ = null;
    }

    public void setStringValue(final String stringVal) {
        stringValue_ = stringVal;
        toString_ = null;
    }

    protected LexicalUnitImpl(final LexicalUnit previous, final LexicalUnitType type) {
        lexicalUnitType_ = type;
        previousLexicalUnit_ = previous;
        if (previousLexicalUnit_ != null) {
            ((LexicalUnitImpl) previousLexicalUnit_).nextLexicalUnit_ = this;
        }
    }

    /**
     * Integer
     */
    protected LexicalUnitImpl(final LexicalUnit previous, final int value) {
        this(previous, LexicalUnitType.INTEGER);
        floatValue_ = value;
    }

    /**
     * Dimension
     */
    protected LexicalUnitImpl(final LexicalUnit previous, final LexicalUnitType type, final float value) {
        this(previous, type);
        floatValue_ = value;
    }

    /**
     * Unknown dimension
     */
    protected LexicalUnitImpl(
            final LexicalUnit previous,
            final LexicalUnitType type,
            final String dimension,
            final float value) {
        this(previous, type);
        dimension_ = dimension;
        floatValue_ = value;
    }

    /**
     * String
     */
    protected LexicalUnitImpl(final LexicalUnit previous, final LexicalUnitType type, final String value) {
        this(previous, type);
        stringValue_ = value;
    }

    /**
     * Function
     */
    protected LexicalUnitImpl(
            final LexicalUnit previous,
            final LexicalUnitType type,
            final String name,
            final LexicalUnit params) {
        this(previous, type);
        functionName_ = name;
        parameters_ = params;
    }

    protected LexicalUnitImpl(final LexicalUnit previous, final LexicalUnitType type, final String name,
            final String stringValue) {
        this(previous, type);
        functionName_ = name;
        stringValue_ = stringValue;
    }

    @Override
    public LexicalUnitType getLexicalUnitType() {
        return lexicalUnitType_;
    }

    @Override
    public LexicalUnit getNextLexicalUnit() {
        return nextLexicalUnit_;
    }

    @Override
    public LexicalUnit getPreviousLexicalUnit() {
        return previousLexicalUnit_;
    }

    @Override
    public int getIntegerValue() {
        return (int) floatValue_;
    }

    @Override
    public float getFloatValue() {
        return floatValue_;
    }

    @Override
    public String getDimensionUnitText() {
        switch (lexicalUnitType_) {
            case EM:
                return "em";
            case EX:
                return "ex";
            case PIXEL:
                return "px";
            case INCH:
                return "in";
            case CENTIMETER:
                return "cm";
            case MILLIMETER:
                return "mm";
            case POINT:
                return "pt";
            case PICA:
                return "pc";
            case PERCENTAGE:
                return "%";
            case DEGREE:
                return "deg";
            case GRADIAN:
                return "grad";
            case RADIAN:
                return "rad";
            case MILLISECOND:
                return "ms";
            case SECOND:
                return "s";
            case HERTZ:
                return "Hz";
            case KILOHERTZ:
                return "kHz";
            case DIMENSION:
                return dimension_;
            default:
                return "";
        }
    }

    @Override
    public String getFunctionName() {
        return functionName_;
    }

    @Override
    public LexicalUnit getParameters() {
        return parameters_;
    }

    @Override
    public String getStringValue() {
        return stringValue_;
    }

    public String getSourceStringValue() {
        return sourceStringValue_;
    }

    @Override
    public LexicalUnit getSubValues() {
        return parameters_;
    }

    /**
     * {@inheritDoc}
     */
    public String getCssText() {
        if (null != toString_) {
            return toString_;
        }

        final StringBuilder sb = new StringBuilder();
        switch (lexicalUnitType_) {
            case OPERATOR_COMMA:
                sb.append(",");
                break;
            case OPERATOR_PLUS:
                sb.append("+");
                break;
            case OPERATOR_MINUS:
                sb.append("-");
                break;
            case OPERATOR_MULTIPLY:
                sb.append("*");
                break;
            case OPERATOR_SLASH:
                sb.append("/");
                break;
            case OPERATOR_MOD:
                sb.append("%");
                break;
            case OPERATOR_EXP:
                sb.append("^");
                break;
            case OPERATOR_LT:
                sb.append("<");
                break;
            case OPERATOR_GT:
                sb.append(">");
                break;
            case OPERATOR_LE:
                sb.append("<=");
                break;
            case OPERATOR_GE:
                sb.append(">=");
                break;
            case OPERATOR_TILDE:
                sb.append("~");
                break;
            case INHERIT:
                sb.append("inherit");
                break;
            case INTEGER:
                sb.append(String.valueOf(getIntegerValue()));
                break;
            case REAL:
                sb.append(getTrimedFloatValue());
                break;
            case EM:
            case EX:
            case PIXEL:
            case INCH:
            case CENTIMETER:
            case MILLIMETER:
            case POINT:
            case PICA:
            case PERCENTAGE:
            case DEGREE:
            case GRADIAN:
            case RADIAN:
            case MILLISECOND:
            case SECOND:
            case HERTZ:
            case KILOHERTZ:
            case DIMENSION:
                sb.append(getTrimedFloatValue());
                final String dimUnitText = getDimensionUnitText();
                if (null != dimUnitText) {
                    sb.append(dimUnitText);
                }
                break;
            case URI:
                sb.append("url(").append(getStringValue()).append(")");
                break;
            case COUNTER_FUNCTION:
                sb.append("counter(");
                appendParams(sb);
                sb.append(")");
                break;
            case COUNTERS_FUNCTION:
                sb.append("counters(");
                appendParams(sb);
                sb.append(")");
                break;
            case RGBCOLOR:
                sb.append("rgb(");
                appendParams(sb);
                sb.append(")");
                break;
            case IDENT:
                sb.append(getStringValue());
                break;
            case STRING_VALUE:
                sb.append("\"");

                String value = getStringValue();
                // replace line breaks
                value = value.replace("\n", "\\A ").replace("\r", "\\D ");
                sb.append(value);

                sb.append("\"");
                break;
            case ATTR:
                sb.append("attr(")
                    .append(getStringValue())
                    .append(")");
                break;
            case RECT_FUNCTION:
                sb.append("rect(");
                appendParams(sb);
                sb.append(")");
                break;
            case UNICODERANGE:
                final String range = getStringValue();
                if (null != range) {
                    sb.append(range);
                }
                break;
            case SUB_EXPRESSION:
                final String subExpression = getStringValue();
                if (null != subExpression) {
                    sb.append(subExpression);
                }
                break;
            case FUNCTION:
                final String functName = getFunctionName();
                if (null != functName) {
                    sb.append(functName);
                }
                sb.append('(');
                appendParams(sb);
                sb.append(")");
                break;
            default:
                break;
        }
        toString_ = sb.toString();
        return toString_;
    }

    @Override
    public String toString() {
        return getCssText();
    }

    public String toDebugString() {
        final StringBuilder sb = new StringBuilder();
        switch (lexicalUnitType_) {
            case OPERATOR_COMMA:
                sb.append("OPERATOR_COMMA");
                break;
            case OPERATOR_PLUS:
                sb.append("OPERATOR_PLUS");
                break;
            case OPERATOR_MINUS:
                sb.append("OPERATOR_MINUS");
                break;
            case OPERATOR_MULTIPLY:
                sb.append("OPERATOR_MULTIPLY");
                break;
            case OPERATOR_SLASH:
                sb.append("OPERATOR_SLASH");
                break;
            case OPERATOR_MOD:
                sb.append("OPERATOR_MOD");
                break;
            case OPERATOR_EXP:
                sb.append("OPERATOR_EXP");
                break;
            case OPERATOR_LT:
                sb.append("OPERATOR_LT");
                break;
            case OPERATOR_GT:
                sb.append("OPERATOR_GT");
                break;
            case OPERATOR_LE:
                sb.append("OPERATOR_LE");
                break;
            case OPERATOR_GE:
                sb.append("OPERATOR_GE");
                break;
            case OPERATOR_TILDE:
                sb.append("OPERATOR_TILDE");
                break;
            case INHERIT:
                sb.append("INHERIT");
                break;
            case INTEGER:
                sb.append("INTEGER(")
                    .append(String.valueOf(getIntegerValue()))
                    .append(")");
                break;
            case REAL:
                sb.append("REAL(")
                    .append(getTrimedFloatValue())
                    .append(")");
                break;
            case EM:
                sb.append("EM(")
                    .append(getTrimedFloatValue())
                    .append(getDimensionUnitText())
                    .append(")");
                break;
            case EX:
                sb.append("EX(")
                    .append(getTrimedFloatValue())
                    .append(getDimensionUnitText())
                    .append(")");
                break;
            case PIXEL:
                sb.append("PIXEL(")
                    .append(getTrimedFloatValue())
                    .append(getDimensionUnitText())
                    .append(")");
                break;
            case INCH:
                sb.append("INCH(")
                    .append(getTrimedFloatValue())
                    .append(getDimensionUnitText())
                    .append(")");
                break;
            case CENTIMETER:
                sb.append("CENTIMETER(")
                    .append(getTrimedFloatValue())
                    .append(getDimensionUnitText())
                    .append(")");
                break;
            case MILLIMETER:
                sb.append("MILLIMETER(")
                    .append(getTrimedFloatValue())
                    .append(getDimensionUnitText())
                    .append(")");
                break;
            case POINT:
                sb.append("POINT(")
                    .append(getTrimedFloatValue())
                    .append(getDimensionUnitText())
                    .append(")");
                break;
            case PICA:
                sb.append("PICA(")
                    .append(getTrimedFloatValue())
                    .append(getDimensionUnitText())
                    .append(")");
                break;
            case PERCENTAGE:
                sb.append("PERCENTAGE(")
                    .append(getTrimedFloatValue())
                    .append(getDimensionUnitText())
                    .append(")");
                break;
            case DEGREE:
                sb.append("DEGREE(")
                    .append(getTrimedFloatValue())
                    .append(getDimensionUnitText())
                    .append(")");
                break;
            case GRADIAN:
                sb.append("GRADIAN(")
                    .append(getTrimedFloatValue())
                    .append(getDimensionUnitText())
                    .append(")");
                break;
            case RADIAN:
                sb.append("RADIAN(")
                    .append(getTrimedFloatValue())
                    .append(getDimensionUnitText())
                    .append(")");
                break;
            case MILLISECOND:
                sb.append("MILLISECOND(")
                    .append(getTrimedFloatValue())
                    .append(getDimensionUnitText())
                    .append(")");
                break;
            case SECOND:
                sb.append("SECOND(")
                    .append(getTrimedFloatValue())
                    .append(getDimensionUnitText())
                    .append(")");
                break;
            case HERTZ:
                sb.append("HERTZ(")
                    .append(getTrimedFloatValue())
                    .append(getDimensionUnitText())
                    .append(")");
                break;
            case KILOHERTZ:
                sb.append("KILOHERTZ(")
                    .append(getTrimedFloatValue())
                    .append(getDimensionUnitText())
                    .append(")");
                break;
            case DIMENSION:
                sb.append("DIMENSION(")
                    .append(getTrimedFloatValue())
                    .append(getDimensionUnitText())
                    .append(")");
                break;
            case URI:
                sb.append("URI(url(")
                    .append(getStringValue())
                    .append("))");
                break;
            case COUNTER_FUNCTION:
                sb.append("COUNTER_FUNCTION(counter(");
                appendParams(sb);
                sb.append("))");
                break;
            case COUNTERS_FUNCTION:
                sb.append("COUNTERS_FUNCTION(counters(");
                appendParams(sb);
                sb.append("))");
                break;
            case RGBCOLOR:
                sb.append("RGBCOLOR(rgb(");
                appendParams(sb);
                sb.append("))");
                break;
            case IDENT:
                sb.append("IDENT(")
                    .append(getStringValue())
                    .append(")");
                break;
            case STRING_VALUE:
                sb.append("STRING_VALUE(\"")
                    .append(getStringValue())
                    .append("\")");
                break;
            case ATTR:
                sb.append("ATTR(attr(")
                    .append(getStringValue())
                    .append("))");
                break;
            case RECT_FUNCTION:
                sb.append("RECT_FUNCTION(rect(");
                appendParams(sb);
                sb.append("))");
                break;
            case UNICODERANGE:
                sb.append("UNICODERANGE(")
                    .append(getStringValue())
                    .append(")");
                break;
            case SUB_EXPRESSION:
                sb.append("SUB_EXPRESSION(")
                    .append(getStringValue())
                    .append(")");
                break;
            case FUNCTION:
                sb.append("FUNCTION(")
                    .append(getFunctionName())
                    .append("(");
                LexicalUnit l = parameters_;
                while (l != null) {
                    sb.append(l.toString());
                    l = l.getNextLexicalUnit();
                }
                sb.append("))");
                break;
            default:
                break;
        }
        return sb.toString();
    }

    private void appendParams(final StringBuilder sb) {
        LexicalUnit l = parameters_;
        if (l != null) {
            sb.append(l.toString());
            l = l.getNextLexicalUnit();
            while (l != null) {
                if (l.getLexicalUnitType() != LexicalUnitType.OPERATOR_COMMA) {
                    sb.append(" ");
                }
                sb.append(l.toString());
                l = l.getNextLexicalUnit();
            }
        }
    }

    private String getTrimedFloatValue() {
        final float f = getFloatValue();
        final int i = (int) f;

        if (f - i == 0) {
            return Integer.toString((int) f);
        }
        return Float.toString(f);
    }

    public static LexicalUnit createNumber(final LexicalUnit prev, final int i) {
        return new LexicalUnitImpl(prev, i);
    }

    public static LexicalUnit createNumber(final LexicalUnit prev, final float f) {
        return new LexicalUnitImpl(prev, LexicalUnitType.REAL, f);
    }

    public static LexicalUnit createPercentage(final LexicalUnit prev, final float f) {
        return new LexicalUnitImpl(prev, LexicalUnitType.PERCENTAGE, f);
    }

    public static LexicalUnit createPixel(final LexicalUnit prev, final float f) {
        return new LexicalUnitImpl(prev, LexicalUnitType.PIXEL, f);
    }

    public static LexicalUnit createCentimeter(final LexicalUnit prev, final float f) {
        return new LexicalUnitImpl(prev, LexicalUnitType.CENTIMETER, f);
    }

    public static LexicalUnit createMillimeter(final LexicalUnit prev, final float f) {
        return new LexicalUnitImpl(prev, LexicalUnitType.MILLIMETER, f);
    }

    public static LexicalUnit createInch(final LexicalUnit prev, final float f) {
        return new LexicalUnitImpl(prev, LexicalUnitType.INCH, f);
    }

    public static LexicalUnit createPoint(final LexicalUnit prev, final float f) {
        return new LexicalUnitImpl(prev, LexicalUnitType.POINT, f);
    }

    public static LexicalUnit createPica(final LexicalUnit prev, final float f) {
        return new LexicalUnitImpl(prev, LexicalUnitType.PICA, f);
    }

    public static LexicalUnit createEm(final LexicalUnit prev, final float f) {
        return new LexicalUnitImpl(prev, LexicalUnitType.EM, f);
    }

    public static LexicalUnit createEx(final LexicalUnit prev, final float f) {
        return new LexicalUnitImpl(prev, LexicalUnitType.EX, f);
    }

    public static LexicalUnit createDegree(final LexicalUnit prev, final float f) {
        return new LexicalUnitImpl(prev, LexicalUnitType.DEGREE, f);
    }

    public static LexicalUnit createRadian(final LexicalUnit prev, final float f) {
        return new LexicalUnitImpl(prev, LexicalUnitType.RADIAN, f);
    }

    public static LexicalUnit createGradian(final LexicalUnit prev, final float f) {
        return new LexicalUnitImpl(prev, LexicalUnitType.GRADIAN, f);
    }

    public static LexicalUnit createMillisecond(final LexicalUnit prev, final float f) {
        return new LexicalUnitImpl(prev, LexicalUnitType.MILLISECOND, f);
    }

    public static LexicalUnit createSecond(final LexicalUnit prev, final float f) {
        return new LexicalUnitImpl(prev, LexicalUnitType.SECOND, f);
    }

    public static LexicalUnit createHertz(final LexicalUnit prev, final float f) {
        return new LexicalUnitImpl(prev, LexicalUnitType.HERTZ, f);
    }

    public static LexicalUnit createDimension(final LexicalUnit prev, final float f, final String dim) {
        return new LexicalUnitImpl(prev, LexicalUnitType.DIMENSION, dim, f);
    }

    public static LexicalUnit createKiloHertz(final LexicalUnit prev, final float f) {
        return new LexicalUnitImpl(prev, LexicalUnitType.KILOHERTZ, f);
    }

    public static LexicalUnit createCounter(final LexicalUnit prev, final LexicalUnit params) {
        return new LexicalUnitImpl(prev, LexicalUnitType.COUNTER_FUNCTION, "counter", params);
    }

    public static LexicalUnit createCounters(final LexicalUnit prev, final LexicalUnit params) {
        return new LexicalUnitImpl(prev, LexicalUnitType.COUNTERS_FUNCTION, "counters", params);
    }

    public static LexicalUnit createAttr(final LexicalUnit prev, final String value) {
        // according to LexicalUnit.ATTR, LexicalUnit.getStringValue(), not
        // LexicalUnit.getParameters() is applicable
        return new LexicalUnitImpl(prev, LexicalUnitType.ATTR, "name", value);
    }

    public static LexicalUnit createRect(final LexicalUnit prev, final LexicalUnit params) {
        return new LexicalUnitImpl(prev, LexicalUnitType.RECT_FUNCTION, "rect", params);
    }

    public static LexicalUnit createRgbColor(final LexicalUnit prev, final LexicalUnit params) {
        return new LexicalUnitImpl(prev, LexicalUnitType.RGBCOLOR, "rgb", params);
    }

    public static LexicalUnit createFunction(final LexicalUnit prev, final String name, final LexicalUnit params) {
        return new LexicalUnitImpl(prev, LexicalUnitType.FUNCTION, name, params);
    }

    public static LexicalUnit createString(final LexicalUnit prev, final String value) {
        return new LexicalUnitImpl(prev, LexicalUnitType.STRING_VALUE, value);
    }

    public static LexicalUnit createString(final LexicalUnit prev, final String value, final String sourceStringValue) {
        final LexicalUnitImpl unit = new LexicalUnitImpl(prev, LexicalUnitType.STRING_VALUE, value);
        unit.sourceStringValue_ = sourceStringValue;
        return unit;
    }

    public static LexicalUnit createIdent(final LexicalUnit prev, final String value) {
        return new LexicalUnitImpl(prev, LexicalUnitType.IDENT, value);
    }

    public static LexicalUnit createURI(final LexicalUnit prev, final String value) {
        return new LexicalUnitImpl(prev, LexicalUnitType.URI, value);
    }

    public static LexicalUnit createComma(final LexicalUnit prev) {
        return new LexicalUnitImpl(prev, LexicalUnitType.OPERATOR_COMMA);
    }
}
