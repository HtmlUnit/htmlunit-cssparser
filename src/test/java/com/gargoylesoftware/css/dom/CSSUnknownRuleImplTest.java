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

import java.io.StringReader;

import org.junit.jupiter.api.Test;

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

        assertEquals("@foo text;", value.getCssText());
        assertEquals("@foo text;", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setCssText() throws Exception {
        final CSSUnknownRuleImpl value = parseUnknownRule("@foo \"text\";");

        assertEquals("@foo text;", value.getCssText());
        assertEquals("@foo text;", value.toString());

        value.setCssText("@foo { key: 'value' };");
        assertEquals("@foo { key: value }", value.getCssText());
        assertEquals("@foo { key: value }", value.toString());

    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void keyframes() throws Exception {
        final CSSUnknownRuleImpl value = parseUnknownRule("@keyframes load5 {0%, 100% {"
                + "box-shadow: 0em -2.6em 0em 0em #000000, 1.8em -1.8em 0 0em rgba(0, 0, 0, 0.2), "
                + "2.5em 0em 0 0em rgba(0, 0, 0, 0.2), 1.75em 1.75em 0 0em rgba(0, 0, 0, 0.2), "
                + "0em 2.5em 0 0em rgba(0, 0, 0, 0.2), -1.8em 1.8em 0 0em rgba(0, 0, 0, 0.2), "
                + "-2.6em 0em 0 0em rgba(0, 0, 0, 0.5), -1.8em -1.8em 0 0em rgba(0, 0, 0, 0.7);"
                + "}");

        assertEquals("@keyframes load5 {0%, 100% {"
                + "box-shadow: 0em -2.6em 0em 0em #000000, 1.8em -1.8em 0 0em rgba(0, 0, 0, 0.2), "
                + "2.5em 0em 0 0em rgba(0, 0, 0, 0.2), 1.75em 1.75em 0 0em rgba(0, 0, 0, 0.2), "
                + "0em 2.5em 0 0em rgba(0, 0, 0, 0.2), -1.8em 1.8em 0 0em rgba(0, 0, 0, 0.2), "
                + "-2.6em 0em 0 0em rgba(0, 0, 0, 0.5), -1.8em -1.8em 0 0em rgba(0, 0, 0, 0.7);"
                + "}", value.getCssText());
        assertEquals("@keyframes load5 {0%, 100% {"
                + "box-shadow: 0em -2.6em 0em 0em #000000, 1.8em -1.8em 0 0em rgba(0, 0, 0, 0.2), "
                + "2.5em 0em 0 0em rgba(0, 0, 0, 0.2), 1.75em 1.75em 0 0em rgba(0, 0, 0, 0.2), "
                + "0em 2.5em 0 0em rgba(0, 0, 0, 0.2), -1.8em 1.8em 0 0em rgba(0, 0, 0, 0.2), "
                + "-2.6em 0em 0 0em rgba(0, 0, 0, 0.5), -1.8em -1.8em 0 0em rgba(0, 0, 0, 0.7);"
                + "}", value.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void type() throws Exception {
        final CSSUnknownRuleImpl value = new CSSUnknownRuleImpl(null, null, null);

        assertEquals("", value.toString());
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

        assertEquals("@foo text;", value.toString());
        assertEquals("@foo text;", value.getCssText());
    }
}
