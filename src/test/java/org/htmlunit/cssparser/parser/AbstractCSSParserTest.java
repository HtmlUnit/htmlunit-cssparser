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
package org.htmlunit.cssparser.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import java.util.Locale;

import org.htmlunit.cssparser.ErrorHandler;
import org.htmlunit.cssparser.dom.AbstractCSSRuleImpl;
import org.htmlunit.cssparser.dom.CSSRuleListImpl;
import org.htmlunit.cssparser.dom.CSSStyleDeclarationImpl;
import org.htmlunit.cssparser.dom.CSSStyleRuleImpl;
import org.htmlunit.cssparser.dom.CSSStyleSheetImpl;
import org.htmlunit.cssparser.dom.CSSValueImpl;
import org.htmlunit.cssparser.dom.Property;
import org.htmlunit.cssparser.parser.condition.AttributeCondition;
import org.htmlunit.cssparser.parser.condition.BeginHyphenAttributeCondition;
import org.htmlunit.cssparser.parser.condition.ClassCondition;
import org.htmlunit.cssparser.parser.condition.Condition;
import org.htmlunit.cssparser.parser.condition.Condition.ConditionType;
import org.htmlunit.cssparser.parser.condition.IdCondition;
import org.htmlunit.cssparser.parser.condition.OneOfAttributeCondition;
import org.htmlunit.cssparser.parser.condition.PrefixAttributeCondition;
import org.htmlunit.cssparser.parser.condition.PseudoClassCondition;
import org.htmlunit.cssparser.parser.condition.SubstringAttributeCondition;
import org.htmlunit.cssparser.parser.condition.SuffixAttributeCondition;
import org.htmlunit.cssparser.parser.media.MediaQueryList;
import org.htmlunit.cssparser.parser.selector.DescendantSelector;
import org.htmlunit.cssparser.parser.selector.ElementSelector;
import org.htmlunit.cssparser.parser.selector.Selector;
import org.htmlunit.cssparser.parser.selector.Selector.SelectorType;
import org.htmlunit.cssparser.parser.selector.SelectorList;
import org.htmlunit.cssparser.parser.selector.SimpleSelector;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Ronald Brill
 */
public abstract class AbstractCSSParserTest {

    private Locale systemLocale_;

    /**
     * Before each test.
     */
    @BeforeEach
    public void setUp() {
        systemLocale_ = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
    }

    /**
     * After each test.
     */
    @AfterEach
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
    protected CSSStyleSheetImpl parse(final String css) throws IOException {
        return parse(css, 0, 0, 0);
    }

    /**
     * Helper.
     *
     * @param css the css input stream to parse
     * @return the style sheet
     * @throws IOException if any error occurs
     */
    protected CSSStyleSheetImpl parse(final InputStream css) throws IOException {
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
    protected CSSStyleSheetImpl parse(final String css, final int err, final int fatal, final int warn) throws IOException {
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
    protected CSSStyleSheetImpl parse(final InputStream css,
            final int err, final int fatal, final int warn) throws IOException {
        final InputSource source = new InputSource(new InputStreamReader(css));
        return parse(source, err, fatal, warn);
    }

    protected CSSStyleSheetImpl parse(final InputSource source,
            final int err, final int fatal, final int warn) throws IOException {
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        assertEquals(err, errorHandler.getErrorCount());
        assertEquals(fatal, errorHandler.getFatalErrorCount());
        assertEquals(warn, errorHandler.getWarningCount());

        return sheet;
    }

    protected MediaQueryList parseMedia(final String media,
            final int err, final int fatal, final int warn) throws IOException {
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final MediaQueryList mediaList = parser.parseMedia(media);

        assertEquals(err, errorHandler.getErrorCount());
        assertEquals(fatal, errorHandler.getFatalErrorCount());
        assertEquals(warn, errorHandler.getWarningCount());

        return mediaList;
    }

    protected SelectorList createSelectors(final String cssText) throws Exception {
        return new CSSOMParser().parseSelectors(cssText);
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
            assertEquals(conditionTypes[i], conditions.get(i).getConditionType());
        }
    }

    protected void conditionAssert(final String cssText, final String name,
            final String value, final boolean specified) throws Exception {

        final List<Condition> conditions = createConditions(cssText);
        assertEquals(1, conditions.size());

        final Condition condition = conditions.get(0);
        switch (condition.getConditionType()) {
            case ATTRIBUTE_CONDITION:
                final AttributeCondition attributeCondition = (AttributeCondition) condition;
                assertEquals(name, attributeCondition.getLocalName());
                assertEquals(value, attributeCondition.getValue());
                break;
            case PSEUDO_CLASS_CONDITION:
                final PseudoClassCondition pseudoClassCondition = (PseudoClassCondition) condition;
                assertEquals(name, pseudoClassCondition.getLocalName());
                assertEquals(value, pseudoClassCondition.getValue());
                break;
            case CLASS_CONDITION:
                final ClassCondition classCondition = (ClassCondition) condition;
                assertEquals(name, classCondition.getLocalName());
                assertEquals(value, classCondition.getValue());
                break;
            case ID_CONDITION:
                final IdCondition idCondition = (IdCondition) condition;
                assertEquals(name, idCondition.getLocalName());
                assertEquals(value, idCondition.getValue());
                break;
            case ONE_OF_ATTRIBUTE_CONDITION:
                final OneOfAttributeCondition oneOfAttributeCondition = (OneOfAttributeCondition) condition;
                assertEquals(name, oneOfAttributeCondition.getLocalName());
                assertEquals(value, oneOfAttributeCondition.getValue());
                break;
            case BEGIN_HYPHEN_ATTRIBUTE_CONDITION:
                final BeginHyphenAttributeCondition beginHyphenAttributeCondition
                                = (BeginHyphenAttributeCondition) condition;
                assertEquals(name, beginHyphenAttributeCondition.getLocalName());
                assertEquals(value, beginHyphenAttributeCondition.getValue());
                break;
            case PREFIX_ATTRIBUTE_CONDITION:
                final PrefixAttributeCondition prefixAttributeCondition = (PrefixAttributeCondition) condition;
                assertEquals(name, prefixAttributeCondition.getLocalName());
                assertEquals(value, prefixAttributeCondition.getValue());
                break;
            case SUFFIX_ATTRIBUTE_CONDITION:
                final SuffixAttributeCondition suffixAttributeCondition = (SuffixAttributeCondition) condition;
                assertEquals(name, suffixAttributeCondition.getLocalName());
                assertEquals(value, suffixAttributeCondition.getValue());
                break;
            case SUBSTRING_ATTRIBUTE_CONDITION:
                final SubstringAttributeCondition substringAttributeCondition = (SubstringAttributeCondition) condition;
                assertEquals(name, substringAttributeCondition.getLocalName());
                assertEquals(value, substringAttributeCondition.getValue());
                break;

            default:
                fail("unsupported condition type " + condition.getConditionType());
                break;
        }
    }

    protected void selectorList(final String cssText, final int length) throws Exception {
        final SelectorList selectors = createSelectors(cssText);
        assertEquals(length, selectors.size());
    }

    protected void selectorType(final String cssText, final SelectorType... selectorTypes) throws Exception {
        final SelectorList selectors = createSelectors(cssText);
        final Selector selector = selectors.get(0);
        assertEquals(selectorTypes[0], selector.getSelectorType());
        if (selectorTypes[0] == SelectorType.DESCENDANT_SELECTOR) {
            final DescendantSelector descendantSelector = (DescendantSelector) selector;
            final Selector ancestor = descendantSelector.getAncestorSelector();
            assertEquals(selectorTypes[1], ancestor.getSelectorType());
            final SimpleSelector simple = descendantSelector.getSimpleSelector();
            assertEquals(selectorTypes[2], simple.getSelectorType());
        }
    }

    protected void checkErrorSelector(final String input, final String errorMsg) throws IOException {
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final SelectorList selectors = parser.parseSelectors(input);

        assertEquals(1, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        assertEquals(errorMsg, errorHandler.getErrorMessage());
        assertNull(selectors);
    }

    protected CSSStyleSheetImpl checkErrorSheet(final String input, final String errorMsg) throws IOException {
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final InputSource source = new InputSource(new StringReader(input));
        final CSSStyleSheetImpl sheet = parser.parseStyleSheet(source, null);

        assertEquals(1, errorHandler.getErrorCount());
        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        assertEquals(errorMsg, errorHandler.getErrorMessage());
        return sheet;
    }

    protected CSSValueImpl dimension(final String dim) throws Exception {
        final String css = "*.dim { top: " + dim + "; }";

        final CSSStyleSheetImpl sheet = parse(css);
        final CSSRuleListImpl rules = sheet.getCssRules();

        assertEquals(1, rules.getLength());
        final AbstractCSSRuleImpl rule = rules.getRules().get(0);
        assertEquals(css, rule.getCssText());

        final CSSStyleRuleImpl ruleImpl = (CSSStyleRuleImpl) rule;
        final CSSStyleDeclarationImpl declImpl = ruleImpl.getStyle();
        final Property prop = declImpl.getPropertyDeclaration("top");
        final CSSValueImpl valueImpl = prop.getValue();

        return valueImpl;
    }

    protected void color(final String expected, final String cssText) throws Exception {
        color(0, expected, cssText);
    }

    protected void color(final int errorCount, final String expected, final String cssText) throws Exception {
        final CSSOMParser parser = new CSSOMParser();
        final ErrorHandler errorHandler = new ErrorHandler();
        parser.setErrorHandler(errorHandler);

        final CSSStyleDeclarationImpl style = parser.parseStyleDeclaration(cssText);

        if (errorCount == 0 && errorHandler.getErrorCount() > 0) {
            Assertions.fail("Found errors; First: " + errorHandler.getErrorMessage());
        }
        assertEquals(errorCount, errorHandler.getErrorCount());
        if (errorCount > 0) {
            assertEquals(expected, errorHandler.getErrorMessage());
        }

        assertEquals(0, errorHandler.getFatalErrorCount());
        assertEquals(0, errorHandler.getWarningCount());

        if (errorCount > 0) {
            return;
        }

        // Enumerate the properties and retrieve their values
        assertEquals(1, style.getLength());

        final String name = style.getProperties().get(0).getName();
        assertEquals(expected, name + ": " + style.getPropertyValue(name));
    }
}
