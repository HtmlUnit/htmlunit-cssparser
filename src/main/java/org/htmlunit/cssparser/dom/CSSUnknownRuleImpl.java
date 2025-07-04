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
package org.htmlunit.cssparser.dom;

import java.io.IOException;

import org.htmlunit.cssparser.parser.CSSException;
import org.htmlunit.cssparser.parser.CSSOMParser;
import org.htmlunit.cssparser.util.ParserUtils;
import org.w3c.dom.DOMException;

/**
 * Implementation of CSSUnknownRule.
 *
 * @author Ronald Brill
 */
public class CSSUnknownRuleImpl extends AbstractCSSRuleImpl {

    private String text_;

    /**
     * Ctor.
     * @param parentStyleSheet the parent style sheet
     * @param parentRule the parent rule
     * @param text the text
     */
    public CSSUnknownRuleImpl(
            final CSSStyleSheetImpl parentStyleSheet,
            final AbstractCSSRuleImpl parentRule,
            final String text) {
        super(parentStyleSheet, parentRule);
        text_ = text;
    }

    /** {@inheritDoc} */
    @Override
    public String getCssText() {
        if (null == text_) {
            return "";
        }
        return text_;
    }

    /** {@inheritDoc} */
    @Override
    public void setCssText(final String cssText) throws DOMException {
        try {
            final CSSOMParser parser = new CSSOMParser();
            final AbstractCSSRuleImpl r = parser.parseRule(cssText);

            // The rule must be an unknown rule
            if (r instanceof CSSUnknownRuleImpl) {
                text_ = ((CSSUnknownRuleImpl) r).text_;
            }
            else {
                throw new DOMExceptionImpl(
                    DOMException.INVALID_MODIFICATION_ERR,
                    DOMExceptionImpl.EXPECTING_FONT_FACE_RULE);
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

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getCssText();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CSSUnknownRuleImpl)) {
            return false;
        }
        final CSSUnknownRuleImpl cur = (CSSUnknownRuleImpl) obj;
        return super.equals(obj)
            && ParserUtils.equals(getCssText(), cur.getCssText());
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = ParserUtils.hashCode(hash, text_);
        return hash;
    }

}
