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

/**
 * @author Ronald Brill
 */
public class DirectAdjacentSelector extends AbstractLocatable implements Selector, Serializable {

    private final SelectorType nodeType_;
    private final Selector selector_;  // child
    private final SimpleSelector siblingSelector_; // direct adjacent

    public DirectAdjacentSelector(final SelectorType nodeType,
            final Selector child, final SimpleSelector directAdjacent) {
        nodeType_ = nodeType;
        selector_ = child;
        if (child != null) {
            setLocator(child.getLocator());
        }
        siblingSelector_ = directAdjacent;
    }

    public SelectorType getNodeType() {
        return nodeType_;
    }

    @Override
    public SelectorType getSelectorType() {
        return SelectorType.DIRECT_ADJACENT_SELECTOR;
    }

    public Selector getSelector() {
        return selector_;
    }

    public SimpleSelector getSiblingSelector() {
        return siblingSelector_;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        if (null != selector_) {
            sb.append(selector_.toString());
        }

        sb.append(" + ");

        if (null != siblingSelector_) {
            sb.append(siblingSelector_.toString());
        }

        return sb.toString();
    }
}
