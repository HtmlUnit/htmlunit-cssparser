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
package org.htmlunit.css.dom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.htmlunit.css.parser.CSSOMParser;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CSSStyleDeclarationImpl}.
 * @author Ronald Brill
 */
public class CSSStyleDeclarationImplTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defaultConstructor() throws Exception {
        final CSSStyleDeclarationImpl style = new CSSStyleDeclarationImpl(null);

        assertEquals("", style.getCssText());
        assertEquals(0, style.getLength());

        assertNull(style.getParentRule());
        assertEquals("", style.getPropertyValue("unknown"));
        assertEquals("", style.getPropertyPriority("unknown"));
        assertNull(style.getPropertyCSSValue("unknown"));

        assertEquals("", style.getPropertyValue(null));
        assertEquals("", style.getPropertyPriority(null));
        assertNull(style.getPropertyCSSValue(null));

        // remove
        assertEquals("", style.removeProperty("unknown"));
        assertEquals(0, style.getLength());

        assertEquals("", style.removeProperty(null));
        assertEquals(0, style.getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyRule() throws Exception {
        final CSSStyleDeclarationImpl style = parseStyleDeclaration("");

        assertEquals("", style.getCssText());
        assertEquals(0, style.getLength());

        assertNull(style.getParentRule());
        assertEquals("", style.getPropertyValue("unknown"));
        assertEquals("", style.getPropertyPriority("unknown"));
        assertNull(style.getPropertyCSSValue("unknown"));

        assertEquals("", style.getPropertyValue(null));
        assertEquals("", style.getPropertyPriority(null));
        assertNull(style.getPropertyCSSValue(null));

        // remove
        assertEquals("", style.removeProperty("unknown"));
        assertEquals(0, style.getLength());

        assertEquals("", style.removeProperty(null));
        assertEquals(0, style.getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void simpleRule() throws Exception {
        final CSSStyleDeclarationImpl style = parseStyleDeclaration("prop: value");

        assertEquals("prop: value", style.getCssText());
        assertEquals(1, style.getLength());

        assertNull(style.getParentRule());
        assertEquals("value", style.getPropertyValue("prop"));
        assertEquals("", style.getPropertyPriority("prop"));
        assertEquals("value", style.getPropertyCSSValue("prop").getCssText());

        assertEquals("", style.getPropertyValue("unknown"));
        assertEquals("", style.getPropertyPriority("unknown"));
        assertNull(style.getPropertyCSSValue("unknown"));

        assertEquals("", style.getPropertyValue(null));
        assertEquals("", style.getPropertyPriority(null));
        assertNull(style.getPropertyCSSValue(null));

        // remove
        assertEquals("", style.removeProperty("unknown"));
        assertEquals(1, style.getLength());

        assertEquals("", style.removeProperty(null));
        assertEquals(1, style.getLength());

        assertEquals("value", style.removeProperty("prop"));
        assertEquals(0, style.getLength());
        assertEquals("", style.getPropertyValue("prop"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void twoRules() throws Exception {
        final CSSStyleDeclarationImpl style = parseStyleDeclaration("prop: value; color: red !important;");

        assertEquals("prop: value; color: red !important", style.getCssText());
        assertEquals(2, style.getLength());

        assertNull(style.getParentRule());
        assertEquals("value", style.getPropertyValue("prop"));
        assertEquals("", style.getPropertyPriority("prop"));
        assertEquals("value", style.getPropertyCSSValue("prop").getCssText());

        assertEquals("red", style.getPropertyValue("color"));
        assertEquals("important", style.getPropertyPriority("color"));
        assertEquals("red", style.getPropertyCSSValue("color").getCssText());

        assertEquals("", style.getPropertyValue("unknown"));
        assertEquals("", style.getPropertyPriority("unknown"));
        assertNull(style.getPropertyCSSValue("unknown"));

        assertEquals("", style.getPropertyValue(null));
        assertEquals("", style.getPropertyPriority(null));
        assertNull(style.getPropertyCSSValue(null));

        // remove
        assertEquals("", style.removeProperty("unknown"));
        assertEquals(2, style.getLength());

        assertEquals("", style.removeProperty(null));
        assertEquals(2, style.getLength());

        assertEquals("value", style.removeProperty("prop"));
        assertEquals(1, style.getLength());
        assertEquals("", style.getPropertyValue("prop"));

        assertEquals("red", style.removeProperty("color"));
        assertEquals(0, style.getLength());
        assertEquals("", style.getPropertyValue("color"));
    }

    /**
     * Regression test for bug 1874800.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void cssTextHasNoCurlyBraces() throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("basic.css");
        assertNotNull(is);

        final CSSOMParser parser = new CSSOMParser();

        final CSSStyleDeclarationImpl style = parser.parseStyleDeclaration(IOUtils.toString(is, StandardCharsets.UTF_8));

        assertFalse(style.getCssText().contains("{"));
        assertFalse(style.getCssText().contains("}"));

        style.setCssText("color: red;");
        assertEquals("color: red", style.getCssText());
    }

    /**
     * Regression test for bug 1691221.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyUrl() throws Exception {
        final CSSStyleDeclarationImpl style = parseStyleDeclaration("background: url()");

        assertEquals("background: url(\"\")", style.getCssText());
        assertEquals(1, style.getLength());
        assertEquals("background", style.getProperties().get(0).getName());
        assertEquals("url(\"\")", style.getPropertyValue("background"));
    }

    /**
     * Test serialization.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void serialize() throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("basic.css");
        assertNotNull(is);

        final CSSOMParser parser = new CSSOMParser();

        final CSSStyleDeclarationImpl style = parser.parseStyleDeclaration(IOUtils.toString(is, StandardCharsets.UTF_8));
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(style);
        oos.flush();
        oos.close();
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        final Object o = ois.readObject();
        ois.close();
        assertEquals(style, o);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setProperty() throws Exception {
        final CSSStyleDeclarationImpl style = new CSSStyleDeclarationImpl(null);
        style.setProperty("display", "value", "false");
        assertEquals("value", style.getPropertyValue("display"));
        assertEquals("", style.getPropertyPriority("display"));

        style.setProperty("display", "value2", "imPOrTant");
        assertEquals("value2", style.getPropertyValue("display"));
        assertEquals("important", style.getPropertyPriority("display"));

        style.setProperty("display", "value2", "TrUE");
        assertEquals("value2", style.getPropertyValue("display"));
        assertEquals("", style.getPropertyPriority("display"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setPropertyNull() throws Exception {
        final CSSStyleDeclarationImpl style = new CSSStyleDeclarationImpl(null);
        style.setProperty("display", "value", null);
        assertEquals("value", style.getPropertyValue("display"));
        assertEquals("", style.getPropertyPriority("display"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setPropertyValue() throws Exception {
        final CSSStyleDeclarationImpl style = new CSSStyleDeclarationImpl(null);
        final CSSValueImpl value = parsePropertyValue("none");
        style.addProperty(new Property("display", value, false));
        style.setProperty("display", "newValue", "false");
        assertEquals("newValue", style.getPropertyValue("display"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setPropertyValueToEmpty() throws Exception {
        final CSSStyleDeclarationImpl style = new CSSStyleDeclarationImpl(null);
        final CSSValueImpl value = parsePropertyValue("none");
        style.addProperty(new Property("display", value, false));
        style.setProperty("display", "", "false");
        assertEquals("", style.getPropertyValue("display"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void setPropertyValueToBlank() throws Exception {
        final CSSStyleDeclarationImpl style = new CSSStyleDeclarationImpl(null);
        final CSSValueImpl value = parsePropertyValue("none");
        style.addProperty(new Property("display", value, false));
        style.setProperty("display", " \t \r \n", "false");
        assertEquals("", style.getPropertyValue("display"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void removeProperty() throws Exception {
        final CSSStyleDeclarationImpl style = new CSSStyleDeclarationImpl(null);
        final CSSValueImpl value = parsePropertyValue("none");
        style.addProperty(new Property("display", value, false));
        assertEquals("none", style.removeProperty("display"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void removePropertyEmpty() throws Exception {
        final CSSStyleDeclarationImpl style = new CSSStyleDeclarationImpl(null);
        final CSSValueImpl value = parsePropertyValue("");
        style.addProperty(new Property("display", value, false));
        assertEquals("", style.removeProperty("display"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void removePropertyBlank() throws Exception {
        final CSSStyleDeclarationImpl style = new CSSStyleDeclarationImpl(null);
        final CSSValueImpl value = parsePropertyValue("  \t  ");
        style.addProperty(new Property("display", value, false));
        assertEquals("", style.removeProperty("display"));
    }

    private CSSStyleDeclarationImpl parseStyleDeclaration(final String value) throws IOException {
        final CSSOMParser parser = new CSSOMParser();
        return parser.parseStyleDeclaration(value);
    }

    private CSSValueImpl parsePropertyValue(final String value) throws IOException {
        final CSSOMParser parser = new CSSOMParser();
        return parser.parsePropertyValue(value);
    }
}
