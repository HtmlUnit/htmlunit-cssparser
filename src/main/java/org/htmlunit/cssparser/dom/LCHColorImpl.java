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

import java.io.Serializable;
import java.util.Locale;

import org.htmlunit.cssparser.parser.LexicalUnit;
import org.htmlunit.cssparser.parser.LexicalUnit.LexicalUnitType;
import org.w3c.dom.DOMException;

/**
 * Implementation of LCHColor.
 *
 * @author Ronald Brill
 */
public class LCHColorImpl implements Serializable {
    private CSSValueImpl lightness_;
    private CSSValueImpl chroma_;
    private CSSValueImpl hue_;
    private CSSValueImpl alpha_;

    /**
     * Constructor that reads the values from the given
     * chain of LexicalUnits.
     * @param function the name of the function; lch
     * @param lu the values
     * @throws DOMException in case of error
     */
    public LCHColorImpl(final String function, final LexicalUnit lu) throws DOMException {
        if (function == null) {
            throw new DOMException(DOMException.SYNTAX_ERR, "Color space lch is required.");
        }
        final String functionLC = function.toLowerCase(Locale.ROOT);
        if (!"lch".equals(functionLC)) {
            throw new DOMException(DOMException.SYNTAX_ERR, "Color space '" + functionLC + "' not supported.");
        }

        LexicalUnit next = lu;
        if (next == null) {
            throw new DOMException(DOMException.SYNTAX_ERR, "lch requires at least three values.");
        }

        lightness_ = getPart(next, "lightness");

        next = next.getNextLexicalUnit();
        if (next == null) {
            throw new DOMException(DOMException.SYNTAX_ERR, "lch requires at least three values.");
        }

        chroma_ = getPart(next, "chroma");

        next = next.getNextLexicalUnit();
        if (next == null) {
            throw new DOMException(DOMException.SYNTAX_ERR, "lch requires at least three values.");
        }

        hue_ = getHuePart(next);
        next = next.getNextLexicalUnit();
        if (next == null) {
            return;
        }

        if (next.getLexicalUnitType() != LexicalUnitType.OPERATOR_SLASH) {
            throw new DOMException(DOMException.SYNTAX_ERR, "lch alpha value must be separated by '/'.");
        }
        next = next.getNextLexicalUnit();
        if (next == null) {
            throw new DOMException(DOMException.SYNTAX_ERR, "Missing alpha value.");
        }

        alpha_ = getAlphaPart(next);

        next = next.getNextLexicalUnit();
        if (next != null) {
            throw new DOMException(DOMException.SYNTAX_ERR, "Too many parameters for lch function.");
        }
    }

    private static CSSValueImpl getPart(final LexicalUnit next, final String part) {
        if (LexicalUnitType.PERCENTAGE == next.getLexicalUnitType()

                || LexicalUnitType.INTEGER == next.getLexicalUnitType()
                || LexicalUnitType.REAL == next.getLexicalUnitType()

                || LexicalUnitType.NONE == next.getLexicalUnitType()) {
            return new CSSValueImpl(next, true);
        }

        throw new DOMException(DOMException.SYNTAX_ERR, "Color " + part + " part has to be numeric or percentage.");
    }

    private static CSSValueImpl getHuePart(final LexicalUnit next) {
        if (LexicalUnitType.DEGREE == next.getLexicalUnitType()
                || LexicalUnitType.RADIAN == next.getLexicalUnitType()
                || LexicalUnitType.GRADIAN == next.getLexicalUnitType()
                || LexicalUnitType.TURN == next.getLexicalUnitType()

                || LexicalUnitType.INTEGER == next.getLexicalUnitType()
                || LexicalUnitType.REAL == next.getLexicalUnitType()

                || LexicalUnitType.NONE == next.getLexicalUnitType()) {
            return new CSSValueImpl(next, true);
        }

        throw new DOMException(DOMException.SYNTAX_ERR, "Color hue part has to be numeric or an angle.");
    }

    private static CSSValueImpl getAlphaPart(final LexicalUnit next) {
        if (LexicalUnitType.PERCENTAGE == next.getLexicalUnitType()

                || LexicalUnitType.INTEGER == next.getLexicalUnitType()
                || LexicalUnitType.REAL == next.getLexicalUnitType()

                || LexicalUnitType.NONE == next.getLexicalUnitType()) {
            return new CSSValueImpl(next, true);
        }

        throw new DOMException(DOMException.SYNTAX_ERR, "Color alpha part has to be numeric or percentage.");
    }

    /**
     * @return the lightness part.
     */
    public CSSValueImpl getLightness() {
        return lightness_;
    }

    /**
     * Sets the lightness part to a new value.
     * @param lightness the new CSSValueImpl
     */
    public void setLightness(final CSSValueImpl lightness) {
        lightness_ = lightness;
    }

    /**
     * @return the chroma part.
     */
    public CSSValueImpl getChroma() {
        return chroma_;
    }

    /**
     * Sets the chroma part to a new value.
     * @param chroma the new CSSValueImpl
     */
    public void setChroma(final CSSValueImpl chroma) {
        chroma_ = chroma;
    }

    /**
     * @return the hue part.
     */
    public CSSValueImpl getHue() {
        return hue_;
    }

    /**
     * Sets the hue part to a new value.
     * @param hue the new CSSValueImpl
     */
    public void setHue(final CSSValueImpl hue) {
        hue_ = hue;
    }

    /**
     * @return the alpha part.
     */
    public CSSValueImpl getAlpha() {
        return alpha_;
    }

    /**
     * Sets the alpha part to a new value.
     * @param alpha the new CSSValueImpl
     */
    public void setAlpha(final CSSValueImpl alpha) {
        alpha_ = alpha;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb
            .append("lch(")
            .append(lightness_)
            .append(" ")
            .append(chroma_)
            .append(" ")
            .append(hue_);

        if (null != alpha_) {
            sb.append(" / ").append(alpha_);
        }

        sb.append(")");

        return sb.toString();
    }
}
