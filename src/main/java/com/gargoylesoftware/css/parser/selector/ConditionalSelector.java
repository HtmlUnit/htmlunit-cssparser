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

package com.gargoylesoftware.css.parser.selector;

import java.io.Serializable;

import com.gargoylesoftware.css.parser.AbstractLocatable;
import com.gargoylesoftware.css.parser.condition.Condition;

/**
 *
 * @author Ronlad Brill
 */
public class ConditionalSelector extends AbstractLocatable implements SimpleSelector, Serializable {

    private final SimpleSelector simpleSelector_;
    private final Condition condition_;

    public ConditionalSelector(final SimpleSelector simpleSelector, final Condition condition) {
        simpleSelector_ = simpleSelector;
        if (simpleSelector != null) {
            setLocator(simpleSelector.getLocator());
        }

        condition_ = condition;
        if (getLocator() == null && condition != null) {
            setLocator(condition.getLocator());
        }
    }

    @Override
    public SelectorType getSelectorType() {
        return SelectorType.CONDITIONAL_SELECTOR;
    }

    public SimpleSelector getSimpleSelector() {
        return simpleSelector_;
    }

    public Condition getCondition() {
        return condition_;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        if (null != simpleSelector_) {
            sb.append(simpleSelector_.toString());
        }

        if (null != condition_) {
            sb.append(condition_.toString());
        }

        return sb.toString();
    }
}
