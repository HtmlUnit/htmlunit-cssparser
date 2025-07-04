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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.htmlunit.cssparser.parser.LexicalUnit;
import org.htmlunit.cssparser.parser.LexicalUnitImpl;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link RGBColorImpl}.
 *
 * @author Ronald Brill
 */
public class RGBColorImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final LexicalUnit rgbLu = LexicalUnitImpl.createNumber(null, 10);
        LexicalUnit lu = LexicalUnitImpl.createComma(rgbLu);
        lu = LexicalUnitImpl.createNumber(lu, 20);
        lu = LexicalUnitImpl.createComma(lu);
        LexicalUnitImpl.createNumber(lu, 30);

        final RGBColorImpl rgb = new RGBColorImpl("rgb", rgbLu);

        assertEquals("rgb(10, 20, 30)", rgb.toString());
    }
}
