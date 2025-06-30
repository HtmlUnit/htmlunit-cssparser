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
package org.htmlunit.cssparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Reader;
import java.io.StringReader;

import org.htmlunit.cssparser.dom.AbstractCSSRuleImpl;
import org.htmlunit.cssparser.dom.CSSMediaRuleImpl;
import org.htmlunit.cssparser.dom.CSSRuleListImpl;
import org.htmlunit.cssparser.dom.CSSStyleSheetImpl;
import org.htmlunit.cssparser.parser.CSSOMParser;
import org.htmlunit.cssparser.parser.InputSource;
import org.junit.jupiter.api.Test;

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

        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        stylesheet.insertRule("P { color: blue }", 0);
        stylesheet.insertRule("@import url(http://www.steadystate.com/primary.css);", 0);
        stylesheet.insertRule("@charset \"US-ASCII\";", 0);

        final CSSRuleListImpl rules = stylesheet.getCssRules();
        assertEquals(3, rules.getLength());

        assertEquals("@charset \"US-ASCII\";", rules.getRules().get(0).getCssText());
        assertEquals("@import url(\"http://www.steadystate.com/primary.css\");", rules.getRules().get(1).getCssText());
        assertEquals("P { color: blue; }", rules.getRules().get(2).getCssText());

        stylesheet.deleteRule(1);

        assertEquals(2, rules.getLength());
        assertEquals("@charset \"US-ASCII\";", rules.getRules().get(0).getCssText());
        assertEquals("P { color: blue; }", rules.getRules().get(1).getCssText());

        AbstractCSSRuleImpl rule = rules.getRules().get(1);
        rule.setCssText("h2 { smell: strong }");
        assertEquals("h2 { smell: strong; }", rules.getRules().get(1).getCssText());

        stylesheet.insertRule("@media speech { h1 { voice: male } }", 1);

        assertEquals(3, rules.getLength());
        assertEquals("@charset \"US-ASCII\";", rules.getRules().get(0).getCssText());
        assertEquals("@media speech {\n  h1 { voice: male; }\n}", rules.getRules().get(1).getCssText());
        assertEquals("h2 { smell: strong; }", rules.getRules().get(2).getCssText());

        rule = rules.getRules().get(1);
        ((CSSMediaRuleImpl) rule).insertRule("p { voice: female }", 1);
        assertEquals("speech", ((CSSMediaRuleImpl) rule).getMediaList().getMediaText());

        // TODO
        ((CSSMediaRuleImpl) rule).getMediaList().setMediaText("speech, signlanguage");
        assertEquals("speech, speech, signlanguage", ((CSSMediaRuleImpl) rule).getMediaList().getMediaText());
    }
}
