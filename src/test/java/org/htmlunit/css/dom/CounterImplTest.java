/*
 * Copyright (c) 2019-2021 Ronald Brill.
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
package org.htmlunit.css.dom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.htmlunit.css.parser.LexicalUnit;
import org.htmlunit.css.parser.LexicalUnitImpl;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CounterImpl}.
 *
 * @author Ronald Brill
 */
public class CounterImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLU() throws Exception {
        final LexicalUnit counterLu = LexicalUnitImpl.createString(null, "ident");

        CounterImpl counter = new CounterImpl(false, counterLu);
        assertEquals("counter(ident)", counter.toString());
        assertEquals("ident", counter.getIdentifier());
        assertNull(counter.getSeparator());
        assertNull(counter.getListStyle());

        counter = new CounterImpl(true, counterLu);
        assertEquals("counter(ident)", counter.toString());
        assertEquals("ident", counter.getIdentifier());
        assertNull(counter.getSeparator());
        assertNull(counter.getListStyle());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUSeparator() throws Exception {
        final LexicalUnit counterLu = LexicalUnitImpl.createString(null, "ident");
        LexicalUnit lu = LexicalUnitImpl.createComma(counterLu);
        lu = LexicalUnitImpl.createString(lu, "sep");

        CounterImpl counter = new CounterImpl(false, counterLu);
        assertEquals("counter(ident, sep)", counter.toString());
        assertEquals("ident", counter.getIdentifier());
        assertNull(counter.getSeparator());
        assertEquals("sep", counter.getListStyle());

        counter = new CounterImpl(true, counterLu);
        assertEquals("counters(ident, \"sep\")", counter.toString());
        assertEquals("ident", counter.getIdentifier());
        assertEquals("sep", counter.getSeparator());
        assertNull(counter.getListStyle());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void constructByLUSeparatorList() throws Exception {
        final LexicalUnit counterLu = LexicalUnitImpl.createString(null, "ident");
        LexicalUnit lu = LexicalUnitImpl.createComma(counterLu);
        lu = LexicalUnitImpl.createString(lu, "sep");
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createString(lu, "list");

        final CounterImpl counter = new CounterImpl(true, counterLu);
        assertEquals("counters(ident, \"sep\", list)", counter.toString());
        assertEquals("ident", counter.getIdentifier());
        assertEquals("sep", counter.getSeparator());
        assertEquals("list", counter.getListStyle());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final LexicalUnit counterLu = LexicalUnitImpl.createString(null, "ident");
        LexicalUnit lu = LexicalUnitImpl.createComma(counterLu);
        lu = LexicalUnitImpl.createString(lu, "sep");
        lu = LexicalUnitImpl.createComma(lu);
        lu = LexicalUnitImpl.createString(lu, "list");

        final CounterImpl counter = new CounterImpl(true, counterLu);

        assertEquals("counters(ident, \"sep\", list)", counter.toString());
    }
}
