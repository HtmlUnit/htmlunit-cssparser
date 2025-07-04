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

import java.io.StringReader;

import org.htmlunit.cssparser.parser.CSSOMParser;
import org.htmlunit.cssparser.parser.InputSource;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CSSImportRuleImpl}.
 *
 * @author Ronald Brill
 */
public class CSSImportRuleImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final CSSImportRuleImpl value = parseImportRule("@import \"ext.css\";");

        assertEquals("@import url(\"ext.css\");", value.getCssText());
        assertEquals("@import url(\"ext.css\");", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setCssText() throws Exception {
        final CSSImportRuleImpl value = parseImportRule("@import \"ext.css\";");

        assertEquals("@import url(\"ext.css\");", value.getCssText());
        assertEquals("@import url(\"ext.css\");", value.toString());

        value.setCssText("@import url(\"cool.css\");");
        assertEquals("@import url(\"cool.css\");", value.getCssText());
        assertEquals("@import url(\"cool.css\");", value.toString());

    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getHref() throws Exception {
        final CSSImportRuleImpl value = parseImportRule("@import \"ext.css\";");
        assertEquals("ext.css", value.getHref());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void type() throws Exception {
        final CSSImportRuleImpl value = new CSSImportRuleImpl(null, null, null, null);

        assertEquals("@import;", value.toString());
    }

    private CSSImportRuleImpl parseImportRule(final String rule) throws Exception {
        final InputSource is = new InputSource(new StringReader(rule));
        final CSSStyleSheetImpl ss = new CSSOMParser().parseStyleSheet(is, null);

        final CSSImportRuleImpl value = (CSSImportRuleImpl) ss.getCssRules().getRules().get(0);
        return value;
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFormated() throws Exception {
        final CSSImportRuleImpl value = parseImportRule("@import \"ext.css\";");

        assertEquals("@import url(\"ext.css\");", value.toString());
        assertEquals("@import url(\"ext.css\");", value.getCssText());
    }
}
