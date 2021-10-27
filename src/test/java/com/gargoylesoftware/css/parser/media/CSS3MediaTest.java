/*
 * Copyright (c) 2019-2020 Ronald Brill.
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
package com.gargoylesoftware.css.parser.media;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.gargoylesoftware.css.dom.AbstractCSSRuleImpl;
import com.gargoylesoftware.css.dom.CSSMediaRuleImpl;
import com.gargoylesoftware.css.dom.CSSStyleSheetImpl;
import com.gargoylesoftware.css.dom.CSSValueImpl;
import com.gargoylesoftware.css.dom.MediaListImpl;
import com.gargoylesoftware.css.dom.Property;
import com.gargoylesoftware.css.parser.AbstractCSSParserTest;
import com.gargoylesoftware.css.parser.LexicalUnit;
import com.gargoylesoftware.css.parser.LexicalUnit.LexicalUnitType;
import com.gargoylesoftware.css.parser.LexicalUnitImpl;

/**
 * @author Ronald Brill
 */
public class CSS3MediaTest extends AbstractCSSParserTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void mediaConditions() throws Exception {
        final String css = "@media screen and (max-width: 30em) { h1 { color: red } }";
        final CSSStyleSheetImpl sheet = parse(css);
        final AbstractCSSRuleImpl cssRule = sheet.getCssRules().getRules().get(0);
        assertTrue(cssRule instanceof CSSMediaRuleImpl);

        final MediaListImpl mediaList = ((CSSMediaRuleImpl) cssRule).getMediaList();
        assertEquals("screen and (max-width: 30em)", mediaList.getMediaText());
        assertEquals(1, mediaList.getLength());

        assertEquals("screen", mediaList.mediaQuery(0).getMedia());
        assertEquals("screen and (max-width: 30em)", mediaList.mediaQuery(0).toString());
        assertEquals(1, mediaList.mediaQuery(0).getProperties().size());
        assertEquals("max-width: 30em", mediaList.mediaQuery(0).getProperties().get(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void withoutValue() throws Exception {
        final String css = "@media screen and (color) { h1 { color: red } }";
        final CSSStyleSheetImpl sheet = parse(css);
        final AbstractCSSRuleImpl cssRule = sheet.getCssRules().getRules().get(0);
        assertTrue(cssRule instanceof CSSMediaRuleImpl);

        final MediaListImpl mediaList = ((CSSMediaRuleImpl) cssRule).getMediaList();
        assertEquals("screen and (color)", mediaList.getMediaText());
        assertEquals(1, mediaList.getLength());

        assertEquals("screen", mediaList.mediaQuery(0).getMedia());
        assertEquals("screen and (color)", mediaList.mediaQuery(0).toString());
        assertEquals(1, mediaList.mediaQuery(0).getProperties().size());
        assertEquals("color", mediaList.mediaQuery(0).getProperties().get(0).toString());
    }

    /**
     * see http://keithclark.co.uk/articles/moving-ie-specific-css-into-media-blocks/media-tests/.
     * @throws Exception if any error occurs
     */
    @Test
    public void mediaBlockHacks() throws Exception {
        // TODO have to check this in more detail
        String css = "@media\\0 { h1 { color: red } }";
        CSSStyleSheetImpl sheet = parse(css, 0, 0, 0);
        assertEquals(1, sheet.getCssRules().getLength());

        css = "@media\\9 { h1 { color: red } }";
        sheet = parse(css, 0, 0, 0);
        assertEquals(1, sheet.getCssRules().getLength());

        css = "@media screen\\0 { h1 { color: red } }";
        sheet = parse(css, 0, 0, 0);
        assertEquals(1, sheet.getCssRules().getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void only() throws Exception {
        final String css = "@media only screen and (color) { h1 { color: red } }";
        final CSSStyleSheetImpl sheet = parse(css);
        final AbstractCSSRuleImpl cssRule = sheet.getCssRules().getRules().get(0);
        assertTrue(cssRule instanceof CSSMediaRuleImpl);

        final MediaListImpl mediaList = ((CSSMediaRuleImpl) cssRule).getMediaList();
        assertEquals("only screen and (color)", mediaList.getMediaText());
        assertEquals(1, mediaList.getLength());

        assertEquals("screen", mediaList.mediaQuery(0).getMedia());
        assertEquals("only screen and (color)", mediaList.mediaQuery(0).toString());
        assertFalse(mediaList.mediaQuery(0).isNot());
        assertTrue(mediaList.mediaQuery(0).isOnly());
        assertEquals(1, mediaList.mediaQuery(0).getProperties().size());
        assertEquals("color", mediaList.mediaQuery(0).getProperties().get(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void not() throws Exception {
        final String css = "@media not screen and (color) { h1 { color: red } }";
        final CSSStyleSheetImpl sheet = parse(css);
        final AbstractCSSRuleImpl cssRule = sheet.getCssRules().getRules().get(0);
        assertTrue(cssRule instanceof CSSMediaRuleImpl);

        final MediaListImpl mediaList = ((CSSMediaRuleImpl) cssRule).getMediaList();
        assertEquals("not screen and (color)", mediaList.getMediaText());
        assertEquals(1, mediaList.getLength());

        assertEquals("screen", mediaList.mediaQuery(0).getMedia());
        assertEquals("not screen and (color)", mediaList.mediaQuery(0).toString());
        assertTrue(mediaList.mediaQuery(0).isNot());
        assertFalse(mediaList.mediaQuery(0).isOnly());
        assertEquals(1, mediaList.mediaQuery(0).getProperties().size());
        assertEquals("color", mediaList.mediaQuery(0).getProperties().get(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dpi() throws Exception {
        final String css = "@media not screen and (min-resolution: 300dpi) { h1 { color: red } }";
        final CSSStyleSheetImpl sheet = parse(css);
        final AbstractCSSRuleImpl cssRule = sheet.getCssRules().getRules().get(0);
        assertTrue(cssRule instanceof CSSMediaRuleImpl);

        final MediaListImpl mediaList = ((CSSMediaRuleImpl) cssRule).getMediaList();
        assertEquals("not screen and (min-resolution: 300dpi)", mediaList.getMediaText());
        assertEquals(1, mediaList.getLength());

        assertEquals("screen", mediaList.mediaQuery(0).getMedia());
        assertEquals("not screen and (min-resolution: 300dpi)", mediaList.mediaQuery(0).toString());
        assertEquals(1, mediaList.mediaQuery(0).getProperties().size());

        final Property prop = mediaList.mediaQuery(0).getProperties().get(0);
        assertEquals("min-resolution: 300dpi", prop.toString());
        assertEquals("min-resolution", prop.getName());
        final CSSValueImpl valueImpl = prop.getValue();
        final LexicalUnit unitImpl = (LexicalUnitImpl) valueImpl.getValue();
        assertEquals(LexicalUnitType.DIMENSION, unitImpl.getLexicalUnitType());
        assertEquals(300, unitImpl.getDoubleValue(), 0.000001);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dpcm() throws Exception {
        final String css = "@media not screen and (min-resolution: 11.8dpcm) { h1 { color: red } }";
        final CSSStyleSheetImpl sheet = parse(css);
        final AbstractCSSRuleImpl cssRule = sheet.getCssRules().getRules().get(0);
        assertTrue(cssRule instanceof CSSMediaRuleImpl);

        final MediaListImpl mediaList = ((CSSMediaRuleImpl) cssRule).getMediaList();
        assertEquals("not screen and (min-resolution: 11.8dpcm)", mediaList.getMediaText());
        assertEquals(1, mediaList.getLength());

        assertEquals("screen", mediaList.mediaQuery(0).getMedia());
        assertEquals("not screen and (min-resolution: 11.8dpcm)", mediaList.mediaQuery(0).toString());
        assertEquals(1, mediaList.mediaQuery(0).getProperties().size());

        final Property prop = mediaList.mediaQuery(0).getProperties().get(0);
        assertEquals("min-resolution: 11.8dpcm", prop.toString());
        assertEquals("min-resolution", prop.getName());
        final CSSValueImpl valueImpl = prop.getValue();
        final LexicalUnit unitImpl = (LexicalUnitImpl) valueImpl.getValue();
        assertEquals(LexicalUnitType.DIMENSION, unitImpl.getLexicalUnitType());
        assertEquals(11.8, unitImpl.getDoubleValue(), 0.000001);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void onlyAndAnd() throws Exception {
        final String css =
                "@media only screen and (max-width: 735px) and (max-device-width: 768px) { h1 { color: red } }";
        final CSSStyleSheetImpl sheet = parse(css);
        final AbstractCSSRuleImpl cssRule = sheet.getCssRules().getRules().get(0);
        assertTrue(cssRule instanceof CSSMediaRuleImpl);

        final MediaListImpl mediaListImpl = ((CSSMediaRuleImpl) cssRule).getMediaList();
        assertEquals("only screen and (max-width: 735px) and (max-device-width: 768px)",
                mediaListImpl.getMediaText());
        assertEquals(1, mediaListImpl.getLength());
        assertEquals("screen", mediaListImpl.mediaQuery(0).getMedia());
        assertEquals("only screen and (max-width: 735px) and (max-device-width: 768px)",
                mediaListImpl.mediaQuery(0).toString());
        assertEquals(2, mediaListImpl.mediaQuery(0).getProperties().size());

        Property prop = mediaListImpl.mediaQuery(0).getProperties().get(0);
        assertEquals("max-width: 735px", prop.toString());
        assertEquals("max-width", prop.getName());
        CSSValueImpl valueImpl = prop.getValue();
        LexicalUnit unitImpl = (LexicalUnitImpl) valueImpl.getValue();
        assertEquals(LexicalUnitType.PIXEL, unitImpl.getLexicalUnitType());
        assertEquals(735, unitImpl.getIntegerValue(), 0.000001);

        prop = mediaListImpl.mediaQuery(0).getProperties().get(1);
        assertEquals("max-device-width: 768px", prop.toString());
        assertEquals("max-device-width", prop.getName());
        valueImpl = prop.getValue();
        unitImpl = (LexicalUnitImpl) valueImpl.getValue();
        assertEquals(LexicalUnitType.PIXEL, unitImpl.getLexicalUnitType());
        assertEquals(768, unitImpl.getIntegerValue(), 0.000001);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void allShorthand() throws Exception {
        // all and (color)
        final String css = "@media (color) { h1 { color: red } }";
        final CSSStyleSheetImpl sheet = parse(css);
        assertEquals(1, sheet.getCssRules().getLength());

        assertEquals(1, sheet.getMedia().getLength());
        assertEquals("", sheet.getMedia().toString());

        final AbstractCSSRuleImpl cssRule = sheet.getCssRules().getRules().get(0);
        assertTrue(cssRule instanceof CSSMediaRuleImpl);

        final MediaListImpl mediaListImpl = ((CSSMediaRuleImpl) cssRule).getMediaList();
        assertEquals("(color)", mediaListImpl.getMediaText());
        assertEquals(1, mediaListImpl.getLength());
        assertEquals("all", mediaListImpl.mediaQuery(0).getMedia());
        assertEquals("(color)", mediaListImpl.mediaQuery(0).toString());
        assertEquals(1, mediaListImpl.mediaQuery(0).getProperties().size());
        assertEquals("color", mediaListImpl.mediaQuery(0).getProperties().get(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseMedia() throws Exception {
        final String css = "speech and (min-device-width: 800px)";
        final MediaQueryList mediaList = parseMedia(css, 0, 0, 0);

        assertEquals(1, mediaList.getLength());
        assertEquals("speech", mediaList.toString());

        final MediaQuery mediaQuery = mediaList.getMediaQueries().get(0);
        assertEquals("speech", mediaQuery.getMedia());
        final List<Property> properties = mediaQuery.getProperties();
        assertEquals(1, properties.size());
        assertEquals("min-device-width: 800px", properties.get(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseMediaComplexRule() throws Exception {
        final String css = "only screen and (max-width: 735px) and (max-device-width: 768px), print,"
                + "aural and (device-aspect-ratio: 16/9)";
        final MediaQueryList mediaList = parseMedia(css, 0, 0, 0);

        assertEquals(3, mediaList.getLength());
        assertEquals("screen, print, aural", mediaList.toString());

        MediaQuery mediaQuery = mediaList.getMediaQueries().get(0);
        assertEquals("screen", mediaQuery.getMedia());
        List<Property> properties = mediaQuery.getProperties();
        assertEquals(2, properties.size());
        assertEquals("max-width: 735px", properties.get(0).toString());
        assertEquals("max-device-width: 768px", properties.get(1).toString());

        mediaQuery = mediaList.getMediaQueries().get(1);
        assertEquals("print", mediaQuery.getMedia());
        properties = mediaQuery.getProperties();
        assertEquals(0, properties.size());

        mediaQuery = mediaList.getMediaQueries().get(2);
        assertEquals("aural", mediaQuery.getMedia());
        properties = mediaQuery.getProperties();
        assertEquals("device-aspect-ratio: 16 / 9", properties.get(0).toString());
    }
}
