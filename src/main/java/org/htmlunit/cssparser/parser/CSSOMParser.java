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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

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
import org.htmlunit.cssparser.dom.MediaListImpl;
import org.htmlunit.cssparser.dom.Property;
import org.htmlunit.cssparser.parser.javacc.CSS3Parser;
import org.htmlunit.cssparser.parser.media.MediaQueryList;
import org.htmlunit.cssparser.parser.selector.SelectorList;
import org.w3c.dom.DOMException;

/**
 * @author Ronald Brill
 */
public class CSSOMParser {

    private final AbstractCSSParser parser_;
    private CSSStyleSheetImpl parentStyleSheet_;

    /**
     * Creates new CSSOMParser.
     * @param parser the parser
     */
    public CSSOMParser(final AbstractCSSParser parser) {
        parser_ = parser;
    }

    /**
     * Creates new CSSOMParser.
     */
    public CSSOMParser() {
        parser_ = new CSS3Parser();
    }

    /**
     * <p>setErrorHandler.</p>
     *
     * @param eh the error handler to be used
     */
    public void setErrorHandler(final CSSErrorHandler eh) {
        parser_.setErrorHandler(eh);
    }

    /**
     * Parses a SAC input source into a CSSOM style sheet.
     *
     * @param source the SAC input source
     * @param href the href
     * @return the CSSOM style sheet
     * @throws IOException if the underlying SAC parser throws an IOException
     */
    public CSSStyleSheetImpl parseStyleSheet(final InputSource source, final String href) throws IOException {
        final CSSOMHandler handler = new CSSOMHandler();
        handler.setHref(href);
        parser_.setDocumentHandler(handler);
        parser_.parseStyleSheet(source);
        final Object o = handler.getRoot();
        if (o instanceof CSSStyleSheetImpl) {
            return (CSSStyleSheetImpl) o;
        }
        return null;
    }

    /**
     * Parses a input string into a CSSOM style declaration.
     *
     * @param styleDecl the input string
     * @return the CSSOM style declaration
     * @throws IOException if the underlying SAC parser throws an IOException
     */
    public CSSStyleDeclarationImpl parseStyleDeclaration(final String styleDecl) throws IOException {
        final CSSStyleDeclarationImpl sd = new CSSStyleDeclarationImpl(null);
        parseStyleDeclaration(sd, styleDecl);
        return sd;
    }

    /**
     * Parses a input string into a CSSOM style declaration.
     *
     * @param styleDecl the input string
     * @param sd the CSSOM style declaration
     * @throws IOException if the underlying SAC parser throws an IOException
     */
    public void parseStyleDeclaration(final CSSStyleDeclarationImpl sd, final String styleDecl) throws IOException {
        try (InputSource source = new InputSource(new StringReader(styleDecl))) {
            final Deque<Object> nodeStack = new ArrayDeque<>();
            nodeStack.push(sd);
            final CSSOMHandler handler = new CSSOMHandler(nodeStack);
            parser_.setDocumentHandler(handler);
            parser_.parseStyleDeclaration(source);
        }
    }

    /**
     * Parses a input string into a CSSValue.
     *
     * @param propertyValue the input string
     * @return the css value
     * @throws IOException if the underlying SAC parser throws an IOException
     */
    public CSSValueImpl parsePropertyValue(final String propertyValue) throws IOException {
        try (InputSource source = new InputSource(new StringReader(propertyValue))) {
            final CSSOMHandler handler = new CSSOMHandler();
            parser_.setDocumentHandler(handler);
            final LexicalUnit lu = parser_.parsePropertyValue(source);
            if (null == lu) {
                return null;
            }
            return new CSSValueImpl(lu);
        }
    }

    /**
     * Parses a string into a CSSRule.
     *
     * @param rule the input string
     * @return the css rule
     * @throws IOException if the underlying SAC parser throws an IOException
     */
    public AbstractCSSRuleImpl parseRule(final String rule) throws IOException {
        try (InputSource source = new InputSource(new StringReader(rule))) {
            final CSSOMHandler handler = new CSSOMHandler();
            parser_.setDocumentHandler(handler);
            parser_.parseRule(source);
            return (AbstractCSSRuleImpl) handler.getRoot();
        }
    }

    /**
     * Parses a string into a CSSSelectorList.
     *
     * @param selectors the input string
     * @return the css selector list
     * @throws IOException if the underlying SAC parser throws an IOException
     */
    public SelectorList parseSelectors(final String selectors) throws IOException {
        try (InputSource source = new InputSource(new StringReader(selectors))) {
            final HandlerBase handler = new HandlerBase();
            parser_.setDocumentHandler(handler);
            return parser_.parseSelectors(source);
        }
    }

    /**
     * Parses a string into a MediaQueryList.
     *
     * @param media the input string
     * @return the css media query list
     * @throws IOException if the underlying SAC parser throws an IOException
     */
    public MediaQueryList parseMedia(final String media) throws IOException {
        try (InputSource source = new InputSource(new StringReader(media))) {
            final HandlerBase handler = new HandlerBase();
            parser_.setDocumentHandler(handler);
            return parser_.parseMedia(source);
        }
    }

    /**
     * <p>setParentStyleSheet.</p>
     *
     * @param parentStyleSheet the new parent stylesheet
     */
    public void setParentStyleSheet(final CSSStyleSheetImpl parentStyleSheet) {
        parentStyleSheet_ = parentStyleSheet;
    }

    /**
     * <p>getParentStyleSheet.</p>
     *
     * @return the parent style sheet
     */
    protected CSSStyleSheetImpl getParentStyleSheet() {
        return parentStyleSheet_;
    }

    class CSSOMHandler implements DocumentHandler {
        private final Deque<Object> nodeStack_;
        private Object root_;
        private String href_;

        private String getHref() {
            return href_;
        }

        private void setHref(final String href) {
            href_ = href;
        }

        CSSOMHandler(final Deque<Object> nodeStack) {
            nodeStack_ = nodeStack;
        }

        CSSOMHandler() {
            nodeStack_ = new ArrayDeque<>();
        }

        Object getRoot() {
            return root_;
        }

        @Override
        public void startDocument(final InputSource source) throws CSSException {
            if (nodeStack_.isEmpty()) {
                final CSSStyleSheetImpl ss = new CSSStyleSheetImpl();
                CSSOMParser.this.setParentStyleSheet(ss);
                ss.setHref(getHref());
                ss.setMediaText(source.getMedia());
                ss.setTitle(source.getTitle());
                // Create the rule list
                final CSSRuleListImpl rules = new CSSRuleListImpl();
                ss.setCssRules(rules);
                nodeStack_.push(ss);
                nodeStack_.push(rules);
            }
        }

        @Override
        public void endDocument(final InputSource source) throws CSSException {
            // Pop the rule list and style sheet nodes
            nodeStack_.pop();
            root_ = nodeStack_.pop();
        }

        @Override
        public void ignorableAtRule(final String atRule, final Locator locator) throws CSSException {
            // Create the unknown rule and add it to the rule list
            final CSSUnknownRuleImpl ir = new CSSUnknownRuleImpl(
                CSSOMParser.this.getParentStyleSheet(),
                getParentRule(),
                atRule);
            ir.setLocator(locator);
            if (!nodeStack_.isEmpty()) {
                ((CSSRuleListImpl) nodeStack_.peek()).add(ir);
            }
            else {
                root_ = ir;
            }
        }

        @Override
        public void charset(final String characterEncoding, final Locator locator)
            throws CSSException {
            final CSSCharsetRuleImpl cr = new CSSCharsetRuleImpl(
                    CSSOMParser.this.getParentStyleSheet(),
                    getParentRule(),
                    characterEncoding);
            cr.setLocator(locator);
            if (!nodeStack_.isEmpty()) {
                ((CSSRuleListImpl) nodeStack_.peek()).add(cr);
            }
            else {
                root_ = cr;
            }
        }

        @Override
        public void importStyle(final String uri, final MediaQueryList media,
            final String defaultNamespaceURI, final Locator locator) throws CSSException {
            // Create the import rule and add it to the rule list
            final CSSImportRuleImpl ir = new CSSImportRuleImpl(
                CSSOMParser.this.getParentStyleSheet(),
                getParentRule(),
                uri,
                new MediaListImpl(media));
            ir.setLocator(locator);
            if (!nodeStack_.isEmpty()) {
                ((CSSRuleListImpl) nodeStack_.peek()).add(ir);
            }
            else {
                root_ = ir;
            }
        }

        @Override
        public void startMedia(final MediaQueryList media, final Locator locator) throws CSSException {
            final MediaListImpl ml = new MediaListImpl(media);
            // Create the media rule and add it to the rule list
            final CSSMediaRuleImpl mr = new CSSMediaRuleImpl(
                CSSOMParser.this.getParentStyleSheet(),
                getParentRule(),
                ml);
            mr.setLocator(locator);
            if (!nodeStack_.isEmpty()) {
                ((CSSRuleListImpl) nodeStack_.peek()).add(mr);
            }

            // Create the rule list
            final CSSRuleListImpl rules = new CSSRuleListImpl();
            mr.setRuleList(rules);
            nodeStack_.push(mr);
            nodeStack_.push(rules);
        }

        @Override
        public void endMedia(final MediaQueryList media) throws CSSException {
            // Pop the rule list and media rule nodes
            nodeStack_.pop();
            root_ = nodeStack_.pop();
        }

        @Override
        public void startPage(final String name, final String pseudoPage, final Locator locator)
            throws CSSException {
            // Create the page rule and add it to the rule list
            final CSSPageRuleImpl pr = new CSSPageRuleImpl(
                CSSOMParser.this.getParentStyleSheet(),
                getParentRule(), pseudoPage);
            pr.setLocator(locator);
            if (!nodeStack_.isEmpty()) {
                ((CSSRuleListImpl) nodeStack_.peek()).add(pr);
            }

            // Create the style declaration
            final CSSStyleDeclarationImpl decl = new CSSStyleDeclarationImpl(pr);
            pr.setStyle(decl);
            nodeStack_.push(pr);
            nodeStack_.push(decl);
        }

        @Override
        public void endPage(final String name, final String pseudoPage) throws CSSException {
            // Pop both the style declaration and the page rule nodes
            nodeStack_.pop();
            root_ = nodeStack_.pop();
        }

        @Override
        public void startFontFace(final Locator locator) throws CSSException {
            // Create the font face rule and add it to the rule list
            final CSSFontFaceRuleImpl ffr = new CSSFontFaceRuleImpl(
                CSSOMParser.this.getParentStyleSheet(),
                getParentRule());
            ffr.setLocator(locator);
            if (!nodeStack_.isEmpty()) {
                ((CSSRuleListImpl) nodeStack_.peek()).add(ffr);
            }

            // Create the style declaration
            final CSSStyleDeclarationImpl decl = new CSSStyleDeclarationImpl(ffr);
            ffr.setStyle(decl);
            nodeStack_.push(ffr);
            nodeStack_.push(decl);
        }

        @Override
        public void endFontFace() throws CSSException {
            // Pop both the style declaration and the font face rule nodes
            nodeStack_.pop();
            root_ = nodeStack_.pop();
        }

        @Override
        public void startSelector(final SelectorList selectors, final Locator locator) throws CSSException {
            // Create the style rule and add it to the rule list
            final CSSStyleRuleImpl sr = new CSSStyleRuleImpl(
                CSSOMParser.this.getParentStyleSheet(),
                getParentRule(), selectors);
            sr.setLocator(locator);
            if (!nodeStack_.isEmpty()) {
                final Object o = nodeStack_.peek();
                ((CSSRuleListImpl) o).add(sr);
            }

            // Create the style declaration
            final CSSStyleDeclarationImpl decl = new CSSStyleDeclarationImpl(sr);
            sr.setStyle(decl);
            nodeStack_.push(sr);
            nodeStack_.push(decl);
        }

        @Override
        public void endSelector(final SelectorList selectors) throws CSSException {
            // Pop both the style declaration and the style rule nodes
            nodeStack_.pop();
            root_ = nodeStack_.pop();
        }

        @Override
        public void property(final String name, final LexicalUnit value, final boolean important,
                                final Locator locator) {
            final CSSStyleDeclarationImpl decl = (CSSStyleDeclarationImpl) nodeStack_.peek();
            try {
                final Property property = new Property(name, new CSSValueImpl(value), important);
                property.setLocator(locator);
                decl.addProperty(property);
            }
            catch (final DOMException e) {
                parser_.getErrorHandler().error(parser_.toCSSParseException(e));
            }
        }

        private AbstractCSSRuleImpl getParentRule() {
            if (!nodeStack_.isEmpty() && nodeStack_.size() > 1) {
                final Iterator<Object> iter = nodeStack_.iterator();
                iter.next(); // skip first
                final Object node = iter.next();
                if (node instanceof AbstractCSSRuleImpl) {
                    return (AbstractCSSRuleImpl) node;
                }
            }
            return null;
        }
    }
}
