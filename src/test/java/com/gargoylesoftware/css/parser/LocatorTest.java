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

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.css.dom.AbstractCSSRuleImpl;
import com.gargoylesoftware.css.dom.CSSCharsetRuleImpl;
import com.gargoylesoftware.css.dom.CSSFontFaceRuleImpl;
import com.gargoylesoftware.css.dom.CSSImportRuleImpl;
import com.gargoylesoftware.css.dom.CSSMediaRuleImpl;
import com.gargoylesoftware.css.dom.CSSPageRuleImpl;
import com.gargoylesoftware.css.dom.CSSRuleListImpl;
import com.gargoylesoftware.css.dom.CSSStyleDeclarationImpl;
import com.gargoylesoftware.css.dom.CSSStyleRuleImpl;
import com.gargoylesoftware.css.dom.CSSStyleSheetImpl;
import com.gargoylesoftware.css.dom.CSSUnknownRuleImpl;
import com.gargoylesoftware.css.dom.CSSValueImpl;
import com.gargoylesoftware.css.dom.CSSValueImpl.CSSValueType;
import com.gargoylesoftware.css.dom.MediaListImpl;
import com.gargoylesoftware.css.dom.Property;
import com.gargoylesoftware.css.parser.javacc.CSS3Parser;

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
    public void locationsCSS3() {
        final String cssCode = CHARSET_RULE
            + IMPORT_RULE
            + UNKNOWN_AT_RULE
            + PAGE_RULE
            + FONT_FACE_RULE
            + MEDIA_RULE_START
            + STYLE_RULE
            + "}\n";
        final Map<Character, List<Integer[]>> positions =
            new Hashtable<Character, List<Integer[]>>();
        final List<Integer[]> rPos = new ArrayList<Integer[]>();
        rPos.add(new Integer[] {1, 1});
        rPos.add(new Integer[] {2, 1});
        rPos.add(new Integer[] {3, 1});
        rPos.add(new Integer[] {4, 1});
        rPos.add(new Integer[] {7, 1});
        rPos.add(new Integer[] {11, 1});
        rPos.add(new Integer[] {12, 1});
        positions.put('R', rPos);
        final List<Integer[]> mPos = new ArrayList<Integer[]>();
        mPos.add(new Integer[] {2, 29});
        mPos.add(new Integer[] {11, 8});
        positions.put('M', mPos);
        final List<Integer[]> pPos = new ArrayList<Integer[]>();
        pPos.add(new Integer[] {5, 3});
        pPos.add(new Integer[] {8, 3});
        pPos.add(new Integer[] {9, 3});
        pPos.add(new Integer[] {13, 3});
        pPos.add(new Integer[] {14, 3});
        pPos.add(new Integer[] {15, 3});
        positions.put('P', pPos);
        final List<Integer[]> vPos = new ArrayList<Integer[]>();
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

    private void locations(final CSSParser cssParser, final String cssCode,
            final Map<Character, List<Integer[]>> positions) {
        final Reader r = new StringReader(cssCode);
        final InputSource source = new InputSource(r);
        final CSSOMParser cssomParser = new CSSOMParser();
        final Map<Character, Integer> counts = new Hashtable<Character, Integer>();
        counts.put('R', 0);
        counts.put('M', 0);
        counts.put('P', 0);
        counts.put('V', 0);
        try {
            final CSSStyleSheetImpl cssStyleSheet = cssomParser.parseStyleSheet(source, null);
            final CSSRuleListImpl cssRules = cssStyleSheet.getCssRules();
            cssRules(cssRules, positions, counts);
        }
        catch (final IOException e) {
            Assert.assertFalse(e.getLocalizedMessage(), true);
        }
    }

    private void cssRules(final CSSRuleListImpl cssRules, final Map<Character, List<Integer[]>> positions,
            final Map<Character, Integer> counts) {
        for (int i = 0; i < cssRules.getLength(); i++) {
            cssRule(cssRules.item(i), positions, counts);
        }
    }

    private void cssRule(final AbstractCSSRuleImpl cssRule, final Map<Character, List<Integer[]>> positions,
            final Map<Character, Integer> counts) {
        final Locator locator = ((Locatable) cssRule).getLocator();
        final Integer[] expected = positions.get('R').get(counts.get('R'));
        final int expectedLine = expected[0];
        final int expectedColumn = expected[1];

        Assert.assertEquals(expectedLine, locator.getLineNumber());
        Assert.assertEquals(expectedColumn, locator.getColumnNumber());
        counts.put('R', counts.get('R') + 1);

        switch (cssRule.getType()) {
            case UNKNOWN_RULE:
                final CSSUnknownRuleImpl cssUnknownRule = (CSSUnknownRuleImpl) cssRule;
                // TODO
                break;
            case CHARSET_RULE:
                final CSSCharsetRuleImpl cssCharsetRule = (CSSCharsetRuleImpl) cssRule;
                // TODO
                break;
            case IMPORT_RULE:
                final CSSImportRuleImpl cssImportRule = (CSSImportRuleImpl) cssRule;
                mediaList(cssImportRule.getMedia(), positions, counts);
                break;
            case MEDIA_RULE:
                final CSSMediaRuleImpl cssMediaRule = (CSSMediaRuleImpl) cssRule;
                mediaList(cssMediaRule.getMedia(), positions, counts);
                cssRules(cssMediaRule.getCssRules(), positions, counts);
                break;
            case PAGE_RULE:
                final CSSPageRuleImpl cssPageRule = (CSSPageRuleImpl) cssRule;
                cssStyleDeclaration(cssPageRule.getStyle(), positions, counts);
                break;
            case FONT_FACE_RULE:
                final CSSFontFaceRuleImpl cssFontFaceRule = (CSSFontFaceRuleImpl) cssRule;
                cssStyleDeclaration(cssFontFaceRule.getStyle(), positions, counts);
                break;
            case STYLE_RULE:
                final CSSStyleRuleImpl cssStyleRule = (CSSStyleRuleImpl) cssRule;
                cssStyleDeclaration(cssStyleRule.getStyle(), positions, counts);
                break;
            default:
                throw new RuntimeException("Unsupported rule type (" + cssRule.getType() + ")");
        }
    }

    private void cssStyleDeclaration(final CSSStyleDeclarationImpl style,
            final Map<Character, List<Integer[]>> positions,
            final Map<Character, Integer> counts)    {
        final Iterator<Property> it = style.getProperties().iterator();
        while (it.hasNext()) {
            property(it.next(), positions, counts);
        }
    }

    private void mediaList(final MediaListImpl mediaList,
            final Map<Character, List<Integer[]>> positions,
            final Map<Character, Integer> counts) {
        if ((mediaList.getLength() > 0) ) {
            final Locator locator = ((Locatable) mediaList).getLocator();
            final Integer[] expected = positions.get('M').get(counts.get('M'));
            final int expectedLine = expected[0];
            final int expectedColumn = expected[1];

            Assert.assertEquals(expectedLine, locator.getLineNumber());
            Assert.assertEquals(expectedColumn, locator.getColumnNumber());
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

        Assert.assertEquals(expectedLine, locator.getLineNumber());
        Assert.assertEquals(expectedColumn, locator.getColumnNumber());
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
        Assert.assertEquals(expectedLine, locator.getLineNumber());
        Assert.assertEquals(expectedColumn, locator.getColumnNumber());
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

        Assert.assertEquals(locator, o);
    }
}
