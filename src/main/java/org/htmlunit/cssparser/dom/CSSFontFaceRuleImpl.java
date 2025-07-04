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
 * Implementation of CSSFontFaceRule.
 *
 * @author Ronald Brill
 *
 */
public class CSSFontFaceRuleImpl extends AbstractCSSRuleImpl {

    private CSSStyleDeclarationImpl style_;

    /**
     * Ctor.
     *
     * @param parentStyleSheet the parent style sheet
     * @param parentRule the parent rule
     */
    public CSSFontFaceRuleImpl(final CSSStyleSheetImpl parentStyleSheet, final AbstractCSSRuleImpl parentRule) {
        super(parentStyleSheet, parentRule);
    }

    /** {@inheritDoc} */
    @Override
    public String getCssText() {
        final StringBuilder sb = new StringBuilder();
        sb.append("@font-face { ");

        final CSSStyleDeclarationImpl style = getStyle();
        if (null != style) {
            sb.append(style.getCssText());
            if (style.getProperties().size() > 0) {
                sb.append(";");
            }
        }
        sb.append(" }");
        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public void setCssText(final String cssText) throws DOMException {
        try {
            final CSSOMParser parser = new CSSOMParser();
            final AbstractCSSRuleImpl r = parser.parseRule(cssText);

            // The rule must be a font face rule
            if (r instanceof CSSFontFaceRuleImpl) {
                style_ = ((CSSFontFaceRuleImpl) r).style_;
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

    /**
     * <p>getStyle.</p>
     *
     * @return the style
     */
    public CSSStyleDeclarationImpl getStyle() {
        return style_;
    }

    /**
     * Sets the style to a new one.
     *
     * @param style the new style
     */
    public void setStyle(final CSSStyleDeclarationImpl style) {
        style_ = style;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CSSFontFaceRuleImpl)) {
            return false;
        }
        final CSSFontFaceRuleImpl cffr = (CSSFontFaceRuleImpl) obj;
        return super.equals(obj)
            && ParserUtils.equals(getStyle(), cffr.getStyle());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = ParserUtils.hashCode(hash, style_);
        return hash;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getCssText();
    }
}
