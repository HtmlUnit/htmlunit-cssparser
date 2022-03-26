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
package com.gargoylesoftware.css.dom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;

import com.gargoylesoftware.css.parser.CSSOMParser;
import com.gargoylesoftware.css.parser.InputSource;

/**
 * Unit tests for {@link CSSPageRuleImpl}.
 *
 * @author Ronald Brill
 */
public class CSSPageRuleImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final CSSPageRuleImpl value = parsePageRule("@page { size: 21.0cm 29.7cm; }");

        assertEquals("@page { size: 21cm 29.7cm; }", value.getCssText());
        assertEquals("@page { size: 21cm 29.7cm; }", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setCssText() throws Exception {
        final CSSPageRuleImpl value = parsePageRule("@page { size: 21.0cm 29.7cm; }");

        assertEquals("@page { size: 21cm 29.7cm; }", value.getCssText());
        assertEquals("@page { size: 21cm 29.7cm; }", value.toString());

        value.setCssText("@page :left { color: blue }");
        assertEquals("@page :left { color: blue; }", value.getCssText());
        assertEquals("@page :left { color: blue; }", value.toString());

    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getSelectorText() throws Exception {
        CSSPageRuleImpl value = parsePageRule("@page { size: 21.0cm 29.7cm; }");
        assertEquals("", value.getSelectorText());

        value = parsePageRule("@page :first {color: blue}");
        assertEquals(":first", value.getSelectorText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setSelectorText() throws Exception {
        final CSSPageRuleImpl value = parsePageRule("@page { color: blue; }");
        assertEquals("", value.getSelectorText());

        value.setSelectorText(":right");
        assertEquals(":right", value.getSelectorText());
        assertEquals("@page :right { color: blue; }", value.getCssText());
        assertEquals("@page :right { color: blue; }", value.toString());

        value.setSelectorText("");
        assertEquals("", value.getSelectorText());
        assertEquals("@page { color: blue; }", value.getCssText());
        assertEquals("@page { color: blue; }", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setSelectorTextInvalid() throws Exception {
        final CSSPageRuleImpl value = parsePageRule("@page { color: blue; }");
        assertEquals("", value.getSelectorText());

        try {
            value.setSelectorText(":invalid");
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertTrue(e.getMessage().startsWith("The text does not represent a page rule (null)"), e.getMessage());
        }
        assertEquals("", value.getSelectorText());
        assertEquals("@page { color: blue; }", value.getCssText());
        assertEquals("@page { color: blue; }", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setPseudoPage() throws Exception {
        CSSPageRuleImpl value = parsePageRule("@page { size: 21.0cm 29.7cm; }");
        assertEquals("", value.getSelectorText());

        value = parsePageRule("@page :blank {color: blue}");
        assertEquals(":blank", value.getSelectorText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getStyle() throws Exception {
        CSSPageRuleImpl value = parsePageRule("@page { size: 21.0cm 29.7cm; }");
        assertEquals("size: 21cm 29.7cm", value.getStyle().toString());

        value = parsePageRule("@page :left {color: blue}");
        assertEquals("color: blue", value.getStyle().toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void type() throws Exception {
        final CSSPageRuleImpl value = new CSSPageRuleImpl(null, null, null);

        assertEquals("@page { }", value.toString());
    }

    private CSSPageRuleImpl parsePageRule(final String pageRule) throws Exception {
        final InputSource is = new InputSource(new StringReader(pageRule));
        final CSSStyleSheetImpl ss = new CSSOMParser().parseStyleSheet(is, null);

        final CSSPageRuleImpl value = (CSSPageRuleImpl) ss.getCssRules().getRules().get(0);
        return value;
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFormated() throws Exception {
        final CSSPageRuleImpl value = parsePageRule("@page { size: 21.0cm 29.7cm; }");

        assertEquals("@page { size: 21cm 29.7cm; }", value.toString());
        assertEquals("@page { size: 21cm 29.7cm; }", value.getCssText());
    }
}
