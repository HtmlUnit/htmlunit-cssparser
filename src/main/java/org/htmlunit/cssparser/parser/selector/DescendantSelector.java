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
package org.htmlunit.cssparser.parser.selector;

/**
 * <p>DescendantSelector class.</p>
 *
 * @author Ronald Brill
 */
public class DescendantSelector extends AbstractSelector {

    private final Selector ancestorSelector_;
    private final SimpleSelector simpleSelector_;

    /**
     * Ctor.
     * @param ancestorSelector the ancestor selector
     * @param simpleSelector the simple selector
     */
    public DescendantSelector(final Selector ancestorSelector, final SimpleSelector simpleSelector) {
        ancestorSelector_ = ancestorSelector;
        if (ancestorSelector != null) {
            setLocator(ancestorSelector.getLocator());
        }

        simpleSelector_ = simpleSelector;
    }

    /** {@inheritDoc} */
    @Override
    public SelectorType getSelectorType() {
        return SelectorType.DESCENDANT_SELECTOR;
    }

    /**
     * <p>getAncestorSelector.</p>
     *
     * @return the anchestor selector
     */
    public Selector getAncestorSelector() {
        return ancestorSelector_;
    }

    /** {@inheritDoc} */
    @Override
    public SimpleSelector getSimpleSelector() {
        return simpleSelector_;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        if (null != ancestorSelector_) {
            sb.append(ancestorSelector_);
        }

        if (SelectorType.PSEUDO_ELEMENT_SELECTOR != getSimpleSelector().getSelectorType()) {
            sb.append(' ');
        }

        if (null != simpleSelector_) {
            sb.append(simpleSelector_);
        }

        return sb.toString();
    }
}
