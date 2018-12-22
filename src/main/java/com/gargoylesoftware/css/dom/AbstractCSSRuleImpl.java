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

import java.io.Serializable;

import com.gargoylesoftware.css.parser.AbstractLocatable;

/**
 * @author Ronald Brill
 */
public abstract class AbstractCSSRuleImpl extends AbstractLocatable implements Serializable {

    public enum CSSRuleType {
        MEDIA_RULE,
        PAGE_RULE,
        IMPORT_RULE,
        FONT_FACE_RULE,
        CHARSET_RULE,
        STYLE_RULE,
        UNKNOWN_RULE
    }

    private CSSStyleSheetImpl parentStyleSheet_;
    private AbstractCSSRuleImpl parentRule_;

    protected CSSStyleSheetImpl getParentStyleSheetImpl() {
        return parentStyleSheet_;
    }

    public void setParentStyleSheet(final CSSStyleSheetImpl parentStyleSheet) {
        parentStyleSheet_ = parentStyleSheet;
    }

    public void setParentRule(final AbstractCSSRuleImpl parentRule) {
        parentRule_ = parentRule;
    }

    public AbstractCSSRuleImpl(final CSSStyleSheetImpl parentStyleSheet, final AbstractCSSRuleImpl parentRule) {
        super();
        parentStyleSheet_ = parentStyleSheet;
        parentRule_ = parentRule;
    }

    public AbstractCSSRuleImpl() {
        super();
    }

    public abstract CSSRuleType getType();

    public abstract String getCssText();
    public abstract void setCssText(String text);

    public CSSStyleSheetImpl getParentStyleSheet() {
        return parentStyleSheet_;
    }

    public AbstractCSSRuleImpl getParentRule() {
        return parentRule_;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AbstractCSSRuleImpl)) {
            return false;
        }
        return super.equals(obj);
        // don't use parentRule and parentStyleSheet in equals()
        // recursive loop -> stack overflow!
    }

    @Override
    public int hashCode() {
        final int hash = super.hashCode();
        // don't use parentRule and parentStyleSheet in hashCode()
        // recursive loop -> stack overflow!
        return hash;
    }
}
