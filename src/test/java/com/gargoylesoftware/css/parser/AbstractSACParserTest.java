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
import com.gargoylesoftware.css.parser.condition.AndConditionImpl;
import com.gargoylesoftware.css.parser.condition.AttributeCondition;
import com.gargoylesoftware.css.parser.condition.Condition;
import com.gargoylesoftware.css.parser.condition.Condition.ConditionType;
import com.gargoylesoftware.css.parser.media.SACMediaList;
import com.gargoylesoftware.css.parser.selector.ConditionalSelector;
import com.gargoylesoftware.css.parser.selector.DescendantSelector;
import com.gargoylesoftware.css.parser.selector.Selector;
import com.gargoylesoftware.css.parser.selector.Selector.SelectorType;
import com.gargoylesoftware.css.parser.selector.SelectorList;
import com.gargoylesoftware.css.parser.selector.SimpleSelector;

/**
 * @author Ronald Brill
 */
public abstract class AbstractSACParserTest {

    private Locale systemLocale_;

    @Before
    public void setUp() {
        systemLocale_ = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
    }

    @After
    public void tearDown() {
        Locale.setDefault(systemLocale_);
    }

    protected CSSOMParser parser() {
        return new CSSOMParser();
    }

    protected CSSStyleSheet parse(final String css) throws IOException {
        return parse(css, 0, 0, 0);
    }

    protected CSSStyleSheet parse(final InputStream css) throws IOException {
        return parse(css, 0, 0, 0);
    }

    protected CSSStyleSheet parse(final String css, final int err, final int fatal, final int warn) throws IOException {
        final InputSource source = new InputSource(new StringReader(css));
        return parse(source, err, fatal, warn);
    }

    protected CSSStyleSheet parse(final InputStream css,
            final int err, final int fatal, final int warn) throws IOException {
        final InputSource source = new InputSource(new InputStreamReader(css));
        return parse(source, err, fatal, warn);
    }

    protected CSSStyleSheet parse(final InputSource source,
            final int err, final int fatal, final int warn) throws IOException {
        final CSSOMParser parser = parser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(err, errorHandler.getErrorCount());
        Assert.assertEquals(fatal, errorHandler.getFatalErrorCount());
        Assert.assertEquals(warn, errorHandler.getWarningCount());

        return sheet;
    }

    protected SACMediaList parseMedia(final String css,
            final int err, final int fatal, final int warn) throws IOException {
        final InputSource source = new InputSource(new StringReader(css));
        return parseMedia(source, err, fatal, warn);
    }

    protected SACMediaList parseMedia(final InputSource source,
            final int err, final int fatal, final int warn) throws IOException {
        final CSSOMParser parser = parser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final SACMediaList mediaList = parser.parseMedia(source);

        Assert.assertEquals(err, errorHandler.getErrorCount());
        Assert.assertEquals(fatal, errorHandler.getFatalErrorCount());
        Assert.assertEquals(warn, errorHandler.getWarningCount());

        return mediaList;
    }

    protected SelectorList createSelectors(final String cssText) throws Exception {
        final InputSource source = new InputSource(new StringReader(cssText));
        return parser().parseSelectors(source);
    }

    protected Condition createCondition(final String cssText) throws Exception {
        final SelectorList selectors = createSelectors(cssText);
        final Selector selector = selectors.item(0);
        final ConditionalSelector conditionalSelector = (ConditionalSelector) selector;
        return conditionalSelector.getCondition();
    }

    protected void conditionType(final String cssText, final ConditionType... conditionTypes) throws Exception {
        final Condition condition = createCondition(cssText);
        conditionType(condition, 0, conditionTypes);
    }

    protected int conditionType(final Condition condition, int initial, final ConditionType... conditionTypes) {
        Assert.assertEquals(conditionTypes[initial], condition.getConditionType());
        if (conditionTypes[initial] == ConditionType.AND_CONDITION) {
            final AndConditionImpl combinatorCondition = (AndConditionImpl) condition;
            final Condition first = combinatorCondition.getFirstCondition();
            final Condition second = combinatorCondition.getSecondCondition();
            initial = conditionType(first, ++initial, conditionTypes);
            initial = conditionType(second, ++initial, conditionTypes);
        }
        return initial;
    }

    protected void conditionAssert(final String cssText, final String name,
            final String value, final boolean specified) throws Exception {
        final Condition condition = createCondition(cssText);
        final AttributeCondition attributeCondition = (AttributeCondition) condition;
        Assert.assertEquals(name, attributeCondition.getLocalName());
        Assert.assertEquals(value, attributeCondition.getValue());
    }

    protected void selectorList(final String cssText, final int length) throws Exception {
        final SelectorList selectors = createSelectors(cssText);
        Assert.assertEquals(length, selectors.getLength());
    }

    protected void selectorType(final String cssText, final SelectorType... selectorTypes) throws Exception {
        final SelectorList selectors = createSelectors(cssText);
        final Selector selector = selectors.item(0);
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
        final CSSOMParser parser = parser();
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
        final CSSOMParser parser = parser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(input));
        final CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);

        Assert.assertEquals(1, errorHandler.getErrorCount());
        Assert.assertEquals(0, errorHandler.getFatalErrorCount());
        Assert.assertEquals(0, errorHandler.getWarningCount());

        Assert.assertEquals(errorMsg, errorHandler.getErrorMessage());
        return sheet;
    }

    protected CSSValueImpl dimension(final String dim) throws Exception {
        final String css = ".dim { top: " + dim + " }";

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
