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

import java.io.Serializable;

import org.htmlunit.cssparser.parser.AbstractLocatable;

/**
 * <p>Abstract AbstractSelector class.</p>
 *
 * @author Ronald Brill
 */
public abstract class AbstractSelector extends AbstractLocatable implements Selector, Serializable {

    private SelectorSpecificity specificity_;

    /** {@inheritDoc} */
    @Override
    public SelectorSpecificity getSelectorSpecificity() {
        if (specificity_ == null) {
            specificity_ = new SelectorSpecificity(this);
        }
        return specificity_;
    }
}
