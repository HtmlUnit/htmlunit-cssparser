/*
 * Copyright (c) 2019-2026 Ronald Brill.
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
 * Implementation of {@link Condition} for the {@code :lang()} pseudo-class.
 *
 * @author Ronald Brill
 */
public class LangCondition extends AbstractLocatable implements Condition, Serializable {

    /** The language. */
    private final String lang_;

    /**
     * Ctor.
     *
     * @param lang the language
     * @param locator the locator
     */
    public LangCondition(final String lang, final Locator locator) {
        lang_ = lang;
        setLocator(locator);
    }

    /** {@inheritDoc} */
    @Override
    public ConditionType getConditionType() {
        return ConditionType.LANG_CONDITION;
    }

    /** {@inheritDoc} */
    @Override
    public String getLocalName() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getValue() {
        return lang_;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(":lang(");

        final String lang = getValue();
        if (null != lang) {
            result.append(lang);
        }

        result.append(")");
        return result.toString();
    }
}
