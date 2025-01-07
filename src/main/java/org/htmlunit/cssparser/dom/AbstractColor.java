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
import java.util.function.Consumer;

import org.htmlunit.cssparser.parser.LexicalUnit;
import org.htmlunit.cssparser.parser.LexicalUnit.LexicalUnitType;
import org.w3c.dom.DOMException;

/**
 * Color base class.
 *
 * @author Ronald Brill
 */
public class AbstractColor implements Serializable {

    private CSSValueImpl alpha_;
    private CSSValueImpl relative_;

    protected void getNumberPercentagePart(final LexicalUnit next, final Consumer<CSSValueImpl> setter) {
        if (LexicalUnitType.PERCENTAGE == next.getLexicalUnitType()

                || LexicalUnitType.INTEGER == next.getLexicalUnitType()
                || LexicalUnitType.REAL == next.getLexicalUnitType()

                || LexicalUnitType.NONE == next.getLexicalUnitType()) {
            setter.accept(new CSSValueImpl(next, true));
            return;
        }

        throw new DOMException(DOMException.SYNTAX_ERR, "Color part has to be numeric or percentage.");
    }

    protected void getAlphaPart(final LexicalUnit lu) {
        if (LexicalUnitType.PERCENTAGE == lu.getLexicalUnitType()

                || LexicalUnitType.INTEGER == lu.getLexicalUnitType()
                || LexicalUnitType.REAL == lu.getLexicalUnitType()

                || LexicalUnitType.NONE == lu.getLexicalUnitType()) {
            setAlpha(new CSSValueImpl(lu, true));
            return;
        }

        throw new DOMException(DOMException.SYNTAX_ERR, "Color alpha part has to be numeric or percentage.");
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
     * @return the relative value or null.
     */
    public CSSValueImpl getRelative() {
        return relative_;
    }

    public boolean handleRelativeColors(final LexicalUnit lu) {
        if (LexicalUnitType.FROM == lu.getLexicalUnitType()) {
            relative_ = new CSSValueImpl(lu.getNextLexicalUnit());
            return true;
        }
        return false;
    }
}
