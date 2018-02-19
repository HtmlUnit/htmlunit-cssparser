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
package com.gargoylesoftware.css.parser.condition;

import java.io.Serializable;

import com.gargoylesoftware.css.parser.AbstractLocatable;
import com.gargoylesoftware.css.parser.Locatable;

/**
 * @author Ronald Brill
 */
public class AndCondition extends AbstractLocatable implements Condition, Serializable {

    private final Condition firstCondition_;
    private final Condition secondCondition_;

    /**
     * Ctor.
     * @param c1 the first condition
     * @param c2 the second condition
     */
    public AndCondition(final Condition c1, final Condition c2) {
        firstCondition_ = c1;
        if (c1 instanceof Locatable) {
            setLocator(((Locatable) c1).getLocator());
        }

        secondCondition_ = c2;
        if (getLocator() == null && secondCondition_ != null) {
            setLocator(secondCondition_.getLocator());
        }
    }

    @Override
    public ConditionType getConditionType() {
        return ConditionType.AND_CONDITION;
    }

    public Condition getFirstCondition() {
        return firstCondition_;
    }

    public Condition getSecondCondition() {
        return secondCondition_;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        Condition cond = getFirstCondition();
        if (null != cond) {
            sb.append(cond.toString());
        }

        cond = getSecondCondition();
        if (null != cond) {
            sb.append(cond.toString());
        }

        return sb.toString();
    }
}
