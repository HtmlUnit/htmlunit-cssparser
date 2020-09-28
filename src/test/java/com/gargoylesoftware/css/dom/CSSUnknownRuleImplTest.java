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
 * Unit tests for {@link CSSUnknownRuleImpl}.
 *
 * @author Ronald Brill
 */
public class CSSUnknownRuleImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssText() throws Exception {
        final CSSUnknownRuleImpl value = parseUnknownRule("@foo \"text\";");

        Assert.assertEquals("@foo text;", value.getCssText());
        Assert.assertEquals("@foo text;", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setCssText() throws Exception {
        final CSSUnknownRuleImpl value = parseUnknownRule("@foo \"text\";");

        Assert.assertEquals("@foo text;", value.getCssText());
        Assert.assertEquals("@foo text;", value.toString());

        value.setCssText("@foo { key: 'value' };");
        Assert.assertEquals("@foo { key: value }", value.getCssText());
        Assert.assertEquals("@foo { key: value }", value.toString());

    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void type() throws Exception {
        final CSSUnknownRuleImpl value = new CSSUnknownRuleImpl(null, null, null);

        Assert.assertEquals("", value.toString());
    }

    private CSSUnknownRuleImpl parseUnknownRule(final String rule) throws Exception {
        final InputSource is = new InputSource(new StringReader(rule));
        final CSSStyleSheetImpl ss = new CSSOMParser().parseStyleSheet(is, null);

        final CSSUnknownRuleImpl value = (CSSUnknownRuleImpl) ss.getCssRules().getRules().get(0);
        return value;
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFormated() throws Exception {
        final CSSUnknownRuleImpl value = parseUnknownRule("@foo \"text\";");

        Assert.assertEquals("@foo text;", value.toString());
        Assert.assertEquals("@foo text;", value.getCssText());
    }
}
