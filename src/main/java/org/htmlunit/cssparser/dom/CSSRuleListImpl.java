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
package org.htmlunit.cssparser.dom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.cssparser.util.ParserUtils;

/**
 * Implementation of CSSRuleList.
 *
 * @author Ronald Brill
 */
public class CSSRuleListImpl implements Serializable {

    private final List<AbstractCSSRuleImpl> rules_ = new ArrayList<>();

    /**
     * <p>getRules.</p>
     *
     * @return the rules
     */
    public List<AbstractCSSRuleImpl> getRules() {
        return rules_;
    }

    /**
     * Ctor.
     */
    public CSSRuleListImpl() {
        super();
    }

    /**
     * <p>getLength.</p>
     *
     * @return the number of rules
     */
    public int getLength() {
        return getRules().size();
    }

    /**
     * Add a rule.
     * @param rule the rule to be added
     */
    public void add(final AbstractCSSRuleImpl rule) {
        getRules().add(rule);
    }

    /**
     * Insert a rule at the given pos.
     * @param rule the rule to be inserted
     * @param index the insert pos
     */
    public void insert(final AbstractCSSRuleImpl rule, final int index) {
        getRules().add(index, rule);
    }

    /**
     * Delete the rule at the given pos.
     * @param index the delete pos
     */
    public void delete(final int index) {
        getRules().remove(index);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final AbstractCSSRuleImpl rule : rules_) {
            if (sb.length() > 0) {
                sb.append("\r\n");
            }
            sb.append(rule.toString());
        }
        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CSSRuleListImpl)) {
            return false;
        }
        final CSSRuleListImpl crl = (CSSRuleListImpl) obj;
        return equalsRules(crl);
    }

    private boolean equalsRules(final CSSRuleListImpl crl) {
        if ((crl == null) || (getLength() != crl.getLength())) {
            return false;
        }
        int i = 0;
        for (final AbstractCSSRuleImpl rule : rules_) {
            final AbstractCSSRuleImpl cssRule2 = crl.rules_.get(i);
            if (!ParserUtils.equals(rule, cssRule2)) {
                return false;
            }
            i++;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = ParserUtils.HASH_SEED;
        hash = ParserUtils.hashCode(hash, rules_);
        return hash;
    }
}
