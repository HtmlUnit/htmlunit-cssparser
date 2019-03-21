/*
 * Copyright (c) 2019 Ronald Brill.
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
package com.gargoylesoftware.css;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.css.dom.AbstractCSSRuleImpl;
import com.gargoylesoftware.css.dom.CSSMediaRuleImpl;
import com.gargoylesoftware.css.dom.CSSRuleListImpl;
import com.gargoylesoftware.css.dom.CSSStyleSheetImpl;
import com.gargoylesoftware.css.parser.CSSOMParser;
import com.gargoylesoftware.css.parser.InputSource;

/**
 * Attempts to perform some illegal operations to ensure the correct exceptions are thrown.
 *
 * @author Ronald Brill
 */
public class TestException {

    /**
     * @throws Exception on failure
     */
    @Test
    public void test() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final Reader r = new StringReader("");
        final InputSource source = new InputSource(r);
        final CSSStyleSheetImpl stylesheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        stylesheet.insertRule("P { color: blue }", 0);
        stylesheet.insertRule("@import url(http://www.steadystate.com/primary.css);", 0);
        stylesheet.insertRule("@charset \"US-ASCII\";", 0);

        final CSSRuleListImpl rules = stylesheet.getCssRules();
        Assert.assertEquals(3, rules.getLength());

        Assert.assertEquals("@charset \"US-ASCII\";", rules.getRules().get(0).getCssText());
        Assert.assertEquals("@import url(http://www.steadystate.com/primary.css);", rules.getRules().get(1).getCssText());
        Assert.assertEquals("P { color: blue }", rules.getRules().get(2).getCssText());

        stylesheet.deleteRule(1);

        Assert.assertEquals(2, rules.getLength());
        Assert.assertEquals("@charset \"US-ASCII\";", rules.getRules().get(0).getCssText());
        Assert.assertEquals("P { color: blue }", rules.getRules().get(1).getCssText());

        AbstractCSSRuleImpl rule = rules.getRules().get(1);
        rule.setCssText("h2 { smell: strong }");
        Assert.assertEquals("h2 { smell: strong }", rules.getRules().get(1).getCssText());

        final int n = stylesheet.insertRule("@media speech { h1 { voice: male } }", 1);
        Assert.assertEquals(1, n);

        Assert.assertEquals(3, rules.getLength());
        Assert.assertEquals("@charset \"US-ASCII\";", rules.getRules().get(0).getCssText());
        Assert.assertEquals("@media speech {h1 { voice: male } }", rules.getRules().get(1).getCssText());
        Assert.assertEquals("h2 { smell: strong }", rules.getRules().get(2).getCssText());

        rule = rules.getRules().get(1);
        ((CSSMediaRuleImpl) rule).insertRule("p { voice: female }", 1);
        Assert.assertEquals("speech", ((CSSMediaRuleImpl) rule).getMedia().getMediaText());

        // TODO
        ((CSSMediaRuleImpl) rule).getMedia().setMediaText("speech, signlanguage");
        Assert.assertEquals("speech, speech, signlanguage", ((CSSMediaRuleImpl) rule).getMedia().getMediaText());

        ((CSSMediaRuleImpl) rule).getMedia().deleteMedium("signlanguage");
        Assert.assertEquals("speech, speech", ((CSSMediaRuleImpl) rule).getMedia().getMediaText());

        ((CSSMediaRuleImpl) rule).getMedia().appendMedium("semaphore");
        Assert.assertEquals("speech, speech, semaphore", ((CSSMediaRuleImpl) rule).getMedia().getMediaText());
    }
}
