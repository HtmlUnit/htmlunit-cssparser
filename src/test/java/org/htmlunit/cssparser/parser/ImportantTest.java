/*
 * Copyright (c) 2019-2023 Ronald Brill.
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
package org.htmlunit.cssparser.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.htmlunit.cssparser.ErrorHandler;
import org.htmlunit.cssparser.dom.AbstractCSSRuleImpl;
import org.htmlunit.cssparser.dom.CSSRuleListImpl;
import org.htmlunit.cssparser.dom.CSSStyleSheetImpl;
import org.htmlunit.cssparser.parser.javacc.CSS3Parser;
import org.junit.jupiter.api.Test;

/**
 * Testcases.
 * @author Ronald Brill
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

        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getWarningCount());
    }

    private void css() throws Exception {
        final ErrorHandler errorHandler = new ErrorHandler();
        new CSS3Parser().setErrorHandler(errorHandler);

        final InputStream is = getClass().getClassLoader().getResourceAsStream("important.css");
        assertNotNull(is);
        final Reader r = new InputStreamReader(is);
        final InputSource source = new InputSource(r);

        final CSSOMParser parser = new CSSOMParser();
        final CSSStyleSheetImpl ss = parser.parseStyleSheet(source, null);

        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = ss.getCssRules();
        assertEquals(5, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        assertEquals("*.sel1 { padding: 0 !important; }", rule.getCssText());

        rule = rules.getRules().get(1);
        assertEquals("*.sel2 { font-weight: normal !important; }", rule.getCssText());

        rule = rules.getRules().get(2);
        assertEquals("*.sel3 { font-weight: normal !important; }", rule.getCssText());

        rule = rules.getRules().get(3);
        assertEquals("*.sel4 { font-weight: normal !important; }", rule.getCssText());

        rule = rules.getRules().get(4);
        assertEquals("*.important { font-weight: bold; }", rule.getCssText());
    }

    private ErrorHandler parserError(final AbstractCSSParser cssParser) throws Exception {
        final ErrorHandler errorHandler = new ErrorHandler();
        cssParser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(
                new StringReader(".foo { font-weight: normal !/* comment */important; }"));

        cssParser.parseStyleSheet(source);
        return errorHandler;
    }
}
