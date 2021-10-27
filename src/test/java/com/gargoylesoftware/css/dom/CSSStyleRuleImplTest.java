/*
 * Copyright (c) 2019-2020 Ronald Brill.
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
package com.gargoylesoftware.css.dom;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

import com.gargoylesoftware.css.parser.CSSOMParser;
import com.gargoylesoftware.css.parser.InputSource;

/**
 * Unit tests for {@link CSSStyleRuleImpl}.
 *
 * @author Ronald Brill
 */
public class CSSStyleRuleImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final CSSStyleRuleImpl value = parseStyleRule("h1 { color: blue }");

        assertEquals("h1 { color: blue; }", value.getCssText());
        assertEquals("h1 { color: blue; }", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setCssText() throws Exception {
        final CSSStyleRuleImpl value = parseStyleRule("h1 { color: blue }");

        assertEquals("h1 { color: blue; }", value.getCssText());
        assertEquals("h1 { color: blue; }", value.toString());

        value.setCssText("p { width: 10px; };");
        assertEquals("p { width: 10px; }", value.getCssText());
        assertEquals("p { width: 10px; }", value.toString());

    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getSelectorText() throws Exception {
        CSSStyleRuleImpl value = parseStyleRule("h1 { color: blue }");
        assertEquals("h1", value.getSelectorText());

        value = parseStyleRule("h1, h2,\r\nb { color: blue }");
        assertEquals("h1, h2, b", value.getSelectorText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void type() throws Exception {
        final CSSStyleRuleImpl value = new CSSStyleRuleImpl(null, null, null);

        assertEquals("", value.toString());
    }

    private CSSStyleRuleImpl parseStyleRule(final String rule) throws Exception {
        final InputSource is = new InputSource(new StringReader(rule));
        final CSSStyleSheetImpl ss = new CSSOMParser().parseStyleSheet(is, null);

        final CSSStyleRuleImpl value = (CSSStyleRuleImpl) ss.getCssRules().getRules().get(0);
        return value;
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFormated() throws Exception {
        final CSSStyleRuleImpl value = parseStyleRule("h1{color:blue}");

        assertEquals("h1 { color: blue; }", value.toString());
        assertEquals("h1 { color: blue; }", value.getCssText());
    }
}
