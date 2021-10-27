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
package com.gargoylesoftware.css.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import com.gargoylesoftware.css.dom.AbstractCSSRuleImpl;
import com.gargoylesoftware.css.dom.CSSPageRuleImpl;
import com.gargoylesoftware.css.dom.CSSRuleListImpl;
import com.gargoylesoftware.css.dom.CSSStyleDeclarationImpl;
import com.gargoylesoftware.css.dom.CSSStyleRuleImpl;
import com.gargoylesoftware.css.dom.CSSStyleSheetImpl;
import com.gargoylesoftware.css.dom.CSSValueImpl;
import com.gargoylesoftware.css.dom.Property;
import com.gargoylesoftware.css.parser.javacc.CSS3Parser;
import com.gargoylesoftware.css.parser.media.MediaQueryList;
import com.gargoylesoftware.css.parser.selector.SelectorList;

/**
 * Test for the CSSOMParser.
 * @author Ronald Brill
 */
public class CSSOMParserTest {

    private String testStyleDeclaration_ = "align: right";
    private String testParseMedia_ = "print, screen";
    private String testParseRule_ = "p { " + testStyleDeclaration_ + "; }";
    private String testSelector_ = "FOO";
    private String testItem_ = "color";
    private String testValue_ = "rgb(1, 2, 3)";
    private String testString_ = testSelector_ + "{ " + testItem_ + ": " + testValue_ + " }";
    private String testPropertyValue_ = "sans-serif";

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defaultConstructor() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        assertNotNull(parser);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defineParserInstance() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        assertNotNull(parser);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseStyleSheet() throws Exception {
        final Reader r = new StringReader(testString_);
        final InputSource is = new InputSource(r);
        final CSSStyleSheetImpl ss = new CSSOMParser().parseStyleSheet(is, null);

        final CSSRuleListImpl rl = ss.getCssRules();
        final AbstractCSSRuleImpl rule = rl.getRules().get(0);

        final CSSStyleRuleImpl sr = (CSSStyleRuleImpl) rule;
        assertEquals(testSelector_, sr.getSelectorText());

        final CSSStyleDeclarationImpl style = sr.getStyle();
        assertEquals(testItem_, style.getProperties().get(0).getName());

        final CSSValueImpl value = style.getPropertyCSSValue(style.getProperties().get(0).getName());
        assertEquals(testValue_, value.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseStyleSheetIgnoreProblemCss3() throws Exception {
        final String test = "p{filter:alpha(opacity=33.3);opacity:0.333}a{color:#123456;}";

        final Reader r = new StringReader(test);
        final InputSource is = new InputSource(r);
        final CSSStyleSheetImpl ss = new CSSOMParser().parseStyleSheet(is, null);
        final CSSRuleListImpl rl = ss.getCssRules();
        assertEquals(2, rl.getLength());

        AbstractCSSRuleImpl rule = rl.getRules().get(0);
        CSSStyleRuleImpl sr = (CSSStyleRuleImpl) rule;
        assertEquals("p { filter: alpha(opacity=33.3); opacity: 0.333; }", sr.getCssText());

        rule = rl.getRules().get(1);
        sr = (CSSStyleRuleImpl) rule;
        assertEquals("a { color: rgb(18, 52, 86); }", sr.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseSelectors() throws Exception {
        final SelectorList sl = new CSSOMParser().parseSelectors(testSelector_);

        assertEquals(testSelector_, sl.get(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseSelectorsEscapedChars() throws Exception {
        final SelectorList sl = new CSSOMParser().parseSelectors("#id\\:withColon");

        assertEquals("*#id:withColon", sl.get(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseSelectorsParseException() throws Exception {
        final SelectorList sl = new CSSOMParser().parseSelectors("table==td");

        assertNull(sl);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parsePropertyValue() throws Exception {
        final CSSValueImpl pv = new CSSOMParser().parsePropertyValue(testPropertyValue_);

        assertEquals(testPropertyValue_, pv.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parsePropertyValueParseException() throws Exception {
        final CSSValueImpl pv = new CSSOMParser().parsePropertyValue("@a");

        assertNull(pv);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseMedia() throws Exception {
        final MediaQueryList ml = new CSSOMParser().parseMedia(testParseMedia_);

        assertEquals(2, ml.getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseMediaParseException() throws Exception {
        final MediaQueryList ml = new CSSOMParser().parseMedia("~xx");

        assertEquals(0, ml.getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseRule() throws Exception {
        final AbstractCSSRuleImpl rule = new CSSOMParser().parseRule(testParseRule_);

        assertEquals(testParseRule_, rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseRuleParseException() throws Exception {
        final AbstractCSSRuleImpl rule = new CSSOMParser().parseRule("~xx");

        assertNull(rule);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseStyleDeclaration() throws Exception {
        final CSSStyleDeclarationImpl sd = new CSSOMParser().parseStyleDeclaration(testStyleDeclaration_);

        assertEquals(testStyleDeclaration_, sd.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseStyleDeclarationParseException() throws Exception {
        final CSSStyleDeclarationImpl sd = new CSSOMParser().parseStyleDeclaration("@abc");

        assertEquals(sd.getLength(), 0);
    }

    /**
     * Regression test for bug 1191376.
     *
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void parseStyleDeclarationWithoutBrace() throws Exception {
        final CSSStyleDeclarationImpl declaration = new CSSOMParser().parseStyleDeclaration("background-color: white");

        assertEquals(1, declaration.getLength());
    }

    /**
     * Regression test for bug 3293695.
     *
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void urlGreedy() throws Exception {
        assertEquals(
            "background: url(\"images/bottom-angle.png\"); background-image: url(\"background.png\")",
            getCssTextFromDeclaration(
                    new CSS3Parser(),
                    "background:url('images/bottom-angle.png');background-image:url('background.png');"));
        assertEquals(
            "background: url(\"images/bottom-angle.png\"); background-image: url(\"background.png\")",
            getCssTextFromDeclaration(
                    new CSS3Parser(),
                    "background:url(\"images/bottom-angle.png\");background-image:url(\"background.png\");"));
        assertEquals(
            "background: rgb(60, 90, 118) url(\"/images/status_bg.png?2\") no-repeat center; "
            + "font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(
                    new CSS3Parser(),
                    "background:#3c5a76 url(\"/images/status_bg.png?2\") no-repeat center;"
                    + "font-family:Arial,'Helvetica Neue',Helvetica,sans-serif"));
    }

    /**
     * Regression test for bug 2042900.
     *
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void commaList() throws Exception {
        assertEquals(
            "font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(new CSS3Parser(),
                    "font-family: Arial,'Helvetica Neue',Helvetica,sans-serif"));
        assertEquals(
            "font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(new CSS3Parser(),
                    "font-family: Arial, 'Helvetica Neue', Helvetica,  sans-serif"));
    }

    /**
     * Regression test for bug 1183734.
     *
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void colorFirst() throws Exception {
        assertEquals(
            "background: rgb(232, 239, 245) url(\"images/bottom-angle.png\") no-repeat",
            getCssTextFromDeclaration(new CSS3Parser(),
                    "background: #e8eff5 url(images/bottom-angle.png) no-repeat"));
        assertEquals(
            "background: red url(\"images/bottom-angle.png\") no-repeat",
            getCssTextFromDeclaration(new CSS3Parser(), "background: red url(images/bottom-angle.png) no-repeat"));
        assertEquals(
            "background: rgb(8, 3, 6) url(\"images/bottom-angle.png\") no-repeat",
            getCssTextFromDeclaration(new CSS3Parser(),
                    "background: rgb(8, 3, 6) url(images/bottom-angle.png) no-repeat"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void speciaChars() throws Exception {
        assertEquals("content: \"�\"",
                getCssTextFromDeclaration(new CSS3Parser(), "content: '�';"));
        assertEquals("content: \"\u0122\"",
            getCssTextFromDeclaration(new CSS3Parser(), "content: '\u0122';"));
        assertEquals("content: \"\u0422\"",
            getCssTextFromDeclaration(new CSS3Parser(), "content: '\u0422';"));
    }

    /**
     * Regression test for bug 1659992.
     *
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void doubleDotSelector() throws Exception {
        doubleDotSelector(new CSS3Parser());
    }

    private void doubleDotSelector(final AbstractCSSParser p) throws Exception {
        final Reader r = new StringReader("..nieuwsframedatum{ font-size : 8pt;}");
        final InputSource source = new InputSource(r);
        final CSSOMParser parser = new CSSOMParser();
        final CSSStyleSheetImpl ss = parser.parseStyleSheet(source, null);

        assertEquals(0, ss.getCssRules().getLength());
    }

    /**
     * Regression test for bug 2796824.
     *
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void importEOF() throws Exception {
        importEOF(new CSS3Parser());
    }

    private void importEOF(final AbstractCSSParser p) throws Exception {
        final Reader r = new StringReader("@import http://www.wetator.org");
        final InputSource source = new InputSource(r);
        final CSSOMParser parser = new CSSOMParser();
        final CSSStyleSheetImpl ss = parser.parseStyleSheet(source, null);

        assertEquals(0, ss.getCssRules().getLength());
    }

    /**
     * Regression test for bug 3198584.
     *
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void importWithoutClosingSemicolon() throws Exception {
        importWithoutClosingSemicolon(new CSS3Parser());
    }

    private void importWithoutClosingSemicolon(final AbstractCSSParser p) throws Exception {
        final Reader r = new StringReader("@import url('a.css'); @import url('c.css')");
        final InputSource source = new InputSource(r);
        final CSSOMParser parser = new CSSOMParser();
        final CSSStyleSheetImpl ss = parser.parseStyleSheet(source, null);

        // second rule is not detected, because the closing semicolon is missed
        assertEquals(1, ss.getCssRules().getLength());
    }

    /**
     * Regression test for bug 1226128.
     *
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#escaped-characters">CSS Spec</a>
     * @throws Exception
     *             if an error occurs
     */
    @Test
    public void escapedChars() throws Exception {
        escapedChars(new CSS3Parser());
    }

    private void escapedChars(final AbstractCSSParser p) throws Exception {
        assertEquals("bogus0: \"abc\"", getCssTextFromDeclaration(p, "bogus0: 'a\\\rbc'"));
        assertEquals("bogus1: \"abc\"", getCssTextFromDeclaration(p, "bogus1: 'a\\\nbc'"));
        assertEquals("bogus2: \"abc\"", getCssTextFromDeclaration(p, "bogus2: 'a\\\fbc'"));
        assertEquals("bogus3: \"abc\"", getCssTextFromDeclaration(p, "bogus3: 'abc\\\r\n'"));
        assertEquals("bogus4: \"abc\"", getCssTextFromDeclaration(p, "bogus4: 'a\\\r\nbc'"));
        assertEquals("bogus5: \"abx\"", getCssTextFromDeclaration(p, "bogus5: '\\61\\62x'"));
        assertEquals("bogus6: \"abc\"", getCssTextFromDeclaration(p, "bogus6: '\\61\\62\\63'"));
        assertEquals("bogus7: \"abx\"", getCssTextFromDeclaration(p, "bogus7: '\\61 \\62x'"));
        assertEquals("bogus8: \"abx\"", getCssTextFromDeclaration(p, "bogus8: '\\61\t\\62x'"));
        assertEquals("bogus9: \"abx\"", getCssTextFromDeclaration(p, "bogus9: '\\61\n\\62x'"));
        assertEquals("bogus10: \"a'bc\"", getCssTextFromDeclaration(p, "bogus10: 'a\\'bc'"));
        assertEquals("bogus11: \"a'bc\"", getCssTextFromDeclaration(p, "bogus11: \"a\\'bc\""));
        assertEquals("bogus12: \"a\\\"bc\"", getCssTextFromDeclaration(p, "bogus12: 'a\\\"bc'"));

        // regression for 2891851
        // double backslashes are needed
        // see http://www.developershome.com/wap/wcss/wcss_tutorial.asp?page=inputExtension2
        assertEquals("bogus13: \"NNNNN\\-NNNN\"", getCssTextFromDeclaration(p, "bogus13: 'NNNNN\\\\-NNNN'"));
        assertEquals("bogus14: \"NNNNN\\-NNNN\"", getCssTextFromDeclaration(p, "bogus14: \"NNNNN\\\\-NNNN\""));

        assertEquals("bogus15: \"\u00a4\"", getCssTextFromDeclaration(p, "bogus15: '\\a4'"));
        assertEquals("bogus16: \"\\A 4\"", getCssTextFromDeclaration(p, "bogus16: '\\a 4'"));
        assertEquals("bogus17: \"\\A o\"", getCssTextFromDeclaration(p, "bogus17: '\\ao'"));
        assertEquals("bogus18: \"\\A o\"", getCssTextFromDeclaration(p, "bogus18: '\\a o'"));
        assertEquals("bogus19: \"\\A  o\"", getCssTextFromDeclaration(p, "bogus19: '\\A  o'"));

        assertEquals("bogus20: \"\u00d4\"", getCssTextFromDeclaration(p, "bogus20: '\\d4'"));
        assertEquals("bogus21: \"\\D 4\"", getCssTextFromDeclaration(p, "bogus21: '\\d 4'"));
        assertEquals("bogus22: \"\\D o\"", getCssTextFromDeclaration(p, "bogus22: '\\do'"));
        assertEquals("bogus23: \"\\D o\"", getCssTextFromDeclaration(p, "bogus23: '\\d o'"));
        assertEquals("bogus24: \"\\D  o\"", getCssTextFromDeclaration(p, "bogus24: '\\D  o'"));
    }

    private String getCssTextFromDeclaration(final AbstractCSSParser p, final String s) throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final CSSStyleDeclarationImpl d = parser.parseStyleDeclaration(s);
        return d.getCssText();
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parsePageDeclaration() throws Exception {
        final Reader r = new StringReader("@page :pageStyle { size: 21.0cm 29.7cm; }");
        final InputSource is = new InputSource(r);
        final CSSStyleSheetImpl ss = new CSSOMParser().parseStyleSheet(is, null);

        assertEquals("@page :pageStyle { size: 21cm 29.7cm; }", ss.toString().trim());

        final CSSRuleListImpl rules = ss.getCssRules();
        assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);

        assertEquals("@page :pageStyle { size: 21cm 29.7cm; }", rule.getCssText());

        final CSSPageRuleImpl pageRule = (CSSPageRuleImpl) rule;
        assertEquals(":pageStyle", pageRule.getSelectorText());
        assertEquals("size: 21cm 29.7cm", pageRule.getStyle().getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parsePageDeclaration2() throws Exception {
        final Reader r = new StringReader("@page { size: 21.0cm 29.7cm; }");
        final InputSource is = new InputSource(r);
        final CSSStyleSheetImpl ss = new CSSOMParser().parseStyleSheet(is, null);

        assertEquals("@page { size: 21cm 29.7cm; }", ss.toString().trim());

        final CSSRuleListImpl rules = ss.getCssRules();
        assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);

        assertEquals("@page { size: 21cm 29.7cm; }", rule.getCssText());

        final CSSPageRuleImpl pageRule = (CSSPageRuleImpl) rule;
        assertEquals("", pageRule.getSelectorText());
        assertEquals("size: 21cm 29.7cm", pageRule.getStyle().getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void overwriteProperties() throws Exception {
        final Reader r = new StringReader(
                "p {"
                + "background: rgb(0, 0, 0);"
                + "background-repeat: repeat-y;"
                + "background: url(img/test.png) no-repeat;"
                + "background-size: 190px 48px;"
                + "}");
        final InputSource is = new InputSource(r);
        final CSSStyleSheetImpl sheet = new CSSOMParser().parseStyleSheet(is, null);

        assertEquals("p { background: rgb(0, 0, 0); "
                + "background-repeat: repeat-y; "
                + "background: url(\"img/test.png\") no-repeat; "
                + "background-size: 190px 48px; }",
                sheet.toString().trim());

        final CSSRuleListImpl rules = sheet.getCssRules();
        assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        final CSSStyleRuleImpl ruleImpl = (CSSStyleRuleImpl) rule;
        final CSSStyleDeclarationImpl declImpl = ruleImpl.getStyle();

        assertEquals(4, declImpl.getLength());

        assertEquals("background", declImpl.getProperties().get(0).getName());
        assertEquals("url(\"img/test.png\") no-repeat", declImpl.getPropertyCSSValue("background").getCssText());

        assertEquals("background-repeat", declImpl.getProperties().get(1).getName());
        assertEquals("repeat-y", declImpl.getPropertyCSSValue("background-repeat").getCssText());

        assertEquals("background", declImpl.getProperties().get(2).getName());
        assertEquals("url(\"img/test.png\") no-repeat", declImpl.getPropertyCSSValue("background").getCssText());

        assertEquals("background-size", declImpl.getProperties().get(3).getName());
        assertEquals("190px 48px", declImpl.getPropertyCSSValue("background-size").getCssText());

        // now check the core results
        assertEquals(4, declImpl.getProperties().size());

        Property prop = declImpl.getProperties().get(0);
        assertEquals("background", prop.getName());
        assertEquals("rgb(0, 0, 0)", prop.getValue().getCssText());

        prop = declImpl.getProperties().get(1);
        assertEquals("background-repeat", prop.getName());
        assertEquals("repeat-y", prop.getValue().getCssText());

        prop = declImpl.getProperties().get(2);
        assertEquals("background", prop.getName());
        assertEquals("url(\"img/test.png\") no-repeat", prop.getValue().getCssText());

        prop = declImpl.getProperties().get(3);
        assertEquals("background-size", prop.getName());
        assertEquals("190px 48px", prop.getValue().getCssText());
    }
}
