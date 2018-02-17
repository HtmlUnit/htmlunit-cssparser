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
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.css.parser.AbstractLocatable;

/**
 * Implementation of {@link SelectorList}.
 *
 * @author Ronald Brill
 */
public class SelectorListImpl extends AbstractLocatable implements SelectorList, Serializable {

    private List<Selector> selectors_ = new ArrayList<Selector>(10);

    public List<Selector> getSelectors() {
        return selectors_;
    }

    public void setSelectors(final List<Selector> selectors) {
        selectors_ = selectors;
    }

    @Override
    public int getLength() {
        return selectors_.size();
    }

    @Override
    public Selector item(final int index) {
        return selectors_.get(index);
    }

    public void add(final Selector sel) {
        selectors_.add(sel);
    }

    @Override
    public String toString() {
        final int len = getLength();

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(item(i).toString());
            if (i < len - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
