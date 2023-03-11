/*
 * Copyright (c) 2019-2023 Ronald Brill.
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

import java.io.StringReader;

import org.htmlunit.cssparser.parser.CSSOMParser;
import org.htmlunit.cssparser.parser.InputSource;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CSSCharsetRuleImpl}.
 *
 * @author Ronald Brill
 */
public class CSSCharsetRuleImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final CSSCharsetRuleImpl value = parseCharsetRule("@charset \"utf-8\";");

        assertEquals("@charset \"utf-8\";", value.getCssText());
        assertEquals("@charset \"utf-8\";", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setCssText() throws Exception {
        final CSSCharsetRuleImpl value = parseCharsetRule("@charset \"utf-8\";");

        assertEquals("@charset \"utf-8\";", value.getCssText());
        assertEquals("@charset \"utf-8\";", value.toString());

        value.setCssText("@charset \"ASCII\";");
        assertEquals("@charset \"ASCII\";", value.getCssText());
        assertEquals("@charset \"ASCII\";", value.toString());

    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getSelectorText() throws Exception {
        final CSSCharsetRuleImpl value = parseCharsetRule("@charset \"Deutsch\";");
        assertEquals("Deutsch", value.getEncoding());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void type() throws Exception {
        final CSSCharsetRuleImpl value = new CSSCharsetRuleImpl(null, null, null);

        assertEquals("@charset \"\";", value.toString());
    }

    private CSSCharsetRuleImpl parseCharsetRule(final String rule) throws Exception {
        final InputSource is = new InputSource(new StringReader(rule));
        final CSSStyleSheetImpl ss = new CSSOMParser().parseStyleSheet(is, null);

        final CSSCharsetRuleImpl value = (CSSCharsetRuleImpl) ss.getCssRules().getRules().get(0);
        return value;
    }
    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFormated() throws Exception {
        final CSSCharsetRuleImpl value = parseCharsetRule("@charset \"utf-8\";");

        assertEquals("@charset \"utf-8\";", value.toString());
        assertEquals("@charset \"utf-8\";", value.getCssText());
    }
}
