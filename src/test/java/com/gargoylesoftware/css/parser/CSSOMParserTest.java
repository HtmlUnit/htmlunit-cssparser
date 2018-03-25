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

import java.io.Reader;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.css.CSSPageRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSValue;

import com.gargoylesoftware.css.dom.CSSStyleDeclarationImpl;
import com.gargoylesoftware.css.dom.CSSStyleRuleImpl;
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
    private String testParseRule_ = "p { " + testStyleDeclaration_ + " }";
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
        Assert.assertNotNull(parser);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void defineParserInstance() throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        Assert.assertNotNull(parser);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseStyleSheet() throws Exception {
        final Reader r = new StringReader(testString_);
        final InputSource is = new InputSource(r);
        final CSSStyleSheet ss = new CSSOMParser().parseStyleSheet(is, null);

        final CSSRuleList rl = ss.getCssRules();
        final CSSRule rule = rl.item(0);

        Assert.assertEquals(CSSRule.STYLE_RULE, rule.getType());

        final CSSStyleRule sr = (CSSStyleRule) rule;
        Assert.assertEquals(testSelector_, sr.getSelectorText());

        final CSSStyleDeclaration style = sr.getStyle();
        Assert.assertEquals(testItem_, style.item(0));

        final CSSValue value = style.getPropertyCSSValue(style.item(0));
        Assert.assertEquals(testValue_, value.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseStyleSheetIgnoreProblemCss3() throws Exception {
        final String test = "p{filter:alpha(opacity=33.3);opacity:0.333}a{color:#123456;}";

        final Reader r = new StringReader(test);
        final InputSource is = new InputSource(r);
        final CSSStyleSheet ss = new CSSOMParser().parseStyleSheet(is, null);
        final CSSRuleList rl = ss.getCssRules();
        Assert.assertEquals(2, rl.getLength());

        CSSRule rule = rl.item(0);
        CSSStyleRule sr = (CSSStyleRule) rule;
        Assert.assertEquals("p { filter: alpha(opacity = 33.3); opacity: 0.333 }", sr.getCssText());

        rule = rl.item(1);
        sr = (CSSStyleRule) rule;
        Assert.assertEquals("a { color: rgb(18, 52, 86) }", sr.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseSelectors() throws Exception {
        final Reader r = new StringReader(testSelector_);
        final InputSource is = new InputSource(r);
        final SelectorList sl = new CSSOMParser().parseSelectors(is);

        Assert.assertEquals(testSelector_, sl.get(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseSelectorsEscapedChars() throws Exception {
        final Reader r = new StringReader("#id\\:withColon");
        final InputSource is = new InputSource(r);
        final SelectorList sl = new CSSOMParser().parseSelectors(is);

        Assert.assertEquals("*#id:withColon", sl.get(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseSelectorsParseException() throws Exception {
        final Reader r = new StringReader("table==td");
        final InputSource is = new InputSource(r);
        final SelectorList sl = new CSSOMParser().parseSelectors(is);

        Assert.assertNull(sl);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parsePropertyValue() throws Exception {
        final Reader r = new StringReader(testPropertyValue_);
        final InputSource is = new InputSource(r);
        final CSSValue pv = new CSSOMParser().parsePropertyValue(is);

        Assert.assertEquals(testPropertyValue_, pv.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parsePropertyValueParseException() throws Exception {
        final Reader r = new StringReader("@a");
        final InputSource is = new InputSource(r);
        final CSSValue pv = new CSSOMParser().parsePropertyValue(is);

        Assert.assertNull(pv);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseMedia() throws Exception {
        final Reader r = new StringReader(testParseMedia_);
        final InputSource is = new InputSource(r);
        final MediaQueryList ml = new CSSOMParser().parseMedia(is);

        Assert.assertEquals(2, ml.getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseMediaParseException() throws Exception {
        final Reader r = new StringReader("~xx");
        final InputSource is = new InputSource(r);
        final MediaQueryList ml = new CSSOMParser().parseMedia(is);

        Assert.assertEquals(0, ml.getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseRule() throws Exception {
        final Reader r = new StringReader(testParseRule_);
        final InputSource is = new InputSource(r);
        final CSSRule rule = new CSSOMParser().parseRule(is);

        Assert.assertEquals(testParseRule_, rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseRuleParseException() throws Exception {
        final Reader r = new StringReader("~xx");
        final InputSource is = new InputSource(r);
        final CSSRule rule = new CSSOMParser().parseRule(is);

        Assert.assertNull(rule);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseStyleDeclaration() throws Exception {
        final Reader r = new StringReader(testStyleDeclaration_);
        final InputSource is = new InputSource(r);
        final CSSStyleDeclaration sd = new CSSOMParser().parseStyleDeclaration(is);

        Assert.assertEquals(testStyleDeclaration_, sd.toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parseStyleDeclarationParseException() throws Exception {
        final Reader r = new StringReader("@abc");
        final InputSource is = new InputSource(r);
        final CSSStyleDeclaration sd = new CSSOMParser().parseStyleDeclaration(is);

        Assert.assertEquals(sd.getLength(), 0);
    }

    /**
     * Regression test for bug 1191376.
     *
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void parseStyleDeclarationWithoutBrace() throws Exception {
        final Reader r = new StringReader("background-color: white");
        final InputSource is = new InputSource(r);
        final CSSStyleDeclaration declaration = new CSSOMParser().parseStyleDeclaration(is);

        Assert.assertEquals(1, declaration.getLength());
    }

    /**
     * Regression test for bug 3293695.
     *
     * @throws Exception
     *             if any error occurs
     */
    @Test
    public void urlGreedy() throws Exception {
        Assert.assertEquals(
            "background: url(images/bottom-angle.png); background-image: url(background.png)",
            getCssTextFromDeclaration(
                    new CSS3Parser(),
                    "background:url('images/bottom-angle.png');background-image:url('background.png');"));
        Assert.assertEquals(
            "background: url(images/bottom-angle.png); background-image: url(background.png)",
            getCssTextFromDeclaration(
                    new CSS3Parser(),
                    "background:url(\"images/bottom-angle.png\");background-image:url(\"background.png\");"));
        Assert.assertEquals(
            "background: rgb(60, 90, 118) url(/images/status_bg.png?2) no-repeat center; "
            + "font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(
                    new CSS3Parser(),
                    "background:#3c5a76 url('/images/status_bg.png?2') no-repeat center;"
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
        Assert.assertEquals(
            "font-family: Arial, \"Helvetica Neue\", Helvetica, sans-serif",
            getCssTextFromDeclaration(new CSS3Parser(),
                    "font-family: Arial,'Helvetica Neue',Helvetica,sans-serif"));
        Assert.assertEquals(
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
        Assert.assertEquals(
            "background: rgb(232, 239, 245) url(images/bottom-angle.png) no-repeat",
            getCssTextFromDeclaration(new CSS3Parser(),
                    "background: #e8eff5 url(images/bottom-angle.png) no-repeat"));
        Assert.assertEquals(
            "background: red url(images/bottom-angle.png) no-repeat",
            getCssTextFromDeclaration(new CSS3Parser(), "background: red url(images/bottom-angle.png) no-repeat"));
        Assert.assertEquals(
            "background: rgb(8, 3, 6) url(images/bottom-angle.png) no-repeat",
            getCssTextFromDeclaration(new CSS3Parser(),
                    "background: rgb(8, 3, 6) url(images/bottom-angle.png) no-repeat"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void speciaChars() throws Exception {
        Assert.assertEquals("content: \"�\"",
                getCssTextFromDeclaration(new CSS3Parser(), "content: '�';"));
        Assert.assertEquals("content: \"\u0122\"",
            getCssTextFromDeclaration(new CSS3Parser(), "content: '\u0122';"));
        Assert.assertEquals("content: \"\u0422\"",
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

    private void doubleDotSelector(final CSSParser p) throws Exception {
        final Reader r = new StringReader("..nieuwsframedatum{ font-size : 8pt;}");
        final InputSource source = new InputSource(r);
        final CSSOMParser parser = new CSSOMParser();
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null);

        Assert.assertEquals(0, ss.getCssRules().getLength());
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

    private void importEOF(final CSSParser p) throws Exception {
        final Reader r = new StringReader("@import http://www.wetator.org");
        final InputSource source = new InputSource(r);
        final CSSOMParser parser = new CSSOMParser();
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null);

        Assert.assertEquals(0, ss.getCssRules().getLength());
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

    private void importWithoutClosingSemicolon(final CSSParser p) throws Exception {
        final Reader r = new StringReader("@import url('a.css'); @import url('c.css')");
        final InputSource source = new InputSource(r);
        final CSSOMParser parser = new CSSOMParser();
        final CSSStyleSheet ss = parser.parseStyleSheet(source, null);

        // second rule is not detected, because the closing semicolon is missed
        Assert.assertEquals(1, ss.getCssRules().getLength());
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

    private void escapedChars(final CSSParser p) throws Exception {
        Assert.assertEquals("bogus0: \"abc\"", getCssTextFromDeclaration(p, "bogus0: 'a\\\rbc'"));
        Assert.assertEquals("bogus1: \"abc\"", getCssTextFromDeclaration(p, "bogus1: 'a\\\nbc'"));
        Assert.assertEquals("bogus2: \"abc\"", getCssTextFromDeclaration(p, "bogus2: 'a\\\fbc'"));
        Assert.assertEquals("bogus3: \"abc\"", getCssTextFromDeclaration(p, "bogus3: 'abc\\\r\n'"));
        Assert.assertEquals("bogus4: \"abc\"", getCssTextFromDeclaration(p, "bogus4: 'a\\\r\nbc'"));
        Assert.assertEquals("bogus5: \"abx\"", getCssTextFromDeclaration(p, "bogus5: '\\61\\62x'"));
        Assert.assertEquals("bogus6: \"abc\"", getCssTextFromDeclaration(p, "bogus6: '\\61\\62\\63'"));
        Assert.assertEquals("bogus7: \"abx\"", getCssTextFromDeclaration(p, "bogus7: '\\61 \\62x'"));
        Assert.assertEquals("bogus8: \"abx\"", getCssTextFromDeclaration(p, "bogus8: '\\61\t\\62x'"));
        Assert.assertEquals("bogus9: \"abx\"", getCssTextFromDeclaration(p, "bogus9: '\\61\n\\62x'"));
        Assert.assertEquals("bogus10: \"a'bc\"", getCssTextFromDeclaration(p, "bogus10: 'a\\'bc'"));
        Assert.assertEquals("bogus11: \"a'bc\"", getCssTextFromDeclaration(p, "bogus11: \"a\\'bc\""));
        Assert.assertEquals("bogus12: \"a\\\"bc\"", getCssTextFromDeclaration(p, "bogus12: 'a\\\"bc'"));

        // regression for 2891851
        // double backslashes are needed
        // see http://www.developershome.com/wap/wcss/wcss_tutorial.asp?page=inputExtension2
        Assert.assertEquals("bogus13: \"NNNNN\\-NNNN\"", getCssTextFromDeclaration(p, "bogus13: 'NNNNN\\\\-NNNN'"));
        Assert.assertEquals("bogus14: \"NNNNN\\-NNNN\"", getCssTextFromDeclaration(p, "bogus14: \"NNNNN\\\\-NNNN\""));

        Assert.assertEquals("bogus15: \"\u00a4\"", getCssTextFromDeclaration(p, "bogus15: '\\a4'"));
        Assert.assertEquals("bogus16: \"\\A 4\"", getCssTextFromDeclaration(p, "bogus16: '\\a 4'"));
        Assert.assertEquals("bogus17: \"\\A o\"", getCssTextFromDeclaration(p, "bogus17: '\\ao'"));
        Assert.assertEquals("bogus18: \"\\A o\"", getCssTextFromDeclaration(p, "bogus18: '\\a o'"));
        Assert.assertEquals("bogus19: \"\\A  o\"", getCssTextFromDeclaration(p, "bogus19: '\\A  o'"));

        Assert.assertEquals("bogus20: \"\u00d4\"", getCssTextFromDeclaration(p, "bogus20: '\\d4'"));
        Assert.assertEquals("bogus21: \"\\D 4\"", getCssTextFromDeclaration(p, "bogus21: '\\d 4'"));
        Assert.assertEquals("bogus22: \"\\D o\"", getCssTextFromDeclaration(p, "bogus22: '\\do'"));
        Assert.assertEquals("bogus23: \"\\D o\"", getCssTextFromDeclaration(p, "bogus23: '\\d o'"));
        Assert.assertEquals("bogus24: \"\\D  o\"", getCssTextFromDeclaration(p, "bogus24: '\\D  o'"));
    }

    private String getCssTextFromDeclaration(final CSSParser p, final String s) throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final Reader r = new StringReader(s);
        final InputSource is = new InputSource(r);
        final CSSStyleDeclaration d = parser.parseStyleDeclaration(is);
        return d.getCssText();
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parsePageDeclaration() throws Exception {
        final Reader r = new StringReader("@page :pageStyle { size: 21.0cm 29.7cm; }");
        final InputSource is = new InputSource(r);
        final CSSStyleSheet ss = new CSSOMParser().parseStyleSheet(is, null);

        Assert.assertEquals("@page :pageStyle {size: 21cm 29.7cm}", ss.toString().trim());

        final CSSRuleList rules = ss.getCssRules();
        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals(CSSRule.PAGE_RULE, rule.getType());

        Assert.assertEquals("@page :pageStyle {size: 21cm 29.7cm}", rule.getCssText());

        final CSSPageRule pageRule = (CSSPageRule) rule;
        Assert.assertEquals(":pageStyle", pageRule.getSelectorText());
        Assert.assertEquals("size: 21cm 29.7cm", pageRule.getStyle().getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void parsePageDeclaration2() throws Exception {
        final Reader r = new StringReader("@page { size: 21.0cm 29.7cm; }");
        final InputSource is = new InputSource(r);
        final CSSStyleSheet ss = new CSSOMParser().parseStyleSheet(is, null);

        Assert.assertEquals("@page {size: 21cm 29.7cm}", ss.toString().trim());

        final CSSRuleList rules = ss.getCssRules();
        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        Assert.assertEquals(CSSRule.PAGE_RULE, rule.getType());

        Assert.assertEquals("@page {size: 21cm 29.7cm}", rule.getCssText());

        final CSSPageRule pageRule = (CSSPageRule) rule;
        Assert.assertEquals("", pageRule.getSelectorText());
        Assert.assertEquals("size: 21cm 29.7cm", pageRule.getStyle().getCssText());
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
        final CSSStyleSheet sheet = new CSSOMParser().parseStyleSheet(is, null);

        Assert.assertEquals("p { background: rgb(0, 0, 0); "
                + "background-repeat: repeat-y; "
                + "background: url(img/test.png) no-repeat; "
                + "background-size: 190px 48px }",
                sheet.toString().trim());

        final CSSRuleList rules = sheet.getCssRules();
        Assert.assertEquals(1, rules.getLength());

        final CSSRule rule = rules.item(0);
        final CSSStyleRuleImpl ruleImpl = (CSSStyleRuleImpl) rule;
        final CSSStyleDeclarationImpl declImpl = (CSSStyleDeclarationImpl) ruleImpl.getStyle();

        Assert.assertEquals(4, declImpl.getLength());

        Assert.assertEquals("background", declImpl.item(0));
        Assert.assertEquals("url(img/test.png) no-repeat", declImpl.getPropertyCSSValue("background").getCssText());

        Assert.assertEquals("background-repeat", declImpl.item(1));
        Assert.assertEquals("repeat-y", declImpl.getPropertyCSSValue("background-repeat").getCssText());

        Assert.assertEquals("background", declImpl.item(2));
        Assert.assertEquals("url(img/test.png) no-repeat", declImpl.getPropertyCSSValue("background").getCssText());

        Assert.assertEquals("background-size", declImpl.item(3));
        Assert.assertEquals("190px 48px", declImpl.getPropertyCSSValue("background-size").getCssText());

        // now check the core results
        Assert.assertEquals(4, declImpl.getProperties().size());

        Property prop = declImpl.getProperties().get(0);
        Assert.assertEquals("background", prop.getName());
        Assert.assertEquals("rgb(0, 0, 0)", prop.getValue().getCssText());

        prop = declImpl.getProperties().get(1);
        Assert.assertEquals("background-repeat", prop.getName());
        Assert.assertEquals("repeat-y", prop.getValue().getCssText());

        prop = declImpl.getProperties().get(2);
        Assert.assertEquals("background", prop.getName());
        Assert.assertEquals("url(img/test.png) no-repeat", prop.getValue().getCssText());

        prop = declImpl.getProperties().get(3);
        Assert.assertEquals("background-size", prop.getName());
        Assert.assertEquals("190px 48px", prop.getValue().getCssText());
    }
}
