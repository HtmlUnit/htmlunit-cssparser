/*
 * Copyright (c) 2019 Ronald Brill.
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import com.gargoylesoftware.css.ErrorHandler;
import com.gargoylesoftware.css.dom.AbstractCSSRuleImpl;
import com.gargoylesoftware.css.dom.CSSMediaRuleImpl;
import com.gargoylesoftware.css.dom.CSSRuleListImpl;
import com.gargoylesoftware.css.dom.CSSStyleDeclarationImpl;
import com.gargoylesoftware.css.dom.CSSStyleRuleImpl;
import com.gargoylesoftware.css.dom.CSSStyleSheetImpl;
import com.gargoylesoftware.css.dom.CSSValueImpl;
import com.gargoylesoftware.css.dom.CSSValueImpl.CSSPrimitiveValueType;
import com.gargoylesoftware.css.dom.CSSValueImpl.CSSValueType;
import com.gargoylesoftware.css.dom.MediaListImpl;
import com.gargoylesoftware.css.dom.Property;
import com.gargoylesoftware.css.parser.LexicalUnit.LexicalUnitType;
import com.gargoylesoftware.css.parser.condition.Condition;
import com.gargoylesoftware.css.parser.condition.Condition.ConditionType;
import com.gargoylesoftware.css.parser.condition.LangCondition;
import com.gargoylesoftware.css.parser.condition.PrefixAttributeCondition;
import com.gargoylesoftware.css.parser.condition.PseudoClassCondition;
import com.gargoylesoftware.css.parser.condition.SubstringAttributeCondition;
import com.gargoylesoftware.css.parser.condition.SuffixAttributeCondition;
import com.gargoylesoftware.css.parser.javacc.CSS3Parser;
import com.gargoylesoftware.css.parser.media.MediaQuery;
import com.gargoylesoftware.css.parser.selector.ChildSelector;
import com.gargoylesoftware.css.parser.selector.ElementSelector;
import com.gargoylesoftware.css.parser.selector.Selector;
import com.gargoylesoftware.css.parser.selector.Selector.SelectorType;
import com.gargoylesoftware.css.parser.selector.SelectorList;

/**
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class CSS3ParserTest  extends AbstractCSSParserTest {

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void selectorList() throws Exception {
        selectorList("h1:first-line", 1);
        selectorList("h1", 1);
        selectorList("h1, h2", 2);
        selectorList("h1:first-line, h2", 2);
        selectorList("h1, h2, h3", 3);
        selectorList("h1, h2,\nh3", 3);
        selectorList("h1, h2, h3#id", 3);
        selectorList("h1.class, h2, h3", 3);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void selector() throws Exception {
        selectorType("a#id.class:link", SelectorType.ELEMENT_NODE_SELECTOR);
        selectorType("a#id.class", SelectorType.ELEMENT_NODE_SELECTOR);
        selectorType("a#id:link", SelectorType.ELEMENT_NODE_SELECTOR);
        selectorType("a#id", SelectorType.ELEMENT_NODE_SELECTOR);
        selectorType("a.class:link", SelectorType.ELEMENT_NODE_SELECTOR);
        selectorType("a.class", SelectorType.ELEMENT_NODE_SELECTOR);
        selectorType("a:link", SelectorType.ELEMENT_NODE_SELECTOR);
        selectorType("a", SelectorType.ELEMENT_NODE_SELECTOR);
        selectorType("#id.class:link", SelectorType.ELEMENT_NODE_SELECTOR);
        selectorType("#id.class", SelectorType.ELEMENT_NODE_SELECTOR);
        selectorType("#id:link", SelectorType.ELEMENT_NODE_SELECTOR);
        selectorType("#id", SelectorType.ELEMENT_NODE_SELECTOR);
        selectorType(".class:link", SelectorType.ELEMENT_NODE_SELECTOR);
        selectorType(".class", SelectorType.ELEMENT_NODE_SELECTOR);
        selectorType(":link", SelectorType.ELEMENT_NODE_SELECTOR);
        selectorType("a:visited", SelectorType.ELEMENT_NODE_SELECTOR);
        selectorType("a:active", SelectorType.ELEMENT_NODE_SELECTOR);

        selectorType("h1 a", SelectorType.DESCENDANT_SELECTOR, SelectorType.ELEMENT_NODE_SELECTOR,
                SelectorType.ELEMENT_NODE_SELECTOR);
        selectorType("h1  a", SelectorType.DESCENDANT_SELECTOR, SelectorType.ELEMENT_NODE_SELECTOR,
                SelectorType.ELEMENT_NODE_SELECTOR);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void selectorPseudo() throws Exception {
        selectorType("h1:first-line", SelectorType.DESCENDANT_SELECTOR, SelectorType.ELEMENT_NODE_SELECTOR,
                SelectorType.PSEUDO_ELEMENT_SELECTOR);
        selectorType("a:first-letter", SelectorType.DESCENDANT_SELECTOR, SelectorType.ELEMENT_NODE_SELECTOR,
                SelectorType.PSEUDO_ELEMENT_SELECTOR);
        selectorType("a:before", SelectorType.DESCENDANT_SELECTOR, SelectorType.ELEMENT_NODE_SELECTOR,
                SelectorType.PSEUDO_ELEMENT_SELECTOR);
        selectorType("a:after", SelectorType.DESCENDANT_SELECTOR, SelectorType.ELEMENT_NODE_SELECTOR,
                SelectorType.PSEUDO_ELEMENT_SELECTOR);

        selectorType("h1:lang(en)", SelectorType.ELEMENT_NODE_SELECTOR, SelectorType.PSEUDO_ELEMENT_SELECTOR);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void selectorPseudoDoubleColon() throws Exception {
        selectorType("h1::first-line", SelectorType.DESCENDANT_SELECTOR, SelectorType.ELEMENT_NODE_SELECTOR,
                SelectorType.PSEUDO_ELEMENT_SELECTOR);
        selectorType("a::first-letter", SelectorType.DESCENDANT_SELECTOR, SelectorType.ELEMENT_NODE_SELECTOR,
                SelectorType.PSEUDO_ELEMENT_SELECTOR);
        selectorType("a::before", SelectorType.DESCENDANT_SELECTOR, SelectorType.ELEMENT_NODE_SELECTOR,
                SelectorType.PSEUDO_ELEMENT_SELECTOR);
        selectorType("a::after", SelectorType.DESCENDANT_SELECTOR, SelectorType.ELEMENT_NODE_SELECTOR,
                SelectorType.PSEUDO_ELEMENT_SELECTOR);

        selectorType("h1::lang(en)", SelectorType.ELEMENT_NODE_SELECTOR, SelectorType.PSEUDO_ELEMENT_SELECTOR);
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/selector.html#lang">http://www.w3.org/TR/CSS21/selector.html#lang</a>
     * @throws Exception if any error occurs
     */
    @Test
    public void selectorLang() throws Exception {
        final String css = "html:lang(fr-ca) { }\n"
                + "html:lang(de) { }\n"
                + ":lang(fr) > Q { }\n"
                + ":lang(de) > Q { }";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(4, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("html:lang(fr-ca) { }", rule.getCssText());
        ElementSelector selector = (ElementSelector) ((CSSStyleRuleImpl) rule).getSelectors().get(0);
        Assert.assertEquals(ConditionType.LANG_CONDITION, selector.getConditions().get(0).getConditionType());
        Assert.assertEquals("fr-ca", ((LangCondition) selector.getConditions().get(0)).getValue());

        rule = rules.getRules().get(1);
        Assert.assertEquals("html:lang(de) { }", rule.getCssText());
        selector = (ElementSelector) ((CSSStyleRuleImpl) rule).getSelectors().get(0);
        Assert.assertEquals(ConditionType.LANG_CONDITION, selector.getConditions().get(0).getConditionType());
        Assert.assertEquals("de", ((LangCondition) selector.getConditions().get(0)).getValue());

        rule = rules.getRules().get(2);
        Assert.assertEquals("*:lang(fr) > Q { }", rule.getCssText());
        ChildSelector childSelector = (ChildSelector) ((CSSStyleRuleImpl) rule).getSelectors().get(0);
        selector = (ElementSelector) childSelector.getAncestorSelector();
        Assert.assertEquals(ConditionType.LANG_CONDITION, selector.getConditions().get(0).getConditionType());
        Assert.assertEquals("fr", ((LangCondition) selector.getConditions().get(0)).getValue());

        rule = rules.getRules().get(3);
        Assert.assertEquals("*:lang(de) > Q { }", rule.getCssText());
        childSelector = (ChildSelector) ((CSSStyleRuleImpl) rule).getSelectors().get(0);
        selector = (ElementSelector) childSelector.getAncestorSelector();
        Assert.assertEquals(ConditionType.LANG_CONDITION, selector.getConditions().get(0).getConditionType());
        Assert.assertEquals("de", ((LangCondition) selector.getConditions().get(0)).getValue());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/selector.html#lang">http://www.w3.org/TR/CSS21/selector.html#lang</a>
     * @throws Exception if any error occurs
     */
    @Test
    public void selectorLangInvalid() throws Exception {
        final String css = "html:lang() { background: red }\n"
                    + "p { color:green; }";

        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());
        Assert.assertEquals("p { color: green }", rules.toString().trim());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void condition() throws Exception {
        conditionType("a#id.class:link", ConditionType.ID_CONDITION, ConditionType.CLASS_CONDITION,
                ConditionType.PSEUDO_CLASS_CONDITION);
        conditionType("a#id.class", ConditionType.ID_CONDITION, ConditionType.CLASS_CONDITION);
        conditionType("a#id:link", ConditionType.ID_CONDITION, ConditionType.PSEUDO_CLASS_CONDITION);
        conditionType("a#id", ConditionType.ID_CONDITION);
        conditionType("a.class:link", ConditionType.CLASS_CONDITION, ConditionType.PSEUDO_CLASS_CONDITION);
        conditionType("a.class", ConditionType.CLASS_CONDITION);
        conditionType("a:link", ConditionType.PSEUDO_CLASS_CONDITION);
        conditionType("#id.class:link", ConditionType.ID_CONDITION, ConditionType.CLASS_CONDITION,
                ConditionType.PSEUDO_CLASS_CONDITION);
        conditionType("#id.class", ConditionType.ID_CONDITION, ConditionType.CLASS_CONDITION);
        conditionType("#id:link", ConditionType.ID_CONDITION, ConditionType.PSEUDO_CLASS_CONDITION);
        conditionType("#id", ConditionType.ID_CONDITION);
        conditionType(".class:link", ConditionType.CLASS_CONDITION, ConditionType.PSEUDO_CLASS_CONDITION);
        conditionType(".class", ConditionType.CLASS_CONDITION);
        conditionType(":link", ConditionType.PSEUDO_CLASS_CONDITION);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void classCondition() throws Exception {
        conditionAssert(".class", null, "class", true);
        conditionAssert("h1.class", null, "class", true);
        Assert.assertNull(createSelectors("."));
        Assert.assertNull(createSelectors("h1."));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void idCondition() throws Exception {
        conditionAssert("#id", null, "id", true);
        conditionAssert("h1#id", null, "id", true);
        Assert.assertNull(createSelectors("#"));
        Assert.assertNull(createSelectors("h1.#"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void pseudoCondition() throws Exception {
        conditionAssert(":link", null, "link", true);
        conditionAssert("a:link", null, "link", true);
        conditionAssert("a:visited", null, "visited", true);
        conditionAssert(":visited", null, "visited", true);
        conditionAssert("a:active", null, "active", true);
        conditionAssert(":active", null, "active", true);
        Assert.assertNull(createSelectors(":"));
        Assert.assertNull(createSelectors("a:"));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void attributeCondition() throws Exception {
        conditionAssert("[rel]", "rel", null, false);
        conditionAssert("[ rel ]", "rel", null, false);

        conditionAssert("[rel=val]", "rel", "val", true);
        conditionAssert("[ rel = val ]", "rel", "val", true);
        Assert.assertNull(createSelectors("[rel=]")); // invalid rule

        conditionAssert("[rel~=val]", "rel", "val", true);
        conditionAssert("[ rel ~= val ]", "rel", "val", true);
        Assert.assertNull(createSelectors("[rel~=]")); // invalid rule

        conditionAssert("[rel|=val]", "rel", "val", true);
        conditionAssert("[ rel |= val]", "rel", "val", true);
        Assert.assertNull(createSelectors("[rel|=]")); // invalid rule

        conditionAssert("[rel^=val]", "rel", "val", true);
        conditionAssert("[ rel ^= val]", "rel", "val", true);
        Assert.assertNull(createSelectors("[rel^=]")); // invalid rule

        conditionAssert("[rel$=val]", "rel", "val", true);
        conditionAssert("[ rel $= val]", "rel", "val", true);
        Assert.assertNull(createSelectors("[rel$=]")); // invalid rule

        conditionAssert("[rel*=val]", "rel", "val", true);
        conditionAssert("[ rel *= val]", "rel", "val", true);
        Assert.assertNull(createSelectors("[rel*=]")); // invalid rule
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dojoCSS() throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("dojo.css");
        Assert.assertNotNull(is);

        final CSSStyleSheetImpl sheet = parse(is);
        Assert.assertEquals(17, sheet.getCssRules().getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyCSS() throws Exception {
        final CSSStyleSheetImpl sheet = parse("");
        Assert.assertEquals(0, sheet.getCssRules().getLength());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void whitespaceOnlyCSS() throws Exception {
        final CSSStyleSheetImpl sheet = parse("  \t \r\n \n");
        Assert.assertEquals(0, sheet.getCssRules().getLength());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void charset() throws Exception {
        final String css = "@charset 'UTF-8';\n"
            + "h1 { color: blue }\n";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(2, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@charset \"UTF-8\";", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void charsetWhitespaceBefore() throws Exception {
        final String css = "/* comment */ \n @charset 'UTF-8';\n"
            + "h1 { color: blue }\n";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(2, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@charset \"UTF-8\";", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void charsetWhitespaceAfter() throws Exception {
        final String css = "@charset 'UTF-8';\n"
            + " \t \n "
            + "h1 { color: blue }\n";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(2, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@charset \"UTF-8\";", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void charsetImportBefore() throws Exception {
        final String css = "@import 'subs.css';\n"
                + "@charset 'UTF-8';\n"
                + "h1 { color: blue }\n"
                + "h2 { color: red }\n";

        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());

        rule = rules.getRules().get(2);
        Assert.assertEquals("h2 { color: red }", rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void charsetImportBeforeSkipComment() throws Exception {
        final String css = "@import 'subs.css';\n"
                + "@charset 'UTF-8';\n"
                + "/* comment */\n"
                + "h1 { color: blue }\n"
                + "h2 { color: red }\n";

        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());

        rule = rules.getRules().get(2);
        Assert.assertEquals("h2 { color: red }", rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void charsetImportBeforeBrokenCharset() throws Exception {
        final String css = "@import 'subs.css';\n"
                + "@charset\n"
                + "h1 { color: blue }\n"
                + "h2 { color: red }\n";

        final CSSStyleSheetImpl sheet = parse(css, 2, 0, 2);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(2, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("h2 { color: red }", rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void importRuleOnly() throws Exception {
        final String css = "@import 'subs.css';";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void importRulesOnly() throws Exception {
        final String css = "@import 'subs.css'; @import 'subs1.css'; @import 'subs2.css';";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("@import url(subs1.css);", rule.getCssText());

        rule = rules.getRules().get(2);
        Assert.assertEquals("@import url(subs2.css);", rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void mediaRulePrint() throws Exception {
        final String css = "@media print { h1 { color: red } }";
        final CSSStyleSheetImpl sheet = parse(css);

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());
        final AbstractCSSRuleImpl cssRule = rules.getRules().get(0);
        Assert.assertEquals("@media print {h1 { color: red } }", cssRule.getCssText());

        final MediaListImpl mediaList = ((CSSMediaRuleImpl) cssRule).getMediaList();

        Assert.assertEquals(1, mediaList.getLength());
        Assert.assertEquals("print", mediaList.getMediaText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void mediaRulePrintAndScreen() throws Exception {
        final String css = "@media print,screen { h1 { color: red } }";
        final CSSStyleSheetImpl sheet = parse(css);

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());
        final AbstractCSSRuleImpl cssRule = rules.getRules().get(0);
        Assert.assertEquals("@media print, screen {h1 { color: red } }", cssRule.getCssText());

        final MediaListImpl mediaList = ((CSSMediaRuleImpl) cssRule).getMediaList();

        Assert.assertEquals(2, mediaList.getLength());
        Assert.assertEquals("print, screen", mediaList.getMediaText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void nestedMediaRule() throws Exception {
        final String css = "@media print { #navigation { display: none }"
                                + "  @media (max-width: 12cm) { .note { float: none } }"
                                + "}";
        final CSSStyleSheetImpl sheet = parse(css);

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());
        AbstractCSSRuleImpl cssRule = rules.getRules().get(0);
        Assert.assertEquals("@media print {*#navigation { display: none } "
                + "@media all and (max-width: 12cm) {*.note { float: none } } }", cssRule.getCssText());

        MediaListImpl mediaList = ((CSSMediaRuleImpl) cssRule).getMediaList();
        Assert.assertEquals(1, mediaList.getLength());
        Assert.assertEquals("print", mediaList.getMediaText());

        final CSSRuleListImpl innerRules = ((CSSMediaRuleImpl) cssRule).getCssRules();
        Assert.assertEquals(2, innerRules.getLength());

        cssRule = innerRules.getRules().get(0);
        Assert.assertEquals("*#navigation { display: none }", cssRule.getCssText());

        cssRule = innerRules.getRules().get(1);
        Assert.assertEquals("@media all and (max-width: 12cm) {*.note { float: none } }", cssRule.getCssText());

        mediaList = ((CSSMediaRuleImpl) cssRule).getMediaList();
        Assert.assertEquals(1, mediaList.getLength());
        Assert.assertEquals("all and (max-width: 12cm)", mediaList.getMediaText());
    }

    /**
     * @see <a href="http://dev.w3.org/csswg/css3-fonts/#font-face-rule">
     *          http://dev.w3.org/csswg/css3-fonts/#font-face-rule</a>
     * @throws Exception if the test fails
     */
    @Test
    public void atRuleFontFace() throws Exception {
        final String css = "@font-face { font-family: Gentium; src: url(http://example.com/fonts/Gentium.ttf); }\n";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@font-face {font-family: Gentium; src: url(http://example.com/fonts/Gentium.ttf)}",
                rule.getCssText());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void atRuleFontFaceUnicodeRangeSingleCodepoint() throws Exception {
        final String css = "@font-face { font-family: Gentium; unicode-range: U+26 }\n";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@font-face {font-family: Gentium; unicode-range: U+26}",
                rule.getCssText());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void atRuleFontFaceUnicodeRangeWildcard() throws Exception {
        final String css = "@font-face { font-family: Gentium; unicode-range: U+4? }\n";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@font-face {font-family: Gentium; unicode-range: U+4?}",
                rule.getCssText());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void atRuleFontFaceUnicodeRangeRange() throws Exception {
        final String css = "@font-face { font-family: Gentium; unicode-range: U+0-7F }\n";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@font-face {font-family: Gentium; unicode-range: U+0-7F}",
                rule.getCssText());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void atRuleFontFaceUnicodeRangeRange2() throws Exception {
        final String css = "@font-face { font-family: Gentium; unicode-range: u+0025-00FF }\n";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@font-face {font-family: Gentium; unicode-range: U+0025-00FF}",
                rule.getCssText());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void atRuleFontFaceUnicodeRangeMultipleValues() throws Exception {
        final String css = "@font-face { font-family: Gentium; unicode-range: U+0025-0??F, U+4?? }\n";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@font-face {font-family: Gentium; unicode-range: U+0025-0??F, U+4??}",
                rule.getCssText());
    }

    /**
     * @see <a href="http://dev.w3.org/csswg/css3-fonts/#font-face-rule">
     *          http://dev.w3.org/csswg/css3-fonts/#font-face-rule</a>
     * @throws Exception if the test fails
     */
    @Test
    public void atRuleFontFaceComplex() throws Exception {
        final String css = "@font-face {\n"
                + "font-family: Headline;\n"
                + "src: local(Futura-Medium), url(fonts.svg#MyGeometricModern) format(\"svg\");}";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@font-face {font-family: Headline; "
                + "src: local(Futura-Medium), url(fonts.svg#MyGeometricModern) format(\"svg\")}",
                rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void atRuleFontFaceComplex2() throws Exception {
        final String css = "@font-face {\n"
                + "font-family: Pangolin;\n"
                + "font-style: normal;\n"
                + "font-weight: 400;\n"
                + "src: local('Indie Flower'), local('IndieFlower'), "
                    + "url(https://fonts.gstatic.com/s/indieflower/v9"
                    + "/10JVD_humAd5zP2yrFqw6ugdm0LZdjqr5-oayXSOefg.woff2) format('woff2');\n"
                + "unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02C6, "
                    + "U+02DA, U+02DC, U+2000-206F, U+2074, U+20AC, U+2212, U+2215;"
                + "}";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@font-face {font-family: Pangolin; "
                + "font-style: normal; font-weight: 400; "
                + "src: local(\"Indie Flower\"), local(\"IndieFlower\"), "
                + "url(https://fonts.gstatic.com/s/indieflower/v9"
                + "/10JVD_humAd5zP2yrFqw6ugdm0LZdjqr5-oayXSOefg.woff2) format(\"woff2\"); "
                + "unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02C6, U+02DA, "
                + "U+02DC, U+2000-206F, U+2074, U+20AC, U+2212, U+2215}",
                rule.getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#at-rules">
     *          http://www.w3.org/TR/CSS21/syndata.html#at-rules</a>
     * @throws Exception if the test fails
     */
    @Test
    public void atRules1() throws Exception {
        final String css = "@import 'subs.css';\n"
            + "h1 { color: blue }\n"
            + "@import 'list.css';\n"
            + "h2 { color: red }\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "@import rule must occur before all other rules, except the @charset rule."
                + " (Invalid token \"@import\". Was expecting one of: <S>, \"<!--\", \"-->\".)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("3", errorHandler.getErrorLines());
        Assert.assertEquals("1", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());

        rule = rules.getRules().get(2);
        Assert.assertEquals("h2 { color: red }", rule.getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#at-rules">
     *  http://www.w3.org/TR/CSS21/syndata.html#at-rules</a>
     * @throws Exception if the test fails
     */
    @Test
    public void atRules2() throws Exception {
        final String css = "@import 'subs.css';\n"
            + "@media print {\n"
            + "  @import 'print-main.css';\n"
            + "  body { font-size: 10pt }\n"
            + "}\n"
            + "h1 {color: blue }\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "@import rule must occur before all other rules, except the @charset rule."
                + " (Invalid token \"@import\". Was expecting: <S>.)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("3", errorHandler.getErrorLines());
        Assert.assertEquals("3", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("@media print {body { font-size: 10pt } }", rule.getCssText());

        rule = rules.getRules().get(2);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());
    }

    /**
     * Test for {@literal @}import after a rule.
     * @throws Exception if the test fails
     */
    @Test
    public void atRules2b() throws Exception {
        final String css = "@import 'subs.css';\n"
            + "@media print {\n"
            + "  body { font-size: 10pt }\n"
            + "  @import 'print-main.css';\n"
            + "}\n"
            + "h1 {color: blue }\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "@import rule must occur before all other rules, except the @charset rule."
                + " (Invalid token \"@import\". Was expecting: <S>.)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("4", errorHandler.getErrorLines());
        Assert.assertEquals("3", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@import url(subs.css);", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("@media print {body { font-size: 10pt } }", rule.getCssText());

        rule = rules.getRules().get(2);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void hexColor() throws Exception {
        final String cssText = "color: #ccc; background: #1c1d00;";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleDeclarationImpl style = parser.parseStyleDeclaration(cssText);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        // Enumerate the properties and retrieve their values
        Assert.assertEquals(2, style.getLength());

        String name = style.getProperties().get(0).getName();
        Assert.assertEquals("color : rgb(204, 204, 204)", name + " : " + style.getPropertyValue(name));

        name = style.getProperties().get(1).getName();
        Assert.assertEquals("background : rgb(28, 29, 0)", name + " : " + style.getPropertyValue(name));
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void rgb() throws Exception {
        final String cssText = "foreground: rgb( 10, 20, 30 )";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleDeclarationImpl style = parser.parseStyleDeclaration(cssText);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        // Enumerate the properties and retrieve their values
        Assert.assertEquals(1, style.getLength());

        final String name = style.getProperties().get(0).getName();
        Assert.assertEquals("foreground : rgb(10, 20, 30)", name + " : " + style.getPropertyValue(name));
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void rgbInsideFunction() throws Exception {
        final String cssText = "color: foo(#cd4);";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleDeclarationImpl style = parser.parseStyleDeclaration(cssText);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        // Enumerate the properties and retrieve their values
        Assert.assertEquals(1, style.getLength());

        final String name = style.getProperties().get(0).getName();
        Assert.assertEquals("color: foo(rgb(204, 221, 68))", name + ": " + style.getPropertyValue(name));
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void funct() throws Exception {
        final String cssText = "clip: foo(rect( 10px, 20em, 30px, max(40, blue(rgb(1,2,3))) ) )";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleDeclarationImpl style = parser.parseStyleDeclaration(cssText);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        // Enumerate the properties and retrieve their values
        Assert.assertEquals(1, style.getLength());

        final String name = style.getProperties().get(0).getName();
        Assert.assertEquals("clip : foo(rect(10px, 20em, 30px, max(40, blue(rgb(1, 2, 3)))))",
                name + " : " + style.getPropertyValue(name));

        final CSSValueImpl value = style.getPropertyCSSValue(name);
        LexicalUnitImpl unit  = (LexicalUnitImpl) value.getValue();
        Assert.assertEquals(LexicalUnitType.FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals("foo", unit.getFunctionName());

        unit  = (LexicalUnitImpl) unit.getParameters();
        Assert.assertEquals(LexicalUnitType.RECT_FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals("rect", unit.getFunctionName());

        unit  = (LexicalUnitImpl) unit.getParameters();
        Assert.assertEquals(LexicalUnitType.PIXEL, unit.getLexicalUnitType());
        Assert.assertEquals(10f, unit.getDoubleValue(), 0.00001);

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnitType.OPERATOR_COMMA, unit.getLexicalUnitType());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnitType.EM, unit.getLexicalUnitType());
        Assert.assertEquals(20f, unit.getDoubleValue(), 0.00001);

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnitType.OPERATOR_COMMA, unit.getLexicalUnitType());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnitType.PIXEL, unit.getLexicalUnitType());
        Assert.assertEquals(30f, unit.getDoubleValue(), 0.00001);

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnitType.OPERATOR_COMMA, unit.getLexicalUnitType());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnitType.FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals("max", unit.getFunctionName());

        unit  = (LexicalUnitImpl) unit.getParameters();
        Assert.assertEquals(LexicalUnitType.INTEGER, unit.getLexicalUnitType());
        Assert.assertEquals(40, unit.getIntegerValue());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnitType.OPERATOR_COMMA, unit.getLexicalUnitType());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnitType.FUNCTION, unit.getLexicalUnitType());
        Assert.assertEquals("blue", unit.getFunctionName());

        unit  = (LexicalUnitImpl) unit.getParameters();
        Assert.assertEquals(LexicalUnitType.RGBCOLOR, unit.getLexicalUnitType());
        Assert.assertEquals("rgb", unit.getFunctionName());

        unit  = (LexicalUnitImpl) unit.getParameters();
        Assert.assertEquals(LexicalUnitType.INTEGER, unit.getLexicalUnitType());
        Assert.assertEquals(1, unit.getIntegerValue());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnitType.OPERATOR_COMMA, unit.getLexicalUnitType());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnitType.INTEGER, unit.getLexicalUnitType());
        Assert.assertEquals(2, unit.getIntegerValue());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnitType.OPERATOR_COMMA, unit.getLexicalUnitType());

        unit  = (LexicalUnitImpl) unit.getNextLexicalUnit();
        Assert.assertEquals(LexicalUnitType.INTEGER, unit.getLexicalUnitType());
        Assert.assertEquals(3, unit.getIntegerValue());

        Assert.assertNull(unit.getNextLexicalUnit());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void beforeAfter() throws Exception {
        final String cssText = "heading:before { content: attr(test) \"testData\" }";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(cssText));
        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final CSSStyleRuleImpl rule = (CSSStyleRuleImpl) rules.getRules().get(0);
        Assert.assertEquals("heading:before { content: attr(test) \"testData\" }", rule.getCssText());

        final CSSStyleDeclarationImpl style = rule.getStyle();

        Assert.assertEquals(1, style.getLength());

        final String name = style.getProperties().get(0).getName();
        Assert.assertEquals("content : attr(test) \"testData\"", name + " : " + style.getPropertyValue(name));
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void rect() throws Exception {
        final String cssText = "clip: rect( 10px, 20px, 30px, 40px )";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleDeclarationImpl style = parser.parseStyleDeclaration(cssText);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        // Enumerate the properties and retrieve their values
        Assert.assertEquals(1, style.getLength());

        final String name = style.getProperties().get(0).getName();
        Assert.assertEquals("clip : rect(10px, 20px, 30px, 40px)", name + " : " + style.getPropertyValue(name));
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void attr() throws Exception {
        final String cssText = "content: attr( data-foo )";

        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleDeclarationImpl style = parser.parseStyleDeclaration(cssText);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        // Enumerate the properties and retrieve their values
        Assert.assertEquals(1, style.getLength());

        final String name = style.getProperties().get(0).getName();
        Assert.assertEquals("content : attr(data-foo)", name + " : " + style.getPropertyValue(name));
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/generate.html#counters">
     *          http://www.w3.org/TR/CSS21/generate.html#counters</a>
     * @throws Exception if the test fails
     */
    @Test
    public void counter() throws Exception {
        final String css =
                  "H1:before        { content: counter(chno, upper-latin) \". \" }\n"
                + "H2:before        { content: counter(section, upper-roman) \" - \" }\n"
                + "BLOCKQUOTE:after { content: \" [\" counter(bq, lower-greek) \"]\" }\n"
                + "DIV.note:before  { content: counter(notecntr, disc) \" \" }\n"
                + "P:before         { content: counter(p, none) }";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(5, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("H1:before { content: counter(chno, upper-latin) \". \" }", rule.getCssText());
        CSSValueImpl value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("content");
        Assert.assertEquals("counter(chno, upper-latin)", value.item(0).getCounterValue().toString());

        rule = rules.getRules().get(1);
        Assert.assertEquals("H2:before { content: counter(section, upper-roman) \" - \" }", rule.getCssText());
        value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("content");
        Assert.assertEquals("counter(section, upper-roman)",
                value.item(0).getCounterValue().toString());

        rule = rules.getRules().get(2);
        Assert.assertEquals("BLOCKQUOTE:after { content: \" [\" counter(bq, lower-greek) \"]\" }", rule.getCssText());
        value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("content");
        Assert.assertEquals("counter(bq, lower-greek)", value.item(1).getCounterValue().toString());

        rule = rules.getRules().get(3);
        Assert.assertEquals("DIV.note:before { content: counter(notecntr, disc) \" \" }", rule.getCssText());
        value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("content");
        Assert.assertEquals("counter(notecntr, disc)", value.item(0).getCounterValue().toString());

        rule = rules.getRules().get(4);
        Assert.assertEquals("P:before { content: counter(p, none) }", rule.getCssText());
        value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("content");
        Assert.assertEquals("counter(p, none)", value.getCounterValue().toString());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/generate.html#counters">
     *          http://www.w3.org/TR/CSS21/generate.html#counters</a>
     * @throws Exception if the test fails
     */
    @Test
    public void counters() throws Exception {
        final String css = "LI:before { content: counters(item, \".\") \" \"; counter-increment: item }";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("LI:before { content: counters(item, \".\") \" \"; counter-increment: item }",
                rule.getCssText());
        final CSSValueImpl value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("content");
        Assert.assertEquals("counters(item, \".\")", value.item(0).getCounterValue().toString());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#parsing-errors">
     *          http://www.w3.org/TR/CSS21/syndata.html#parsing-errors</a>
     * @throws Exception if the test fails
     */
    @Test
    public void unknownProperty() throws Exception {
        final String css = "h1 { color: red; rotation: 70minutes }\n";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        // parser accepts this
        Assert.assertEquals("h1 { color: red; rotation: 70minutes }", rule.getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#parsing-errors">
     *          http://www.w3.org/TR/CSS21/syndata.html#parsing-errors</a>
     * @throws Exception if the test fails
     */
    @Test
    public void illegalValues() throws Exception {
        final String css = "img { float: left } /* correct CSS 2.1 */\n"
                    + "img { float: left here } /* 'here' is not a value of 'float' */\n"
                    + "img { background: \"red\" } /* keywords cannot be quoted */\n"
                    + "img { background: \'red\' } /* keywords cannot be quoted */\n"
                    + "img { border-width: 3 } /* a unit must be specified for length values */\n";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(5, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("img { float: left }", rule.getCssText());

        // parser accepts this
        rule = rules.getRules().get(1);
        Assert.assertEquals("img { float: left here }", rule.getCssText());
        rule = rules.getRules().get(2);
        Assert.assertEquals("img { background: \"red\" }", rule.getCssText());
        rule = rules.getRules().get(3);
        Assert.assertEquals("img { background: \"red\" }", rule.getCssText());
        rule = rules.getRules().get(4);
        Assert.assertEquals("img { border-width: 3 }", rule.getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#parsing-errors">
     *          http://www.w3.org/TR/CSS21/syndata.html#parsing-errors</a>
     * @throws Exception if the test fails
     */
    @Test
    public void malformedDeclaration() throws Exception {
        final String css = "p { color:green }\n"
                    + "p { color:green; background } /* malformed declaration missing ':', value */\n"
                    + "p { color:red;   background; visibility:hidden } /* same with expected recovery */\n"
                    + "p { color:green; background: } /* malformed declaration missing value */\n"
                    + "p { color:red;   background:; visibility:hidden } /* same with expected recovery */\n"
                    + "p { color:green; background{;visibility:hidden} } /* unexpected tokens { } */\n"
                    + "p { color:red;   background{;visibility:hidden}; display:block } /* same with recovery */\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(7, errorHandler.getErrorCount());
        final String expected = "Error in declaration. (Invalid token \"}\". Was expecting one of: <S>, \":\".)"
                + " Error in declaration. (Invalid token \";\". Was expecting one of: <S>, \":\".)"
                + " Error in expression. (Invalid token \"}\". Was expecting one of: <S>, <NUMBER>, \"inherit\", "
                        + "<IDENT>, <STRING>, \"-\", <PLUS>, <HASH>, <EMS>, <REM>, <EXS>, "
                        + "<LENGTH_PX>, <LENGTH_CM>, <LENGTH_MM>, "
                        + "<LENGTH_IN>, <LENGTH_PT>, <LENGTH_PC>, <ANGLE_DEG>, <ANGLE_RAD>, <ANGLE_GRAD>, <TIME_MS>, "
                        + "<TIME_S>, <FREQ_HZ>, <FREQ_KHZ>, <RESOLUTION_DPI>, <RESOLUTION_DPCM>, <PERCENTAGE>, "
                        + "<DIMENSION>, <UNICODE_RANGE>, <URI>, <FUNCTION>, \"progid:\".)"
                + " Error in expression. (Invalid token \";\". Was expecting one of: <S>, <NUMBER>, \"inherit\", "
                        + "<IDENT>, <STRING>, \"-\", <PLUS>, <HASH>, <EMS>, <REM>, <EXS>, "
                        + "<LENGTH_PX>, <LENGTH_CM>, <LENGTH_MM>, "
                        + "<LENGTH_IN>, <LENGTH_PT>, <LENGTH_PC>, <ANGLE_DEG>, <ANGLE_RAD>, <ANGLE_GRAD>, <TIME_MS>, "
                        + "<TIME_S>, <FREQ_HZ>, <FREQ_KHZ>, <RESOLUTION_DPI>, <RESOLUTION_DPCM>, <PERCENTAGE>, "
                        + "<DIMENSION>, <UNICODE_RANGE>, <URI>, <FUNCTION>, \"progid:\".)"
                + " Error in declaration. (Invalid token \"{\". Was expecting one of: <S>, \":\".)"
                + " Error in style rule. (Invalid token \" \". Was expecting one of: <EOF>, \"}\", \";\".)"
                + " Error in declaration. (Invalid token \"{\". Was expecting one of: <S>, \":\".)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("2 3 4 5 6 6 7", errorHandler.getErrorLines());
        Assert.assertEquals("29 28 30 29 28 48 28", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());
        Assert.assertTrue(errorHandler.getWarningMessage(),
                errorHandler.getWarningMessage().startsWith("Ignoring the following declarations in this rule."));
        Assert.assertEquals("6", errorHandler.getWarningLines());
        Assert.assertEquals("48", errorHandler.getWarningColumns());

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(7, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("p { color: green }", rule.getCssText());

        // parser accepts this
        rule = rules.getRules().get(1);
        Assert.assertEquals("p { color: green }", rule.getCssText());
        rule = rules.getRules().get(2);
        Assert.assertEquals("p { color: red; visibility: hidden }", rule.getCssText());
        rule = rules.getRules().get(3);
        Assert.assertEquals("p { color: green }", rule.getCssText());
        rule = rules.getRules().get(4);
        Assert.assertEquals("p { color: red; visibility: hidden }", rule.getCssText());
        rule = rules.getRules().get(5);
        Assert.assertEquals("p { color: green }", rule.getCssText());
        rule = rules.getRules().get(6);
        Assert.assertEquals("p { color: red; display: block }", rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void malformedDeclarationSkipOnlyDeclaration() throws Exception {
        final String css = "h1 { color: red&; width: 10px; }\n"
                            + "h2 { color = red; width: 10px }\n"
                            + "h3 { color: red& }\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(3, errorHandler.getErrorCount());
        Assert.assertEquals("1 2 3", errorHandler.getErrorLines());
        Assert.assertEquals("16 12 16", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();
        Assert.assertEquals(3, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("h1 { color: red; width: 10px }", rule.getCssText());
        rule = rules.getRules().get(1);
        Assert.assertEquals("h2 { width: 10px }", rule.getCssText());
        rule = rules.getRules().get(2);
        Assert.assertEquals("h3 { color: red }", rule.getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#parsing-errors">
     *          http://www.w3.org/TR/CSS21/syndata.html#parsing-errors</a>
     * @throws Exception if the test fails
     */
    @Test
    public void malformedStatements() throws Exception {
        final String css = "p { color:green }\n"
                    + "p @here {color:red} /* ruleset with unexpected at-keyword '@here' */\n"
                    + "@foo @bar; /* at-rule with unexpected at-keyword '@bar' */\n"
                    // TODO + "}} {{ - }} /* ruleset with unexpected right brace */\n"
                    // TODO + ") ( {} ) p {color: red } /* ruleset with unexpected right parenthesis */\n"
                    + "p { color:blue; }\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "Error in style rule. "
                + "(Invalid token \"@here\". Was expecting one of: <S>, <LBRACE>, <COMMA>.)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("2", errorHandler.getErrorLines());
        Assert.assertEquals("3", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());
        Assert.assertEquals("Ignoring the following declarations in this rule.", errorHandler.getWarningMessage());
        Assert.assertEquals("2", errorHandler.getWarningLines());
        Assert.assertEquals("3", errorHandler.getWarningColumns());

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("p { color: green }", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("@foo @bar;", rule.getCssText());

        rule = rules.getRules().get(2);
        Assert.assertEquals("p { color: blue }", rule.getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#parsing-errors">
     *          http://www.w3.org/TR/CSS21/syndata.html#parsing-errors</a>
     * @throws Exception if the test fails
     */
    @Test
    public void atRulesWithUnknownAtKeywords() throws Exception {
        final String css = "@three-dee {\n"
                            + "  @background-lighting {\n"
                            + "    azimuth: 30deg;\n"
                            + "    elevation: 190deg;\n"
                            + "  }\n"
                            + "  h1 { color: red }\n"
                            + "  }\n"
                            + "  h1 { color: blue }\n";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(2, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@three-dee {\n"
                            + "  @background-lighting {\n"
                            + "    azimuth: 30deg;\n"
                            + "    elevation: 190deg;\n"
                            + "  }\n"
                            + "  h1 { color: red }\n"
                            + "  }", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("h1 { color: blue }", rule.getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#parsing-errors">
     *          http://www.w3.org/TR/CSS21/syndata.html#parsing-errors</a>
     * @throws Exception if the test fails
     */
    @Test
    public void unexpectedEndOfStyleSheet() throws Exception {
        final String css = "@media screen {\n"
                            + "  p:before { content: Hello";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "Error in @media rule. (Invalid token \"<EOF>\". Was expecting one of: ";
        Assert.assertTrue(errorHandler.getErrorMessage(), errorHandler.getErrorMessage().startsWith(expected));
        Assert.assertEquals("2", errorHandler.getErrorLines());
        Assert.assertEquals("27", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());
        Assert.assertEquals("Ignoring the whole rule.", errorHandler.getWarningMessage());
        Assert.assertEquals("2", errorHandler.getWarningLines());
        Assert.assertEquals("27", errorHandler.getWarningColumns());

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@media screen {p:before { content: Hello } }", rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unexpectedEndOfMediaRule() throws Exception {
        final String css = "@media screen {\n"
                            + "  p:before { content: Hello }";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "Error in @media rule. (Invalid token \"<EOF>\". Was expecting one of: ";
        Assert.assertTrue(errorHandler.getErrorMessage(), errorHandler.getErrorMessage().startsWith(expected));
        Assert.assertEquals("2", errorHandler.getErrorLines());
        Assert.assertEquals("29", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());
        Assert.assertEquals("Ignoring the whole rule.", errorHandler.getWarningMessage());
        Assert.assertEquals("2", errorHandler.getWarningLines());
        Assert.assertEquals("29", errorHandler.getWarningColumns());

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@media screen {p:before { content: Hello } }", rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unexpectedEndOfPageRule() throws Exception {
        final String css = "@page :pageStyle { size: 21.0cm 29.7cm;";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "Error in @page rule. "
                + "(Invalid token \"<EOF>\". Was expecting one of: <S>, <IDENT>, \"}\", \";\", \"*\".)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("1", errorHandler.getErrorLines());
        Assert.assertEquals("39", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());
        Assert.assertEquals("Ignoring the whole rule.", errorHandler.getWarningMessage());
        Assert.assertEquals("1", errorHandler.getWarningLines());
        Assert.assertEquals("39", errorHandler.getWarningColumns());

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@page :pageStyle {size: 21cm 29.7cm}", rule.getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#parsing-errors">
     *          http://www.w3.org/TR/CSS21/syndata.html#parsing-errors</a>
     *
     * @throws Exception in case of failure
     */
    @Test
    public void unexpectedEndOfString() throws Exception {
        final String css = "p {\n"
                            + "  color: green;\n"
                            + "  font-family: 'Courier New Times\n"
                            + "  visibility: hidden;\n"
                            + "  background: green;\n"
                            + "}";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "Error in expression. "
                + "(Invalid token \"\\'\". Was expecting one of: <S>, <NUMBER>, \"inherit\", "
                        + "<IDENT>, <STRING>, \"-\", <PLUS>, <HASH>, <EMS>, <REM>, <EXS>, "
                        + "<LENGTH_PX>, <LENGTH_CM>, <LENGTH_MM>, "
                        + "<LENGTH_IN>, <LENGTH_PT>, <LENGTH_PC>, <ANGLE_DEG>, <ANGLE_RAD>, <ANGLE_GRAD>, <TIME_MS>, "
                        + "<TIME_S>, <FREQ_HZ>, <FREQ_KHZ>, <RESOLUTION_DPI>, <RESOLUTION_DPCM>, <PERCENTAGE>, "
                        + "<DIMENSION>, <UNICODE_RANGE>, <URI>, <FUNCTION>, \"progid:\".)";
        Assert.assertEquals(expected, errorHandler.getErrorMessage());
        Assert.assertEquals("3", errorHandler.getErrorLines());
        Assert.assertEquals("16", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("p { color: green; background: green }", rule.getCssText());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void numbersOutsideDeclaration() throws Exception {
        final String css = ".red { color: red }\n"
                            + ".invalid {} 0.75in\n"
                            + ".blue { color: blue }\n"
                            + ".green { color: green }\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "Error in rule. (Invalid token \"0.75\".";
        Assert.assertTrue("'" + errorHandler.getErrorMessage() + "' does not start with '" + expected + "'",
                        errorHandler.getErrorMessage().startsWith(expected));
        Assert.assertEquals("2", errorHandler.getErrorLines());
        Assert.assertEquals("13", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());
        Assert.assertEquals("*.red { color: red }", rules.getRules().get(0).getCssText());
        Assert.assertEquals("*.invalid { }", rules.getRules().get(1).getCssText());
        Assert.assertEquals("*.green { color: green }", rules.getRules().get(2).getCssText());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void unbalancedRBraceAtEnd() throws Exception {
        final String css = ".red { color: red }\n}";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        final String expected = "Error in rule. (Invalid token \"}\".";
        Assert.assertTrue("'" + errorHandler.getErrorMessage() + "' does not start with '" + expected + "'",
                        errorHandler.getErrorMessage().startsWith(expected));
        Assert.assertEquals("2", errorHandler.getErrorLines());
        Assert.assertEquals("1", errorHandler.getErrorColumns());

        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());
        Assert.assertEquals("*.red { color: red }", rules.getRules().get(0).getCssText());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void whitespaceAtEnd() throws Exception {
        final String css = ".red { color: red }\n   \t \r\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSStyleSheetImpl sheet = parse(source, 0, 0, 0);

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());
        Assert.assertEquals("*.red { color: red }", rules.getRules().get(0).getCssText());
    }

    /**
     * @see <a href="http://www.w3.org/TR/CSS21/syndata.html#strings">
     *          http://www.w3.org/TR/CSS21/syndata.html#strings</a>
     *
     * @throws Exception in case of failure
     */
    @Test
    public void strings() throws Exception {
        final String css = "h1 { background: url(\"this is a 'string'\") }\n"
                            + "h2 { background: url(\"this is a \\\"string\\\"\") }\n"
                            + "h4 { background: url('this is a \"string\"') }\n"
                            + "h5 { background: url('this is a \\'string\\'') }"
                            + "h6 { background: url('this is a \\\r\n string') }"
                            + "h1:before { content: 'chapter\\A hoofdstuk\\00000a chapitre' }";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(6, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("h1 { background: url(this is a 'string') }", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("h2 { background: url(this is a \"string\") }", rule.getCssText());

        rule = rules.getRules().get(2);
        Assert.assertEquals("h4 { background: url(this is a \"string\") }", rule.getCssText());

        rule = rules.getRules().get(3);
        Assert.assertEquals("h5 { background: url(this is a 'string') }", rule.getCssText());

        rule = rules.getRules().get(4);
        Assert.assertEquals("h6 { background: url(this is a  string) }", rule.getCssText());

        rule = rules.getRules().get(5);
        Assert.assertEquals("h1:before { content: \"chapter\\A hoofdstuk\\A  chapitre\" }", rule.getCssText());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void emptyUrl() throws Exception {
        final String css = "h1 { background: url() }\n"
                            + "h2 { background: url(\"\") }\n"
                            + "h4 { background: url('') }\n"
                            + "h5 { background: url( ) }";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(4, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("h1 { background: url() }", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("h2 { background: url() }", rule.getCssText());

        rule = rules.getRules().get(2);
        Assert.assertEquals("h4 { background: url() }", rule.getCssText());

        rule = rules.getRules().get(3);
        Assert.assertEquals("h5 { background: url() }", rule.getCssText());
    }

    /**
     * Regression test for bug 1420893.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void invalidCommaInDef() throws Exception {
        final String css = ".a, .b, { test: 1; }";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        parser.parseStyleSheet(source, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(1, errorHandler.getWarningCount());

        Assert.assertTrue(errorHandler.getErrorMessage(),
                errorHandler.getErrorMessage().startsWith("Error in simple selector."));
        Assert.assertEquals("Ignoring the whole rule.", errorHandler.getWarningMessage());
    }

    /**
     * Regression test for bug 1420893.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void missingValue() throws Exception {
        final String css = ".a { test; }\n"
                           + "p { color: green }";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        Assert.assertTrue(errorHandler.getErrorMessage(),
                errorHandler.getErrorMessage().startsWith("Error in declaration."));

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(2, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("*.a { }", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("p { color: green }", rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void skipDeklarationsErrorBefore() throws Exception {
        final String css = ".a { test; color: green }";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        Assert.assertTrue(errorHandler.getErrorMessage(),
                errorHandler.getErrorMessage().startsWith("Error in declaration."));

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("*.a { color: green }", rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void skipDeklarationsErrorBetween() throws Exception {
        final String css = ".a { color: blue; test; background: green }";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        Assert.assertTrue(errorHandler.getErrorMessage(),
                errorHandler.getErrorMessage().startsWith("Error in declaration."));

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("*.a { color: blue; background: green }", rule.getCssText());
    }

    /**
     * See https://issues.jboss.org/browse/RF-11741.
     * @throws Exception if any error occurs
     */
    @Test
    public void skipJBossIssue() throws Exception {
        final String css = ".shadow {\n"
                + " -webkit-box-shadow: 1px 4px 5px '#{richSkin.additionalBackgroundColor}';"
                + " -moz-box-shadow: 2px 4px 5px '#{richSkin.additionalBackgroundColor}';"
                + " box-shadow: 3px 4px 5px '#{richSkin.additionalBackgroundColor}';"
                + "}";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        final CSSStyleRuleImpl ruleImpl = (CSSStyleRuleImpl) rule;
        final CSSStyleDeclarationImpl declImpl = ruleImpl.getStyle();

        Property prop = declImpl.getPropertyDeclaration("-webkit-box-shadow");
        CSSValueImpl valueImpl = prop.getValue();
        Assert.assertEquals("1px 4px 5px \"#{richSkin.additionalBackgroundColor}\"", valueImpl.getCssText());

        prop = declImpl.getPropertyDeclaration("-moz-box-shadow");
        valueImpl = prop.getValue();
        Assert.assertEquals("2px 4px 5px \"#{richSkin.additionalBackgroundColor}\"", valueImpl.getCssText());

        prop = declImpl.getPropertyDeclaration("box-shadow");
        valueImpl = prop.getValue();
        Assert.assertEquals("3px 4px 5px \"#{richSkin.additionalBackgroundColor}\"", valueImpl.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionPercent() throws Exception {
        final CSSValueImpl value = dimension("2%");
        Assert.assertEquals(CSSPrimitiveValueType.CSS_PERCENTAGE, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionPX() throws Exception {
        final CSSValueImpl value = dimension("3px");
        Assert.assertEquals(CSSPrimitiveValueType.CSS_PX, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionCM() throws Exception {
        final CSSValueImpl value = dimension("5cm");
        Assert.assertEquals(CSSPrimitiveValueType.CSS_CM, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionMM() throws Exception {
        final CSSValueImpl value = dimension("7mm");
        Assert.assertEquals(CSSPrimitiveValueType.CSS_MM, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionIN() throws Exception {
        final CSSValueImpl value = dimension("11in");
        Assert.assertEquals(CSSPrimitiveValueType.CSS_IN, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionPT() throws Exception {
        final CSSValueImpl value = dimension("13pt");
        Assert.assertEquals(CSSPrimitiveValueType.CSS_PT, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionEMS() throws Exception {
        final CSSValueImpl value = dimension("17em");
        Assert.assertEquals(CSSPrimitiveValueType.CSS_EMS, value.getPrimitiveType());
    }
    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionREM() throws Exception {
        final CSSValueImpl value = dimension("17rem");
        Assert.assertEquals(CSSPrimitiveValueType.CSS_UNKNOWN, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionEXS() throws Exception {
        final CSSValueImpl value = dimension("19ex");
        Assert.assertEquals(CSSPrimitiveValueType.CSS_EXS, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionDEG() throws Exception {
        final CSSValueImpl value = dimension("13deg");
        Assert.assertEquals(CSSPrimitiveValueType.CSS_DEG, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionRAD() throws Exception {
        final CSSValueImpl value = dimension("99rad");
        Assert.assertEquals(CSSPrimitiveValueType.CSS_RAD, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionGRAD() throws Exception {
        final CSSValueImpl value = dimension("31grad");
        Assert.assertEquals(CSSPrimitiveValueType.CSS_GRAD, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionMS() throws Exception {
        final CSSValueImpl value = dimension("37ms");
        Assert.assertEquals(CSSPrimitiveValueType.CSS_MS, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionS() throws Exception {
        final CSSValueImpl value = dimension("41s");
        Assert.assertEquals(CSSPrimitiveValueType.CSS_S, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionHZ() throws Exception {
        final CSSValueImpl value = dimension("43Hz");
        Assert.assertEquals(CSSPrimitiveValueType.CSS_HZ, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionKHZ() throws Exception {
        final CSSValueImpl value = dimension("47kHz");
        Assert.assertEquals(CSSPrimitiveValueType.CSS_KHZ, value.getPrimitiveType());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void dimensionPC() throws Exception {
        dimension("5pc");
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void illegalDimension() throws Exception {
        final String css = ".a { top: 0\\9; }"
                + ".b { top: -01.234newDim; }";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(2, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("*.a { top: 0\\9 }", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("*.b { top: -1.234newDim }", rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void opacity() throws Exception {
        final String css = ".a {\n"
                            + "-ms-filter: \"progid:DXImageTransform.Microsoft.Alpha(Opacity=90)\";\n"
                            + "filter: alpha(opacity=90);\n"
                            + "-moz-opacity: 0.9;\n"
                            + "opacity: 0.9;\n"
                            + "}";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);

        CSSValueImpl value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("-ms-filter");
        Assert.assertEquals("\"progid:DXImageTransform.Microsoft.Alpha(Opacity=90)\"", value.getCssText());

        value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("filter");
        Assert.assertEquals("alpha(opacity = 90)", value.getCssText());

        value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("-moz-opacity");
        Assert.assertEquals("0.9", value.getCssText());

        value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("opacity");
        Assert.assertEquals("0.9", value.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void gradientIE6Style() throws Exception {
        final String css = ".a {\n"
                            + "filter: progid:DXImageTransform.Microsoft."
                                + "gradient(GradientType=0, startColorstr=#6191bf, endColorstr=#cde6f9);\n"
                            + "color: green;\n"
                            + "}\n"
                            + ".img {filter: progid:DXImageTransform.Microsoft.gradient(GradientType=0)}\n"
                            + ".h1 {filter: progid:DXImageTransform.Microsoft.gradient(GradientType=1) \t }\n"
                            + ".h1 {filter: progid: }\n";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(4, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        CSSValueImpl value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("filter");
        Assert.assertEquals("progid:DXImageTransform.Microsoft.gradient("
                + "GradientType=0, startColorstr=#6191bf, endColorstr=#cde6f9)", value.getCssText());
        value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("color");
        Assert.assertEquals("green", value.getCssText());
        value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("color");
        Assert.assertEquals("green", value.getCssText());

        rule = rules.getRules().get(1);
        value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("filter");
        Assert.assertEquals("progid:DXImageTransform.Microsoft.gradient(GradientType=0)", value.getCssText());

        rule = rules.getRules().get(2);
        value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("filter");
        Assert.assertEquals("progid:DXImageTransform.Microsoft.gradient(GradientType=1)", value.getCssText());

        rule = rules.getRules().get(3);
        value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("filter");
        Assert.assertEquals("progid:", value.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void evalIEStyle() throws Exception {
        final String css = ".a {\n"
                            + "top: expression((eval(document.documentElement||document.body).scrollTop));\n"
                            + "color: green;"
                            + "}";

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        Assert.assertTrue(errorHandler.getErrorMessage(),
                errorHandler.getErrorMessage().startsWith("Error in expression. (Invalid token \"(\"."));

        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("*.a { color: green }", rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void gradientIE8Style() throws Exception {
        final String css =
                ".a {\n"
                + "-ms-filter: \"progid:DXImageTransform.Microsoft."
                    + "gradient(GradientType=0, startColorstr=#6191bf, endColorstr=#cde6f9)\";\n"
                + "color: green;"
                + "}\n"
                + ".img {\n"
                + "-ms-filter: 'progid:DXImageTransform.Microsoft.MotionBlur(strength=50), "
                    + "progid:DXImageTransform.Microsoft.BasicImage(mirror=1)'\n"
                + "}";
        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(2, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        CSSValueImpl value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("-ms-filter");
        Assert.assertEquals("\"progid:DXImageTransform.Microsoft."
            + "gradient(GradientType=0, startColorstr=#6191bf, endColorstr=#cde6f9)\"", value.getCssText());
        value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("color");
        Assert.assertEquals("green", value.getCssText());

        rule = rules.getRules().get(1);
        value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("-ms-filter");
        Assert.assertEquals("\"progid:DXImageTransform.Microsoft.MotionBlur(strength=50), "
            + "progid:DXImageTransform.Microsoft.BasicImage(mirror=1)\"", value.getCssText());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void transformRotate() throws Exception {
        final String css = ".flipped {\n"
                + "  transform: rotateY(180deg);\n"
                + "}";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);

        final CSSValueImpl value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("transform");
        Assert.assertEquals("rotateY(180deg)", value.getCssText());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void rgba() throws Exception {
        final String css = "p {\n"
                + "  background-color: rgba(0,0,0,0.2);\n"
                + "}";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);

        final CSSValueImpl value = ((CSSStyleRuleImpl) rule).getStyle().
                            getPropertyCSSValue("background-color");
        Assert.assertEquals("rgba(0, 0, 0, 0.2)", value.getCssText());

        Assert.assertEquals(CSSValueType.CSS_PRIMITIVE_VALUE, value.getCssValueType());
        Assert.assertEquals(CSSPrimitiveValueType.CSS_STRING, value.getPrimitiveType());
        Assert.assertEquals("rgba(0, 0, 0, 0.2)", value.getStringValue());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void linearGradient() throws Exception {
        final String css = "h1 { background: linear-gradient(top, #fff, #f2f2f2); }\n"
                + "h2 { background: linear-gradient( 45deg, blue, red ); }\n"
                + "h3 { background: linear-gradient( to left top, #00f, red); }\n"
                + "h4 { background: linear-gradient( 0 deg, blue, green 40%, red ); }\n";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(4, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        CSSValueImpl value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("background");
        Assert.assertEquals("linear-gradient(top, rgb(255, 255, 255), rgb(242, 242, 242))", value.getCssText());

        rule = rules.getRules().get(1);
        value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("background");
        Assert.assertEquals("linear-gradient(45deg, blue, red)", value.getCssText());

        rule = rules.getRules().get(2);
        value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("background");
        Assert.assertEquals("linear-gradient(to left top, rgb(0, 0, 255), red)", value.getCssText());

        rule = rules.getRules().get(3);
        value = ((CSSStyleRuleImpl) rule).getStyle().getPropertyCSSValue("background");
        Assert.assertEquals("linear-gradient(0 deg, blue, green 40%, red)", value.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void prefixAttributeCondition() throws Exception {
        final SelectorList selectors = createSelectors("[rel^=val]");
        final ElementSelector selector = (ElementSelector) selectors.get(0);
        Assert.assertTrue(selector.getConditions().get(0) instanceof PrefixAttributeCondition);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void suffixAttributeCondition() throws Exception {
        final SelectorList selectors = createSelectors("[rel$=val]");
        final ElementSelector selector = (ElementSelector) selectors.get(0);
        Assert.assertTrue(selector.getConditions().get(0) instanceof SuffixAttributeCondition);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void substringAttributeCondition() throws Exception {
        final SelectorList selectors = createSelectors("[rel*=val]");
        final ElementSelector selector = (ElementSelector) selectors.get(0);
        Assert.assertTrue(selector.getConditions().get(0) instanceof SubstringAttributeCondition);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void nth_child() throws Exception {
        String cssText = "div:nth-child(0)";
        SelectorList selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.get(0).toString());

        cssText = "div:nth-child(2n+1)";
        selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.get(0).toString());

        cssText = "div:nth-child(2n-1)";
        selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.get(0).toString());

        cssText = "div:nth-child(odd)";
        selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.get(0).toString());

        cssText = "div:nth-child(even)";
        selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.get(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void nth_last_child() throws Exception {
        String cssText = "div:nth-last-child(-n+2)";
        SelectorList selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.get(0).toString());

        cssText = "div:nth-last-child(odd)";
        selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.get(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void nth_of_type() throws Exception {
        String cssText = "div:nth-of-type(2n+1)";
        SelectorList selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.get(0).toString());

        cssText = "div:nth-of-type(2n)";
        selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.get(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void nth_preserveInnerWhitespace() throws Exception {
        SelectorList selectors = createSelectors("div:nth-child( 0 )");
        Assert.assertEquals("div:nth-child(0)", selectors.get(0).toString());

        selectors = createSelectors("div:nth-child( + 4 n )");
        Assert.assertEquals("div:nth-child(+ 4 n)", selectors.get(0).toString());

        selectors = createSelectors("div:nth-child( - 5 n + 2 )");
        Assert.assertEquals("div:nth-child(- 5 n + 2)", selectors.get(0).toString());

        selectors = createSelectors("div:nth-child( - 5     n\t\t+ \t 2 )");
        Assert.assertEquals("div:nth-child(- 5     n\t\t+ \t 2)", selectors.get(0).toString());

        selectors = createSelectors("div:nth-child( odd )");
        Assert.assertEquals("div:nth-child(odd)", selectors.get(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void generalAdjacentSelector() throws Exception {
        final String cssText = "div ~ hi";
        final SelectorList selectors = createSelectors(cssText);
        Assert.assertEquals(cssText, selectors.get(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void selectorTrimWhitespace() throws Exception {
        final String cssText = "  \t\r\n  div > hi  \t\r\n  ";
        final SelectorList selectors = createSelectors(cssText);
        Assert.assertEquals(cssText.trim(), selectors.get(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void not() throws Exception {
        // element name
        SelectorList selectors = createSelectors("input:not(abc)");
        Assert.assertEquals("input:not(abc)", selectors.get(0).toString());

        selectors = createSelectors("input:not(*)");
        Assert.assertEquals("input:not(*)", selectors.get(0).toString());

        // hash
        selectors = createSelectors("input:not(#test)");
        Assert.assertEquals("input:not(#test)", selectors.get(0).toString());

        // class
        selectors = createSelectors("input:not(.home)");
        Assert.assertEquals("input:not(.home)", selectors.get(0).toString());

        // attrib
        selectors = createSelectors("input:not([title])");
        Assert.assertEquals("input:not([title])", selectors.get(0).toString());

        selectors = createSelectors("input:not([type = 'file'])");
        Assert.assertEquals("input:not([type=\"file\"])", selectors.get(0).toString());

        selectors = createSelectors("input:not([type ~= 'file'])");
        Assert.assertEquals("input:not([type~=\"file\"])", selectors.get(0).toString());

        // pseudo
        selectors = createSelectors("input:not(:last)");
        Assert.assertEquals("input:not(:last)", selectors.get(0).toString());

        // whitespace
        selectors = createSelectors("input:not( .hi \t)");
        Assert.assertEquals("input:not(.hi)", selectors.get(0).toString());
    }

    /**
     * Testcase for issue #56.
     * @throws Exception if any error occurs
     */
    @Test
    public void notRule() throws Exception {
        final String css = "#stageList li:not(.memberStage) { display: none; }";
        final CSSStyleSheetImpl sheet = parse(css, 0, 0, 0);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("*#stageList li:not(.memberStage) { display: none }", rule.getCssText());

        final CSSStyleSheetImpl theOutputSheet = new CSSStyleSheetImpl();
        theOutputSheet.insertRule(rule.getCssText(), 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void not_elementName() throws Exception {
        final SelectorList selectors = createSelectors("p:not(abc)");
        Assert.assertEquals("p:not(abc)", selectors.get(0).toString());

        Assert.assertEquals(1, selectors.size());
        final Selector selector = selectors.get(0);

        Assert.assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, selector.getSelectorType());

        final ElementSelector elemSel = (ElementSelector) selector;
        Assert.assertEquals(1, elemSel.getConditions().size());

        final Condition condition = elemSel.getConditions().get(0);
        Assert.assertEquals(ConditionType.PSEUDO_CLASS_CONDITION, condition.getConditionType());

        final PseudoClassCondition pseudo = (PseudoClassCondition) condition;
        Assert.assertEquals("not(abc)", pseudo.getValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void not_universal() throws Exception {
        final SelectorList selectors = createSelectors("p:not(*)");
        Assert.assertEquals("p:not(*)", selectors.get(0).toString());

        Assert.assertEquals(1, selectors.size());
        final Selector selector = selectors.get(0);

        Assert.assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, selector.getSelectorType());

        final ElementSelector elemSel = (ElementSelector) selector;
        Assert.assertEquals(1, elemSel.getConditions().size());

        final Condition condition = elemSel.getConditions().get(0);
        Assert.assertEquals(ConditionType.PSEUDO_CLASS_CONDITION, condition.getConditionType());

        final PseudoClassCondition pseudo = (PseudoClassCondition) condition;
        Assert.assertEquals("not(*)", pseudo.getValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void not_hash() throws Exception {
        final SelectorList selectors = createSelectors("p:not( #test)");
        Assert.assertEquals("p:not(#test)", selectors.get(0).toString());

        Assert.assertEquals(1, selectors.size());
        final Selector selector = selectors.get(0);

        Assert.assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, selector.getSelectorType());

        final ElementSelector elemSel = (ElementSelector) selector;
        Assert.assertEquals(1, elemSel.getConditions().size());

        final Condition condition = elemSel.getConditions().get(0);
        Assert.assertEquals(ConditionType.PSEUDO_CLASS_CONDITION, condition.getConditionType());

        final PseudoClassCondition pseudo = (PseudoClassCondition) condition;
        Assert.assertEquals("not(#test)", pseudo.getValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void not_class() throws Exception {
        // element name
        final SelectorList selectors = createSelectors("p:not(.klass)");
        Assert.assertEquals("p:not(.klass)", selectors.get(0).toString());

        Assert.assertEquals(1, selectors.size());
        final Selector selector = selectors.get(0);

        Assert.assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, selector.getSelectorType());

        final ElementSelector elemSel = (ElementSelector) selector;
        Assert.assertEquals(1, elemSel.getConditions().size());

        final Condition condition = elemSel.getConditions().get(0);
        Assert.assertEquals(ConditionType.PSEUDO_CLASS_CONDITION, condition.getConditionType());

        final PseudoClassCondition pseudo = (PseudoClassCondition) condition;
        Assert.assertEquals("not(.klass)", pseudo.getValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void not_attrib() throws Exception {
        final SelectorList selectors = createSelectors("p:not([type='file'])");
        Assert.assertEquals("p:not([type=\"file\"])", selectors.get(0).toString());

        Assert.assertEquals(1, selectors.size());
        final Selector selector = selectors.get(0);

        Assert.assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, selector.getSelectorType());

        final ElementSelector elemSel = (ElementSelector) selector;
        Assert.assertEquals(1, elemSel.getConditions().size());

        final Condition condition = elemSel.getConditions().get(0);
        Assert.assertEquals(ConditionType.PSEUDO_CLASS_CONDITION, condition.getConditionType());

        final PseudoClassCondition pseudo = (PseudoClassCondition) condition;
        Assert.assertEquals("not([type=\"file\"])", pseudo.getValue());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void not_pseudo() throws Exception {
        final SelectorList selectors = createSelectors("p:not(:first)");
        Assert.assertEquals("p:not(:first)", selectors.get(0).toString());

        Assert.assertEquals(1, selectors.size());
        final Selector selector = selectors.get(0);

        Assert.assertEquals(SelectorType.ELEMENT_NODE_SELECTOR, selector.getSelectorType());

        final ElementSelector elemSel = (ElementSelector) selector;
        Assert.assertEquals(1, elemSel.getConditions().size());

        final Condition condition = elemSel.getConditions().get(0);
        Assert.assertEquals(ConditionType.PSEUDO_CLASS_CONDITION, condition.getConditionType());

        final PseudoClassCondition pseudo = (PseudoClassCondition) condition;
        Assert.assertEquals("not(:first)", pseudo.getValue());
    }
    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void invalid_not() throws Exception {
        checkErrorSelector("input:not(.home:visited)",
                "Error in pseudo class or element. (Invalid token \":\". Was expecting one of: <S>, \")\".)");

        checkErrorSelector("input:not(.home p)",
                "Error in pseudo class or element. (Invalid token \"p\". Was expecting one of: <S>, \")\".)");

        checkErrorSelector("input:not()",
                "Error in pseudo class or element. (Invalid token \")\"."
                + " Was expecting one of: <S>, <IDENT>, \".\", \":\", \"*\", \"[\", <HASH>.)");

        checkErrorSelector("input:not(*.home)",
                "Error in pseudo class or element. (Invalid token \".\"."
                + " Was expecting one of: <S>, \")\".)");
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void twoPseudo() throws Exception {
        SelectorList selectors = createSelectors("input:lang(en):lang(de)");
        Assert.assertEquals("input:lang(en):lang(de)", selectors.get(0).toString());

        selectors = createSelectors("input:foo(test):foo(rest)");
        Assert.assertEquals("input:foo(test):foo(rest)", selectors.get(0).toString());

        selectors = createSelectors("input:foo(test):before");
        Assert.assertEquals("input:foo(test):before", selectors.get(0).toString());

        selectors = createSelectors("input:not(#test):not(#rest)");
        Assert.assertEquals("input:not(#test):not(#rest)", selectors.get(0).toString());

        selectors = createSelectors("input:not(#test):nth-child(even)");
        Assert.assertEquals("input:not(#test):nth-child(even)", selectors.get(0).toString());

        selectors = createSelectors("input:not(#test):before");
        Assert.assertEquals("input:not(#test):before", selectors.get(0).toString());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void pseudoElementsErrors() throws Exception {
        // two pseudo elements
        checkErrorSelector("input:before:after", "Duplicate pseudo class \":after\" "
                + "or pseudo class \":after\" not at end.");
        checkErrorSelector("input::before::after", "Duplicate pseudo class \":after\" "
                + "or pseudo class \":after\" not at end.");

        checkErrorSelector("input:before:lang(de)",
                "Duplicate pseudo class \":lang(de)\" or pseudo class \":lang(de)\" not at end.");
        checkErrorSelector("input:before:foo(ab)",
                "Duplicate pseudo class \":foo(ab)\" or pseudo class \":foo(ab)\" not at end.");
        checkErrorSelector("input:before:",
                "Error in pseudo class or element. (Invalid token \"<EOF>\". "
                + "Was expecting one of: <IDENT>, \":\", <FUNCTION_NOT>, <FUNCTION_LANG>, <FUNCTION>.)");

        // pseudo element not at end
        checkErrorSelector("input:before:not(#test)",
                "Duplicate pseudo class \":not(#test)\" or pseudo class \":not(#test)\" not at end.");
        checkErrorSelector("input:before[type='file']",
                "Error in attribute selector. (Invalid token \"type\". Was expecting: <S>.)");
        checkErrorSelector("input:before.styleClass", "Error in class selector. (Invalid token \"\". "
                + "Was expecting one of: .)");
        checkErrorSelector("input:before#hash", "Error in hash. (Invalid token \"\". Was expecting one of: .)");
    }

    /**
     * The CDO (<!--) and CDC (-->) symbols may appear in certain locations of a stylesheet.
     * In other locations, they should cause parts of the stylesheet to be ignored.
     * @see <a href="http://www.hixie.ch/tests/evil/mixed/cdocdc.html">
     *          http://www.hixie.ch/tests/evil/mixed/cdocdc.html</a>
     * @see <a href="https://test.csswg.org/suites/css2.1/20101027/html4/sgml-comments-002.htm">
     *          https://test.csswg.org/suites/css2.1/20101027/html4/sgml-comments-002.htm</a>
     * @throws Exception if any error occurs
     */
    @Test
    public void cdoCdc() throws Exception {
        final String css = "\n"
                + "    OL { list-style-type: lower-alpha; }\n"
                + "\n"
                + "<!--\n"
                + "\n"
                + "    .a { color: green; background: white none; }\n"
                + "<!--.b { color: green; background: white none; } --> <!-- --> <!--\n"
                + "    .c { color: green; background: white none; }\n"
                + "\n"
                + "<!--\n"
                + ".d { color: green; background: white none; }\n"
                + "-->\n"
                + "\n"
                + "    .e { color: green; background: white none; }\n"
                + "\n"
                + "\n"
                + "    <!--    .f { color: green; background: white none; }-->\n"
                + "-->.g { color: green; background: white none; }<!--\n"
                + "    .h { color: green; background: white none; }\n"
                + "-->-->-->-->-->-->.i { color: green; background: white none; }-->-->-->-->\n"
                + "\n"
                + "<!-- .j { color: green; background: white none; } -->\n"
                + "\n"
                + "<!--\n"
                + "     .k { color: green; background: white none; }\n"
                + "-->\n"
                + "\n"
                + "    .xa <!-- { color: yellow; background: red none; }\n"
                + "\n"
                + "    .xb { color: yellow -->; background: red none <!--; }\n"
                + "\n"
                + "    .xc { <!-- color: yellow; --> background: red none; }\n"
                + "\n"
                + "    .xd { <!-- color: yellow; background: red none -->; }\n"
                + "\n"
                + " <! -- .xe { color: yellow; background: red none; }\n"
                + "\n"
                + "--> <!--       --> <!-- -- >\n"
                + "\n"
                + "  .xf { color: yellow; background: red none; }\n";
        final CSSStyleSheetImpl sheet = parse(css, 6, 0, 6);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(15, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("OL { list-style-type: lower-alpha }", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("*.a { color: green; background: white none }", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("*.a { color: green; background: white none }", rule.getCssText());

        rule = rules.getRules().get(2);
        Assert.assertEquals("*.b { color: green; background: white none }", rule.getCssText());

        rule = rules.getRules().get(3);
        Assert.assertEquals("*.c { color: green; background: white none }", rule.getCssText());

        rule = rules.getRules().get(4);
        Assert.assertEquals("*.d { color: green; background: white none }", rule.getCssText());

        rule = rules.getRules().get(5);
        Assert.assertEquals("*.e { color: green; background: white none }", rule.getCssText());

        rule = rules.getRules().get(6);
        Assert.assertEquals("*.f { color: green; background: white none }", rule.getCssText());

        rule = rules.getRules().get(7);
        Assert.assertEquals("*.g { color: green; background: white none }", rule.getCssText());

        rule = rules.getRules().get(8);
        Assert.assertEquals("*.h { color: green; background: white none }", rule.getCssText());

        rule = rules.getRules().get(9);
        Assert.assertEquals("*.i { color: green; background: white none }", rule.getCssText());

        rule = rules.getRules().get(10);
        Assert.assertEquals("*.j { color: green; background: white none }", rule.getCssText());

        rule = rules.getRules().get(11);
        Assert.assertEquals("*.k { color: green; background: white none }", rule.getCssText());

        rule = rules.getRules().get(12);
//         Assert.assertEquals(".xb { }", rule.getCssText());

        rule = rules.getRules().get(13);
        Assert.assertEquals("*.xc { }", rule.getCssText());

        rule = rules.getRules().get(14);
        Assert.assertEquals("*.xd { }", rule.getCssText());
    }

    /**
     * Comments.
     * @throws Exception if any error occurs
     */
    @Test
    public void comment() throws Exception {
        final String css = "p { color: red; /* background: white; */ background: green; }";
        final CSSStyleSheetImpl sheet = parse(css, 0, 0, 0);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("p { color: red; background: green }", rule.getCssText());
    }

    /**
     * Empty declaration.
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyDeclaration() throws Exception {
        final String css = "p {  }";
        final CSSStyleSheetImpl sheet = parse(css, 0, 0, 0);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("p { }", rule.getCssText());
    }

    /**
     * Empty declaration only semicolon.
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyDeclarationSemicolon() throws Exception {
        final String css = "p { ; }";
        final CSSStyleSheetImpl sheet = parse(css, 0, 0, 0);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("p { }", rule.getCssText());
    }

    /**
     * Empty declaration only some semicolon.
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyDeclarationManySemicolon() throws Exception {
        final String css = "p { ; ; \t     ; }";
        final CSSStyleSheetImpl sheet = parse(css, 0, 0, 0);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("p { }", rule.getCssText());
    }

    /**
     * Empty declaration only comment.
     * @throws Exception if any error occurs
     */
    @Test
    public void emptyDeclarationComment() throws Exception {
        final String css = "p { /* background: white; */ }";
        final CSSStyleSheetImpl sheet = parse(css, 0, 0, 0);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("p { }", rule.getCssText());
    }

    /**
     * Comments.
     * @throws Exception if any error occurs
     */
    @Test
    public void commentNotClosed() throws Exception {
        final String css = "p { color: red; /* background: white; }"
                + "h1 { color: blue; }";
        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 0);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("p { color: red }", rule.getCssText());
    }

    /**
     * Handle the famous star hack as smart as possible.
     * @throws Exception if any error occurs
     */
    @Test
    public void starHackFirst() throws Exception {
        String css = "p { *color: red; background: white; }"
                + "h1 { color: blue; }";
        CSSStyleSheetImpl sheet = parse(css, 1, 0, 0);

        CSSRuleListImpl rules = sheet.getCssRules();
        Assert.assertEquals(2, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("p { background: white }", rule.getCssText());

        css = "p { color: red; *background: white }"
                + "h1 { color: blue; }";
        sheet = parse(css, 1, 0, 0);

        rules = sheet.getCssRules();
        Assert.assertEquals(2, rules.getLength());

        rule = rules.getRules().get(0);
        Assert.assertEquals("p { color: red }", rule.getCssText());
    }

    /**
     * Star Hack support.
     * @throws Exception if any error occurs
     */
    @Test
    public void starHackSupport() throws Exception {
        final String css = "p { color: blue; *color: red; background: white; }";
        final CSSParser parser = new CSS3Parser();
        parser.setIeStarHackAccepted(true);

        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser cssParser = new CSSOMParser(parser);
        final ErrorHandler errorHandler = new ErrorHandler();
        cssParser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = cssParser.parseStyleSheet(source, null);

        Assert.assertEquals(0, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        final CSSRuleListImpl rules = sheet.getCssRules();
        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("p { color: blue; *color: red; background: white }", rule.getCssText());
    }

    /**
     * Testcase for the backslash zero ie hack.
     * @throws Exception if any error occurs
     */
    @Test
    public void backslashZeroHack() throws Exception {
        String css = "p { margin-top: 0px\\0; }"
                + "h1 { color: blue; }";
        CSSStyleSheetImpl sheet = parse(css, 0, 0, 0);

        CSSRuleListImpl rules = sheet.getCssRules();
        Assert.assertEquals(2, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("p { margin-top: 0px\\0 }", rule.getCssText());

        css = "p { background: \\0green; }"
                + "h1 { color: blue; }";
        sheet = parse(css, 0, 0, 0);

        rules = sheet.getCssRules();
        Assert.assertEquals(2, rules.getLength());

        rule = rules.getRules().get(0);
        // spec says we have to replace U+0000 with U+FFFD
        Assert.assertEquals("p { background: \uFFFDgreen }", rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unicode() throws Exception {
        unicode("@p\\41ge :pageStyle {}", "@page :pageStyle {}");
        unicode("@p\\041ge :pageStyle {}", "@page :pageStyle {}");
        unicode("@p\\0041ge :pageStyle {}", "@page :pageStyle {}");
        unicode("@p\\00041ge :pageStyle {}", "@page :pageStyle {}");
        unicode("@p\\000041ge :pageStyle {}", "@page :pageStyle {}");

        // \\0000041 - fails
        unicode("@p\\0000041ge :pageStyle {}", "@p\\0000041ge :pageStyle {}");

        // terminated by whitespace
        unicode("@\\0070 age :pageStyle {}", "@page :pageStyle {}");
        unicode("@\\0070\tage :pageStyle {}", "@page :pageStyle {}");
        unicode("@\\0070\r\nage :pageStyle {}", "@page :pageStyle {}");

        // terminated by lenght
        unicode("@\\000070age :pageStyle {}", "@page :pageStyle {}");

        // backslash ignored
        unicode("@\\page :pageStyle {}", "@page :pageStyle {}");
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unicodeEscaping() throws Exception {
        unicode("@media paper\\7b { }", "@media paper{ {}");
        unicode(".class\\7b { color: blue }", "*.class{ { color: blue }");
        unicode("@page :pseu\\64o { color: blue }", "@page :pseudo {color: blue}");
        unicode("h1:first-l\\69ne { color: blue }", "h1:first-line { color: blue }");
        unicode(".cls { color: blu\\65 }", "*.cls { color: blue }");
    }

    private void unicode(final String css, final String expected) throws IOException {
        final InputSource source = new InputSource(new StringReader(css));
        final CSSOMParser parser = new CSSOMParser();

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());
        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals(expected, rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeBackslash() throws Exception {
        Assert.assertEquals("abuv", new CSS3Parser().unescape("ab\\uv", false));
        Assert.assertEquals("ab\\ab", new CSS3Parser().unescape("ab\\\\ab", false));
        Assert.assertEquals("ab\\", new CSS3Parser().unescape("ab\\", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeOneHexDigit() throws Exception {
        Assert.assertEquals("ab\u0009x", new CSS3Parser().unescape("ab\\9x", false));
        Assert.assertEquals("ab\u0009", new CSS3Parser().unescape("ab\\9", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeTwoHexDigits() throws Exception {
        Assert.assertEquals("ab\u0009x", new CSS3Parser().unescape("ab\\09x", false));
        Assert.assertEquals("ab\u00e9x", new CSS3Parser().unescape("ab\\e9x", false));
        Assert.assertEquals("ab\u00e9", new CSS3Parser().unescape("ab\\e9", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeThreeHexDigits() throws Exception {
        Assert.assertEquals("ab\u0009x", new CSS3Parser().unescape("ab\\009x", false));
        Assert.assertEquals("ab\u00e9x", new CSS3Parser().unescape("ab\\0e9x", false));
        Assert.assertEquals("ab\u0ce9x", new CSS3Parser().unescape("ab\\ce9x", false));
        Assert.assertEquals("ab\u0ce9", new CSS3Parser().unescape("ab\\ce9", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeFourHexDigits() throws Exception {
        Assert.assertEquals("ab\u0009x", new CSS3Parser().unescape("ab\\0009x", false));
        Assert.assertEquals("ab\u00e9x", new CSS3Parser().unescape("ab\\00e9x", false));
        Assert.assertEquals("ab\u0ce9x", new CSS3Parser().unescape("ab\\0ce9x", false));
        Assert.assertEquals("ab\u1ce9x", new CSS3Parser().unescape("ab\\1ce9x", false));
        Assert.assertEquals("ab\u1ce9", new CSS3Parser().unescape("ab\\1ce9", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeFiveHexDigits() throws Exception {
        Assert.assertEquals("ab\u0009x", new CSS3Parser().unescape("ab\\00009x", false));
        Assert.assertEquals("ab\u00e9x", new CSS3Parser().unescape("ab\\000e9x", false));
        Assert.assertEquals("ab\u0ce9x", new CSS3Parser().unescape("ab\\00ce9x", false));
        Assert.assertEquals("ab\u1ce9x", new CSS3Parser().unescape("ab\\01ce9x", false));
        Assert.assertEquals("ab\ufffdx", new CSS3Parser().unescape("ab\\a1ce9x", false));
        Assert.assertEquals("ab\ufffd", new CSS3Parser().unescape("ab\\a1ce9", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeSixHexDigits() throws Exception {
        Assert.assertEquals("ab\u0009x", new CSS3Parser().unescape("ab\\000009x", false));
        Assert.assertEquals("ab\u00e9x", new CSS3Parser().unescape("ab\\0000e9x", false));
        Assert.assertEquals("ab\u0ce9x", new CSS3Parser().unescape("ab\\000ce9x", false));
        Assert.assertEquals("ab\u1ce9x", new CSS3Parser().unescape("ab\\001ce9x", false));
        Assert.assertEquals("ab\ufffdx", new CSS3Parser().unescape("ab\\0a1ce9x", false));
        Assert.assertEquals("ab\ufffdx", new CSS3Parser().unescape("ab\\3a1ce9x", false));
        Assert.assertEquals("ab\ufffd", new CSS3Parser().unescape("ab\\3a1ce9", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeSevenHexDigits() throws Exception {
        Assert.assertEquals("ab\ufffd9x", new CSS3Parser().unescape("ab\\0000009x", false));
        Assert.assertEquals("ab\u000e9x", new CSS3Parser().unescape("ab\\00000e9x", false));
        Assert.assertEquals("ab\u00ce9x", new CSS3Parser().unescape("ab\\0000ce9x", false));
        Assert.assertEquals("ab\u01ce9x", new CSS3Parser().unescape("ab\\0001ce9x", false));
        Assert.assertEquals("ab\ua1ce9x", new CSS3Parser().unescape("ab\\00a1ce9x", false));
        Assert.assertEquals("ab\ufffd9x", new CSS3Parser().unescape("ab\\03a1ce9x", false));
        Assert.assertEquals("ab\ufffd9x", new CSS3Parser().unescape("ab\\73a1ce9x", false));
        Assert.assertEquals("ab\ufffd9", new CSS3Parser().unescape("ab\\73a1ce9", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void unescapeAutoterminate() throws Exception {
        Assert.assertEquals("ab\uabcd", new CSS3Parser().unescape("ab\\abcd", false));

        Assert.assertEquals("ab\u00e9a", new CSS3Parser().unescape("ab\\e9 a", false));
        Assert.assertEquals("ab\u00e9", new CSS3Parser().unescape("ab\\e9 ", false));
        Assert.assertEquals("ab\u00e9 a", new CSS3Parser().unescape("ab\\e9  a", false));
        Assert.assertEquals("ab\u00e9 a", new CSS3Parser().unescape("ab\\0000e9 a", false));

        Assert.assertEquals("ab\u00e9a", new CSS3Parser().unescape("ab\\e9\ta", false));
        Assert.assertEquals("ab\u00e9", new CSS3Parser().unescape("ab\\e9\t", false));
        Assert.assertEquals("ab\u00e9\ta", new CSS3Parser().unescape("ab\\e9\t\ta", false));
        Assert.assertEquals("ab\u00e9\ta", new CSS3Parser().unescape("ab\\0000e9\ta", false));

        Assert.assertEquals("ab\u00e9a", new CSS3Parser().unescape("ab\\e9\ra", false));
        Assert.assertEquals("ab\u00e9", new CSS3Parser().unescape("ab\\e9\r", false));
        Assert.assertEquals("ab\u00e9\ra", new CSS3Parser().unescape("ab\\e9\r\ra", false));
        Assert.assertEquals("ab\u00e9\ra", new CSS3Parser().unescape("ab\\0000e9\ra", false));
        Assert.assertEquals("ab\u00e9a", new CSS3Parser().unescape("ab\\e9\r\na", false));
        Assert.assertEquals("ab\u00e9", new CSS3Parser().unescape("ab\\e9\r\n", false));
        Assert.assertEquals("ab\u00e9\ra", new CSS3Parser().unescape("ab\\e9\r\n\ra", false));
        Assert.assertEquals("ab\u00e9\r", new CSS3Parser().unescape("ab\\e9\r\n\r", false));
        Assert.assertEquals("ab\u00e9\na", new CSS3Parser().unescape("ab\\e9\r\n\na", false));
        Assert.assertEquals("ab\u00e9\n", new CSS3Parser().unescape("ab\\e9\r\n\n", false));

        Assert.assertEquals("ab\u00e9a", new CSS3Parser().unescape("ab\\e9\na", false));
        Assert.assertEquals("ab\u00e9", new CSS3Parser().unescape("ab\\e9\n", false));
        Assert.assertEquals("ab\u00e9\na", new CSS3Parser().unescape("ab\\e9\n\na", false));
        Assert.assertEquals("ab\u00e9\na", new CSS3Parser().unescape("ab\\0000e9\na", false));
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void realWorldMicrosoft() throws Exception {
        realWorld("realworld/style.csx.css", 701, 1391,
                "screen and (-webkit-min-device-pixel-ratio: 0);"
                + "screen and (max-width: 480px);"
                + "screen and (max-width: 539px);"
                + "screen and (max-width: 667px) and (min-width: 485px);"
                + "screen and (max-width: 679px);"
                + "screen and (max-width: 680px);"
                + "screen and (max-width: 900px);"
                + "screen and (min-width: 0) and (max-width: 899px);"
                + "screen and (min-width: 1024px);"
                + "screen and (min-width: 1025px);"
                + "screen and (min-width: 1025px) and (min-height: 900px);"
                + "screen and (min-width: 33.75em);"
                + "screen and (min-width: 42.5em);"
                + "screen and (min-width: 53.5em);"
                + "screen and (min-width: 540px);"
                + "screen and (min-width: 540px) and (max-width: 679px);"
                + "screen and (min-width: 560px);"
                + "screen and (min-width: 600px);"
                + "screen and (min-width: 64.0625em);"
                + "screen and (min-width: 64.0625em) and (min-height: 768px);"
                + "screen and (min-width: 64.0625em) and (min-height: 900px);"
                + "screen and (min-width: 668px);"
                + "screen and (min-width: 668px) and (max-width: 1024px);"
                + "screen and (min-width: 680px);"
                + "screen and (min-width: 680px) and (max-width: 899px);"
                + "screen and (min-width: 70em);"
                + "screen and (min-width: 70em) and (min-height: 768px);"
                + "screen and (min-width: 70em) and (min-height: 900px);"
                + "screen and (min-width: 900px);", 145, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void realWorldOracle() throws Exception {
        realWorld("realworld/compass-homestyle.css", 735, 2160,
                "all and (max-height: 600px);"
                + "all and (max-width: 600px);"
                + "all and (max-width: 770px);"
                + "all and (min-width: 0) and (max-width: 1012px);"
                + "all and (min-width: 0) and (max-width: 1018px);"
                + "all and (min-width: 0) and (max-width: 1111px);"
                + "all and (min-width: 0) and (max-width: 1312px);"
                + "all and (min-width: 0) and (max-width: 390px);"
                + "all and (min-width: 0) and (max-width: 400px);"
                + "all and (min-width: 0) and (max-width: 410px);"
                + "all and (min-width: 0) and (max-width: 450px);"
                + "all and (min-width: 0) and (max-width: 600px);"
                + "all and (min-width: 0) and (max-width: 640px);"
                + "all and (min-width: 0) and (max-width: 680px);"
                + "all and (min-width: 0) and (max-width: 720px);"
                + "all and (min-width: 0) and (max-width: 770px);"
                + "all and (min-width: 0) and (max-width: 870px);"
                + "all and (min-width: 0) and (max-width: 974px);"
                + "all and (min-width: 601px);"
                + "all and (min-width: 771px) and (max-width: 990px);"
                + "only screen and (max-width: 974px);"
                + "only screen and (min-width: 0) and (max-width: 1024px);"
                + "only screen and (min-width: 0) and (max-width: 320px);"
                + "only screen and (min-width: 0) and (max-width: 500px);"
                + "only screen and (min-width: 0) and (max-width: 600px);"
                + "only screen and (min-width: 0) and (max-width: 770px);"
                + "only screen and (min-width: 0) and (max-width: 880px);"
                + "only screen and (min-width: 0) and (max-width: 974px);"
                + "only screen and (min-width: 1024px) and (max-width: 1360px);"
                + "only screen and (min-width: 1360px);"
                + "only screen and (min-width: 601px) and (max-width: 974px);"
                + "only screen and (min-width: 771px) and (max-width: 974px);"
                + "only screen and (min-width: 880px);"
                + "only screen and (min-width: 974px);"
                + "only screen and (min-width: 975px) and (max-width: 1040px);"
                + "\ufffdscreen,screen\t;", 27, 1);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void realWorldIBM() throws Exception {
        realWorld("realworld/www.css", 493, 983,
                "only screen and (min-device-width: 768px) and (max-device-width: 1024px);print;screen;", 14, 1);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void realWorldApple() throws Exception {
        realWorld("realworld/home.built.css", 675, 1027,
                "only screen and (-webkit-min-device-pixel-ratio: 1.5);"
                + "only screen and (max-device-width: 767px);"
                + "only screen and (max-height: 970px);"
                + "only screen and (max-width: 1023px);"
                + "only screen and (max-width: 1024px);"
                + "only screen and (max-width: 1024px) and (-webkit-min-device-pixel-ratio: 1.5);"
                + "only screen and (max-width: 1024px) and (min-resolution: 144dpi);"
                + "only screen and (max-width: 1024px) and (min-resolution: 144dppx);"
                + "only screen and (max-width: 28em) and (max-device-width: 735px);"
                + "only screen and (max-width: 320px);"
                + "only screen and (max-width: 735px) and (max-device-width: 768px);"
                + "only screen and (max-width: 735px) and (max-device-width: 768px) "
                        + "and (-webkit-min-device-pixel-ratio: 1.5);"
                + "only screen and (max-width: 735px) and (max-device-width: 768px) and (min-resolution: 144dpi);"
                + "only screen and (max-width: 735px) and (max-device-width: 768px) and (min-resolution: 144dppx);"
                + "only screen and (max-width: 735px) and (max-device-width: 768px) and (orientation: portrait);"
                + "only screen and (min-device-width: 768px);"
                + "only screen and (min-width: 1442px);"
                + "only screen and (min-width: 1442px) and (-webkit-min-device-pixel-ratio: 1.5);"
                + "only screen and (min-width: 1442px) and (min-height: 1251px);"
                + "only screen and (min-width: 1442px) and (min-resolution: 144dpi);"
                + "only screen and (min-width: 1442px) and (min-resolution: 144dppx);"
                + "print;"
                + "screen and (min-resolution: 144dpi);"
                + "screen and (min-resolution: 144dppx);", 1, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void realWorldWikipedia() throws Exception {
        realWorld("realworld/load.php.css", 90, 227, "print;screen;screen and (min-width: 982px);", 59, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void realWorldNormalize() throws Exception {
        realWorld("realworld/normalize.css", 40, 64, "", 0, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void realWorldBlueprint() throws Exception {
        realWorld("realworld/blueprint/screen.css", 245, 341, "", 0, 0);
        realWorld("realworld/blueprint/print.css", 15, 33, "", 0, 0);
        realWorld("realworld/blueprint/ie.css", 22, 30, "", 1, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void realWorldBootstrap() throws Exception {
        final String media = "all and (-webkit-transform-3d);"
                + "all and (max-device-width: 480px) and (orientation: landscape);"
                + "all and (max-width: 767px);"
                + "all and (min-width: 1200px);"
                + "all and (min-width: 768px);"
                + "all and (min-width: 768px) and (max-width: 991px);all and (min-width: 992px);"
                + "all and (min-width: 992px) and (max-width: 1199px);"
                + "all and (transform-3d);print;"
                + "screen and (-webkit-min-device-pixel-ratio: 0);"
                + "screen and (max-width: 767px);"
                + "screen and (min-width: 768px);";
        realWorld("realworld/bootstrap_3_3_7_min.css", 1193, 2306, media, 1, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void realWorldSpiegel() throws Exception {
        realWorld("realworld/style-V5-11.css", 2088, 6021, "screen and (min-width: 1030px);", 54, 0);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void realStackoverflow() throws Exception {
        final String media = "not all;"
                + "only screen and (min--moz-device-pixel-ratio: 1.5);"
                + "print;screen and (max-height: 740px);"
                + "screen and (max-height: 750px);"
                + "screen and (max-width: 1090px);"
                + "screen and (max-width: 920px);";
        realWorld("realworld/all.css", 5235, 12377, media, 26, 2);
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void realMui() throws Exception {
        final String media = "all and (-ms-high-contrast: active);"
                + "all and (-ms-high-contrast: none);"
                + "all and (max-width: 543px);"
                + "all and (min-width: 1200px);"
                + "all and (min-width: 480px);"
                + "all and (min-width: 544px);"
                + "all and (min-width: 544px) and (max-width: 767px);"
                + "all and (min-width: 768px);"
                + "all and (min-width: 768px) and (max-width: 991px);"
                + "all and (min-width: 992px);"
                + "all and (min-width: 992px) and (max-width: 1199px);"
                + "all and (orientation: landscape) and (max-height: 480px);";
        realWorld("realworld/mui.css", 342, 752, media, 0, 0);
    }

    private void realWorld(final String resourceName, final int rules, final int properties,
                final String media,
                final int err, final int warn) throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);
        Assert.assertNotNull(is);

        final InputSource css = new InputSource(new InputStreamReader(is, "UTF-8"));
        final CSSStyleSheetImpl sheet = parse(css, err, 0, warn);

        final CSSRuleListImpl foundRules = sheet.getCssRules();
        Assert.assertEquals(rules, foundRules.getLength());

        int foundProperties = 0;
        for (int i = 0; i < foundRules.getLength(); i++) {
            final AbstractCSSRuleImpl rule = foundRules.getRules().get(i);
            if (rule instanceof CSSStyleRuleImpl) {
                foundProperties += ((CSSStyleRuleImpl) rule).getStyle().getLength();
            }
        }
        Assert.assertEquals(properties, foundProperties);

        final Set<String> mediaQ = new TreeSet<String>();
        for (int i = 0; i < sheet.getCssRules().getLength(); i++) {
            final AbstractCSSRuleImpl cssRule = sheet.getCssRules().getRules().get(i);
            if (cssRule instanceof CSSMediaRuleImpl) {
                final MediaListImpl mediaList = ((CSSMediaRuleImpl) cssRule).getMediaList();
                for (int j = 0; j < mediaList.getLength(); j++) {
                    final MediaQuery mediaQuery = mediaList.mediaQuery(j);
                    Assert.assertEquals(mediaQuery.toString(), mediaQuery.toString());
                    mediaQ.add(mediaQuery.toString());
                }
            }
        }
        final StringBuilder queries = new StringBuilder();
        for (String string : mediaQ) {
            queries.append(string);
            queries.append(";");
        }
        Assert.assertEquals(media, queries.toString());
    }

    /**
     * Test unicode input based on a byte stream.
     *
     * @throws Exception in case of failure
     */
    @Test
    public void unicodeInputByteStream() throws Exception {
        final String css = "h1:before { content: \"\u04c5 - \u0666\" }";

        final InputSource source = new InputSource(new ByteArrayInputStream(css.getBytes("UTF-8")), "UTF-8");
        final CSSStyleSheetImpl sheet = parse(source, 0, 0, 0);

        Assert.assertEquals(css, sheet.toString());
    }

    /**
     * Test unicode input based on a byte stream.
     *
     * @throws Exception in case of failure
     */
    @Test
    public void unicodeInputByteStreamDefaultEncoding() throws Exception {
        final String css = "h1:before { content: \"\u00fe - \u00e4\" }";

        final InputSource source = new InputSource(new ByteArrayInputStream(css.getBytes()), null);

        final CSSStyleSheetImpl sheet = parse(source, 0, 0, 0);

        Assert.assertEquals(css, sheet.toString());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void clip() throws Exception {
        final String css = "h1 { clip: rect(0px 0px 1px 1px); }";

        final InputSource source = new InputSource(new ByteArrayInputStream(css.getBytes()), null);
        final CSSStyleSheetImpl sheet = parse(source, 0, 0, 0);

        Assert.assertEquals("h1 { clip: rect(0px, 0px, 1px, 1px) }", sheet.toString());
    }

    /**
     * @throws Exception in case of failure
     */
    @Test
    public void brokenClip() throws Exception {
        final CSSStyleSheetImpl sheet = checkErrorSheet("h1 { clip: rect(0px); }",
                "DOM exception: 'Rect misses second parameter.'");

        Assert.assertEquals("h1 { }", sheet.toString());
    }

    /**
     * Test page selectors.
     *
     * @throws IOException in case of failure
     */
    @Test
    public void pageSelectors() throws IOException {
        pageSelectors("@page rotated { size : landscape }\n", "@page rotated {size: landscape}", 0, 0, 0);

        pageSelectors("@page { size : landscape }\n", "@page {size: landscape}", 0, 0, 0);
        pageSelectors("@page{ size : landscape }\n", "@page {size: landscape}", 0, 0, 0);
        pageSelectors("@page  \t { size : landscape }\n", "@page {size: landscape}", 0, 0, 0);

        pageSelectors("@page :left { size : landscape }\n", "@page :left {size: landscape}", 0, 0, 0);
        pageSelectors("@page :left{ size : landscape }\n", "@page :left {size: landscape}", 0, 0, 0);
        pageSelectors("@page:left { size : landscape }\n", "@page :left {size: landscape}", 0, 0, 0);

        pageSelectors("@page :left:right { size : landscape }\n", "@page :left:right {size: landscape}", 0, 0, 0);
        pageSelectors("@page toc, :left:right { size : landscape }\n",
                "@page toc, :left:right {size: landscape}", 0, 0, 0);
        pageSelectors("@page toc,:left:right { size : landscape }\n",
                "@page toc, :left:right {size: landscape}", 0, 0, 0);
        pageSelectors("@page :left:right, toc:right { size : landscape }\n",
                "@page :left:right, toc:right {size: landscape}", 0, 0, 0);
        pageSelectors("@page toc:first { size : landscape }\n", "@page toc:first {size: landscape}", 0, 0, 0);
        pageSelectors("@page toc,    index   { size : landscape }\n", "@page toc, index {size: landscape}", 0, 0, 0);

        // invalid
        pageSelectors("@page :left :right { size : landscape }\n", "@page :left:right {size: landscape}", 1, 0, 1);
        pageSelectors("@page toc :left{ size : landscape }\n", "@page :left:right {size: landscape}", 1, 0, 1);
        pageSelectors("@page toc index { size : landscape }\n", "@page :left:right {size: landscape}", 1, 0, 1);
    }

    private void pageSelectors(final String css, final String expected,
            final int err, final int fatal, final int warn) throws IOException {
        final CSSStyleSheetImpl sheet = parse(css, err, fatal, warn);
        final CSSRuleListImpl rules = sheet.getCssRules();

        if (err == 0) {
            Assert.assertEquals(1, rules.getLength());
            final AbstractCSSRuleImpl rule = rules.getRules().get(0);
            Assert.assertEquals(expected, rule.getCssText());
        }
    }

    /**
     * Test keyframes at rule.
     *
     * @throws IOException in case of failure
     */
    @Test
    public void keyframe() throws IOException {
        final String css = "@keyframes background900 { "
                + "0% { background-position:0 0; } 100% { background-position:0 -900px; } }";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("@keyframes background900 { "
                + "0% { background-position:0 0; } 100% { background-position:0 -900px; } }",
                   rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void commaSeparatedTerms() throws Exception {
        final String css =  "h1 { box-shadow: inset 0 0 1em gold, 0 0 1em red }\n"
                            + "h2 { box-shadow: 0 0 0 1px #fff, 0 0 0 1px silver }\n"
                            + "h3 { box-shadow: 0 0 0 1px #fff / 0 0 0 1px silver }\n";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(3, rules.getLength());

        AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("h1 { box-shadow: inset 0 0 1em gold, 0 0 1em red }", rule.getCssText());

        rule = rules.getRules().get(1);
        Assert.assertEquals("h2 { box-shadow: 0 0 0 1px rgb(255, 255, 255), 0 0 0 1px silver }", rule.getCssText());

        rule = rules.getRules().get(2);
        Assert.assertEquals("h3 { box-shadow: 0 0 0 1px rgb(255, 255, 255) / 0 0 0 1px silver }", rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void errorAtStart() throws Exception {
        final String css = "// comments \n"
                + "h1 { color: red; }"
                + "h2 { color: blue; }";
        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("h2 { color: blue }", rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void errorAtStart2() throws Exception {
        final String css = "& test\n"
                + "h1 { color: red; }"
                + "h2 { color: blue; }";
        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("h2 { color: blue }", rule.getCssText());
    }

    /**
     * @throws Exception if any error occurs
     */
    @Test
    public void errorAtStartOnly() throws Exception {
        final String css = "// comments";
        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(0, rules.getLength());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void pseudoElement() throws Exception {
        final String css = "h1:first-line { color: red }\n";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("h1:first-line { color: red }", rule.toString());
        Assert.assertEquals("h1:first-line { color: red }", rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void doubleColonsPseudoElement() throws Exception {
        final String css = "h1::first-line { color: red }\n";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("h1::first-line { color: red }", rule.toString());
        Assert.assertEquals("h1::first-line { color: red }", rule.getCssText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void doubleColonsPseudoClass() throws Exception {
        final String css = "h1::link { color: red }\n";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());

        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        Assert.assertEquals("h1::link { color: red }", rule.toString());
        Assert.assertEquals("h1::link { color: red }", rule.getCssText());
    }

//
//    /**
//     * @throws Exception if any error occurs
//     */
//    @Test
//    public void fakeSingleLineCommentBetweenRules() throws Exception {
//        final String css =
//                "h1 { color: red; }"
//                + "// comment"
//                + "h2 { color: blue; }";
//        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
//        final CSSRuleListImpl rules = sheet.getCssRules();
//
//        Assert.assertEquals(1, rules.getLength());
//
//        final CSSRule rule = rules.getRules().get(0);
//        Assert.assertEquals("p { color: red; background: green }", rule.getCssText());
//    }
//
//    /**
//     * @throws Exception if any error occurs
//     */
//    @Test
//    public void fakeSingleLineCommentInsideRule() throws Exception {
//        final String css =
//                "h1 {\n"
//                + " color: red; \n"
//                + "  // comment \n"
//                + " background: blue;\n"
//                + " height: 20px;\n"
//                + "}";
//        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
//        final CSSRuleListImpl rules = sheet.getCssRules();
//
//        Assert.assertEquals(1, rules.getLength());
//
//        final CSSRule rule = rules.getRules().get(0);
//        Assert.assertEquals("h1 { color: red; height: 20px }", rule.getCssText());
//    }
//
//    /**
//     * @throws Exception if any error occurs
//     */
//    @Test
//    public void fakeSingleLineCommentBeforeFirstRule() throws Exception {
//        final String css =
//                "h1 {\n"
//                + "  // comment \n"
//                + " background: blue;\n"
//                + " height: 20px;\n"
//                + "}";
//        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
//        final CSSRuleListImpl rules = sheet.getCssRules();
//
//        Assert.assertEquals(1, rules.getLength());
//
//        final CSSRule rule = rules.getRules().get(0);
//        Assert.assertEquals("h1 { height: 20px }", rule.getCssText());
//    }
//
//    /**
//     * @throws Exception if any error occurs
//     */
//    @Test
//    public void fakeSingleLineCommentAsLastRule() throws Exception {
//        final String css =
//                "h1 {\n"
//                + " color: red; \n"
//                + " background: blue;\n"
//                + " height: 20px;\n"
//                + "  // comment \n"
//                + "}";
//        final CSSStyleSheetImpl sheet = parse(css, 1, 0, 1);
//        final CSSRuleListImpl rules = sheet.getCssRules();
//
//        Assert.assertEquals(1, rules.getLength());
//
//        final CSSRule rule = rules.getRules().get(0);
//        Assert.assertEquals("h1 { color: red; height: 20px }", rule.getCssText());
//    }
}
