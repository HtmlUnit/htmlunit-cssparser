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
package com.gargoylesoftware.css.dom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;

import org.w3c.dom.DOMException;

import com.gargoylesoftware.css.parser.CSSException;
import com.gargoylesoftware.css.parser.CSSOMParser;
import com.gargoylesoftware.css.parser.InputSource;
import com.gargoylesoftware.css.util.LangUtils;
import com.gargoylesoftware.css.util.ThrowCssExceptionErrorHandler;

/**
 * Implementation of CSSMediaRule.
 *
 * @author Ronald Brill
 */
public class CSSMediaRuleImpl extends AbstractCSSRuleImpl {

    private MediaListImpl media_;
    private CSSRuleListImpl cssRules_;

    public void setMedia(final MediaListImpl media) {
        media_ = media;
    }

    public void setCssRules(final CSSRuleListImpl cssRules) {
        cssRules_ = cssRules;
    }

    public CSSMediaRuleImpl(
            final CSSStyleSheetImpl parentStyleSheet,
            final AbstractCSSRuleImpl parentRule,
            final MediaListImpl media) {
        super(parentStyleSheet, parentRule);
        media_ = media;
    }

    /**
     * {@inheritDoc}
     */
    public String getCssText() {
        final StringBuilder sb = new StringBuilder("@media ");

        sb.append(getMedia().getMediaText());
        sb.append(" {");
        for (int i = 0; i < getCssRules().getLength(); i++) {
            final AbstractCSSRuleImpl rule = getCssRules().item(i);
            sb.append(rule.getCssText()).append(" ");
        }
        sb.append("}");
        return sb.toString();
    }

    public void setCssText(final String cssText) throws DOMException {
        final CSSStyleSheetImpl parentStyleSheet = getParentStyleSheetImpl();
        if (parentStyleSheet != null && parentStyleSheet.isReadOnly()) {
            throw new DOMExceptionImpl(
                DOMException.NO_MODIFICATION_ALLOWED_ERR,
                DOMExceptionImpl.READ_ONLY_STYLE_SHEET);
        }

        try {
            final InputSource is = new InputSource(new StringReader(cssText));
            final CSSOMParser parser = new CSSOMParser();
            final AbstractCSSRuleImpl r = parser.parseRule(is);

            // The rule must be a media rule
            if (r instanceof CSSMediaRuleImpl) {
                media_ = ((CSSMediaRuleImpl) r).media_;
                cssRules_ = ((CSSMediaRuleImpl) r).cssRules_;
            }
            else {
                throw new DOMExceptionImpl(
                    DOMException.INVALID_MODIFICATION_ERR,
                    DOMExceptionImpl.EXPECTING_MEDIA_RULE);
            }
        }
        catch (final CSSException e) {
            throw new DOMExceptionImpl(
                DOMException.SYNTAX_ERR,
                DOMExceptionImpl.SYNTAX_ERROR,
                e.getMessage());
        }
        catch (final IOException e) {
            throw new DOMExceptionImpl(
                DOMException.SYNTAX_ERR,
                DOMExceptionImpl.SYNTAX_ERROR,
                e.getMessage());
        }
    }

    public MediaListImpl getMedia() {
        return media_;
    }

    public CSSRuleListImpl getCssRules() {
        if (cssRules_ == null) {
            cssRules_ = new CSSRuleListImpl();
        }
        return cssRules_;
    }

    public int insertRule(final String rule, final int index) throws DOMException {
        final CSSStyleSheetImpl parentStyleSheet = getParentStyleSheetImpl();
        if (parentStyleSheet != null && parentStyleSheet.isReadOnly()) {
            throw new DOMExceptionImpl(
                DOMException.NO_MODIFICATION_ALLOWED_ERR,
                DOMExceptionImpl.READ_ONLY_STYLE_SHEET);
        }

        try {
            final InputSource is = new InputSource(new StringReader(rule));
            final CSSOMParser parser = new CSSOMParser();
            parser.setParentStyleSheet(parentStyleSheet);
            parser.setErrorHandler(ThrowCssExceptionErrorHandler.INSTANCE);
            final AbstractCSSRuleImpl r = parser.parseRule(is);

            // Insert the rule into the list of rules
            getCssRules().insert(r, index);

        }
        catch (final IndexOutOfBoundsException e) {
            throw new DOMExceptionImpl(
                DOMException.INDEX_SIZE_ERR,
                DOMExceptionImpl.INDEX_OUT_OF_BOUNDS,
                e.getMessage());
        }
        catch (final CSSException e) {
            throw new DOMExceptionImpl(
                DOMException.SYNTAX_ERR,
                DOMExceptionImpl.SYNTAX_ERROR,
                e.getMessage());
        }
        catch (final IOException e) {
            throw new DOMExceptionImpl(
                DOMException.SYNTAX_ERR,
                DOMExceptionImpl.SYNTAX_ERROR,
                e.getMessage());
        }
        return index;
    }

    public void deleteRule(final int index) throws DOMException {
        final CSSStyleSheetImpl parentStyleSheet = getParentStyleSheetImpl();
        if (parentStyleSheet != null && parentStyleSheet.isReadOnly()) {
            throw new DOMExceptionImpl(
                DOMException.NO_MODIFICATION_ALLOWED_ERR,
                DOMExceptionImpl.READ_ONLY_STYLE_SHEET);
        }
        try {
            getCssRules().delete(index);
        }
        catch (final IndexOutOfBoundsException e) {
            throw new DOMExceptionImpl(
                DOMException.INDEX_SIZE_ERR,
                DOMExceptionImpl.INDEX_OUT_OF_BOUNDS,
                e.getMessage());
        }
    }

    public void setRuleList(final CSSRuleListImpl rules) {
        cssRules_ = rules;
    }

    @Override
    public String toString() {
        return getCssText();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CSSMediaRuleImpl)) {
            return false;
        }
        final CSSMediaRuleImpl cmr = (CSSMediaRuleImpl) obj;
        return super.equals(obj)
            && LangUtils.equals(getMedia(), cmr.getMedia())
            && LangUtils.equals(getCssRules(), cmr.getCssRules());
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = LangUtils.hashCode(hash, media_);
        hash = LangUtils.hashCode(hash, cssRules_);
        return hash;
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.writeObject(cssRules_);
        out.writeObject(media_);
    }

    private void readObject(final ObjectInputStream in)
        throws IOException, ClassNotFoundException {

        cssRules_ = (CSSRuleListImpl) in.readObject();
        if (cssRules_ != null) {
            for (int i = 0; i < cssRules_.getLength(); i++) {
                final AbstractCSSRuleImpl cssRule = cssRules_.item(i);
                cssRule.setParentRule(this);
                cssRule.setParentStyleSheet(getParentStyleSheetImpl());
            }
        }
        media_ = (MediaListImpl) in.readObject();
    }
}
