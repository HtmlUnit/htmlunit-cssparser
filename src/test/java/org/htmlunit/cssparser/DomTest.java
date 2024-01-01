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
package org.htmlunit.cssparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;

import org.htmlunit.cssparser.dom.CSSRuleListImpl;
import org.htmlunit.cssparser.dom.CSSStyleDeclarationImpl;
import org.htmlunit.cssparser.dom.CSSStyleSheetImpl;
import org.htmlunit.cssparser.dom.CSSValueImpl;
import org.htmlunit.cssparser.dom.CSSValueImpl.CSSValueType;
import org.htmlunit.cssparser.parser.CSSOMParser;
import org.htmlunit.cssparser.parser.InputSource;
import org.junit.jupiter.api.Test;

/**
 * Tests the CSS DOM implementation by loading a stylesheet and performing a few operations upon it.
 *
 * @author Ronald Brill
 */
public class DomTest {

    /**
     * @throws Exception on failure
     */
    @Test
    public void test() throws Exception {
        final String cssText =
            "foo: 1.5; bogus: 3, 2, 1; bar-color: #0FEED0; background: #abc; foreground: rgb( 10, 20, 30 )";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleDeclarationImpl style = parser.parseStyleDeclaration(cssText);

        assertEquals(0, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        // Enumerate the properties and retrieve their values
        assertEquals(5, style.getLength());

        String name = style.getProperties().get(0).getName();
        assertEquals("foo : 1.5", name + " : " + style.getPropertyValue(name));
        name = style.getProperties().get(1).getName();
        assertEquals("bogus : 3, 2, 1", name + " : " + style.getPropertyValue(name));
        name = style.getProperties().get(2).getName();
        assertEquals("bar-color : rgb(15, 238, 208)", name + " : " + style.getPropertyValue(name));
        name = style.getProperties().get(3).getName();
        assertEquals("background : rgb(170, 187, 204)", name + " : " + style.getPropertyValue(name));
        name = style.getProperties().get(4).getName();
        assertEquals("foreground : rgb(10, 20, 30)", name + " : " + style.getPropertyValue(name));

        // Get the style declaration as a single lump of text
        assertEquals("foo: 1.5; "
                + "bogus: 3, 2, 1; "
                + "bar-color: rgb(15, 238, 208); "
                + "background: rgb(170, 187, 204); "
                + "foreground: rgb(10, 20, 30)", style.getCssText());

        // Directly set the CSS style declaration
        style.setCssText("alpha: 2; beta: 20px; gamma: 40em; delta: 1mm; epsilon: 24pt");
        assertEquals("alpha: 2; beta: 20px; gamma: 40em; delta: 1mm; epsilon: 24pt", style.getCssText());

        // Remove some properties, from the middle, beginning, and end
        style.removeProperty("gamma");
        assertEquals("alpha: 2; beta: 20px; delta: 1mm; epsilon: 24pt", style.getCssText());

        style.removeProperty("alpha");
        assertEquals("beta: 20px; delta: 1mm; epsilon: 24pt", style.getCssText());

        style.removeProperty("epsilon");
        assertEquals("beta: 20px; delta: 1mm", style.getCssText());

        // Use the setProperty method to modify an existing property,
        // and add a new one.
        style.setProperty("beta", "40px", null);
        assertEquals("beta: 40px; delta: 1mm", style.getCssText());

        style.setProperty("omega", "1", "important");
        assertEquals("beta: 40px; delta: 1mm; omega: 1 !important", style.getCssText());

        // Work with CSSValues
        CSSValueImpl value = style.getPropertyCSSValue("beta");
        assertEquals("40px", value.getCssText());
        assertEquals(40f, value.getDoubleValue(), 0.000000f);

        value.setDoubleValue(100);
        assertEquals("100", value.getCssText());

        style.setProperty("list-test", "100 200 300", null);
        assertEquals("beta: 100; delta: 1mm; omega: 1 !important; list-test: 100 200 300", style.getCssText());

        value = style.getPropertyCSSValue("list-test");
        assertEquals(CSSValueType.CSS_VALUE_LIST, value.getCssValueType());

        final CSSValueImpl vl = style.getPropertyCSSValue("list-test");
        assertEquals(3, vl.getLength());

        value = vl.item(0);
        assertEquals(100, value.getDoubleValue(), 0.000000f);

        value = vl.item(1);
        assertEquals(200, value.getDoubleValue(), 0.000000f);

        value = vl.item(2);
        assertEquals(300, value.getDoubleValue(), 0.000000f);

        // When a CSSValue is modified, it modifies the declaration
        assertEquals("beta: 100; delta: 1mm; omega: 1 !important; list-test: 100 200 300", style.getCssText());

        // Using the setCssText method, we can change the type of value
        vl.setCssText("bogus");
        assertEquals(CSSValueType.CSS_PRIMITIVE_VALUE, value.getCssValueType());
        assertEquals("beta: 100; delta: 1mm; omega: 1 !important; list-test: bogus", style.getCssText());
    }

    /**
     * @throws Exception on failure
     */
    @Test
    public void inheritGetStringValue() throws Exception {
        final String cssText = "p { font-size: 2em } p a:link { font-size: inherit }";
        final InputSource source = new InputSource(new StringReader(cssText));
        final CSSOMParser cssomParser = new CSSOMParser();

        final CSSStyleSheetImpl css = cssomParser.parseStyleSheet(source, "http://www.example.org/css/style.css");

        final CSSRuleListImpl rules = css.getCssRules();
        assertEquals(2, rules.getLength());

        assertEquals("p { font-size: 2em; }", rules.getRules().get(0).getCssText());
        assertEquals("p a:link { font-size: inherit; }", rules.getRules().get(1).getCssText());
    }
}
