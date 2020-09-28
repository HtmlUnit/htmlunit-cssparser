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

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.css.parser.CSSOMParser;
import com.gargoylesoftware.css.parser.InputSource;

/**
 * Unit tests for {@link CSSFontFaceRuleImpl}.
 *
 * @author Ronald Brill
 */
public class CSSFontFaceRuleImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final CSSFontFaceRuleImpl value = parseFontFaceRule("@font-face { font-family: 'Scarface' }");

        Assert.assertEquals("@font-face {font-family: \"Scarface\"}", value.getCssText());
        Assert.assertEquals("@font-face {font-family: \"Scarface\"}", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setCssText() throws Exception {
        final CSSFontFaceRuleImpl value = parseFontFaceRule("@font-face { font-family: 'Scarface' }");

        Assert.assertEquals("@font-face {font-family: \"Scarface\"}", value.getCssText());
        Assert.assertEquals("@font-face {font-family: \"Scarface\"}", value.toString());

        value.setCssText("@font-face { font-family: 'Ariel'; font-style: 'cute'; }");
        Assert.assertEquals("@font-face {font-family: \"Ariel\"; font-style: \"cute\"}", value.getCssText());
        Assert.assertEquals("@font-face {font-family: \"Ariel\"; font-style: \"cute\"}", value.toString());

    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getStyle() throws Exception {
        CSSFontFaceRuleImpl value = parseFontFaceRule("@font-face { font-family: 'Scarface' }");
        Assert.assertEquals("font-family: \"Scarface\"", value.getStyle().toString());

        value = parseFontFaceRule("@font-face { font-style: cute; }");
        Assert.assertEquals("font-style: cute", value.getStyle().toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void type() throws Exception {
        final CSSFontFaceRuleImpl value = new CSSFontFaceRuleImpl(null, null);

        Assert.assertEquals("@font-face {}", value.toString());
    }

    private CSSFontFaceRuleImpl parseFontFaceRule(final String rule) throws Exception {
        final InputSource is = new InputSource(new StringReader(rule));
        final CSSStyleSheetImpl ss = new CSSOMParser().parseStyleSheet(is, null);

        final CSSFontFaceRuleImpl value = (CSSFontFaceRuleImpl) ss.getCssRules().getRules().get(0);
        return value;
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFormated() throws Exception {
        final CSSFontFaceRuleImpl value = parseFontFaceRule("@font-face { font-family: 'Scarface' }");

        Assert.assertEquals("@font-face {font-family: \"Scarface\"}", value.toString());
        Assert.assertEquals("@font-face {font-family: \"Scarface\"}", value.getCssText());
    }
}
