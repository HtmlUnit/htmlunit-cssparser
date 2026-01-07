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
package org.htmlunit.cssparser.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.htmlunit.cssparser.dom.AbstractCSSRuleImpl;
import org.htmlunit.cssparser.dom.CSSCharsetRuleImpl;
import org.htmlunit.cssparser.dom.CSSFontFaceRuleImpl;
import org.htmlunit.cssparser.dom.CSSImportRuleImpl;
import org.htmlunit.cssparser.dom.CSSMediaRuleImpl;
import org.htmlunit.cssparser.dom.CSSPageRuleImpl;
import org.htmlunit.cssparser.dom.CSSRuleListImpl;
import org.htmlunit.cssparser.dom.CSSStyleDeclarationImpl;
import org.htmlunit.cssparser.dom.CSSStyleRuleImpl;
import org.htmlunit.cssparser.dom.CSSStyleSheetImpl;
import org.htmlunit.cssparser.dom.CSSUnknownRuleImpl;
import org.htmlunit.cssparser.dom.CSSValueImpl;
import org.htmlunit.cssparser.dom.CSSValueImpl.CSSValueType;
import org.htmlunit.cssparser.dom.MediaListImpl;
import org.htmlunit.cssparser.dom.Property;
import org.htmlunit.cssparser.parser.javacc.CSS3Parser;
import org.junit.jupiter.api.Test;

/**
 * Testcases for correct error location reporting.
 *
 * @author Ronald Brill
 */
public class LocatorTest {

    private static final String CHARSET_RULE = "@charset \"utf-8\";\n";
    private static final String IMPORT_RULE =
        "@import url('./import.css') screen, projection;\n";
    private static final String UNKNOWN_AT_RULE = "@foo bar;\n";
    private static final String FONT_FACE_RULE = "@font-face {\n"
        + "  font-family: \"Robson Celtic\";\n"
        + "  src: url('http://site/fonts/rob-celt')\n"
        + "}\n";
    private static final String PAGE_RULE = "@page :left {\n"
        + "  margin: 3cm\n"
        + "}\n";
    private static final String MEDIA_RULE_START = "@media handheld {\n";
    private static final String STYLE_RULE =
        "h1, h2, .foo, h3#bar {\n"
        + "  font-weight: bold;\n"
        + "  padding-left: -1px;\n"
        + "  border-right: medium solid #00f\n"
        + "}\n";

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void locationsCSS3() throws IOException {
        final String cssCode = CHARSET_RULE
            + IMPORT_RULE
            + UNKNOWN_AT_RULE
            + PAGE_RULE
            + FONT_FACE_RULE
            + MEDIA_RULE_START
            + STYLE_RULE
            + "}\n";

        final Map<Character, List<Integer[]>> positions = new Hashtable<>();
        final List<Integer[]> rPos = new ArrayList<>();
        rPos.add(new Integer[] {1, 1});
        rPos.add(new Integer[] {2, 1});
        rPos.add(new Integer[] {3, 1});
        rPos.add(new Integer[] {4, 1});
        rPos.add(new Integer[] {7, 1});
        rPos.add(new Integer[] {11, 1});
        rPos.add(new Integer[] {12, 1});
        positions.put('R', rPos);
        final List<Integer[]> mPos = new ArrayList<>();
        mPos.add(new Integer[] {2, 29});
        mPos.add(new Integer[] {11, 16});
        positions.put('M', mPos);
        final List<Integer[]> pPos = new ArrayList<>();
        pPos.add(new Integer[] {5, 3});
        pPos.add(new Integer[] {8, 3});
        pPos.add(new Integer[] {9, 3});
        pPos.add(new Integer[] {13, 3});
        pPos.add(new Integer[] {14, 3});
        pPos.add(new Integer[] {15, 3});
        positions.put('P', pPos);
        final List<Integer[]> vPos = new ArrayList<>();
        vPos.add(new Integer[] {5, 11});
        vPos.add(new Integer[] {8, 16});
        vPos.add(new Integer[] {9, 8});
        vPos.add(new Integer[] {13, 16});
        vPos.add(new Integer[] {14, 17});
        vPos.add(new Integer[] {15, 17});
        vPos.add(new Integer[] {15, 17});
        vPos.add(new Integer[] {15, 24});
        vPos.add(new Integer[] {15, 30});
        positions.put('V', vPos);

        locations(new CSS3Parser(), cssCode, positions);
    }

    private void locations(final AbstractCSSParser cssParser, final String cssCode,
            final Map<Character, List<Integer[]>> positions) throws IOException {
        final Reader r = new StringReader(cssCode);
        final InputSource source = new InputSource(r);
        final CSSOMParser cssomParser = new CSSOMParser();
        final Map<Character, Integer> counts = new Hashtable<>();
        counts.put('R', 0);
        counts.put('M', 0);
        counts.put('P', 0);
        counts.put('V', 0);

        final CSSStyleSheetImpl cssStyleSheet = cssomParser.parseStyleSheet(source, null);
        final CSSRuleListImpl cssRules = cssStyleSheet.getCssRules();
        cssRules(cssRules, positions, counts);
    }

    private void cssRules(final CSSRuleListImpl cssRules, final Map<Character, List<Integer[]>> positions,
            final Map<Character, Integer> counts) {
        for (int i = 0; i < cssRules.getLength(); i++) {
            cssRule(cssRules.getRules().get(i), positions, counts);
        }
    }

    private void cssRule(final AbstractCSSRuleImpl cssRule, final Map<Character, List<Integer[]>> positions,
            final Map<Character, Integer> counts) {
        final Locator locator = cssRule.getLocator();
        final Integer[] expected = positions.get('R').get(counts.get('R'));
        final int expectedLine = expected[0];
        final int expectedColumn = expected[1];

        assertEquals(expectedLine, locator.getLineNumber());
        assertEquals(expectedColumn, locator.getColumnNumber());
        counts.put('R', counts.get('R') + 1);

        if (cssRule instanceof CSSUnknownRuleImpl cssUnknownRule) {
            // TODO
        }
        else if (cssRule instanceof CSSCharsetRuleImpl cssCharsetRule) {
            // TODO
        }
        else if (cssRule instanceof CSSImportRuleImpl cssImportRule) {
            mediaList(cssImportRule.getMedia(), positions, counts);
        }
        else if (cssRule instanceof CSSMediaRuleImpl cssMediaRule) {
            mediaList(cssMediaRule.getMediaList(), positions, counts);
            cssRules(cssMediaRule.getCssRules(), positions, counts);
        }
        else if (cssRule instanceof CSSPageRuleImpl cssPageRule) {
            cssStyleDeclaration(cssPageRule.getStyle(), positions, counts);
        }
        else if (cssRule instanceof CSSFontFaceRuleImpl cssFontFaceRule) {
            cssStyleDeclaration(cssFontFaceRule.getStyle(), positions, counts);
        }
        else if (cssRule instanceof CSSStyleRuleImpl cssStyleRule) {
            cssStyleDeclaration(cssStyleRule.getStyle(), positions, counts);
        }
        else {
            throw new RuntimeException("Unsupported rule type (" + cssRule.getClass().getSimpleName() + ")");
        }
    }

    private void cssStyleDeclaration(final CSSStyleDeclarationImpl style,
            final Map<Character, List<Integer[]>> positions,
            final Map<Character, Integer> counts)    {
        for (Property property : style.getProperties()) {
            property(property, positions, counts);
        }
    }

    private void mediaList(final MediaListImpl mediaList,
            final Map<Character, List<Integer[]>> positions,
            final Map<Character, Integer> counts) {
        if (mediaList.getLength() > 0) {
            final Locator locator = mediaList.getLocator();
            final Integer[] expected = positions.get('M').get(counts.get('M'));
            final int expectedLine = expected[0];
            final int expectedColumn = expected[1];

            assertEquals(expectedLine, locator.getLineNumber());
            assertEquals(expectedColumn, locator.getColumnNumber());
            counts.put('M', counts.get('M') + 1);
        }
    }

    private void property(final Property property,
            final Map<Character, List<Integer[]>> positions,
            final Map<Character, Integer> counts) {
        final Locator locator = property.getLocator();
        final Integer[] expected = positions.get('P').get(counts.get('P'));
        final int expectedLine = expected[0];
        final int expectedColumn = expected[1];

        assertEquals(expectedLine, locator.getLineNumber());
        assertEquals(expectedColumn, locator.getColumnNumber());
        counts.put('P', counts.get('P') + 1);
        cssValue(property.getValue(), positions, counts);
    }

    private void cssValue(final CSSValueImpl cssValue,
            final Map<Character, List<Integer[]>> positions,
            final Map<Character, Integer> counts) {
        final Locator locator = cssValue.getLocator();
        final Integer[] expected = positions.get('V').get(counts.get('V'));
        final int expectedLine = expected[0];
        final int expectedColumn = expected[1];

        if (locator == null) {
            System.out.println("#");
        }
        assertEquals(expectedLine, locator.getLineNumber());
        assertEquals(expectedColumn, locator.getColumnNumber());
        counts.put('V', counts.get('V') + 1);

        if (cssValue.getCssValueType() == CSSValueType.CSS_VALUE_LIST) {
            final CSSValueImpl cssValueList = cssValue;
            for (int i = 0; i < cssValueList.getLength(); i++) {
                cssValue(cssValueList.item(i), positions, counts);
            }
        }
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void serializeTest() throws Exception {
        final Locator locator = new Locator("uri", 1, 2);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(locator);
        oos.flush();
        oos.close();
        final byte[] bytes = baos.toByteArray();
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        final Object o = ois.readObject();

        assertEquals(locator, o);
    }
}
