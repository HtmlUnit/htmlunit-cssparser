/*
 * Copyright (c) 2019-2024 Ronald Brill.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.cssparser.parser.condition;

import java.io.Serializable;

import org.htmlunit.cssparser.parser.AbstractLocatable;
import org.htmlunit.cssparser.parser.Locator;

/**
 * <p>PseudoClassCondition class.</p>
 *
 * @author Ronald Brill
 */
public class PseudoClassCondition extends AbstractLocatable implements Condition, Serializable {

    private final String value_;
    private final boolean doubleColon_;

    /**
     * Ctor.
     * @param value the value
     * @param locator the locator
     * @param doubleColon true if was prefixed by double colon
     */
    public PseudoClassCondition(final String value, final Locator locator, final boolean doubleColon) {
        value_ = value;
        setLocator(locator);
        doubleColon_ = doubleColon;
    }

    /** {@inheritDoc} */
    @Override
    public ConditionType getConditionType() {
        return ConditionType.PSEUDO_CLASS_CONDITION;
    }

    /** {@inheritDoc} */
    @Override
    public String getLocalName() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getValue() {
        return value_;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final String value = getValue();
        if (value == null) {
            return value;
        }
        return (doubleColon_ ? "::" : ":") + value;
    }
}
