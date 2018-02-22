/*
 * Copyright (c) 2018 Ronald Brill.
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
package com.gargoylesoftware.css.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import com.gargoylesoftware.css.ErrorHandler;

/**
 * Testcases.
 */
public class ImportantTest {
    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void css3() throws Exception {
        css();
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void css3Error() throws Exception {
        final ErrorHandler errorHandler = parserError(new CSS3Parser());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());
    }

    private void css() throws Exception {
        final ErrorHandler errorHandler = new ErrorHandler();
        new CSS3Parser().setErrorHandler(errorHandler);

        final InputStream is = getClass().getClassLoader().getResourceAsStream("important.css");
        Assert.assertNotNull(is);
        final Reader r = new InputStreamReader(is);
        final InputSource source = new InputSource(r);

        final CSSOMParser parser = new CSSOMParser();
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null);

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleList rules = ss.getCssRules();
        Assert.assertEquals(5, rules.getLength());

        CSSRule rule = rules.item(0);
        Assert.assertEquals("*.sel1 { padding: 0 !important }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());

        rule = rules.item(1);
        Assert.assertEquals("*.sel2 { font-weight: normal !important }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());

        rule = rules.item(2);
        Assert.assertEquals("*.sel3 { font-weight: normal !important }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());

        rule = rules.item(3);
        Assert.assertEquals("*.sel4 { font-weight: normal !important }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());

        rule = rules.item(4);
        Assert.assertEquals("*.important { font-weight: bold }", rule.getCssText());
        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());
    }

    private ErrorHandler parserError(final CSSParser cssParser) throws Exception {
        final ErrorHandler errorHandler = new ErrorHandler();
        cssParser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(
                new StringReader(".foo { font-weight: normal !/* comment */important; }"));

        cssParser.parseStyleSheet(source);
        return errorHandler;
    }
}
