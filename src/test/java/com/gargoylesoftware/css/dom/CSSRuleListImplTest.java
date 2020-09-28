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
 * Unit tests for {@link CSSPageRuleImpl}.
 *
 * @author Ronald Brill
 */
public class CSSRuleListImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defaultConstructor() throws Exception {
        final CSSRuleListImpl rl = new CSSRuleListImpl();
        Assert.assertEquals("", rl.toString());
        Assert.assertTrue(rl.getRules().isEmpty());
        Assert.assertEquals(0, rl.getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getLength() throws Exception {
        final CSSRuleListImpl rl = new CSSRuleListImpl();
        Assert.assertEquals(0, rl.getLength());

        rl.add(new CSSFontFaceRuleImpl(null, null));
        Assert.assertEquals(1, rl.getLength());

        rl.add(new CSSFontFaceRuleImpl(null, null));
        Assert.assertEquals(2, rl.getLength());

        rl.add(new CSSFontFaceRuleImpl(null, null));
        Assert.assertEquals(3, rl.getLength());
    }

    private CSSRuleListImpl parseRuleList(final String rules) throws Exception {
        final InputSource is = new InputSource(new StringReader(rules));
        final CSSStyleSheetImpl ss = new CSSOMParser().parseStyleSheet(is, null);

        return ss.getCssRules();
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFormated() throws Exception {
        final CSSRuleListImpl value = parseRuleList("h1 {} h2 {color:green}");

        Assert.assertEquals("h1 { }\r\nh2 { color: green }", value.toString());
    }
}
