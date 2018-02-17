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

/**
 * Special ElementSelectorImpl used by the parser at all the places where
 * the parser inserts a '*' selector. The selector will be ignored when
 * generating output.
 * This is done to be backward compatible.
 *
 * @author Ronald Brill
 */
public class SyntheticElementSelector extends ElementSelector {

    public SyntheticElementSelector() {
        super(null, null);
    }

    @Override
    public String toString() {
        return "";
    }
}
