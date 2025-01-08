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
 * Unit tests for {@link LCHColorImpl}.
 *
 * @author Ronald Brill
 */
public class LCHColorImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final LexicalUnit lchLu = LexicalUnitImpl.createNumber(null, 235);
        final LexicalUnit lu = LexicalUnitImpl.createPercentage(lchLu, 20);
        LexicalUnitImpl.createRadian(lu, 30);

        final LCHColorImpl lch = new LCHColorImpl("lch", lchLu);

        assertEquals("lch(235 20% 30rad)", lch.toString());
    }
}
