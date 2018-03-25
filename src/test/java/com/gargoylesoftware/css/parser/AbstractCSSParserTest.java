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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import com.gargoylesoftware.css.ErrorHandler;
import com.gargoylesoftware.css.dom.CSSStyleDeclarationImpl;
import com.gargoylesoftware.css.dom.CSSStyleRuleImpl;
import com.gargoylesoftware.css.dom.CSSValueImpl;
import com.gargoylesoftware.css.dom.Property;
import com.gargoylesoftware.css.parser.condition.AttributeCondition;
import com.gargoylesoftware.css.parser.condition.BeginHyphenAttributeCondition;
import com.gargoylesoftware.css.parser.condition.ClassCondition;
import com.gargoylesoftware.css.parser.condition.Condition;
import com.gargoylesoftware.css.parser.condition.Condition.ConditionType;
import com.gargoylesoftware.css.parser.condition.IdCondition;
import com.gargoylesoftware.css.parser.condition.OneOfAttributeCondition;
import com.gargoylesoftware.css.parser.condition.PrefixAttributeCondition;
import com.gargoylesoftware.css.parser.condition.PseudoClassCondition;
import com.gargoylesoftware.css.parser.condition.SubstringAttributeCondition;
import com.gargoylesoftware.css.parser.condition.SuffixAttributeCondition;
import com.gargoylesoftware.css.parser.media.MediaQueryList;
import com.gargoylesoftware.css.parser.selector.DescendantSelector;
import com.gargoylesoftware.css.parser.selector.ElementSelector;
import com.gargoylesoftware.css.parser.selector.Selector;
import com.gargoylesoftware.css.parser.selector.Selector.SelectorType;
import com.gargoylesoftware.css.parser.selector.SelectorList;
import com.gargoylesoftware.css.parser.selector.SimpleSelector;

/**
 * @author Ronald Brill
 */
public abstract class AbstractCSSParserTest {

    private Locale systemLocale_;

    /**
     * {@inheritDoc}
     */
    @Before
    public void setUp() {
        systemLocale_ = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
    }

    /**
     * {@inheritDoc}
     */
    @After
    public void tearDown() {
        Locale.setDefault(systemLocale_);
    }

    /**
     * Helper.
     *
     * @param css the css string to parse
     * @return the style sheet
     * @throws IOException if any error occurs
     */
    protected CSSStyleSheet parse(final String css) throws IOException {
        return parse(css, 0, 0, 0);
    }

    /**
     * Helper.
     *
     * @param css the css input stream to parse
     * @return the style sheet
     * @throws IOException if any error occurs
     */
    protected CSSStyleSheet parse(final InputStream css) throws IOException {
        return parse(css, 0, 0, 0);
    }

    /**
     * Helper.
     *
     * @param css the css string to parse
     * @param err the number of expected errors
     * @param fatal the number of expected fatal errors
     * @param warn the number of expected warnings
     * @return the style sheet
     * @throws IOException if any error occurs
     */
    protected CSSStyleSheet parse(final String css, final int err, final int fatal, final int warn) throws IOException {
        final InputSource source = new InputSource(new StringReader(css));
        return parse(source, err, fatal, warn);
    }

    /**
     * Helper.
     *
     * @param css the css input stream to parse
     * @param err the number of expected errors
     * @param fatal the number of expected fatal errors
     * @param warn the number of expected warnings
     * @return the style sheet
     * @throws IOException if any error occurs
     */
    protected CSSStyleSheet parse(final InputStream css,
            final int err, final int fatal, final int warn) throws IOException {
        final InputSource source = new InputSource(new InputStreamReader(css));
        return parse(source, err, fatal, warn);
    }

    protected CSSStyleSheet parse(final InputSource source,
            final int err, final int fatal, final int warn) throws IOException {
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(err, errorHandler.getErrorCount());
        Assert.assertEquals(fatal, errorHandler.getFatalErrorCount());
        Assert.assertEquals(warn, errorHandler.getWarningCount());

        return sheet;
    }

    protected MediaQueryList parseMedia(final String css,
            final int err, final int fatal, final int warn) throws IOException {
        final InputSource source = new InputSource(new StringReader(css));
        return parseMedia(source, err, fatal, warn);
    }

    protected MediaQueryList parseMedia(final InputSource source,
            final int err, final int fatal, final int warn) throws IOException {
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final MediaQueryList mediaList = parser.parseMedia(source);

        Assert.assertEquals(err, errorHandler.getErrorCount());
        Assert.assertEquals(fatal, errorHandler.getFatalErrorCount());
        Assert.assertEquals(warn, errorHandler.getWarningCount());

        return mediaList;
    }

    protected SelectorList createSelectors(final String cssText) throws Exception {
        final InputSource source = new InputSource(new StringReader(cssText));
        return new CSSOMParser().parseSelectors(source);
    }

    protected List<Condition> createConditions(final String cssText) throws Exception {
        final SelectorList selectors = createSelectors(cssText);
        final Selector selector = selectors.get(0);
        final ElementSelector elementSelector = (ElementSelector) selector;
        return elementSelector.getConditions();
    }

    protected void conditionType(final String cssText, final ConditionType... conditionTypes) throws Exception {
        final List<Condition> conditions = createConditions(cssText);

        for (int i = 0; i < conditionTypes.length; i++) {
            Assert.assertEquals(conditionTypes[i], conditions.get(i).getConditionType());
        }
    }

    protected void conditionAssert(final String cssText, final String name,
            final String value, final boolean specified) throws Exception {

        final List<Condition> conditions = createConditions(cssText);
        Assert.assertEquals(1, conditions.size());

        final Condition condition = conditions.get(0);
        switch (condition.getConditionType()) {
            case ATTRIBUTE_CONDITION:
                final AttributeCondition attributeCondition = (AttributeCondition) condition;
                Assert.assertEquals(name, attributeCondition.getLocalName());
                Assert.assertEquals(value, attributeCondition.getValue());
                break;
            case PSEUDO_CLASS_CONDITION:
                final PseudoClassCondition pseudoClassCondition = (PseudoClassCondition) condition;
                Assert.assertEquals(name, pseudoClassCondition.getLocalName());
                Assert.assertEquals(value, pseudoClassCondition.getValue());
                break;
            case CLASS_CONDITION:
                final ClassCondition classCondition = (ClassCondition) condition;
                Assert.assertEquals(name, classCondition.getLocalName());
                Assert.assertEquals(value, classCondition.getValue());
                break;
            case ID_CONDITION:
                final IdCondition idCondition = (IdCondition) condition;
                Assert.assertEquals(name, idCondition.getLocalName());
                Assert.assertEquals(value, idCondition.getValue());
                break;
            case ONE_OF_ATTRIBUTE_CONDITION:
                final OneOfAttributeCondition oneOfAttributeCondition = (OneOfAttributeCondition) condition;
                Assert.assertEquals(name, oneOfAttributeCondition.getLocalName());
                Assert.assertEquals(value, oneOfAttributeCondition.getValue());
                break;
            case BEGIN_HYPHEN_ATTRIBUTE_CONDITION:
                final BeginHyphenAttributeCondition beginHyphenAttributeCondition
                                = (BeginHyphenAttributeCondition) condition;
                Assert.assertEquals(name, beginHyphenAttributeCondition.getLocalName());
                Assert.assertEquals(value, beginHyphenAttributeCondition.getValue());
                break;
            case PREFIX_ATTRIBUTE_CONDITION:
                final PrefixAttributeCondition prefixAttributeCondition = (PrefixAttributeCondition) condition;
                Assert.assertEquals(name, prefixAttributeCondition.getLocalName());
                Assert.assertEquals(value, prefixAttributeCondition.getValue());
                break;
            case SUFFIX_ATTRIBUTE_CONDITION:
                final SuffixAttributeCondition suffixAttributeCondition = (SuffixAttributeCondition) condition;
                Assert.assertEquals(name, suffixAttributeCondition.getLocalName());
                Assert.assertEquals(value, suffixAttributeCondition.getValue());
                break;
            case SUBSTRING_ATTRIBUTE_CONDITION:
                final SubstringAttributeCondition substringAttributeCondition = (SubstringAttributeCondition) condition;
                Assert.assertEquals(name, substringAttributeCondition.getLocalName());
                Assert.assertEquals(value, substringAttributeCondition.getValue());
                break;

            default:
                Assert.fail("unsupported condition type " + condition.getConditionType());
                break;
        }
    }

    protected void selectorList(final String cssText, final int length) throws Exception {
        final SelectorList selectors = createSelectors(cssText);
        Assert.assertEquals(length, selectors.size());
    }

    protected void selectorType(final String cssText, final SelectorType... selectorTypes) throws Exception {
        final SelectorList selectors = createSelectors(cssText);
        final Selector selector = selectors.get(0);
        Assert.assertEquals(selectorTypes[0], selector.getSelectorType());
        if (selectorTypes[0] == SelectorType.DESCENDANT_SELECTOR) {
            final DescendantSelector descendantSelector = (DescendantSelector) selector;
            final Selector ancestor = descendantSelector.getAncestorSelector();
            Assert.assertEquals(selectorTypes[1], ancestor.getSelectorType());
            final SimpleSelector simple = descendantSelector.getSimpleSelector();
            Assert.assertEquals(selectorTypes[2], simple.getSelectorType());
        }
    }

    protected void checkErrorSelector(final String input, final String errorMsg) throws IOException {
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(input));
        final SelectorList selectors = parser.parseSelectors(source);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        Assert.assertEquals(errorMsg, errorHandler.getErrorMessage());
        Assert.assertNull(selectors);
    }

    protected CSSStyleSheet checkErrorSheet(final String input, final String errorMsg) throws IOException {
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(input));
        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        Assert.assertEquals(errorMsg, errorHandler.getErrorMessage());
        return sheet;
    }

    protected CSSValueImpl dimension(final String dim) throws Exception {
        final String css = "*.dim { top: " + dim + " }";

        final CSSStyleSheet sheet = parse(css);
        final CSSRuleList rules = sheet.getCssRules();

        Assert.assertEquals(1, rules.getLength());
        final CSSRule rule = rules.item(0);
        Assert.assertEquals(css, rule.getCssText());

        final CSSStyleRuleImpl ruleImpl = (CSSStyleRuleImpl) rule;
        final CSSStyleDeclarationImpl declImpl = (CSSStyleDeclarationImpl) ruleImpl.getStyle();
        final Property prop = declImpl.getPropertyDeclaration("top");
        final CSSValueImpl valueImpl = (CSSValueImpl) prop.getValue();

        return valueImpl;
    }
}
