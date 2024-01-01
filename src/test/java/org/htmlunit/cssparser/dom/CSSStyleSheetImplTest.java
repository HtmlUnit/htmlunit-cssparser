/*
 * Copyright (c) 2019-2024 Ronald Brill.
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
package org.htmlunit.cssparser.dom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;

import org.htmlunit.cssparser.parser.CSSOMParser;
import org.htmlunit.cssparser.parser.InputSource;
import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMException;

/**
 * Unit tests for {@link CSSStyleSheetImpl}.
 *
 * @author Ronald Brill
 */
public class CSSStyleSheetImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void insertRule() throws Exception {
        final CSSStyleSheetImpl ss = parseStyleSheet("");

        ss.insertRule(".testStyle { height: 42px; }", 0);
        assertEquals("*.testStyle { height: 42px; }", ss.getCssRules().getRules().get(0).getCssText());

        ss.insertRule(".testStyle { height: 43px; }", 0);
        assertEquals("*.testStyle { height: 43px; }", ss.getCssRules().getRules().get(0).getCssText());
        assertEquals("*.testStyle { height: 42px; }", ss.getCssRules().getRules().get(1).getCssText());

        ss.insertRule(".testStyle { height: 44px; }", 2);
        assertEquals("*.testStyle { height: 43px; }", ss.getCssRules().getRules().get(0).getCssText());
        assertEquals("*.testStyle { height: 42px; }", ss.getCssRules().getRules().get(1).getCssText());
        assertEquals("*.testStyle { height: 44px; }", ss.getCssRules().getRules().get(2).getCssText());

        ss.insertRule(".testStyle { height: 45px; }", 2);
        assertEquals("*.testStyle { height: 43px; }", ss.getCssRules().getRules().get(0).getCssText());
        assertEquals("*.testStyle { height: 42px; }", ss.getCssRules().getRules().get(1).getCssText());
        assertEquals("*.testStyle { height: 45px; }", ss.getCssRules().getRules().get(2).getCssText());
        assertEquals("*.testStyle { height: 44px; }", ss.getCssRules().getRules().get(3).getCssText());
    }

    /**
     * Regression test for bug 2123264.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void insertRuleWithLeadingWhitespace() throws Exception {
        final CSSStyleSheetImpl ss = parseStyleSheet("");

        ss.insertRule(" .testStyleDef { height: 42px; }", 0);
        assertEquals("*.testStyleDef { height: 42px; }", ss.getCssRules().getRules().get(0).getCssText());

        ss.insertRule("      .testStyleDef { height: 43px;}   ", 0);
        assertEquals("*.testStyleDef { height: 43px; }", ss.getCssRules().getRules().get(0).getCssText());
        assertEquals("*.testStyleDef { height: 42px; }", ss.getCssRules().getRules().get(1).getCssText());

        ss.insertRule("\t.testStyleDef { height: 44px }\r\n", 0);
        assertEquals("*.testStyleDef { height: 44px; }", ss.getCssRules().getRules().get(0).getCssText());
        assertEquals("*.testStyleDef { height: 43px; }", ss.getCssRules().getRules().get(1).getCssText());
        assertEquals("*.testStyleDef { height: 42px; }", ss.getCssRules().getRules().get(2).getCssText());
    }

    /**
     * Regression test for bug 2123264.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void insertRuleWithoutDeclaration() throws Exception {
        final CSSStyleSheetImpl ss = parseStyleSheet("");

        try {
            ss.insertRule(".testStyleDef", 0);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertTrue(e.getMessage().startsWith("Syntax error"), e.getMessage());
            assertEquals(0, ss.getCssRules().getLength());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void insertRuleRuleOrderCharset() throws Exception {
        final CSSStyleSheetImpl ss = parseStyleSheet("");

        ss.insertRule("@charset \"US-ASCII\";", 0);
        try {
            ss.insertRule("@charset \"US-ASCII\";", 0);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertTrue(e.getMessage().startsWith("A charset rule already exists"), e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void insertRuleRuleImport() throws Exception {
        final CSSStyleSheetImpl ss = parseStyleSheet("");

        ss.insertRule("@import \"great.css\";", 0);

        ss.insertRule("@charset \"US-ASCII\";", 0);
        ss.insertRule("@import \"great.css\";", 1);

        try {
            ss.insertRule("testStyleDef { height: 42px }", 0);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertTrue(e.getMessage().startsWith("Can't insert a rule before the last charset or import rule"),
                    e.getMessage());
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void deleteRule() throws Exception {
        final CSSStyleSheetImpl ss = parseStyleSheet("test { height: 42px }");

        ss.deleteRule(0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void deleteRuleWrongIndex() throws Exception {
        final CSSStyleSheetImpl ss = parseStyleSheet("");

        try {
            ss.deleteRule(7);
            fail("DOMException expected");
        }
        catch (final DOMException e) {
            assertTrue(e.getMessage().startsWith("Index out of bounds error"), e.getMessage());
            assertEquals(0, ss.getCssRules().getLength());
        }
    }

    /**
     * Test serialization.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void serialize() throws Exception {
        final String cssText =
            "h1 {\n"
            + "  font-size: 2em\n"
            + "}\n"
            + "\n"
            + "@media handheld {\n"
            + "  h1 {\n"
            + "    font-size: 1.5em\n"
            + "  }\n"
            + "}";
        final InputSource source = new InputSource(new StringReader(cssText));
        final CSSOMParser cssomParser = new CSSOMParser();
        final CSSStyleSheetImpl css = cssomParser.parseStyleSheet(source, "http://www.example.org/css/style.css");

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(css);
        oos.flush();
        oos.close();

        final byte[] bytes = baos.toByteArray();
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        final Object o = ois.readObject();
        assertEquals(css.toString(), o.toString());
    }

    private CSSStyleSheetImpl parseStyleSheet(final String rule) throws Exception {
        final InputSource is = new InputSource(new StringReader(rule));
        final CSSStyleSheetImpl ss = new CSSOMParser().parseStyleSheet(is, null);

        return ss;
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void getCssTextFormated() throws Exception {
        final CSSStyleSheetImpl value = parseStyleSheet("h1{color:blue}");

        assertEquals("h1 { color: blue; }", value.toString());
    }
}
