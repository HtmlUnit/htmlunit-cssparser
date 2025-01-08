/*
 * Copyright (c) 2019-2024 Ronald Brill.
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
package org.htmlunit.cssparser.dom;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.htmlunit.cssparser.parser.LexicalUnit;
import org.htmlunit.cssparser.parser.LexicalUnitImpl;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link HSLColorImpl}.
 *
 * @author Ronald Brill
 * @author Paul Selormey
 */
public class HSLColorImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final LexicalUnit hslLu = LexicalUnitImpl.createNumber(null, 235);
        LexicalUnit lu = LexicalUnitImpl.createComma(hslLu);
        lu = LexicalUnitImpl.createPercentage(lu, 20);
        lu = LexicalUnitImpl.createComma(lu);
        LexicalUnitImpl.createPercentage(lu, 30);

        final HSLColorImpl hsl = new HSLColorImpl("hsl", hslLu);

        assertEquals("hsl(235, 20%, 30%)", hsl.toString());
    }
}
