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
import java.io.StringReader;

import org.w3c.dom.DOMException;

import com.gargoylesoftware.css.parser.CSSException;
import com.gargoylesoftware.css.parser.CSSOMParser;
import com.gargoylesoftware.css.parser.InputSource;
import com.gargoylesoftware.css.util.LangUtils;

/**
 * Implementation of CSSCharsetRule.
 *
 * @author Ronald Brill
 */
public class CSSCharsetRuleImpl extends AbstractCSSRuleImpl {

    private String encoding_;

    public CSSCharsetRuleImpl(
            final CSSStyleSheetImpl parentStyleSheet,
            final AbstractCSSRuleImpl parentRule,
            final String encoding) {
        super(parentStyleSheet, parentRule);
        encoding_ = encoding;
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

            // The rule must be a charset rule
            if (r instanceof CSSCharsetRuleImpl) {
                encoding_ = ((CSSCharsetRuleImpl) r).encoding_;
            }
            else {
                throw new DOMExceptionImpl(
                    DOMException.INVALID_MODIFICATION_ERR,
                    DOMExceptionImpl.EXPECTING_CHARSET_RULE);
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

    public String getEncoding() {
        return encoding_;
    }

    public void setEncoding(final String encoding) throws DOMException {
        encoding_ = encoding;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CSSCharsetRuleImpl)) {
            return false;
        }
        final CSSCharsetRuleImpl ccr = (CSSCharsetRuleImpl) obj;
        return super.equals(obj)
            && LangUtils.equals(getEncoding(), ccr.getEncoding());
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = LangUtils.hashCode(hash, encoding_);
        return hash;
    }

    @Override
    public String toString() {
        return getCssText();
    }

    public String getCssText() {
        final StringBuilder sb = new StringBuilder();

        sb.append("@charset \"");

        final String enc = getEncoding();
        if (null != enc) {
            sb.append(enc);
        }
        sb.append("\";");
        return sb.toString();
    }
}
