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
package org.htmlunit.cssparser.parser.selector;

import org.htmlunit.cssparser.parser.Locatable;

/**
 * Interface for CSS selectors.
 *
 * @author Ronald Brill
 */
public interface Selector extends Locatable {

    /**
     * SelectorType enum.
     */
    enum SelectorType {
        /** CHILD_SELECTOR. */
        CHILD_SELECTOR,

        /** DESCENDANT_SELECTOR. */
        DESCENDANT_SELECTOR,

        /** DIRECT_ADJACENT_SELECTOR. */
        DIRECT_ADJACENT_SELECTOR,

        /** ELEMENT_NODE_SELECTOR. */
        ELEMENT_NODE_SELECTOR,

        /** GENERAL_ADJACENT_SELECTOR. */
        GENERAL_ADJACENT_SELECTOR,

        /** PSEUDO_ELEMENT_SELECTOR. */
        PSEUDO_ELEMENT_SELECTOR,

        /** RELATIVE_SELECTOR. */
        RELATIVE_SELECTOR
    }

    /**
     * Returns the associated selector type.
     *
     * @return the associated selector type
     */
    SelectorType getSelectorType();

    /**
     * Returns the simple selector part.
     *
     * @return the simple selector part
     */
    SimpleSelector getSimpleSelector();

    /**
     * Returns the selector specificity.
     *
     * @return the selector specificity
     */
    SelectorSpecificity getSelectorSpecificity();
}
